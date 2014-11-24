package sample;

import geometry.Point;

import java.util.Collections;
import java.util.Vector;

public class FullQuerySample extends FullSample
{

	// ���������Ĳ�����Ĵ���
	public Vector<Cost> costs;

	// // ���������Ĳ�����Ĵ���
	// public Vector<Cost> leftCosts;
	//
	// // ���������Ĳ�����Ĵ���
	// public Vector<Cost> rightCosts;

	public FullQuerySample(Vector<Point> points, Vector<Point> leftContourPoints, Vector<Point> rightContourPoints, int a, int b)
	{
		super(points, leftContourPoints, rightContourPoints, a, b);
		// TODO Auto-generated constructor stub

		this.costs = new Vector<Cost>();
		//
		// this.leftCosts = new Vector<Cost>();
		//
		// this.rightCosts = new Vector<Cost>();
	}

	/**
	 * ���Ͼ�������
	 * 
	 * @param otherSamples
	 * */
	public void addDistanceData(Vector<FullLibSample> otherSamples)
	{
		for (int i = 0; i < otherSamples.size(); i++)
		{
			float dis = feature.getDistance(otherSamples.get(i).feature);
			float leftDis = leftFeature.getDistance(otherSamples.get(i).leftFeature);
			float rightDis = leftFeature.getDistance(otherSamples.get(i).rightFeature);

			dis = (float) Math.sqrt(dis * dis + leftDis * leftDis + rightDis * rightDis);

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
