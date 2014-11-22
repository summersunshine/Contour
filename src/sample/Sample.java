package sample;

import java.awt.Graphics2D;
import java.util.Vector;

import Geometry.Point;

public class Sample
{
	// ��ǰstroke��ĵ�a���ʴ�
	public int a;

	// ����stroke�ĵ�b������
	public int b;

	// ����
	public Feature feature;

	public Sample(Vector<Point> points, int a, int b)
	{
		this.a = a;
		this.b = b;
		feature = new Feature(points, a, b);
	}

	/**
	 * ����shape context
	 * */
	public void drawShapeContext(Graphics2D graphics2d)
	{
		feature.drawShapeContext(graphics2d);
	}
}
