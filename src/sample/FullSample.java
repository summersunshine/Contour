package sample;

import feature.Feature;
import geometry.Point;

import java.util.Vector;

public class FullSample extends Sample
{
	// ×óÂÖÀªÌØÕ÷
	public Feature leftFeature;

	// ÓÒÂÖÀª
	public Feature rightFeature;

	public FullSample(Vector<Point> points, Vector<Point> leftContourPoints, Vector<Point> rightContourPoints,double angle,double averageR, int a, int b)
	{
		super(points,angle,averageR, a, b);
		// TODO Auto-generated constructor stub

		leftFeature = new Feature(leftContourPoints,angle,averageR, a, b);

		rightFeature = new Feature(rightContourPoints,angle,averageR, a, b);
	}

}
