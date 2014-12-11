package geometry;

public class SamplePoint extends Point
{
	// 记录切线方向
	public double	angle;

	public SamplePoint(double x, double y, double angle)
	{
		super(x, y);
		this.angle = angle;
	}

	public SamplePoint(Point point, double angle)
	{
		super(point);
		this.angle = angle;
	}
}
