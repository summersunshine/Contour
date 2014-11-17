package Geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

public class Histogram
{
	
	public static final float ratio = 4;
	public static final int pBins = 5;
	public static final int angleBins = 6;
	
	Point sourcePoint;
	
	private Vector<LogPolar> logPolars;
	
	private int [][] statistics = new int[pBins][angleBins]; 
	
	public 	Histogram(Vector<Point> points, int sourceIndex)
	{
		logPolars = new Vector<LogPolar>();
		
		sourcePoint = points.get(sourceIndex);
		
		for (int i = 0; i < points.size(); i++)
		{
			if (sourceIndex!=i)
			{
				LogPolar logPolar = new LogPolar(points.get(i).sub(sourcePoint));

				int x = (int) ((int) logPolar.p);
				int y = (int)(logPolar.angle*angleBins/360);
				if (x < pBins)
				{
					System.out.println("x: "+x + " y: " + y);
					statistics[x][y] ++;
				}
			}
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
	
	
	
}
