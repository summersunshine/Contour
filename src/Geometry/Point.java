package geometry;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Comparator;
import java.util.Vector;

public class Point
{
	// x坐标
	public double x;

	// y坐标
	public double y;

	/**
	 * 构造函数
	 * 
	 * @param x
	 * @param y
	 * */
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * 构造函数
	 * 
	 * @param x
	 * @param y
	 * */
	public Point(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * 构造函数
	 * 
	 * @param x
	 * @param y
	 * */
	public Point(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public Point()
	{
		this.x = 0;
		this.y = 0;
	}

	public boolean equal(Point point)
	{
		return (int) point.x == (int) x && (int) point.y == (int) y;
	}

	public double lengthSqr()
	{
		return x * x + y * y;
	}
	
	public double length()
	{
		return Math.sqrt(x * x + y * y);
	}

	/**
	 * 垂直
	 */
	public Point perpendicularPoint()
	{
		return new Point(-y, x);
	}

	/**
	 * 获取单位向量
	 * */
	public Point normalizePoint()
	{
		if (length() == 0)
		{
			return new Point();
		}
		else
		{
			return div(length());
		}
	}

	/**
	 * 加上一个点
	 * 
	 * @param point
	 * */
	public Point add(Point point)
	{
		return new Point(this.x + point.x, this.y + point.y);
	}

	/**
	 * 减去一个点
	 * 
	 * @param point
	 * */
	public Point sub(Point point)
	{
		return new Point(this.x - point.x, this.y - point.y);
	}

	/**
	 * 乘以一个值
	 * 
	 * @param m
	 * */
	Point mul(float m)
	{
		return new Point(m * this.x, m * this.y);
	}

	/**
	 * 除以一个值
	 * 
	 * @param m
	 * */
	public Point div(double m)
	{
		return new Point(this.x / m, this.y / m);
	}

	/**
	 * 打印
	 * */
	public void print()
	{
		System.out.println("x: " + x + " y: " + y);
	}

	/**
	 * 绘制
	 * 
	 * @param grphics2d
	 * @param color
	 * @param size
	 * */
	public void drawPoint(Graphics2D graphics2d, Color color)
	{
		drawPoint(graphics2d, color, 4);
	}

	/**
	 * 绘制
	 * 
	 * @param grphics2d
	 * @param color
	 * @param size
	 * */
	public void drawPoint(Graphics2D graphics2d, Color color, int size)
	{

		graphics2d.setColor(color);
		graphics2d.fillRect((int) x - size / 2, (int) y - size / 2, size, size);
	}

	/**
	 * 获取两个点之间的距离
	 * 
	 * @param point1
	 * @param point2
	 * */
	public static double getDistance(Point point1, Point point2)
	{
		return point1.sub(point2).length();
	}

	/**
	 * 获取两个点之间的终点
	 * 
	 * @param point1
	 * @param point2
	 * */
	public static Point getMidPoint(Point point1, Point point2)
	{
		return new Point((point1.x + point2.x) / 2.0f, (point1.y + point2.y) / 2.0f);
	}

	/**
	 * 获取两个点之间的某一点
	 * 
	 * @param point1
	 * @param point2
	 * @param percetage1
	 *            point1占据的比重
	 * */
	public static Point getPointBetweenTweenPoint(Point point1, Point point2, float percentage1)
	{
		float percentage2 = 1 - percentage1;
		double x = point1.x * percentage1 + point2.x * percentage2;
		double y = point1.y * percentage1 + point2.y * percentage2;
		return new Point(x, y);
	}

	/**
	 * 获取相对于中点的对称点
	 * 
	 * @param midPoint
	 * @param point
	 * */
	public static Point getSymmetryPoint(Point midPoint, Point point)
	{
		return midPoint.mul(2).sub(point);
	}

	/**
	 * 获取一组点的x坐标
	 * 
	 * @param points
	 * */
	public int[] getPointX(Vector<Point> points)
	{
		int[] pointX = new int[points.size()];

		for (int i = 0; i < pointX.length; i++)
		{
			pointX[i] = (int) points.get(i).x;
		}
		return pointX;
	}

	/**
	 * 获取一组点的y坐标
	 * 
	 * @param points
	 * */
	public int[] getPointY(Vector<Point> points)
	{
		int[] pointX = new int[points.size()];

		for (int i = 0; i < pointX.length; i++)
		{
			pointX[i] = (int) points.get(i).y;
		}
		return pointX;
	}

	public float getAngle()
	{
		return (float) Geometry.getAngle(this);
	}

	@SuppressWarnings("rawtypes")
	public static final class AngleComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			Point s1 = (Point) o1;
			Point s2 = (Point) o2;

			if (s1.getAngle() > s2.getAngle())
			{
				return 1;
			}
			else if (s1.getAngle() < s2.getAngle())
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	}

	public static float getDotProduct(Point pointA, Point pointB)
	{

		return 0;

	}

}
