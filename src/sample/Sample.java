package sample;

import feature.Feature;
import geometry.Point;

import java.awt.Graphics2D;
import java.util.Vector;

public class Sample
{
	
	// ��ǰstroke��ĵ�a���ʴ�
	public int a;

	// ����stroke�ĵ�b������
	public int b;
	
	//�����ת��
	public double angle;

	// ���ĵ�����
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
	 * ����shape context
	 * */
	public void drawShapeContext(Graphics2D graphics2d)
	{
		feature.drawShapeContext(graphics2d);
	}
	
	

}
