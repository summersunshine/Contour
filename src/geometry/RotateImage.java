package geometry;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class RotateImage
{

	public static BufferedImage getImage(BufferedImage src,Point queryPoint,Point libPoint, double angle)
	{
		int width = src.getWidth();
		int height = src.getHeight();
		BufferedImage res = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
		
//		Point rotateLibPoint = Geometry.getRotatePoint(libPoint, angle);
		Point offsetPoint = queryPoint.sub(libPoint);
//		queryPoint.print();
//		libPoint.print();
//		rotateLibPoint.print();
//		offsetPoint.print();
		
		Graphics2D g2 = res.createGraphics();
		// transform
		g2.translate(offsetPoint.x, offsetPoint.y);
		g2.rotate(angle, 0,0);
		
		

		g2.drawImage(src, null, null);
		return res;
	}
	
	


	public static Rectangle CalcRotatedSize(Rectangle src, int angel)
	{
		// if angel is greater than 90 degree, we need to do some conversion
		if (angel >= 90)
		{
			if (angel / 90 % 2 == 1)
			{
				int temp = src.height;
				src.height = src.width;
				src.width = temp;
			}
			angel = angel % 90;
		}

		double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
		double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
		double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
		double angel_dalta_width = Math.atan((double) src.height / src.width);
		double angel_dalta_height = Math.atan((double) src.width / src.height);

		int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
		int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
		int des_width = src.width + len_dalta_width * 2;
		int des_height = src.height + len_dalta_height * 2;
		return new java.awt.Rectangle(new Dimension(des_width, des_height));
	}
}