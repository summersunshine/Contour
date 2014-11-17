package Geometry;


public class LogPolar
{

	public float p;
	public float angle;
	
	public LogPolar(Point point)
	{
		
		p = (float) Math.log(Math.ceil((point.length()/Histogram.ratio)));
		angle = (float) (Math.atan2(point.y, point.x)*180/Math.PI);
		angle = angle < 0?angle + 360:angle;
	}
	
	
	public void print()
	{
		System.out.println("p: " + p + "angle " + angle);
	}
}
