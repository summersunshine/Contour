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
		int begin = (int) (otherSamples.size() * (percent - 0.15));
		int end = (int) (otherSamples.size() * (percent + 0.15));
		begin = begin < 0 ? 0 : begin;
		end = end > otherSamples.size() ? otherSamples.size() : end;
		for (int i = begin; i < end; i++)
		{
			float dis = feature.getDistance(otherSamples.get(i).feature);

			this.costs.addElement(new Cost(otherSamples.get(i).a, otherSamples.get(i).b, b, dis));

		}
	}

	/**
	 * ����totalDistance��������
	 * */
	@SuppressWarnings("unchecked")
	public void sort()
	{
		Collections.sort(costs, new Cost.CostComparator());

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
