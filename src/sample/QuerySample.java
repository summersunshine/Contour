package sample;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import feature.Cost;
import geometry.Point;

public class QuerySample extends Sample
{
	// 距离其他的采样点的代价
	public Vector<Cost>			costs;

	public double				radius;

	public Map<String, Float>	costMap;

	public QuerySample(Vector<Point> points, double radius, double angle, double averageR, int a, int b)
	{
		super(points, angle, averageR, a, b);

		this.costs = new Vector<Cost>();

		this.costMap = new HashMap<String, Float>();

		this.radius = radius;
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

		// int begin = (int) ((otherSamples.size() - 1) * beginPercent);
		// int end = (int) ((otherSamples.size() - 1) * endPercent);

		// for (int i = begin; i < end; i++)
		// {
		// float dis = feature.getDistance(otherSamples.get(i).feature);
		//
		// this.costs.addElement(new Cost(otherSamples.get(i).a,
		// otherSamples.get(i).b, b, dis));
		//
		// }
		//

		for (int i = 0; i < otherSamples.size(); i++)
		{
			float dis = feature.getDistance(otherSamples.get(i).feature);

			// this.costs.addElement(new Cost(otherSamples.get(i).a,
			// otherSamples.get(i).b, b, dis));

			String key = otherSamples.get(i).a + "_" + otherSamples.get(i).b;

			this.costMap.put(key, dis);
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
