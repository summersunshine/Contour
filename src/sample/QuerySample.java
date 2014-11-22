package sample;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import Geometry.Point;
import config.SampleConfig;

public class QuerySample extends Sample
{
	// ���������Ĳ�����Ĵ���
	public Vector<Cost> costs;

	public QuerySample(Vector<Point> points, int a, int b)
	{
		super(points, a, b);

		this.costs = new Vector<Cost>();

	}

	/**
	 * ���Ͼ�������
	 * 
	 * @param otherSamples
	 * */
	public void addDistanceData(Vector<LibSample> otherSamples)
	{
		for (int i = 0; i < otherSamples.size(); i++)
		{
			float dis = feature.getDistance(otherSamples.get(i).feature);

			this.costs.addElement(new Cost(otherSamples.get(i).a, otherSamples.get(i).b, b, (int) dis));

		}
	}

	/**
	 * ����totalDistance��������
	 * */
	@SuppressWarnings("unchecked")
	public void sort()
	{
		Collections.sort(costs, new Cost.CostComparator());

//		while (costs.size() > 2 * SampleConfig.K)
//		{
//			costs.remove(costs.size() - 1);
//		}
	}

	/**
	 * ���knn
	 * */
	public void printKNN()
	{
		System.out.println("index: " + b);
		for (int i = 0; i < 5; i++)
		{
			costs.get(i).print();
		}
	}

}
