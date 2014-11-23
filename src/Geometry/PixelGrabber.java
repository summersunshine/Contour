package Geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import javax.xml.transform.Templates;

import sample.LibStroke;

/**
 * 抓取像素
 * */
public class PixelGrabber
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

			// 依据不同的情况更改添加点的顺序
			Vector<Point> polygon = new Vector<Point>();
			polygon.add(point1);
			polygon.add(point2);
			polygon.add(point3);
			polygon.add(point4);
			
			polygon = getClockWisePoints(polygon);
			polygon.addElement(polygon.firstElement());
//			if (point1.x > point2.x)
//			{
//				polygon.add(point2);
//				polygon.add(point1);
//				polygon.add(point4);
//				polygon.add(point3);
//			}
//			else
//			{
//
//				polygon.add(point1);
//				polygon.add(point2);
//				polygon.add(point3);
//				polygon.add(point4);
//
//			}

			// 获取所有四个点的最大最小的x与y值
			int minX = (int) Math.floor(Math.min(Math.min(Math.min(point1.x, point2.x), point3.x), point4.x));
			int maxX =  (int) Math.ceil(Math.max(Math.max(Math.max(point1.x, point2.x), point3.x), point4.x));
			int minY =  (int) Math.floor(Math.min(Math.min(Math.min(point1.y, point2.y), point3.y), point4.y));
			int maxY = (int) Math.ceil(Math.max(Math.max(Math.max(point1.y, point2.y), point3.y), point4.y));

			minX = minX < 0 ? 0 : minX;
			maxX = maxX > image.getWidth()-1 ? image.getWidth() -1: maxX;
			minY = minY < 0 ? 0 : minY;
			maxY = maxY > image.getHeight()-1 ? image.getHeight() -1: maxY;

			// 循环，判断是否在四边形内
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					if (isPointInPolygon(new Point(x, y), polygon))
					{
						graphics2d.setColor(new Color(libStroke.sourceImage.getRGB(x, y)));
						graphics2d.drawRect(x, y, 1, 1);
					}
				}
			}
		}

		return image;
	}

	
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
	
//	public static boolean isPointInPolygon(double px, double py, Vector<Point> polygon)
//	{
//		boolean isInside = false;
//		double ESP = 1e-9;
//		int count = 0;
//		double linePoint1x;
//		double linePoint1y;
//		double linePoint2x = 180;
//		double linePoint2y;
//
//		linePoint1x = px;
//		linePoint1y = py;
//		linePoint2y = py;
//
//		for (int i = 0; i < polygon.size() - 1; i++)
//		{
//			double cx1 = polygon.get(i).x;
//			double cy1 = polygon.get(i).y;
//			double cx2 = polygon.get(i + 1).x;
//			double cy2 = polygon.get(i + 1).y;
//			if (isPointOnLine(px, py, cx1, cy1, cx2, cy2))
//			{
//				return true;
//			}
//			if (Math.abs(cy2 - cy1) < ESP)
//			{
//				continue;
//			}
//
//			if (isPointOnLine(cx1, cy1, linePoint1x, linePoint1y, linePoint2x, linePoint2y))
//			{
//				if (cy1 > cy2)
//					count++;
//			}
//			else if (isPointOnLine(cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y))
//			{
//				if (cy2 > cy1)
//					count++;
//			}
//			else if (isIntersect(cx1, cy1, cx2, cy2, linePoint1x, linePoint1y, linePoint2x, linePoint2y))
//			{
//				count++;
//			}
//		}
//		if (count % 2 == 1)
//		{
//			isInside = true;
//		}
//
//		return isInside;
//	}

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
