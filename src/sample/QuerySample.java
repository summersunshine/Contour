package sample;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import Geometry.Point;
import config.SampleConfig;

public class QuerySample extends Sample
{
	// 距离其他的采样点的代价
	public Vector<Cost> costs;

	public QuerySample(Vector<Point> points, int a, int b)
	{
		super(points, a, b);

		this.costs = new Vector<Cost>();

	}

	/**
	 * 加上距离数据
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
	 * 依据totalDistance进行排序
	 * */
	@SuppressWarnings("unchecked")
	public void sort()
	{
		Collections.sort(costs, new Cost.CostComparator());

	}

	/**
	 * 输出knn
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
