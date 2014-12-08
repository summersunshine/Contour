package feature;

import geometry.Point;

import java.awt.Graphics2D;
import java.io.File;
import java.util.Vector;

import util.ShapeContextUtil;
import config.SampleConfig;

public class Feature
{
	public static boolean isLoadBegin = false;

	// ��ǰstroke��ĵ�a���ʴ�
	public int a;

	// ����stroke�ĵ�b������
	public int b;

	public double angle;

	public double maxR;

	// ��ǰ��֮ǰ��shapeConext
	public ShapeContext historyShapeConext;

	// ��ǰ��֮���shapecontext
	public ShapeContext futureShapeConext;

	public Vector<Point> points;

	public Feature(Vector<Point> points, double angle, double maxR, int a, int b)
	{
		this.a = a;
		this.b = b;
		this.points = points;
		this.angle = angle;
		this.historyShapeConext = new ShapeContext(points, angle, maxR, b, -1);
		this.futureShapeConext = new ShapeContext(points, angle, maxR, b, 1);

		if (Feature.isLoadBegin)
		{
			this.createShapeContextImage();
			this.createShapeContextTxt();
		}

	}

	/**
	 * ����shape context
	 * */
	public void drawShapeContext(Graphics2D graphics2d)
	{
		// ShapeContextUtil.drawCoordinateSystem();
		ShapeContextUtil.drawCoordinateSystem(historyShapeConext, graphics2d);
		ShapeContextUtil.drawShapeContext(historyShapeConext, graphics2d);
		ShapeContextUtil.drawShapeContext(futureShapeConext, graphics2d);

	}

	/**
	 * ��ȡ����������֮��ľ���
	 * 
	 * @param otherFeature
	 * */
	public float getDistance(Feature otherFeature)
	{
		// float historyDistance =
		// this.historyShapeConext.getDistanceL2(otherFeature.historyShapeConext);
		// float futureDistance =
		// this.futureShapeConext.getDistanceL2(otherFeature.futureShapeConext);

		// float historyDistance =
		// this.historyShapeConext.getChiSquare(otherFeature.historyShapeConext);
		// float futureDistance =
		// this.futureShapeConext.getChiSquare(otherFeature.futureShapeConext);

		// return (float) ((float) Math.sqrt(historyDistance * historyDistance +
		// futureDistance * futureDistance));

		return getShapeDistance(otherFeature) + getEndDistance(otherFeature);
	}

	public float getShapeDistance(Feature otherFeature)
	{

		float historyDistance = this.historyShapeConext.getChiSquare(otherFeature.historyShapeConext);
		float futureDistance = this.futureShapeConext.getChiSquare(otherFeature.futureShapeConext);

		return (float) ((float) Math.sqrt(historyDistance * historyDistance + futureDistance * futureDistance));
	}

	public float getHistoryEndDistance()
	{
		return (float) Math.min(1, Math.sqrt(b / ShapeContext.winSize));
	}

	public float getFutureEndDistance()
	{
		return (float) Math.min(1, Math.sqrt((points.size() - b) / ShapeContext.winSize));
	}

	public float getEndDistance(Feature otherFeature)
	{

		float historyDistance = getHistoryEndDistance() - otherFeature.getHistoryEndDistance();
		float futureDistance = getFutureEndDistance() - otherFeature.getFutureEndDistance();

		return (float) ((float) Math.sqrt(historyDistance * historyDistance + futureDistance * futureDistance));
	}

	/**
	 * ��ͼ����ʽ�ֱ����ǰ���shape context
	 * */
	public void createShapeContextImage()
	{
		File file = new File(SampleConfig.OUTPUT_PATH + a + "\\img\\");
		if (!file.exists())
		{
			file.mkdirs();
		}

		ShapeContextUtil.createShapeContextImage(historyShapeConext, SampleConfig.OUTPUT_PATH + a + "\\img\\" + b + "_histroy.jpg");
		ShapeContextUtil.createShapeContextImage(futureShapeConext, SampleConfig.OUTPUT_PATH + a + "\\img\\" + b + "_future.jpg");

	}

	/**
	 * ���ı���ʽ���ǰ���shape context
	 * */
	public void createShapeContextTxt()
	{
		File file = new File(SampleConfig.OUTPUT_PATH + a + "\\txt\\");
		if (!file.exists())
		{
			file.mkdirs();
		}

		ShapeContextUtil.createShapeContextText(historyShapeConext, SampleConfig.OUTPUT_PATH + a + "\\img\\" + b + "_histroy.txt");
		ShapeContextUtil.createShapeContextText(futureShapeConext, SampleConfig.OUTPUT_PATH + a + "\\img\\" + b + "_future.txt");

	}
}
