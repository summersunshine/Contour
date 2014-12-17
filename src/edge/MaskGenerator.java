package edge;

import geometry.Geometry;
import geometry.Point;

import java.awt.image.BufferedImage;

import config.Global;

public class MaskGenerator
{
	public static final int	width	= 1280;
	public static final int	height	= 720;

	// public static BufferedImage getImage(Vector<Point> points)
	// {
	// BufferedImage image = new BufferedImage(width, height,
	// BufferedImage.TYPE_INT_RGB);
	//
	// for (int i = 0; i < points.size(); i++)
	// {
	// float radius = Global.BRUSH_WDITH;
	// if (i>0 && i < points.size()-1)
	// {
	// float ratio = (float)
	// getRatio(points.get(i-1),points.get(i),points.get(i+1));
	// System.out.println("ratio " + ratio);
	// radius*= ratio;
	// }
	// updateImage(image, (int) points.get(i).x, (int) points.get(i).y,
	// (int)radius);
	// }
	//
	// return image;
	// }

	public static BufferedImage getImage(SpinePoints spinePoints)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < spinePoints.spinePoints.size(); i++)
		{
			double ratio = spinePoints.radiusDoubles.get(i);
			double radius = Global.BRUSH_WDITH * ratio;
			System.out.println("i: " + i + "ratio " + ratio);
			Point point = spinePoints.spinePoints.get(i);
			updateImage(image, (int) point.x, (int) point.y, (int) radius);
		}

		return image;
	}

	public static double getRatio(Point point1, Point point2, Point point3)
	{
		return 2 - Geometry.getCos(point2.sub(point1), point3.sub(point2));
	}

	/**
	 * ����Ӧ����ԴͼƬ�ϵ�Ч��������ԭͼƬ������£���������ΪԲ������
	 * 
	 * @param sourceImage
	 *            ԭͼƬ
	 * @param centerX
	 *            ���������ĵ�X����
	 * @param centerY
	 *            ���������ĵ�Y����
	 * @param radius
	 *            �����İ뾶
	 * */
	public static void updateImage(BufferedImage sourceImage, int centerX, int centerY, int radius)
	{
		int startX = centerX - radius;
		int startY = centerY - radius;
		int endX = centerX + radius;
		int endY = centerY + radius;

		startX = startX < 0 ? 0 : startX;
		startY = startY < 0 ? 0 : startY;
		endX = endX > sourceImage.getWidth() ? sourceImage.getWidth() : endX;
		endY = endY > sourceImage.getHeight() ? sourceImage.getHeight() : endY;

		for (int y = startY; y < endY; y++)
		{
			for (int x = startX; x < endX; x++)
			{
				if (Geometry.getDistance(x, y, centerX, centerY) < radius)
				{
					sourceImage.setRGB(x, y, Global.WHITE_VALUE);
				}
			}
		}

	}
}
