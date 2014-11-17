package Geometry;

public class Point
{
	// x坐标
	public float x;

	// y坐标
	public float y;

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

	public float length()
	{
		return (float) Math.sqrt(x * x + y * y);
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
	Point add(Point point)
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
	Point div(float m)
	{
		return new Point(this.x / m, this.y / m);
	}

	/**
	 * 打印
	 * 
	 * @param m
	 * */
	void print()
	{
		System.out.println("x: " + x + " y: " + y);
	}
	
	/**
	 * 获取两个点之间的距离
	 * 
	 * @param point1
	 * @param point2
	 * */
	public static float getDistance(Point point1, Point point2)
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
		float x = point1.x * percentage1 + point2.x * percentage2;
		float y = point1.y * percentage1 + point2.y * percentage2;
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
}
