package stroke;

import feature.Cost;
import geometry.Geometry;
import geometry.Point;
import geometry.SamplePoint;

import java.awt.Graphics2D;
import java.util.Vector;

import sample.QuerySample;
import config.Penalty;

public class QueryStroke extends Stroke
{

	// 查询样例
	public Vector<QuerySample>	querySamples;

	private Vector<Double>		angleDoubles;

	/**
	 * @param points
	 * @param rightCountourPoints
	 * @param leftCountourPoints
	 * */
	// public QueryStroke(Vector<Point> points, Vector<Point>
	// rightCountourPoints, Vector<Point> leftCountourPoints)
	// {
	// super();
	// this.points = points;
	// this.rightContourPoints = rightCountourPoints;
	// this.leftContourPoints = leftCountourPoints;
	// this.calDirAngle();
	// this.setAverageR();
	// this.initQuerySample();
	// }

	/**
	 * @param points
	 * @param rightCountourPoints
	 * @param leftCountourPoints
	 * */
	public QueryStroke(Vector<Point> points, Vector<Double> angleDoubles, Vector<Point> rightCountourPoints, Vector<Point> leftCountourPoints)
	{
		super();
		this.points = points;
		this.angleDoubles = angleDoubles;
		this.rightContourPoints = rightCountourPoints;
		this.leftContourPoints = leftCountourPoints;
		this.calDirAngle();
		this.setAverageR();
		this.initQuerySample();
	}

	/**
	 * 初始化查询样例
	 * */
	public void initQuerySample()
	{
		this.querySamples = new Vector<QuerySample>();

		for (int i = 0; i < points.size(); i++)
		{
			// double angle = angleDoubles.get(i).doubleValue();
			this.querySamples.addElement(new QuerySample(points, dirAngle.get(i), averageR, -1, i));
		}
	}

	public void drawShapeContext(Graphics2D graphics2d, int i)
	{
		querySamples.get(i).drawShapeContext(graphics2d);
	}

	/**
	 * 加上Transition 数据
	 * 
	 * @param otherSamples
	 * */
	public void addTransitionData(Vector<LibStroke> libStrokes)
	{
		for (int i = 1; i < querySamples.size(); i++)
		{
			Cost lastCost = querySamples.get(i - 1).costs.get(0);

			for (int j = 0; j < querySamples.get(i).costs.size(); j++)
			{
				int a = querySamples.get(i).costs.get(j).a;
				int b = querySamples.get(i).costs.get(j).b;
				if (a == lastCost.a)
				{
					if (b == lastCost.b)
					{
						querySamples.get(i).costs.get(j).addEt(0);
					}
					else if (b == lastCost.b)
					{
						querySamples.get(i).costs.get(j).addEt(Penalty.Cr);
					}
					else if (b == lastCost.b + 2)
					{
						querySamples.get(i).costs.get(j).addEt(Penalty.Cs);
					}
					else
					{
						Point v1 = libStrokes.get(lastCost.a).libSamples.get(lastCost.b).velocity;
						Point v2 = libStrokes.get(a).libSamples.get(b).velocity;
						int penalty = (int) (Penalty.Ct + Penalty.Cp * (1 - Geometry.getAngle(v1, v2)));
						querySamples.get(i).costs.get(j).addEt(penalty);
					}
				}
			}

			querySamples.get(i).sort();
		}
	}

}
