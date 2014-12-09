package util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageUtil
{
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
}
