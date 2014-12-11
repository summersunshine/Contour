package geometry;

public class CoordDiff extends Point
{
	public double x2;
	public double y2;
	public double dx;
	public double dy;

	public CoordDiff(Point point, Point secondPoint)
	{
		this.x = (int)point.x;
		this.y = (int)point.y;
		this.x2 = (int)secondPoint.x;
		this.y2 = (int)secondPoint.y;
		this.dx = (int)secondPoint.x - (int)point.x;
		this.dy = (int)secondPoint.y - (int)point.y;

	}

	public CoordDiff(double x, double y, double dx, double dy)
	{
		super(x, y);
		this.dx = dx;
		this.dy = dy;
		this.x2 = x+dx;
		this.y2 = y+dy;

	}
	
	public int getIntX2()
	{
		return (int)x2;
	}
	
	public int getIntY2()
	{
		return (int)y2;
	}
	
}
