package util;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImgUtil
{

	/**
	 * 显示BufferedImage
	 * 
	 * @param bimg
	 * @return JPanel图片
	 */
	public static JPanel getImagePanel(final BufferedImage bimg)
	{
		class ImgPanel extends JPanel
		{

			@Override
			public void paint(Graphics g)
			{
				Graphics2D g2 = (Graphics2D) g;
				try
				{
					g2.drawImage(bimg, 0, 0, null);
				}
				finally
				{
					g2.dispose();
				}
			}
		}
		ImgPanel ip = new ImgPanel();
		return ip;
	}

	/**
	 * 通过文件名读取图像
	 * 
	 * @param fileName
	 * */
	public static BufferedImage getImg(String fileName)
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

	/**
	 * 通过文件名读取图像,保留alpha通道
	 * 
	 * @param fileName
	 * */
	public static BufferedImage getAlphaImg(String fileName)
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

	/**
	 * 颜色之间的欧几里得距离
	 * */
	public static int getColorDiff(int[] rgb1, int[] rgb2)
	{
		int r = rgb1[0] - rgb2[0];
		int g = rgb1[1] - rgb2[1];
		int b = rgb1[2] - rgb2[2];

		return (int) Math.sqrt(r * r + g * g + b * b);
	}

	public static void getRGB(BufferedImage image, int[] inPixels)
	{
		// TODO Auto-generated method stub

		int count = 0;
		for (int y = 0; y < image.getHeight(); y++)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				inPixels[count++] = image.getRGB(x, y);
			}
		}

	}

	public static void getRGB(BufferedImage image, int[] inPixels, int startX, int startY, int width, int height)
	{
		// TODO Auto-generated method stub

		int count = 0;
		for (int y = startY; y < startY + height; y++)
		{
			for (int x = startX; x < startX + width; x++)
			{
				inPixels[count++] = image.getRGB(x, y);
			}
		}

	}

	public static void setRGB(BufferedImage image, int[] outPixels)
	{
		// TODO Auto-generated method stub

		int count = 0;
		for (int y = 0; y < image.getHeight(); y++)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				image.setRGB(x, y, outPixels[count++]);
			}
		}

	}

	public static void setRGB(BufferedImage image, int[] outPixels, int startX, int startY, int width, int height)
	{
		// TODO Auto-generated method stub

		int count = 0;
		for (int y = startY; y < startY + height; y++)
		{
			for (int x = startX; x < startX + width; x++)
			{
				image.setRGB(x, y, outPixels[count++]);
			}
		}

	}

	/**
	 * @param image1
	 * @param image2
	 *            将两张图加起来 以两张图中，最小的长和宽作为输出图像的长宽
	 * */
	public static BufferedImage addImage(BufferedImage image1, BufferedImage image2)
	{
		int width = image1.getWidth() < image2.getWidth() ? image1.getWidth() : image2.getWidth();
		int height = image1.getHeight() < image2.getHeight() ? image1.getHeight() : image2.getHeight();

		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int[] rgb1 = ImgUtil.getSplitRGB(image1.getRGB(j, i));
				int[] rgb2 = ImgUtil.getSplitRGB(image2.getRGB(j, i));
				outputImage.setRGB(j, i, ImgUtil.getRGB(rgb1[0] + rgb2[0], rgb1[1] + rgb2[1], rgb1[2] + rgb2[2]));

			}
		}

		return outputImage;
	}

	/**
	 * @param image1
	 * @param image2
	 *            将两张图加起来 以两张图中，最小的长和宽作为输出图像的长宽
	 * */
	public static BufferedImage subImage(BufferedImage image1, BufferedImage image2)
	{
		int width = image1.getWidth() < image2.getWidth() ? image1.getWidth() : image2.getWidth();
		int height = image1.getHeight() < image2.getHeight() ? image1.getHeight() : image2.getHeight();

		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		for (int i = 0; i < height; i++)
		{
			for (int j = 0; j < width; j++)
			{
				int[] rgb1 = ImgUtil.getSplitRGB(image1.getRGB(j, i));
				int[] rgb2 = ImgUtil.getSplitRGB(image2.getRGB(j, i));
				outputImage.setRGB(j, i, ImgUtil.getRGB(rgb1[0] - rgb2[0], rgb1[1] - rgb2[1], rgb1[2] - rgb2[2]));

			}
		}

		return outputImage;
	}

	/**
	 * @param image
	 * @param x
	 * @param y
	 *            判断一个坐标点是否在图像里面
	 * */
	public static boolean isInsideImage(BufferedImage image, int x, int y)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		return 0 <= x && x < width && 0 <= y && y < height;
	}
}
