package sample;

import java.util.Vector;

import Geometry.Point;
import Geometry.Position;


public class QueryStroke extends Stroke
{
	
	
	public Vector<QuerySample> querySamples;
	
	public QueryStroke(Vector<Point> points,Vector<Point> rightCountourPoints,Vector<Point> leftCountourPoints)
	{
		super();
		this.points = points;
		this.rightCountourPoints = rightCountourPoints;
		this.leftCountourPoints = leftCountourPoints;
		
		this.initQuerySample();
	}
	
	public void initQuerySample()
	{
		this.querySamples = new Vector<QuerySample>();
		
		for (int i = 0; i < points.size(); i++)
		{
			this.querySamples.addElement(new QuerySample(points, -1,i));
		}
	}
}
