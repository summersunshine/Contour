package sample;

import geometry.Point;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

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
		float beginPercent = percent - 0.15f;
		float endPercent = percent + 0.15f;

		if (beginPercent < 0)
		{
			beginPercent = 0;
			endPercent = 2 * percent;
			endPercent = endPercent<0.15f?0.15f:endPercent;
		}

		if (endPercent > 1)
		{
			endPercent = 1;
			beginPercent = 2 * percent - endPercent;
			beginPercent = beginPercent>0.85f?0.85f:beginPercent;
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
