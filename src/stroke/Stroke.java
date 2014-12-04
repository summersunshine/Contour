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

	public Stroke()
	{
		this.points = new Vector<Point>();
		this.rightContourPoints = new Vector<Point>();
		this.leftContourPoints = new Vector<Point>();
		
	}
	
	public void calDirAngle()
	{
		this.dirAngle = Geometry.getDir(points);
	}

	public int getSampleNumber()
	{
		return this.points.size();
	}
}
