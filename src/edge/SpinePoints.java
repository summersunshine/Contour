package edge;

import geometry.BezierCurve;
import geometry.Geometry;
import geometry.Point;
import geometry.SamplePoint;

import java.util.Vector;

import util.ImageUtil;
import config.Global;
import config.SampleConfig;

//获取路径走向
public class SpinePoints
{
	public static int			SAMPLE_RATE	= 6;
	public static double		maxRadius	= 1.4;
	public static float			scale		= 0.6f;

	private Vector<Point>		originPoints;
	private Vector<Point>		middlePoints;
	private Vector<Point>		extraPoints;
	public Vector<Point>		spinePoints;
	public Vector<Double>		angleDoubles;
	public Vector<Double>		radius;
	public Vector<SamplePoint>	spineSamplePoints;

	public SpinePoints(Vector<Point> points)
	{

		this.middlePoints = new Vector<Point>();
		this.extraPoints = new Vector<Point>();
		this.spinePoints = new Vector<Point>();
		this.angleDoubles = new Vector<Double>();
		this.radius = new Vector<Double>();
		this.spineSamplePoints = new Vector<SamplePoint>();

		this.sparsePoint(points);
		this.addAssistPoint();
		this.createMidPoints();
		this.createExtraPoints();
		this.removeAssistPoint();
		this.setSpineSamplePoints();

		ImageUtil.drawPointImage(points, SampleConfig.OUTPUT_PATH + "After\\Points.jpg");
		ImageUtil.drawPointImage(originPoints, SampleConfig.OUTPUT_PATH + "After\\SparsePoints.jpg");
		ImageUtil.drawPointImage(spinePoints, SampleConfig.OUTPUT_PATH + "After\\SpinePoints.jpg");
	}

	public static double getRatio(Point point1, Point point2, Point point3)
	{
		double cos = Geometry.getCos(point2.sub(point1), point3.sub(point2));

		if (cos < 0)
		{
			return 2;
		}
		else
		{
			return 1 + Math.sqrt(1 - cos);
		}
	}

	private void sparsePoint(Vector<Point> points)
	{
		this.originPoints = new Vector<Point>();
		for (int i = 0; i < points.size(); i++)
		{
			if (i % SAMPLE_RATE == 0)
			{
				this.originPoints.add(points.get(i));
			}
		}

	}

	/**
	 * 添加辅助点
	 * */
	private void addAssistPoint()
	{
		Point firstPoint = originPoints.firstElement();
		Point secondPoint = originPoints.get(1);
		Point tempFirstPoint = Point.getSymmetryPoint(firstPoint, secondPoint);
		originPoints.add(0, tempFirstPoint);

		Point lastPoint = originPoints.lastElement();
		Point lastSecondPoint = originPoints.get(originPoints.size() - 2);
		Point tempLastPoint = Point.getSymmetryPoint(lastPoint, lastSecondPoint);
		originPoints.add(tempLastPoint);
	}

	/**
	 * 添加辅助点
	 * */
	private void removeAssistPoint()
	{
		originPoints.remove(0);

		originPoints.remove(originPoints.lastElement());
	}

	/**
	 * 生成中点
	 * */
	public void createMidPoints()
	{
		// 生成中点
		for (int i = 0; i < originPoints.size() - 1; i++)
		{
			Point currPoint = originPoints.get(i);
			Point nextPoint = originPoints.get(i + 1);
			Point midPoint = Point.getMidPoint(currPoint, nextPoint);
			middlePoints.add(midPoint);
		}
	}

	/**
	 * 获取额外的控制点
	 * */
	public void createExtraPoints()
	{
		for (int i = 0; i < middlePoints.size() - 1; i++)
		{
			// 当前原始点
			Point currOriginPoint = originPoints.get(i + 1);

			// 当前的中点
			Point currMiddlePoint = middlePoints.get(i);
			Point nextMiddlePoint = middlePoints.get(i + 1);

			// 两个中点的中点
			Point midInMidPoint = Point.getMidPoint(currMiddlePoint, nextMiddlePoint);
			Point offsetPoint = currOriginPoint.sub(midInMidPoint);

			// 当前的控制点
			Point currExtraPoint = currMiddlePoint.add(offsetPoint);
			Point nextExtraPoint = nextMiddlePoint.add(offsetPoint);

			currExtraPoint = Point.getPointBetweenTweenPoint(currExtraPoint, currOriginPoint, scale);
			nextExtraPoint = Point.getPointBetweenTweenPoint(nextExtraPoint, currOriginPoint, scale);

			extraPoints.add(currExtraPoint);
			extraPoints.add(nextExtraPoint);
		}
	}

	public void setSpineSamplePoints()
	{
		// radius.add((double) 0.3);
		for (int i = 0; i < originPoints.size() - 1; i++)
		{
			createBezierCurves(i);
		}

	}

	/**
	 * 在控制点生成*的bezier曲线上进行采样
	 * */
	public void createBezierCurves(int index)
	{
		Point[] controlPoint = new Point[4];
		controlPoint[0] = originPoints.get(index);
		controlPoint[1] = extraPoints.get(2 * index + 1);
		controlPoint[2] = extraPoints.get(2 * index + 2);
		controlPoint[3] = originPoints.get(index + 1);

		// 两个控制点直接的距离
		double length = controlPoint[0].sub(controlPoint[3]).length();

		// 依据距离分成诺干等分
		double num = Math.ceil(length / Global.SAMPLE_DIST);

		double interval = 1 / num;

		double lastRadius;

		if (index == 0)
		{
			lastRadius = 0.3;
		}
		else
		{
			lastRadius = radius.lastElement();
		}

		double nextRadius;
		if (index == originPoints.size() - 2)
		{
			nextRadius = 0.3;
		}
		else
		{
			Point point1 = originPoints.get(index);
			Point point2 = originPoints.get(index + 1);
			Point point3 = originPoints.get(index + 2);
			nextRadius = getRatio(point1, point2, point3);
			nextRadius = nextRadius < maxRadius ? nextRadius : maxRadius;
		}
		System.out.println("LastRadius :" + lastRadius + " nextRadius :" + nextRadius);

		spineSamplePoints = new Vector<SamplePoint>();
		for (float j = 1; j >= interval / 3; j -= interval)
		{
			Point point = BezierCurve.bezier3func(j, controlPoint);
			radius.add(lastRadius * j + nextRadius * (1 - j));
			spinePoints.add(point);
			// spineSamplePoints.add(new SamplePoint(point, angle));
		}
	}
}
