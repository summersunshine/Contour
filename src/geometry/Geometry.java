package geometry;

import java.util.Vector;

public class Geometry
{
	/**
	 * 获取两点之间的距离
	 * 
	 * @param x1
	 *            第一个点的X坐标
	 * @param y1
	 *            第一个点的y坐标
	 * @param x2
	 *            第二个点的X坐标
	 * @param y2
	 *            第二个点的y坐标
	 * */
	public static int getDistance(int x1, int y1, int x2, int y2)
	{
		int x = x1 - x2;
		int y = y1 - y2;
		return (int) Math.sqrt(x * x + y * y);
	}

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

	static double determinant(double v1, double v2, double v3, double v4) // 行列式
	{
		return (v1 * v3 - v2 * v4);
	}

	/**
	 * 判断point1和point2组成的线段与point3和point4组成的线段是否相交
	 * */
	public static boolean isIntersect1(Point point1, Point point2, Point point3, Point point4)
	{

		double delta = determinant(point2.x - point1.x, point3.x - point4.x, point2.y - point1.y, point3.y - point4.y);
		if (delta <= (1e-6) && delta >= -(1e-6)) // delta=0，表示两线段重合或平行
		{
			return false;
		}
		double namenda = determinant(point3.x - point1.x, point3.x - point4.x, point3.y - point1.y, point3.y - point4.y) / delta;
		if (namenda > 1 || namenda < 0)
		{
			return false;
		}
		double miu = determinant(point2.x - point1.x, point3.x - point1.x, point2.y - point1.y, point3.y - point1.y) / delta;
		if (miu > 1 || miu < 0)
		{
			return false;
		}
		return true;
	}

	/**
	 * 获取一组点的中点
	 * */
	public static Vector<Point> getDirPoints(Vector<Point> points)
	{
		Vector<Point> dirPoints = new Vector<Point>();
		dirPoints.add(points.get(1).sub(points.get(0)));
		for (int i = 1; i < points.size() - 1; i++)
		{
			Point middlePoint1 = Point.getMidPoint(points.get(i - 1), points.get(i));
			Point middlePoint2 = Point.getMidPoint(points.get(i + 1), points.get(i));
			dirPoints.add(middlePoint2.sub(middlePoint1));
		}
		dirPoints.add(points.lastElement().sub(points.get(points.size() - 1)));
		return dirPoints;
	}

	/**
	 * 获取一组点的方向
	 * */
	public static Vector<Double> getDir(Vector<Point> points)
	{
		Vector<Double> dirPoints = new Vector<Double>();
		dirPoints.add(getAngle(points.get(0).sub(points.get(1))));
		for (int i = 1; i < points.size() - 1; i++)
		{
			Point middlePoint1 = Point.getMidPoint(points.get(i - 1), points.get(i));
			Point middlePoint2 = Point.getMidPoint(points.get(i + 1), points.get(i));
			double angle = getAngle(middlePoint2.sub(middlePoint1));
			dirPoints.add(angle);
		}
		dirPoints.add(getAngle(points.get(points.size() - 1).sub(points.get(points.size() - 2))));
		return dirPoints;
	}

	public static Vector<Integer> getReverseIndex(Vector<Point> points)
	{

		Vector<Integer> reverseIntegers = new Vector<Integer>();
		for (int i = 1; i < points.size() - 1; i++)
		{
			Point diffPoint1 = points.get(i).sub(points.get(i - 1));
			Point diffPoint2 = points.get(i).sub(points.get(i + 1));
			if (getCos(diffPoint1, diffPoint2) > 0)
			{
				reverseIntegers.add(i);
			}
		}
		return reverseIntegers;
	}

	public static Vector<Point> removeIntersect(Vector<Point> points)
	{
		for (int i = 0; i < points.size() - 3; i++)
		{
			for (int j = points.size() - 2; j > (i + 1); j--)
			{
				if (points.get(i).sub(points.get(j)).length() > 40)
				{
					continue;
				}

				if (isIntersect(points.get(i), points.get(i + 1), points.get(j), points.get(j + 1)))
				{
					points.get(i).print();
					points.get(i + 1).print();
					points.get(j).print();
					points.get(j + 1).print();
					System.out.println("i:" + i + " j:" + j);

					int count = j - i;
					while ((count--) != 0)
					{
						points.removeElementAt(i + 1);
					}
				}
			}
		}

		return points;
	}

	public static Vector<Point> removeIntersect(Vector<Point> points, int index)
	{
		int begin = 0, end = 0;
		for (int i = index - 1; i > 0; i--)
		{
			for (int j = index + 1; j < points.size() - 1; j++)
			{
				if (isIntersect(points.get(i), points.get(i - 1), points.get(j), points.get(j + 1)))
				{
					begin = i;
					end = j;
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

	public static Vector<Point> removeClose(Vector<Point> originPoints, double disatnce)
	{
		for (int i = 0; i < originPoints.size() - 1; i++)
		{

			if ((float) originPoints.get(i).sub(originPoints.get(i + 1)).length() < disatnce)
			{
				Point midPoint = Point.getMidPoint(originPoints.get(i), originPoints.get(i + 1));
				originPoints.remove(i);
				originPoints.remove(i);
				originPoints.insertElementAt(midPoint, i);
			}
		}
		return originPoints;
	}

	public static Vector<Point> normalize(Vector<Point> originPoints, float maxDistance)
	{
		// float maxDistance = 8;

		Vector<Float> lengths = new Vector<Float>();
		for (int i = 1; i < originPoints.size(); i++)
		{
			lengths.add((float) originPoints.get(i).sub(originPoints.get(i - 1)).length());
		}

		Vector<Point> points = new Vector<Point>();

		points.add(originPoints.firstElement());

		double l = 0;
		for (int i = 1; i < originPoints.size();)
		{
			int count = 0;

			l = points.lastElement().sub(originPoints.get(i)).length();

			while (l < maxDistance && i + count < originPoints.size())
			{
				l += lengths.get(i + count - 1).floatValue();
				count++;
			}

			if (count == 0)
			{
				Point dir = originPoints.get(i).sub(points.lastElement());
				dir = dir.div(dir.length()).mul(maxDistance);
				points.add(points.lastElement().add(dir));
			}
			else
			{
				points.add(originPoints.get(i + count - 1));
				i = i + count;
			}
		}

		points.add(originPoints.lastElement());
		return points;
	}

	public static Vector<Point> getContourPoints(Vector<Point> points, double width, boolean isTransition)
	{
		Vector<Point> contourPoints = new Vector<Point>();
		Point contourPoint;
		float part = 20;
		double ratio;

		// beginning
		ratio = isTransition ? 0.1 : 1;
		contourPoint = getContourPointAtBegin(points.get(0), points.get(1), width * ratio);
		contourPoints.add(contourPoint);

		// middle
		for (int i = 1; i < points.size() - 1; i++)
		{
			if (isTransition)
			{
				if (i < points.size() / part)
				{
					ratio = i * part / points.size();
				}
				else if (i > points.size() * (part - 1) / part)
				{
					ratio = (points.size() - i) * part / points.size();
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
			ratio = Math.sin(ratio * Math.PI / 2);
			contourPoint = getContourPoint(points.get(i - 1), points.get(i), points.get(i + 1), ratio * width);
			contourPoints.add(contourPoint);

		}

		// end
		ratio = isTransition ? 0.1 : 1;
		contourPoint = getContourPointAtEnd(points.get(points.size() - 1), points.get(points.size() - 2), width * ratio);
		contourPoints.add(contourPoint);

		return contourPoints;
	}

	public static Point getContourPoint(Point lastPoint, Point currPoint, Point nextPoint, double width)
	{
		Point diffPoint1 = currPoint.sub(lastPoint);
		Point diffPoint2 = currPoint.sub(nextPoint);

		double angle = getAngle(diffPoint1, diffPoint2);

		double halfAngle = angle / 2;
		double distance = width / Math.sin(halfAngle);

		Point rotatePoint = getRotatePoint(diffPoint1, halfAngle);
		rotatePoint = rotatePoint.div(rotatePoint.length()).mul((float) distance);

		return currPoint.add(rotatePoint);

	}

	public static boolean isLeftSide(Point beginPoint, Point endPoint, Point testPoint)
	{
		double dy = endPoint.y - beginPoint.y;
		double dx = beginPoint.x - endPoint.x;
		double c = endPoint.x * beginPoint.y - beginPoint.x * endPoint.y;
		double d = dy * testPoint.x + dx * testPoint.y + c;
		return d < -0.00001;
	}

	public static boolean isRightSide(Point beginPoint, Point endPoint, Point testPoint)
	{
		double dy = endPoint.y - beginPoint.y;
		double dx = beginPoint.x - endPoint.x;
		double c = endPoint.x * beginPoint.y - beginPoint.x * endPoint.y;
		double d = dy * testPoint.x + dx * testPoint.y + c;
		return d > 0.00001;
	}

	public static Point getRotatePoint(Point point, double angle)
	{
		double x = point.x * Math.cos(angle) - point.y * Math.sin(angle);
		double y = point.x * Math.sin(angle) + point.y * Math.cos(angle);

		return new Point((float) (x), (float) (y));
	}

	public static double getCos(Point diffPoint1, Point diffPoint2)
	{
		return (diffPoint1.x * diffPoint2.x + diffPoint1.y * diffPoint2.y) / (diffPoint1.length() * diffPoint2.length());
	}

	public static double getAngle(Point diffPoint1)
	{
		return Math.atan2(diffPoint1.y, diffPoint1.x);

	}

	public static double getAngle(Point diffPoint1, Point diffPoint2)
	{
		double cos = getCos(diffPoint1, diffPoint2);
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

	public static double getAngle(Point point0, Point point1, Point point2)
	{
		return getAngle(point0.sub(point1), point2.sub(point1));
	}

	public static Point getContourPointAtBegin(Point endPoint, Point neighbourPoint, double width)
	{
		Point diffPoint = neighbourPoint.sub(endPoint);

		Point normalPoint = new Point(diffPoint.y, -diffPoint.x);

		double length = normalPoint.length();

		normalPoint = normalPoint.div(length).mul((float) width);

		return endPoint.sub(normalPoint);

	}

	public static Point getContourPointAtEnd(Point endPoint, Point neighbourPoint, double width)
	{
		Point diffPoint = neighbourPoint.sub(endPoint);

		Point normalPoint = new Point(diffPoint.y, -diffPoint.x);

		double length = normalPoint.length();

		normalPoint = normalPoint.div(length).mul((float) width);

		return endPoint.add(normalPoint);

	}

	public static boolean isInBox(int minX, int maxX, int minY, int maxY, Point point)
	{
		return point.x >= minX && point.x <= maxX && point.y >= minY && point.y < maxY;
	}
}
