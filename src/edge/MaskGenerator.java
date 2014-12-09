package edge;

import geometry.Geometry;
import geometry.Point;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import config.Global;
import config.SampleConfig;

public class MaskGenerator
{
	public static final int width = 1280;
	public static final int height = 720;

	public static BufferedImage getImage(Vector<Point> points)
	{
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < points.size(); i++)
		{
			updateImage(image, (int) points.get(i).x, (int) points.get(i).y, (int)Global.BRUSH_WDITH);
		}

		return image;
	}

	public static void saveImage(BufferedImage image, String path)
	{
		File file = new File(path);

		try
		{
			ImageIO.write(image, "JPG", file);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
