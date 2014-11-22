package sample;

import java.util.Vector;

import Geometry.Point;

public class Stroke
{
	public Vector<Point> points;
	public Vector<Point> rightCountourPoints;
	public Vector<Point> leftCountourPoints;

	public Stroke()
	{
		this.points = new Vector<Point>();
		this.rightCountourPoints = new Vector<Point>();
		this.leftCountourPoints = new Vector<Point>();

	}

	
	public int getSampleNumber()
	{
		return this.points.size();
	}
}
