package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import config.Global;

public class ImageUtil
{

	/**
	 * 通过文件名读取图像,保留alpha通道
	 * 
	 * @param fileName
	 * */
	public static BufferedImage getImage(String fileName)
	{
		BufferedImage bufferedImage = null;
		try
		{
			bufferedImage = ImageIO.read(new File(fileName));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return bufferedImage;
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

	public static BufferedImage getCloneImage(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				outputImage.setRGB(x, y, image.getRGB(x, y));
			}
		}

		return outputImage;

	}

	public static BufferedImage getReverseImage(BufferedImage image)
	{
		int height = image.getHeight();
		int width = image.getWidth();

		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				Color color = new Color(image.getRGB(x, y));
				Color reverseColor = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());
				outputImage.setRGB(x, y, reverseColor.getRGB());
			}
		}
		return outputImage;

	}

	public static BufferedImage getConverterImage(BufferedImage alphaMaskImage, Color brushColor)
	{

		for (int y = 0; y < Global.height; y++)
		{
			for (int x = 0; x < Global.width; x++)
			{

				Color color = new Color(alphaMaskImage.getRGB(x, y));
				double alpha = (color.getRed() + color.getGreen() + color.getBlue()) / 255f / 3;
				int r = (int) (255 - (255 - brushColor.getRed()) * alpha);
				int g = (int) (255 - (255 - brushColor.getGreen()) * alpha);
				int b = (int) (255 - (255 - brushColor.getBlue()) * alpha);

				alphaMaskImage.setRGB(x, y, ImageUtil.getRGB(r, g, b));
			}
		}
		return alphaMaskImage;

	}

	// /////////////////////////////////////////////下面都是打酱油的/////////////////////////////////////////
	public static int[] getSplitRGB(int rgb)
	{
		int[] rgbs = new int[3];
		rgbs[0] = (rgb & 0xff0000) >> 16;
		rgbs[1] = (rgb & 0xff00) >> 8;
		rgbs[2] = (rgb & 0xff);
		return rgbs;
	}

	public static int getRGB(int r, int g, int b)
	{
		r = r << 16;
		g = g << 8;

		return (r | g | b);
	}

	public static float clamp(float c, float value)
	{
		if (c < 0)
			return 0;
		if (c > value)
			return value;
		return c;
	}

	public static int clamp(int c, int value)
	{
		if (c < 0)
			return 0;
		if (c > value)
			return value;
		return c;
	}

	public static float clamp(float c)
	{
		if (c < 0)
			return 0;
		if (c > 255)
			return 255;
		return c;
	}

	public static int clampIn255(int c)
	{
		if (c < 0)
			return 0;
		if (c > 255)
			return 255;
		return c;
	}

}
