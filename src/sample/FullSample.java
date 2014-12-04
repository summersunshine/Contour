package sample;

import feature.Feature;
import geometry.Point;

import java.util.Vector;

public class FullSample extends Sample
{
	// ����������
	public Feature leftFeature;

	// ������
	public Feature rightFeature;

	public FullSample(Vector<Point> points, Vector<Point> leftContourPoints, Vector<Point> rightContourPoints,double angle, int a, int b)
	{
		super(points,angle, a, b);
		// TODO Auto-generated constructor stub

		leftFeature = new Feature(leftContourPoints,angle, a, b);

		rightFeature = new Feature(rightContourPoints,angle, a, b);
	}

}
