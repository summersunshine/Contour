package edge;

import geometry.Point;

import java.util.Vector;

public class UniformSample
{

	public static Vector<Point> normalize(Vector<Point> originPoints, float maxDistance)
	{

		Vector<Point> outputPoints = new Vector<Point>();
		outputPoints.add(originPoints.firstElement());

		double length = 0;
		for (int i = 1; i < originPoints.size() - 1;)
		{

			int count = 0;
			Point point1 = outputPoints.lastElement();
			Point point2 = originPoints.get(i);
			double recentLength = point2.sub(point1).length();
			length = recentLength;

			while (length < maxDistance)
			{
				if (i + count + 1 >= originPoints.size())
				{
					i = originPoints.size();
					break;
				}

				point1 = point2;
				point2 = originPoints.get(i + count + 1);
				recentLength = point2.sub(point1).length();
				length += recentLength;

				count++;
			}

			if (length > maxDistance)
			{
				float percent = (float) ((length - maxDistance) / recentLength);
				Point newPoint = Point.getPointBetweenTweenPoint(point1, point2, percent);
				outputPoints.add(newPoint);

				i = i + count;

			}
			else if (length == maxDistance)
			{
				outputPoints.add(point2);

				i = i + count + 1;
			}
			// outputPoints.lastElement().print();

		}

		if (outputPoints.lastElement().sub(originPoints.lastElement()).length() > 1)
		{
			outputPoints.add(originPoints.lastElement());
		}
		// outputPoints.add(originPoints.lastElement());
		return outputPoints;
	}
	//
	//
	// public static Vector<Point> normalize(Vector<Point> originPoints, float
	// maxDistance)
	// {
	// // float maxDistance = 8;
	//
	// Vector<Float> lengths = new Vector<Float>();
	// for (int i = 1; i < originPoints.size(); i++)
	// {
	// lengths.add((float) originPoints.get(i).sub(originPoints.get(i -
	// 1)).length());
	// }
	//
	// Vector<Point> points = new Vector<Point>();
	//
	// points.add(originPoints.firstElement());
	//
	// double l = 0;
	// for (int i = 1; i < originPoints.size();)
	// {
	// int count = 0;
	//
	// l = points.lastElement().sub(originPoints.get(i)).length();
	//
	// while (l < maxDistance && i + count < originPoints.size())
	// {
	// l += lengths.get(i + count - 1).floatValue();
	// count++;
	// }
	//
	// if (count == 0)
	// {
	// Point dir = originPoints.get(i).sub(points.lastElement());
	// dir = dir.div(dir.length()).mul(maxDistance);
	// points.add(points.lastElement().add(dir));
	// }
	// else
	// {
	// points.add(originPoints.get(i + count - 1));
	// i = i + count;
	// }
	// }
	//
	// points.add(originPoints.lastElement());
	// return points;
	// }
}
