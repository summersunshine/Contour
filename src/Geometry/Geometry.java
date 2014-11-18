package Geometry;

import java.io.File;
import java.util.Vector;

import javax.print.attribute.standard.Sides;


public class Geometry
{
	
	/**
	 * 判断point1和point2组成的线段与point3和point4组成的线段是否相交
	 * */
	public static boolean isIntersect(Point point1, Point point2, Point point3, Point point4)
	{

		boolean flag = false;
		double d = (point2.x - point1.x) * (point4.y - point3.y) - (point2.y - point1.y) * (point4.x - point3.x);
		if (d != 0)
		{
			double r = ((point1.y - point3.y) * (point4.x - point3.x) - (point1.x - point3.x) * (point4.y - point4.y)) / d;
			double s = ((point1.y - point3.y) * (point2.x - point1.x) - (point1.x - point3.x) * (point2.y - point1.y)) / d;
			if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1))
			{
				flag = true;
			}
		}
		return flag;
	}

	static double determinant(double v1, double v2, double v3, double v4)  // 行列式  
	{  
	    return (v1*v3-v2*v4);  
	} 
	
	/**
	 * 判断point1和point2组成的线段与point3和point4组成的线段是否相交
	 * */
	public static boolean isIntersect1(Point point1, Point point2, Point point3, Point point4)
	{

	    double delta = determinant(point2.x-point1.x, point3.x-point4.x, point2.y-point1.y, point3.y-point4.y);  
	    if ( delta<=(1e-6) && delta>=-(1e-6) )  // delta=0，表示两线段重合或平行  
	    {  
	        return false;  
	    }  
	    double namenda = determinant(point3.x-point1.x, point3.x-point4.x, point3.y-point1.y, point3.y-point4.y) / delta;  
	    if ( namenda>1 || namenda<0 )  
	    {  
	        return false;  
	    }  
	    double miu = determinant(point2.x-point1.x, point3.x-point1.x, point2.y-point1.y, point3.y-point1.y) / delta;  
	    if ( miu>1 || miu<0 )  
	    {  
	        return false;  
	    }  
	    return true;  
	}

	
	
	public static Vector<Integer> getReverseIndex(Vector<Point> points)
	{
		
		Vector<Integer> reverseIntegers = new Vector<Integer>();
		for (int i = 1; i < points.size()-1; i++)
		{
			Point diffPoint1 = points.get(i).sub(points.get(i-1));
			Point diffPoint2 = points.get(i).sub(points.get(i+1));
			if (getCos(diffPoint1, diffPoint2)>0)
			{
				reverseIntegers.add(i);
			}
		}
		return reverseIntegers;
	}
	
	public static  Vector<Point> removeIntersect(Vector<Point> points)
	{
		for (int i = 0 ; i < points.size()-3; i++)
		{
			for (int j = points.size()-2 ; j > (i+1); j--)
			{
				if (points.get(i).sub(points.get(j)).length() > 40)
				{
					continue;
				}
				
				
				if (isIntersect(points.get(i), points.get(i+1), points.get(j), points.get(j+1)))
				{
					points.get(i).print();
					points.get(i+1).print();
					points.get(j).print();
					points.get(j+1).print();
					System.out.println("i:" + i + " j:" + j);
					
					int count = j - i;
					while ((count--)!=0)
					{
						points.removeElementAt(i+1);
					}
				}
			}
		}
		
		return points;
	}
	
	

	public static  Vector<Point> removeIntersect(Vector<Point> points,int index)
	{
		int begin=0,end=0;
		for (int i = index -1 ; i > 0; i--)
		{
			for (int j = index + 1; j < points.size()-1; j++)
			{
				if (isIntersect(points.get(i), points.get(i-1), points.get(j), points.get(j+1)))
				{
					begin = i;
					end  = j;
					break;
				}
			}
		}
		
		

		
		for (int i = 0; i < (end - begin); i++)
		{
			points.removeElementAt(begin);
		}
		return points;
	}
	
	
	public static Vector<Point> removeClose(Vector<Point> originPoints,double disatnce)
	{
		for (int i = 0; i < originPoints.size()-1; i++)
		{
			
			if((float) originPoints.get(i).sub(originPoints.get(i+1)).length() < disatnce)
			{
				Point midPoint = Point.getMidPoint(originPoints.get(i), originPoints.get(i+1));
				originPoints.remove(i);
				originPoints.remove(i);
				originPoints.insertElementAt(midPoint, i);
			}
		}
		return originPoints;
	}
	
	public static Vector<Point> normalize(Vector<Point> originPoints)
	{
		Vector<Float> lengths = new Vector<Float>();
		for (int i = 1; i < originPoints.size(); i++)
		{
			lengths.add((float) originPoints.get(i).sub(originPoints.get(i-1)).length());
		}
		
		Vector<Point> points = new Vector<Point>();
		
		points.add(originPoints.firstElement());
		
		double l = 0;
		for (int i = 1; i < originPoints.size();)
		{
			int count = 0;
			
			l = points.lastElement().sub(originPoints.get(i)).length();
			
			
			while (l< 10 && i+count < originPoints.size())
			{
				l+=lengths.get(i + count-1).floatValue();
				count++;
			}
			
			if (count==0)
			{
				Point dir = originPoints.get(i).sub(points.lastElement());
				dir = dir.div(dir.length()).mul(10);
				points.add(points.lastElement().add(dir));
			}
			else
			{
				points.add(originPoints.get(i+count-1));
				i = i+ count;
			}	
		}
		
		
		points.add(originPoints.lastElement());
		return points;
	}
	
	
	
	
	
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
			points.get(i-1).print();
			points.get(i).print();
			points.get(i+1).print();
			contourPoint.print();
			System.out.println("");
			contourPoints.add(contourPoint);
		}
		
		//end
		ratio = isTransition?0.1:1;
		contourPoint = getContourPointAtEnd(points.get(points.size()-1), points.get(points.size()-2), width*ratio);
		contourPoints.add(contourPoint);
		
		return contourPoints;
	}
	
//	double distance = halfAngle==0?width:width/(Math.sin(halfAngle));
//	distance = distance>3*width?3*width:distance;
	
	public static Point getContourPoint(Point lastPoint,Point currPoint,Point nextPoint,double width)
	{
		Point diffPoint1 = currPoint.sub(lastPoint);
		Point diffPoint2 = currPoint.sub(nextPoint);
		
		double angle = getAngle(diffPoint1, diffPoint2);
	
		double halfAngle = angle/2;
	//	double distance = width/Math.sin(halfAngle);

		Point rotatePoint = getRotatePoint(diffPoint1, halfAngle);
		rotatePoint = rotatePoint.div(rotatePoint.length()).mul((float)width);
		
		return currPoint.add(rotatePoint);
		
	}
	
	public static Point getRotatePoint(Point point,double angle)
	{
		double x = point.x*Math.cos(angle) - point.y*Math.sin(angle);
		double y = point.x*Math.sin(angle) + point.y*Math.cos(angle);
		
		return new Point((float)(x), (float)(y));
	}
	
	public static double getCos(Point diffPoint1,Point diffPoint2)
	{
		double cos = (diffPoint1.x*diffPoint2.x + diffPoint1.y*diffPoint2.y)/(diffPoint1.length()*diffPoint2.length());
		return cos;
	}
	
	public static double getAngle(Point diffPoint1,Point diffPoint2)
	{
		double cos = (diffPoint1.x*diffPoint2.x + diffPoint1.y*diffPoint2.y)/(diffPoint1.length()*diffPoint2.length());
		if (cos > 1)
		{
			cos = 1;
		}
		if (cos < -1)
		{
			cos = -1;
		}
		return Math.acos(cos);
	}
	
	public static Point getContourPointAtBegin(Point endPoint,Point neighbourPoint,double width)
	{
		Point diffPoint = neighbourPoint.sub(endPoint);
		
		Point normalPoint = new Point(diffPoint.y,-diffPoint.x);
		
		double length = normalPoint.length();
		
		normalPoint = normalPoint.div(length).mul((float)width);
		
		return endPoint.sub(normalPoint);
		
	}
	
	public static Point getContourPointAtEnd(Point endPoint,Point neighbourPoint,double width)
	{
		Point diffPoint = neighbourPoint.sub(endPoint);
		
		Point normalPoint = new Point(diffPoint.y,-diffPoint.x);
		
		double length = normalPoint.length();
		
		normalPoint = normalPoint.div(length).mul((float)width);
		
		return endPoint.add(normalPoint);
		
	}
}



//public static Vector<Point> normalize(Vector<Point> originPoints)
//{
//	Vector<Float> lengths = new Vector<Float>();
//	float totalLength = 0;
//	for (int i = 1; i < originPoints.size(); i++)
//	{
//		lengths.add((float) originPoints.get(i).sub(originPoints.get(i-1)).length());
//		totalLength +=lengths.lastElement();
//	}
//	
//	int pointNum = (int) (totalLength/10);
//	Vector<Point> points = new Vector<Point>();
//	
//	points.add(originPoints.firstElement());
//	
//	float l = 0;
//	float lastl = 0;
//	boolean isEnd = false;
//	for (int i = 0; i < originPoints.size()-1; i++)
//	{
//
//		int count = 0;
//		lastl = l;
//		while (true)
//		{
//			if (count +i >= originPoints.size())
//			{
//				isEnd = true;
//				break;
//			}
//			
//			l += lengths.get(count+i).floatValue();
//			
//			if (l>10)
//			{
//				break;
//			}
//			
//			count ++;
//			lastl =l;
//		}
//		
//		if (isEnd)
//		{
//			points.add(originPoints.lastElement());
//			break;
//		}
//		else
//		{
//			float percentage1 = (10f-lastl)/(l-lastl);
//			Point point1 = originPoints.get(i+count+1);
//			Point point2 = originPoints.get(i+count);
//			Point newPoint = Point.getPointBetweenTweenPoint(point1, point2, percentage1);
//			
//			points.add(newPoint);
//		}
//
//		l = l-10;
//		i = count + i + 1;
//		
//	}
//	points.add(originPoints.lastElement());
//	return points;
//}

