package sample;

import java.awt.Graphics2D;
import java.io.File;
import java.util.Vector;

import Geometry.Point;
import config.SampleConfig;

public class Feature
{
	public static boolean isLoadBegin = false;

	// ��ǰstroke��ĵ�a���ʴ�
	public int a;

	// ����stroke�ĵ�b������
	public int b;

	// ��ǰ��֮ǰ��shapeConext
	public ShapeConext historyShapeConext;

	// ��ǰ��֮���shapecontext
	public ShapeConext futureShapeConext;

	public Feature(Vector<Point> points, int a, int b)
	{
		this.a = a;
		this.b = b;
		this.historyShapeConext = new ShapeConext(points, b, -1);
		this.futureShapeConext = new ShapeConext(points, b, 1);

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
		this.historyShapeConext.drawCoordinateSystem(graphics2d);
		this.historyShapeConext.drawShapeContext(graphics2d);
		this.futureShapeConext.drawShapeContext(graphics2d);
	}

	/**
	 * ��ȡ����������֮��ľ���
	 * 
	 * @param otherFeature
	 * */
	public float getDistance(Feature otherFeature)
	{
		float historyDistance = this.historyShapeConext.getDistanceL2(otherFeature.historyShapeConext);
		float futureDistance = this.futureShapeConext.getDistanceL2(otherFeature.futureShapeConext);

		return (float) Math.sqrt(historyDistance * historyDistance + futureDistance * futureDistance);
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

		historyShapeConext.createShapeContextImage(SampleConfig.OUTPUT_PATH + a + "\\img\\" + b + "_histroy.jpg");
		futureShapeConext.createShapeContextImage(SampleConfig.OUTPUT_PATH + a + "\\img\\" + b + "_future.jpg");
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

		historyShapeConext.createShapeContextText(SampleConfig.OUTPUT_PATH + a + "\\txt\\" + b + "_histroy.txt");
		futureShapeConext.createShapeContextText(SampleConfig.OUTPUT_PATH + a + "\\txt\\" + b + "_future.txt");
	}
}
