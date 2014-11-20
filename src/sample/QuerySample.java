package sample;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.Vector;

import config.SampleConfig;
import sample.Cost.CostComparator;
import Geometry.Point;

public class QuerySample extends Sample
{

	//public int index;
	
	
	public Vector<Cost> costs;
	
	
	
	public QuerySample(Vector<Point> points, int a,int b)
	{
		super(points, a,b);
		// TODO Auto-generated constructor stub
		
		//this.index = index;
		
		this.costs = new Vector<Cost>();
		
		
	}

	
	public void addDistanceData(LibSample otherSample)
	{
		float dis = feature.getDistance(otherSample.feature);
		
		this.costs.addElement(new Cost(otherSample.a, otherSample.b, b,(int) dis));
	}
	
	
	
	public void getKNN(Vector<LibSample> otherLibSamples,int k)
	{
		for (int i = 0; i < otherLibSamples.size(); i++)
		{
			addDistanceData(otherLibSamples.get(i));
		}
		
	
	}
	
	@SuppressWarnings("unchecked")
	public void sort()
	{
		Collections.sort(costs, new Comparator<Cost>()
		{

			@Override
			public int compare(Cost cost1, Cost cost2)
			{
				// TODO Auto-generated method stub
				if (cost1.ef > cost2.ef)
				{
					return 1;
				}
				else if(cost1.ef < cost2.ef)
				{
					return -1;
				}
				else
				{
					return 0;
				}
			}
			
		});
		
//		
//		for (int i = 0; i < costs.size(); i++)
//		{
//			costs.get(i).print();
//		}
	}
	
	public void printKNN()
	{
		System.out.println("index: " + b);
		for (int i = 0; i < SampleConfig.K; i++)
		{
			costs.get(i).print();
		}
	}
	
	
}
