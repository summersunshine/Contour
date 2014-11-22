package Geometry;


/**
 * 对数极坐标
 * */
public class LogPolar
{

	public float p;
	
	//取值范围 0-360
	public float angle;
	
	public LogPolar(Point point)
	{
	
		p = (float) Math.log(Math.ceil((point.length())));
		angle = (float) (Math.atan2(point.y, point.x)*180/Math.PI);
		angle = angle < 0?angle + 360:angle;
		angle = angle==360?0:angle;
	}
	
	
	public void print()
	{
		System.out.println("p: " + p + "angle " + angle);
	}
}
