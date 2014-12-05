package util;

import geometry.Geometry;
import geometry.Point;
import geometry.Point.AngleComparator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Vector;

import stroke.LibStroke;

/**
 * ץȡ����
 * */
public class PixelGrabberUtil
{
	public static BufferedImage getIamge(LibStroke libStroke,int start,int end)
	{
		BufferedImage image = new BufferedImage(libStroke.width, libStroke.height, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();


		for (int i = start; i < end - 1; i++)
		{
			Point point1 = libStroke.leftContourPoints.get(i);
			Point point2 = libStroke.leftContourPoints.get(i + 1);

			Point point3 = libStroke.rightContourPoints.get(i + 1);
			Point point4 = libStroke.rightContourPoints.get(i);



			// ��ȡ�����ĸ���������С��x��yֵ
			int minX = (int) Math.floor(Math.min(Math.min(Math.min(point1.x, point2.x), point3.x), point4.x));
			int maxX =  (int) Math.ceil(Math.max(Math.max(Math.max(point1.x, point2.x), point3.x), point4.x));
			int minY =  (int) Math.floor(Math.min(Math.min(Math.min(point1.y, point2.y), point3.y), point4.y));
			int maxY = (int) Math.ceil(Math.max(Math.max(Math.max(point1.y, point2.y), point3.y), point4.y));

			minX = minX < 0 ? 0 : minX;
			maxX = maxX > image.getWidth()-1 ? image.getWidth() -1: maxX;
			minY = minY < 0 ? 0 : minY;
			maxY = maxY > image.getHeight()-1 ? image.getHeight() -1: maxY;


			
			// ѭ�����ж��Ƿ����ı�����
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					int or = libStroke.tightImage.getRGB(x, y)|image.getRGB(x, y);
					
					if ( or == -16777216)
					{
						graphics2d.setColor(new Color(255,255,255));
						graphics2d.drawRect(x, y, 1, 1);
					}
				}
			}
			
		}

//		BufferedImage cloneImage = getCloneImage(libStroke.sourceImage);
//		
//		for (int x = 0; x < image.getWidth(); x++)
//		{
//			for (int y = 0; y < image.getHeight(); y++)
//			{
//				int rgb =image.getRGB(x, y);
//				if (image.getRGB(x, y) == -16777216)
//				{
//					cloneImage.setRGB(x, y, -1);
//				}
//			}
//		}
//		return cloneImage;

		BufferedImage cloneImage = ImageUtil.getCloneImage(libStroke.alphaImage);
		
		for (int x = 0; x < image.getWidth(); x++)
		{
			for (int y = 0; y < image.getHeight(); y++)
			{
				if (image.getRGB(x, y) == -16777216)
				{
					cloneImage.setRGB(x, y, -16777216);
				}
			}
		}
		return cloneImage;
		
	}
	



	
	@SuppressWarnings("unchecked")
	public static Vector<Point> getClockWisePoints(Vector<Point> points)
	{
		Point midPoint = new Point();
		
		for (int i = 0; i < points.size(); i++)
		{
			midPoint.add(points.get(i));
		}
		
		midPoint = midPoint.div(points.size());
		
		
		for (int i = 0; i < points.size(); i++)
		{
			points.get(i).sub(midPoint);
		}
		
		Collections.sort(points,new Point.AngleComparator());
		

		for (int i = 0; i < points.size(); i++)
		{
			points.get(i).add(midPoint);
		}
		
		return points;
		
//		for (int i = 0; i < points.size(); i++)
//		{
//			Geometry.getAngle(points.get(i).sub(midPoint));
//		}
		
	}
	
	
	
	public static boolean isPointInPolygon(Point point, Vector<Point> polygon)
	{
		double angle =0;
		
		
		for (int i = 0; i < polygon.size()-1; i++)
		{
			Point point1= polygon.get(i).sub(point);
			Point point2 = polygon.get(i+1).sub(point);
			double temp  =  Geometry.getAngle(point1, point2)*180/Math.PI;
			angle +=temp;
		}
		
		System.out.println("angle :" + angle);
		return Math.abs(angle-360) < 60;
	}

	public static boolean isIntersect(double px1, double py1, double px2, double py2, double px3, double py3, double px4, double py4)
	{
		boolean flag = false;
		double d = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);
		if (d != 0)
		{
			double r = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;
			double s = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;
			if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1))
			{
				flag = true;
			}
		}
		return flag;
	}

	public static boolean isPointOnLine(double px0, double py0, double px1, double py1, double px2, double py2)
	{
		boolean flag = false;
		double ESP = 1e-9;
		if ((Math.abs(Multiply(px0, py0, px1, py1, px2, py2)) < ESP) && ((px0 - px1) * (px0 - px2) <= 0) && ((py0 - py1) * (py0 - py2) <= 0))
		{
			flag = true;
		}
		return flag;
	}

	public static double Multiply(double px0, double py0, double px1, double py1, double px2, double py2)
	{
		return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));
	}
}