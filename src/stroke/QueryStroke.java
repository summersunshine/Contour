package stroke;

import geometry.Point;

import java.awt.Graphics2D;
import java.util.Vector;

import sample.QuerySample;
import sequence.QuerySegement;
import config.SegementConfig;

public class QueryStroke extends Stroke
{
	public static final int			STATE_SMALL	= 0;
	public static final int			STAET_LARGE	= 1;

	// 查询样例
	public Vector<QuerySample>		querySamples;

	public Vector<QuerySegement>	querySegements;

	private Vector<Double>			radius;

	private int						currState;

	private int						boundaryIndex;

	/**
	 * @param points
	 * @param rightCountourPoints
	 * @param leftCountourPoints
	 * */
	public QueryStroke(Vector<Point> points, Vector<Double> radius, Vector<Point> rightCountourPoints, Vector<Point> leftCountourPoints)
	{
		super();
		this.points = points;
		this.radius = radius;
		this.rightContourPoints = rightCountourPoints;
		this.leftContourPoints = leftCountourPoints;
		this.calDirAngle();
		this.setAverageR();
		this.initQuerySample();
		this.divideIntoSegements();
		this.printQuerySegements();
		this.mergeQuerySegements();
		this.printQuerySegements();
		this.removeLongSegements();
		this.printQuerySegements();
		this.extentQuerySegements();
		this.printQuerySegements();
	}

	/**
	 * 初始化查询样例
	 * */
	public void initQuerySample()
	{
		this.querySamples = new Vector<QuerySample>();

		for (int i = 0; i < points.size(); i++)
		{
			this.querySamples.addElement(new QuerySample(points, radius.get(i), dirAngle.get(i), averageR, -1, i));
		}
	}

	public void divideIntoSegements()
	{
		querySegements = new Vector<QuerySegement>();
		currState = STATE_SMALL;
		boundaryIndex = 0;
		for (int i = 0; i < querySamples.size() - 1; i++)
		{
			boolean isLarge = querySamples.get(i).radius > SegementConfig.RADIUS_THRESHOLD;
			if (currState == STAET_LARGE)
			{
				if (!isLarge)
				{
					querySegements.add(new QuerySegement(boundaryIndex, i, QuerySegement.TYPE_LARGE));
					boundaryIndex = i;
					currState = STATE_SMALL;
				}
			}
			else
			{
				if (isLarge)
				{

					querySegements.add(new QuerySegement(boundaryIndex, i, QuerySegement.TYPE_SMALL));
					boundaryIndex = i;
					currState = STAET_LARGE;

				}
			}
		}
		querySegements.add(new QuerySegement(boundaryIndex, querySamples.size() - 1, QuerySegement.TYPE_SMALL));

	}

	public void mergeQuerySegements()
	{
		// 小于两段就不处理啦
		if (querySamples.size() <= 2)
		{
			return;
		}
		for (int i = 0; i < querySegements.size(); i++)
		{
			QuerySegement currSegement = querySegements.get(i);
			if (currSegement.getLength() < SegementConfig.MIN_LENGTH)
			{
				if (i != querySegements.size() - 1)
				{
					querySegements.get(i + 1).mergeSegement(currSegement);
					querySegements.remove(i);
					i--;
				}
				else
				{
					querySegements.get(i - 1).mergeSegement(currSegement);
					querySegements.remove(i);
				}

			}
		}
	}

	public void removeLongSegements()
	{
		for (int i = 0; i < querySegements.size(); i++)
		{
			if (querySegements.get(i).getLength() > SegementConfig.MAX_LENGTH)
			{
				int endIndex = querySegements.get(i).endIndex;
				int startIndex = endIndex - querySegements.get(i).getLength() / 2;
				int type = querySegements.get(i).type;
				querySegements.get(i).endIndex = startIndex;
				querySegements.add(i + 1, new QuerySegement(startIndex, endIndex, type));
				i--;
			}
		}
	}

	public void extentQuerySegements()
	{
		for (int i = 0; i < querySegements.size() - 1; i++)
		{
			QuerySegement querySegement = querySegements.get(i);
			if (querySegement.type == QuerySegement.TYPE_LARGE)
			{
				double maxRadius = radius.get(querySegement.startIndex);
				for (int j = querySegement.startIndex; j < querySegement.endIndex; j++)
				{
					if (maxRadius < radius.get(j))
					{
						maxRadius = radius.get(j);
					}
				}

				querySegement.extend((int) (SegementConfig.EXTENT_LENGTH * maxRadius));

			}

		}
	}

	public void printQuerySegements()
	{
		for (int i = 0; i < querySegements.size(); i++)
		{
			System.out.println("start: " + querySegements.get(i).startIndex + " end: " + querySegements.get(i).endIndex);
		}
	}

	public void drawShapeContext(Graphics2D graphics2d, int i)
	{
		querySamples.get(i).drawShapeContext(graphics2d);
	}

	public float getFeatureDistance(int i, int a, int b)
	{
		return querySamples.get(i).costMap.get(a + "_" + b);
	}

}
