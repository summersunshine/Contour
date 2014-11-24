package sample;

import geometry.Point;

import java.util.Vector;

public class Stroke
{
	public Vector<Point> points;
	public Vector<Point> rightContourPoints;
	public Vector<Point> leftContourPoints;

	public Stroke()
	{
		this.points = new Vector<Point>();
		this.rightContourPoints = new Vector<Point>();
		this.leftContourPoints = new Vector<Point>();

	}

	public int getSampleNumber()
	{
		return this.points.size();
	}
}
