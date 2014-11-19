package sample;

import java.util.Vector;

import Geometry.Point;

public class LibSample extends Sample
{
	//保存与查询的笔触其他点之间的特征的距离
	public float [] distance;
	
	
	public LibSample(Vector<Point> points, int a,int b)
	{
		super(points, b);
		// TODO Auto-generated constructor stub
		this.a  = a;
	}


}
