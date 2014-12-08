package geometry;

public class CoordDiff extends Point
{

	public double dx;
	public double dy;

	public CoordDiff(Point point, Point secondPoint)
	{
		this.x = (int)point.x;
		this.y = (int)point.y;
		this.dx = (int)secondPoint.x - (int)point.x;
		this.dy = (int)secondPoint.y - (int)point.y;

	}

	public CoordDiff(double x, double y, double dx, double dy)
	{
		super(x, y);
		this.dx = dx;
		this.dy = dy;

	}

}
