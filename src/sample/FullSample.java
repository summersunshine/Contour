package sample;

import java.util.Vector;

import Geometry.Point;

public class FullSample extends Sample
{
	// ×óÂÖÀªÌØÕ÷
	public Feature leftFeature;

	// ÓÒÂÖÀª
	public Feature rightFeature;

	public FullSample(Vector<Point> points, Vector<Point> leftContourPoints, Vector<Point> rightContourPoints, int a, int b)
	{
		super(points, a, b);
		// TODO Auto-generated constructor stub

		leftFeature = new Feature(leftContourPoints, a, b);

		rightFeature = new Feature(rightContourPoints, a, b);
	}

}
