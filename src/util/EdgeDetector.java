package util;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class EdgeDetector
{
	public static int[][] LAPLACE_MASK = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };

	/**
	 * ªÒ»°±ﬂ‘µºÏ≤‚ÕºœÒ
	 * 
	 * */
	public static BufferedImage getImage(BufferedImage image, int type)
	{
		int maskSize = 3;
		int halfMaskSize = maskSize / 2;
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Color[][] imageMatrix = getRGBMatrix(image);

		for (int y = halfMaskSize; y < height - halfMaskSize; y++)
		{
			for (int x = halfMaskSize; x < width - halfMaskSize; x++)
			{
				int gray = getValue(imageMatrix, x, y, LAPLACE_MASK);

				outputImage.setRGB(x, y, ImgUtil.getRGB(gray, gray, gray));

			}
		}

		return outputImage;

	}

	public static Color[][] getRGBMatrix(BufferedImage image)
	{
		int width = image.getWidth();
		int height = image.getHeight();
		Color[][] matrix = new Color[width][height];

		for (int y = 0; y < height; y++)
		{
			for (int x = 0; x < width; x++)
			{
				matrix[x][y] = new Color(image.getRGB(x, y));
			}
		}
		return matrix;

	}

	static int getValue(Color[][] imageMatrix, int x, int y, int[][] Mask)
	{
		int sum = 0;
		for (int i = -1; i <= 1; i++)
		{
			for (int j = -1; j <= 1; j++)
			{
				sum += imageMatrix[x + i][y + j].getRed() * Mask[i + 1][j + 1];
			}
		}
		sum = ImgUtil.clampIn255(sum);

		return sum;
	}
}
