package sample;

import java.util.Vector;

import Geometry.Feature;
import Geometry.Point;

public class Sample
{
	//��ǰstroke��ĵ�a���ʴ�
	public int a;
	
	//����stroke�ĵ�b������
	public int b;
	
	public Feature feature;
	

	public Sample(Vector<Point> points,int index)
	{
		feature = new Feature(points, index);
	}
	
	
}
