package geometry;

public class BezierCurve
{

	/**
	 * 三次贝塞尔曲线
	 * 
	 * @param percent
	 * @param controlP
	 * */
	public static Point bezier3func(float percent, Point[] controlP)
	{
		float x = bezier3funcX(percent, controlP);
		float y = bezier3funcY(percent, controlP);
		return new Point(x, y);
	}

	public static double berzier3Angle(float percent, Point[] controlP)
	{
		double x = bezierDerivativeX(percent, controlP);
		double y = bezierDerivativeY(percent, controlP);
		return Math.atan2(y, x);
	}

	public static double bezierDerivativeX(float percent, Point[] controlP)
	{
		double part0 = 3 * (1 - percent) * (1 - percent) * (controlP[2].x - controlP[3].x);
		double part1 = 6 * (1 - percent) * percent * (controlP[1].x - controlP[2].x);
		double part2 = 6 * percent * percent * percent * (controlP[0].x - controlP[1].x);
		return part0 + part1 + part2;
	}

	public static double bezierDerivativeY(float percent, Point[] controlP)
	{
		double part0 = 3 * (1 - percent) * (1 - percent) * (controlP[2].y - controlP[3].y);
		double part1 = 6 * (1 - percent) * percent * (controlP[1].y - controlP[2].y);
		double part2 = 6 * percent * percent * percent * (controlP[0].y - controlP[1].y);
		return part0 + part1 + part2;
	}

	/**
	 * 三次贝塞尔曲线X
	 *
	 * @param percent
	 * @param controlP
	 * */
	public static float bezier3funcX(float percent, Point[] controlP)
	{
		float part0 = (float) (controlP[0].x * percent * percent * percent);
		float part1 = (float) (3 * controlP[1].x * percent * percent * (1 - percent));
		float part2 = (float) (3 * controlP[2].x * percent * (1 - percent) * (1 - percent));
		float part3 = (float) (controlP[3].x * (1 - percent) * (1 - percent) * (1 - percent));
		return part0 + part1 + part2 + part3;
	}

	/**
	 * 三次贝塞尔曲线Y
	 * 
	 * @param percent
	 * @param controlP
	 * */
	public static float bezier3funcY(float percent, Point[] controlP)
	{
		float part0 = (float) (controlP[0].y * percent * percent * percent);
		float part1 = (float) (3 * controlP[1].y * percent * percent * (1 - percent));
		float part2 = (float) (3 * controlP[2].y * percent * (1 - percent) * (1 - percent));
		float part3 = (float) (controlP[3].y * (1 - percent) * (1 - percent) * (1 - percent));
		return part0 + part1 + part2 + part3;
	}
}
