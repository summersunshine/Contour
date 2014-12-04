package sample;

import geometry.Point;

import java.util.Vector;

public class LibSample extends Sample
{

	public Point velocity;

	public LibSample(Vector<Point> points,double angle,double averageR, int a, int b)
	{
		super(points,angle,averageR, a, b);
		// TODO Auto-generated constructor stub

		this.initVelocity(points, b);
	}

	private void initVelocity(Vector<Point> points, int index)
	{
		if (index == 0)
		{
			velocity = points.get(1).sub(points.get(0)).normalizePoint();
		}
		else
		{
			velocity = points.get(index).sub(points.get(index - 1)).normalizePoint();
		}
	}
}
