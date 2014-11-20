package sample;

import java.util.Comparator;
import java.util.Vector;

import Geometry.Point;

public class Sample
{
	//当前stroke库的第a个笔触
	public int a;
	
	//属于stroke的第b个样例
	public int b;
	
	public Feature feature;
	

	public Sample(Vector<Point> points,int a,int b)
	{
		this.a = a;
		this.b = b;
		feature = new Feature(points,a, b);
	}
	
	
	
	
}
