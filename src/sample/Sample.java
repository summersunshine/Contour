package sample;

import java.util.Comparator;
import java.util.Vector;

import Geometry.Point;

public class Sample
{
	//��ǰstroke��ĵ�a���ʴ�
	public int a;
	
	//����stroke�ĵ�b������
	public int b;
	
	public Feature feature;
	

	public Sample(Vector<Point> points,int a,int b)
	{
		this.a = a;
		this.b = b;
		feature = new Feature(points,a, b);
	}
	
	
	
	
}
