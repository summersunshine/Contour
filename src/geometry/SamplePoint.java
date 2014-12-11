package geometry;

public class SamplePoint extends Point
{
	// ��¼���߷���
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
