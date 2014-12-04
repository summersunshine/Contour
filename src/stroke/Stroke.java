package stroke;

import geometry.Geometry;
import geometry.Point;

import java.util.Vector;

public class Stroke
{
	public Vector<Double> dirAngle;
	public Vector<Point> points;
	public Vector<Point> rightContourPoints;
	public Vector<Point> leftContourPoints;
	protected float averageR;
	public Stroke()
	{
		this.points = new Vector<Point>();
		this.rightContourPoints = new Vector<Point>();
		this.leftContourPoints = new Vector<Point>();
		this.setAverageR();
	}
	
	public void calDirAngle()
	{
		this.dirAngle = Geometry.getDir(points);
	}

	public int getSampleNumber()
	{
		return this.points.size();
	}
	
	public void setAverageR()
	{
		averageR = 0;
		for (int i = 0; i < points.size(); i++)
		{
			for (int j = i+1; j < points.size(); j++)
			{
				averageR += Point.getDistance(points.get(i), points.get(j));
			}
		}
		averageR = averageR*2/(points.size()+1)/points.size();
		
	}
	
}
