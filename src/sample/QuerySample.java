package sample;

import java.util.Vector;

import Geometry.Point;

public class QuerySample extends Sample
{
	
	//������������֮��������ľ���
	public float [][] distance;
	
	
	public QuerySample(Vector<Point> points, int index)
	{
		super(points, index);
		// TODO Auto-generated constructor stub
	}

	
	public void setDistance(LibSample otherSample)
	{
		float dis = feature.getDistance(otherSample.feature);
		
		distance[otherSample.a][otherSample.b] = dis;
	}
	
	
	public void getKNN(Vector<LibSample> otherLibSamples,int k)
	{
		for (int i = 0; i < otherLibSamples.size(); i++)
		{
			setDistance(otherLibSamples.get(i));
		}
	}
	
}
