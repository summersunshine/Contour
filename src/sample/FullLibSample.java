package sample;

import geometry.Point;

import java.util.Vector;

public class FullLibSample extends FullSample
{
	public Point velocity;

	public Point leftVelocity;

	public Point rightVelocity;

	public FullLibSample(Vector<Point> points, Vector<Point> leftContourPoints, Vector<Point> rightContourPoints, double angle, double averageR, int a, int b)
	{
		super(points, leftContourPoints, rightContourPoints, angle, averageR, a, b);
		// TODO Auto-generated constructor stub

		this.initVelocity(points, leftContourPoints, rightContourPoints, b);
	}

	private void initVelocity(Vector<Point> points, Vector<Point> leftContourPoints, Vector<Point> rightContourPoints, int index)
	{
		if (index == 0)
		{
			velocity = points.get(1).sub(points.get(0)).normalizePoint();
			leftVelocity = leftContourPoints.get(1).sub(leftContourPoints.get(0)).normalizePoint();
			rightVelocity = rightContourPoints.get(1).sub(rightContourPoints.get(0)).normalizePoint();
		}
		else
		{
			velocity = points.get(index).sub(points.lastElement()).normalizePoint();
			leftVelocity = leftContourPoints.get(index).sub(leftContourPoints.lastElement()).normalizePoint();
			rightVelocity = rightContourPoints.get(index).sub(rightContourPoints.lastElement()).normalizePoint();
		}
	}
}
