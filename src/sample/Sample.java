package sample;

import java.awt.Graphics2D;
import java.util.Vector;

import Geometry.Point;

public class Sample
{
	// 当前stroke库的第a个笔触
	public int a;

	// 属于stroke的第b个样例
	public int b;

	// 特征
	public Feature feature;

	public Sample(Vector<Point> points, int a, int b)
	{
		this.a = a;
		this.b = b;
		feature = new Feature(points, a, b);
	}

	/**
	 * 绘制shape context
	 * */
	public void drawShapeContext(Graphics2D graphics2d)
	{
		feature.drawShapeContext(graphics2d);
	}
}
