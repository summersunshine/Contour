package util;

import geometry.Geometry;
import geometry.Point;

import java.util.Vector;

public class TurningPointUtil
{
	public static Point getPoint(Vector<Point> points)
	{
		Point beginPoint = points.firstElement();
		Point endPoint = points.lastElement();
		Point turningPoint = points.get(1);

		double minAngle = Geometry.getAngle(beginPoint, points.get(1), endPoint);
		for (int i = 1; i < points.size() - 1; i++)
		{
			double angle = Geometry.getAngle(beginPoint, points.get(i), endPoint);
			if (angle < minAngle)
			{
				turningPoint = points.get(i);
				minAngle = angle;
			}
		}

		if (minAngle > 90 || beginPoint.sub(turningPoint).length() < 50)
		{
			System.out.println("endpoint");
			endPoint.print();
			return endPoint;
		}
		else
		{
			System.out.println("turningPoint");
			turningPoint.print();
			return turningPoint;
		}
		// return endPoint;
	}

}
