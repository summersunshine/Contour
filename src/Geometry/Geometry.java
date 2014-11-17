package Geometry;

import java.io.File;
import java.util.Vector;

import javax.print.attribute.standard.Sides;

public class Geometry
{

	public static Vector<Point> getContourPoints(Vector<Point> points,double width,boolean isTransition)
	{
		Vector<Point> contourPoints = new Vector<Point>();
		Point contourPoint;
		double ratio;
		
		//beginning
		ratio = isTransition?0.1:1;
		contourPoint = getContourPointAtBegin(points.get(0), points.get(1), width*ratio);
		contourPoints.add(contourPoint);
		
		//middle
		for (int i = 1; i < points.size()-1; i++)
		{
			if (isTransition)
			{
				if (i < points.size()/5)
				{
					ratio = i*5.0f/points.size();
					ratio = Math.sin(ratio*Math.PI/2);
				}
				else if (i > points.size()*4/5)
				{
					ratio = (points.size() - i)*5.0f/points.size();
					ratio = Math.sin(ratio*Math.PI/2);
				}
				else
				{
					ratio = 1;
				}
			}
			else
			{
				ratio = 1;
			}

			contourPoint = getContourPoint(points.get(i-1), points.get(i),points.get(i+1), ratio*width);
			contourPoints.add(contourPoint);
		}
		
		//end
		ratio = isTransition?0.1:1;
		contourPoint = getContourPointAtEnd(points.get(points.size()-1), points.get(points.size()-2), width*ratio);
		contourPoints.add(contourPoint);
		
		return contourPoints;
	}
	
	public static Point getContourPoint(Point lastPoint,Point currPoint,Point nextPoint,double width)
	{
		Point diffPoint1 = currPoint.sub(lastPoint);
		Point diffPoint2 = currPoint.sub(nextPoint);
		
		double angle = getAngle(diffPoint1, diffPoint2);
		double halfAngle = angle/2;
		double distance = width/(Math.sin(halfAngle));
		
		Point rotatePoint = getRotatePoint(diffPoint1, halfAngle);
		rotatePoint = rotatePoint.div(rotatePoint.length()).mul((float)distance);
		
		//System.out.println("angle: " + 180*angle/Math.PI);
		//rotatePoint.print();
		return currPoint.add(rotatePoint);
		
	}
	
	public static Point getRotatePoint(Point point,double angle)
	{
		double x = point.x*Math.cos(angle) - point.y*Math.sin(angle);
		double y = point.x*Math.sin(angle) + point.y*Math.cos(angle);
		
		return new Point((float)(x), (float)(y));
	}
	
	public static double getAngle(Point diffPoint1,Point diffPoint2)
	{
		double cos = (diffPoint1.x*diffPoint2.x + diffPoint1.y*diffPoint2.y)/(diffPoint1.length()*diffPoint2.length());
		if (cos > 1)
		{
			cos = 0.99;
		}
		if (cos < -1)
		{
			cos = -0.99;
		}
		return Math.acos(cos);
	}
	
	public static Point getContourPointAtBegin(Point endPoint,Point neighbourPoint,double width)
	{
		Point diffPoint = neighbourPoint.sub(endPoint);
		
		Point normalPoint = new Point(diffPoint.y,-diffPoint.x);
		
		float length = normalPoint.length();
		
		normalPoint = normalPoint.div(length).mul((float)width);
		
		return endPoint.sub(normalPoint);
		
	}
	
	public static Point getContourPointAtEnd(Point endPoint,Point neighbourPoint,double width)
	{
		Point diffPoint = neighbourPoint.sub(endPoint);
		
		Point normalPoint = new Point(diffPoint.y,-diffPoint.x);
		
		float length = normalPoint.length();
		
		normalPoint = normalPoint.div(length).mul((float)width);
		
		return endPoint.add(normalPoint);
		
	}
}
