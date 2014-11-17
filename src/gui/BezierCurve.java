package gui;

import java.util.Vector;

import Geometry.Point;

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
		return new Point(x,y);
	}
	
	
	/**
	 * 三次贝塞尔曲线X
	 *
 	 * @param percent
	 * @param controlP
	 * */
	public static float bezier3funcX(float percent, Point[] controlP)
	{
		float part0 = controlP[0].x * percent * percent * percent;
		float part1 = 3 * controlP[1].x * percent * percent * (1 - percent);
		float part2 = 3 * controlP[2].x * percent * (1 - percent) * (1 - percent);
		float part3 = controlP[3].x * (1 - percent) * (1 - percent) * (1 - percent);
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
		float part0 = controlP[0].y * percent * percent * percent;
		float part1 = 3 * controlP[1].y * percent * percent * (1 - percent);
		float part2 = 3 * controlP[2].y * percent * (1 - percent) * (1 - percent);
		float part3 = controlP[3].y * (1 - percent) * (1 - percent) * (1 - percent);
		return part0 + part1 + part2 + part3;
	}
}
