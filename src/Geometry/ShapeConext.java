package Geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

public class ShapeConext
{
	
	public static final float ratio = 4;
	public static final int pBins = 5;
	public static final int angleBins = 6;
	
	Point sourcePoint;
	
	private Vector<LogPolar> logPolars;
	private Vector<Point> points;
	private int sourceIndex;
	
	
	private int [][] statistics = new int[pBins][angleBins]; 
	
	
	
	/**
	 * 构造函数
	 * 
	 * @param points
	 * @param sourceIndex
	 * @param dir
	 * 
	 * 如果dir>0，只统计sourceIndex之后的点
	 * 如果dir<0, 只统计souceIndex之前的点
	 * 如果dir=0, 则统计所有的点
	 * */
	public 	ShapeConext(Vector<Point> points, int sourceIndex,int dir)
	{
		logPolars = new Vector<LogPolar>();
		
		sourcePoint = points.get(sourceIndex);
		
		this.sourceIndex = sourceIndex;
		
		
		if (dir>0)
		{
			countForFuture();
		}
		else if (dir<0)
		{
			countForHistory();
		}
		else
		{
			countForAll();
		}

	}
	
	
	private void countForFuture()
	{
		for (int i = sourceIndex+1; i < points.size(); i++)
		{
			count(i);
		}
	}
	
	private void countForHistory()
	{
		for (int i = 0; i < sourceIndex; i++)
		{
			count(i);
		}
	}
	
	private void countForAll()
	{
		for (int i = 0; i < points.size(); i++)
		{
			if (sourceIndex!=i)
			{
				count(i);
			}
		}
	}
	

	
	private void count(int i)
	{
		LogPolar logPolar = new LogPolar(points.get(i).sub(sourcePoint));
		logPolars.addElement(logPolar);
		
		int x = (int)(logPolar.p);
		int y = (int)(logPolar.angle*angleBins/360);
		
		if (x < pBins)
		{
			System.out.println("x: "+x + " y: " + y);
			statistics[x][y] ++;
		}
	}
	
	
	
	public void drawHistogram(Graphics2D graphics2d)
	{
		//graphics2d.drawRect(0, 500, 150, 360);
		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				int value = 245 - statistics[i][j]*10;
				value = value < 0 ? 0:value;
				
				graphics2d.setColor(new Color(value, value,value));
				graphics2d.fillRect(j*30,600-i*30, 30, 30);
				
			}
		}
	}
	
	public void drawCoordinateSystem(Graphics2D graphics2d)
	{
		graphics2d.setColor(new Color(255,0,0));
		
		sourcePoint.print();
		for (int i = 1; i <= pBins; i++)
		{
			double radius = Math.exp(i)*ratio;
			graphics2d.drawOval((int)(sourcePoint.x - radius/2), (int)(sourcePoint.y-radius/2), (int)radius, (int)radius);
		}
		
		for (int i = 0; i < angleBins; i++)
		{
			float endX = (float) (Math.exp(pBins)*ratio*Math.cos(i*2f/angleBins*Math.PI));
			float endY = (float) (Math.exp(pBins)*ratio*Math.sin(i*2f/angleBins*Math.PI));
			graphics2d.drawLine((int)sourcePoint.x, (int)sourcePoint.y, (int)(endX+sourcePoint.x), (int)(endY+sourcePoint.y));
		}
	}
	
	
	/**
	 * 曼哈顿距离
	 * */
	public int getDistanceL1(ShapeConext shapeConext)
	{
		int distance = 0;

		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				distance += Math.abs(shapeConext.statistics[i][j] - statistics[i][j]);
			}
		}
		return distance;
	}
	
	
	/**
	 * 欧拉距离
	 * */
	public  float getDistanceL2( ShapeConext shapeConext)
	{
		float distance = 0;

		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				distance += Math.pow(shapeConext.statistics[i][j] - statistics[i][j], 2);
			}
		}
		return (float) Math.sqrt(distance);
	}
}
