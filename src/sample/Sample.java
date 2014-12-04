package sample;

import feature.Feature;
import geometry.Point;

import java.awt.Graphics2D;
import java.util.Vector;

public class Sample
{
	
	// 当前stroke库的第a个笔触
	public int a;

	// 属于stroke的第b个样例
	public int b;
	
	//相对旋转点
	public double angle;

	// 中心点特征
	public Feature feature;

	public float percent;

	public Sample(Vector<Point> points,double angle,double averageR, int a, int b)
	{
		this.a = a;
		this.b = b;
		this.feature = new Feature(points,angle,averageR, a, b);
		this.percent = b * 1.0f / (points.size() - 1);
	}

	/**
	 * 绘制shape context
	 * */
	public void drawShapeContext(Graphics2D graphics2d)
	{
		feature.drawShapeContext(graphics2d);
	}
	
	

}
