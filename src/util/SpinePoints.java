package util;

import geometry.BezierCurve;
import geometry.Point;
import geometry.SamplePoint;

import java.util.Vector;

import config.Global;

//��ȡ·������
public class SpinePoints
{
	public static float scale = 0.6f;
	
	private Vector<Point> originPoints;
	private Vector<Point> middlePoints;
	private Vector<Point> extraPoints;
	public Vector<Point> spinePoints;
	public Vector<Double> angleDoubles;
	public Vector<SamplePoint> spineSamplePoints;
	
	
	public SpinePoints(Vector<Point> points)
	{
		
		this.middlePoints = new Vector<Point>();
		this.extraPoints = new Vector<Point>();
		this.spinePoints = new Vector<Point>();
		this.angleDoubles = new Vector<Double>();
		this.spineSamplePoints = new Vector<SamplePoint>();

		this.sparsePoint(points);
		this.addAssistPoint();
		this.createMidPoints();
		this.createExtraPoints();
		this.removeAssistPoint();
		this.setSpineSamplePoints();
	}
	
	
	private void sparsePoint(Vector<Point> points)
	{
		this.originPoints = new Vector<Point>();
		for (int i = 0; i < points.size(); i++)
		{
			if (i%4==0)
			{
				this.originPoints.add(points.get(i));
			}
		}
	}
	
	/**
	 * ��Ӹ�����
	 * */
	private void addAssistPoint()
	{
		Point firstPoint = originPoints.firstElement();
		Point secondPoint = originPoints.get(1);
		Point tempFirstPoint = Point.getSymmetryPoint(firstPoint, secondPoint);
		originPoints.add(0,tempFirstPoint);

		Point lastPoint = originPoints.lastElement();
		Point lastSecondPoint = originPoints.get(originPoints.size() - 2);
		Point tempLastPoint = Point.getSymmetryPoint(lastPoint, lastSecondPoint);
		originPoints.add(tempLastPoint);
	}
	
	/**
	 * ��Ӹ�����
	 * */
	private void removeAssistPoint()
	{
		originPoints.remove(0);

		originPoints.remove(originPoints.lastElement());
	}
	
	/**
	 * �����е�
	 * */
	public void createMidPoints()
	{
		// �����е�
		for (int i = 0; i < originPoints.size() - 1; i++)
		{
			Point currPoint = originPoints.get(i);
			Point nextPoint = originPoints.get(i+1);
			Point midPoint = Point.getMidPoint(currPoint, nextPoint);
			middlePoints.add(midPoint);
		}
	}
	
	
	/**
	 * ��ȡ����Ŀ��Ƶ�
	 * */
	public void createExtraPoints()
	{
		for (int i = 0; i < middlePoints.size()-1; i++)
		{
			// ��ǰԭʼ��
			Point currOriginPoint = originPoints.get(i+1);

			// ��ǰ���е�
			Point currMiddlePoint = middlePoints.get(i);
			Point nextMiddlePoint = middlePoints.get(i + 1);

			// �����е���е�
			Point midInMidPoint = Point.getMidPoint(currMiddlePoint, nextMiddlePoint);
			Point offsetPoint = currOriginPoint.sub(midInMidPoint);

			// ��ǰ�Ŀ��Ƶ�
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
		for (int i = 0; i < originPoints.size()-1; i++)
		{
			createBezierCurves(i);
		}
	}
	
	/**
	 * �ڿ��Ƶ�����*��bezier�����Ͻ��в���
	 * */
	public void createBezierCurves(int index)
	{
		Point[]controlPoint = new Point[4];
		controlPoint[0] = originPoints.get(index);
		controlPoint[1] = extraPoints.get(2 * index + 1);
		controlPoint[2] = extraPoints.get(2 * index + 2);
		controlPoint[3] = originPoints.get(index + 1);
		
		//�������Ƶ�ֱ�ӵľ���
		double length = controlPoint[0].sub(controlPoint[3]).length();
		
		//���ݾ���ֳ�ŵ�ɵȷ�
		double num = Math.ceil(length/Global.SAMPLE_DIST);
		
		double interval = 1/num;
		
		spineSamplePoints = new Vector<SamplePoint>();
		for (float j = 1; j >= interval/2; j -= interval)
		{
			Point point = BezierCurve.bezier3func(j, controlPoint);
			double angle = BezierCurve.berzier3Angle(j, controlPoint);
			spinePoints.add(point);
			angleDoubles.add(angle);
			spineSamplePoints.add(new SamplePoint(point, angle));
		}
	}
	
	


}
