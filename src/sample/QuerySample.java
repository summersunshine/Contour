package sample;

import java.util.Collections;
import java.util.Vector;

import feature.Cost;
import geometry.Point;

public class QuerySample extends Sample
{
	// 距离其他的采样点的代价
	public Vector<Cost>	costs;

	public QuerySample(Vector<Point> points, double angle, double averageR, int a, int b)
	{
		super(points, angle, averageR, a, b);

		this.costs = new Vector<Cost>();

	}

	/**
	 * 加上距离数据
	 * 
	 * @param otherSamples
	 * */
	public void addDistanceData(Vector<LibSample> otherSamples)
	{
		float range = 0.25f;
		float beginPercent = percent - range;
		float endPercent = percent + range;

		if (beginPercent < 0)
		{
			beginPercent = 0;
			endPercent = 2 * percent;
			endPercent = endPercent < range ? range : endPercent;
		}

		if (endPercent > 1)
		{
			endPercent = 1;
			beginPercent = 2 * percent - endPercent;
			beginPercent = beginPercent > (1 - range) ? (1 - range) : beginPercent;
		}

		int begin = (int) ((otherSamples.size() - 1) * beginPercent);
		int end = (int) ((otherSamples.size() - 1) * endPercent);

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
