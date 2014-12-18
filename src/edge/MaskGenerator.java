package edge;

import geometry.Geometry;
import geometry.Point;

import java.awt.image.BufferedImage;

import config.Global;

public class MaskGenerator
{
	public static final int	width	= 1280;
	public static final int	height	= 720;

	public static BufferedImage getImage(SpinePoints spinePoints)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		int startIndex = (int) (Global.BRUSH_WDITH / Global.SAMPLE_DIST);

		for (int i = 1; i < spinePoints.spinePoints.size() - 1; i++)
		{
			double ratio = spinePoints.radius.get(i);
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
	 * 擦除应用在源图片上的效果（仅有原图片的情况下，擦除区域为圆形区域）
	 * 
	 * @param sourceImage
	 *            原图片
	 * @param centerX
	 *            擦除的中心点X坐标
	 * @param centerY
	 *            擦除的中心店Y坐标
	 * @param radius
	 *            擦除的半径
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
