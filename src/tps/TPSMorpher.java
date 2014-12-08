package tps;

import geometry.CoordDiff;
import geometry.Point;

import java.util.Random;
import java.util.Vector;

import Jama.Matrix;

public class TPSMorpher
{

	public Matrix matrixL;
	public Matrix matrixV;
	public Matrix matrixK;
	public Vector<CoordDiff> samples;

	public TPSMorpher(Vector<CoordDiff> samples, double regularization, double subsamplingFactor)
	{
		this.samples = samples;
		int p = samples.size();
		int m = (int) (p * subsamplingFactor);
		if (m < 3)
		{
			m = 3;
		}
		if (m > p)
		{
			m = p;
		}
		if (m < p)
		{
			for (int i = 0; i < samples.size() - 1; i++)
			{
				Random random = new Random();
				int j = i + random.nextInt(samples.size() - 1);
				CoordDiff tmp = samples.get(j);
				samples.set(i, samples.get(j));
				samples.set(j, tmp);
			}
		}

		matrixL = new Matrix(p + 3, m + 3);
		matrixV = new Matrix(p + 3, 2);
		matrixK = new Matrix(p, m);

		// Fill K (p x m, upper left of L)
		for (int i = 0; i < p; i++)
		{
			for (int j = 0; j < m; j++)
			{
				double elen2 = samples.get(i).sub(samples.get(j)).lengthSqr();
				double value = baseFunc(elen2);
				matrixL.set(i, j, value);
				matrixK.set(i, j, value);
			}
		}

		// Empiric value for avg. distance between points
		//
		// This variable is normally calculated to make regularization
		// scale independent, but since our shapes in this application are
		// always
		// normalized to maxspect [-.5,.5]x[-.5,.5], this approximation is
		// pretty
		// safe and saves us p*p square roots

		final double a = 0.5;

		for (int i = 0; i < p; i++)
		{
			matrixL.set(i, m + 0, 1.0);
			matrixL.set(i, m + 1, samples.get(i).x);
			matrixL.set(i, m + 2, samples.get(i).y);

			if (i < m)
			{
				matrixL.set(i, i, regularization * a * a);
				matrixK.set(i, i, regularization * a * a);

				matrixL.set(p + 0, i, 1.0);
				matrixL.set(p + 1, i, samples.get(i).x);
				matrixL.set(p + 2, i, samples.get(i).y);
			}
		}
		// O (3 x 3, lower right)
		for (int i = p; i < p + 3; i++)
		{
			for (int j = m; j < m + 3; j++)
			{
				matrixL.set(i, j, 0);
			}
		}

		// Fill the right hand matrix V
		for (int i = 0; i < p; i++)
		{
			matrixV.set(i, 0, samples.get(i).dx);
			matrixV.set(i, 1, samples.get(i).dy);
		}
		matrixV.set(p + 0, 0, 0);
		matrixV.set(p + 1, 0, 0);
		matrixV.set(p + 2, 0, 0);
		matrixV.set(p + 0, 1, 0);
		matrixV.set(p + 1, 1, 0);
		matrixV.set(p + 2, 1, 0);


		
		matrixV = matrixL.solve(matrixV);

	}
	
	
	public Vector<CoordDiff> morphCoordDiff(Vector<CoordDiff> coordDiffs)
	{
		for (int i = 0; i < coordDiffs.size(); i++)
		{
			int m = matrixV.getRowDimension() - 3;
			
			double x = coordDiffs.get(i).x;
			double y = coordDiffs.get(i).y;
			double dx = matrixV.get(m + 0, 0) + matrixV.get(m + 1, 0) * x + matrixV.get(m + 2, 0) * y;
			double dy = matrixV.get(m + 0, 1) + matrixV.get(m + 1, 1) * x + matrixV.get(m + 2, 1) * y;
			System.out.print("dx: " + dx + "dy: " + dy);
			for (int j = 0; j < m; j++)
			{
				double offsetX = samples.get(j).x - coordDiffs.get(i).x;
				double offsetY = samples.get(j).y - coordDiffs.get(i).y;
				double d = baseFunc(offsetX * offsetX + offsetY * offsetY);
				dx += matrixV.get(j, 0) * d;
				dy += matrixV.get(j, 1) * d;
			}

			CoordDiff coordDiff = new CoordDiff(coordDiffs.get(i), new Point(dx,dy));
			coordDiffs.set(i, coordDiff);
		}
		return coordDiffs;
	}
	

//	public Vector<Point> morphPoints(Vector<Point> points)
//	{
//		for (int i = 0; i < points.size(); i++)
//		{
//			int m = matrixV.getRowDimension() - 3;
//			double x = (int) points.get(i).x;
//			double y = (int) points.get(i).y;
//			double dx = matrixV.get(m + 0, 0) + matrixV.get(m + 1, 0) * x + matrixV.get(m + 2, 0) * y;
//			double dy = matrixV.get(m + 0, 1) + matrixV.get(m + 1, 1) * x + matrixV.get(m + 2, 1) * y;
//
//			System.out.print("dx: " + dx + "dy: " + dy);
//
//			for (int j = 0; j < m; j++)
//			{
//				double offsetX = (int) samples.get(j).x - (int) points.get(i).x;
//				double offsetY = (int) samples.get(j).y - (int) points.get(i).y;
//				double d = baseFunc(offsetX * offsetX + offsetY * offsetY);
//				dx += matrixV.get(j, 0) * d;
//				dy += matrixV.get(j, 1) * d;
//			}
//
//			System.out.println("  dx: " + dx + "dy: " + dy);
//			Point newPoint = points.get(i).add(new Point(dx, dy));
//			points.set(i, newPoint);
//		}
//		return points;
//	}
	
	public Vector<CoordDiff> morphPoints(Vector<Point> points)
	{
		Vector<CoordDiff> coordDiffs = new Vector<CoordDiff>();
		for (int i = 0; i < points.size(); i++)
		{
			int m = matrixV.getRowDimension() - 3;
			double x = (int) points.get(i).x;
			double y = (int) points.get(i).y;
			double dx = matrixV.get(m + 0, 0) + matrixV.get(m + 1, 0) * x + matrixV.get(m + 2, 0) * y;
			double dy = matrixV.get(m + 0, 1) + matrixV.get(m + 1, 1) * x + matrixV.get(m + 2, 1) * y;

			//System.out.print("dx: " + dx + "dy: " + dy);

			for (int j = 0; j < m; j++)
			{
				double offsetX = (int) samples.get(j).x - (int) points.get(i).x;
				double offsetY = (int) samples.get(j).y - (int) points.get(i).y;
				double d = baseFunc(offsetX * offsetX + offsetY * offsetY);
				dx += matrixV.get(j, 0) * d;
				dy += matrixV.get(j, 1) * d;
			}
			
			CoordDiff coordDiff = new CoordDiff(points.get(i).x,points.get(i).y, dx,dy);
			coordDiffs.add(coordDiff);
		}
		return coordDiffs;
	}

	double baseFunc(double r2)
	{
		// same as r*r * log(r), but for r^2:
		return (r2 == 0) ? 0.0 // function limit at 0
				: r2 * Math.log(r2) * 0.217147241; // = 1/(2*log(10))
	}

}