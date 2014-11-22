package sample;

import java.awt.Graphics2D;
import java.io.File;
import java.util.Vector;

import Geometry.Point;
import config.SampleConfig;

public class Feature
{
	public static boolean isLoadBegin = false;

	// 当前stroke库的第a个笔触
	public int a;

	// 属于stroke的第b个样例
	public int b;

	// 当前点之前的shapeConext
	public ShapeConext historyShapeConext;

	// 当前点之后的shapecontext
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
	 * 绘制shape context
	 * */
	public void drawShapeContext(Graphics2D graphics2d)
	{
		this.historyShapeConext.drawCoordinateSystem(graphics2d);
		this.historyShapeConext.drawShapeContext(graphics2d);
		this.futureShapeConext.drawShapeContext(graphics2d);
	}

	/**
	 * 获取与其他特征之间的距离
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
	 * 以图形形式分别输出前后的shape context
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
	 * 以文本形式输出前后的shape context
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
