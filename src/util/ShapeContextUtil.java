package util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;

import config.SampleConfig;
import feature.ShapeContext;
import geometry.Geometry;
import geometry.Point;

public class ShapeContextUtil
{
	/**
	 * 将shape context的信息以文本的方式输出到文件中
	 * 
	 * @param path
	 * */
	public static void createShapeContextText(ShapeContext shapeContext, String path)
	{
		File file = new File(path);
		try
		{
			FileWriter fileWriter = new FileWriter(file);

			for (int i = 0; i < shapeContext.statistics.length; i++)
			{
				for (int j = 0; j < shapeContext.statistics[i].length; j++)
				{
					fileWriter.write(shapeContext.statistics[shapeContext.statistics.length - i - 1][j] + " ");
				}

				fileWriter.write("\r\n");
			}

			fileWriter.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 将shape context的信息以图像的方式输出到文件中
	 * 
	 * @param path
	 * */
	public static void createShapeContextImage(ShapeContext shapeContext, String path)
	{
		int width = 30 * ShapeContext.angleBins;
		int height = 30 * ShapeContext.pBins;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for (int i = 0; i < shapeContext.statistics.length; i++)
		{
			for (int j = 0; j < shapeContext.statistics[i].length; j++)
			{
				int value = 245 - shapeContext.statistics[i][j] * 10;
				value = value < 0 ? 0 : value;
				graphics2d.setColor(new Color(value, value, value));

				int x = j * 30;
				int y = (ShapeContext.pBins - 1 - i) * 30;
				graphics2d.fillRect(x, y, 30, 30);

			}
		}

		File file = new File(path);
		System.out.println(file.getPath());
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
	 * 将shape context绘制出来
	 * 
	 * @param graphics2d
	 * */
	public static void drawShapeContext(ShapeContext shapeContext, Graphics2D graphics2d)
	{

		for (int i = 0; i < shapeContext.statistics.length; i++)
		{
			for (int j = 0; j < shapeContext.statistics[i].length; j++)
			{
				int value = 245 - shapeContext.statistics[i][j] * 10;
				value = value < 0 ? 0 : value;

				graphics2d.setColor(new Color(value, value, value));
				int x = (int) shapeContext.startPoint.x + j * 30;
				int y = (int) shapeContext.startPoint.y - i * 30;
				graphics2d.fillRect(x, y, 30, 30);

			}
		}

		drawDescribe(shapeContext, graphics2d);
	}

	/**
	 * 将shape context的文本描述绘制出来
	 * 
	 * @param graphics2d
	 * */
	public static void drawDescribe(ShapeContext shapeContext, Graphics2D graphics2d)
	{
		Font f = new Font("宋体", Font.BOLD, 16);
		Color[] colors = { Color.ORANGE, Color.LIGHT_GRAY };
		graphics2d.setFont(f);
		graphics2d.setPaint(colors[0]);

		String str = shapeContext.getDescribe();
		int x = (int) shapeContext.startPoint.x + 50;
		int y = (int) shapeContext.startPoint.y + 50;
		graphics2d.drawString(str, x, y);
	}

	/**
	 * 绘制对数极坐标坐标系
	 * 
	 * @param graphics2d
	 * */
	public static void drawCoordinateSystem()
	{
		double radius = Math.exp(ShapeContext.pBins) * 2;

		BufferedImage image = new BufferedImage((int) radius, (int) radius, BufferedImage.TYPE_INT_ARGB);

		int halfWidth = image.getWidth() / 2;
		int halfHeight = image.getHeight() / 2;

		for (int y = 0; y < image.getHeight(); y++)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				image.setRGB(x, y, 0x00ffffff);
			}
		}

		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setStroke(new BasicStroke(3));
		graphics2d.setColor(new Color(255, 0, 0));

		// sourcePoint.print();
		for (int i = 1; i <= ShapeContext.pBins; i++)
		{
			radius = Math.exp(i) * 2;
			int x = (int) (halfWidth - radius / 2);
			int y = (int) (halfHeight - radius / 2);
			graphics2d.drawOval(x, y, (int) radius, (int) radius);
		}

		for (int i = 0; i < ShapeContext.angleBins; i++)
		{
			float endX = (float) (Math.exp(ShapeContext.pBins) * Math.cos(i * 2f / ShapeContext.angleBins * Math.PI));
			float endY = (float) (Math.exp(ShapeContext.pBins) * Math.sin(i * 2f / ShapeContext.angleBins * Math.PI));
			Point endPoint = new Point(endX, endY);
			graphics2d.drawLine((int) radius / 2, (int) radius / 2, (int) (endPoint.x + radius / 2), (int) (endPoint.y + radius / 2));
		}

		File file = new File(SampleConfig.OUTPUT_PATH + "coordinate.png");
		System.out.println(file.getPath());
		try
		{
			ImageIO.write(image, "PNG", file);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 绘制对数极坐标坐标系
	 * 
	 * @param graphics2d
	 * */
	public static void drawCoordinateSystem(ShapeContext shapeContext, Graphics2D graphics2d)
	{
		graphics2d.setColor(new Color(255, 0, 0));

		// sourcePoint.print();
		for (int i = 0; i < ShapeContext.pBins; i++)
		{
			double radius = shapeContext.maxR * Math.pow(0.5f, i);
			graphics2d.drawOval((int) (shapeContext.sourcePoint.x - radius), (int) (shapeContext.sourcePoint.y - radius), (int) (2 * radius),
					(int) (2 * radius));
		}

		for (int i = 0; i < ShapeContext.angleBins; i++)
		{
			float endX = (float) (shapeContext.maxR * Math.cos(i * 2f / ShapeContext.angleBins * Math.PI));
			float endY = (float) (shapeContext.maxR * Math.sin(i * 2f / ShapeContext.angleBins * Math.PI));
			Point endPoint = new Point(endX, endY);
			endPoint = Geometry.getRotatePoint(endPoint, shapeContext.angle);

			graphics2d.setStroke(new BasicStroke(3));
			// graphics2d.setColor(new Color(i*30,i*30,i*30));
			graphics2d.drawLine((int) shapeContext.sourcePoint.x, (int) shapeContext.sourcePoint.y, (int) (endPoint.x + shapeContext.sourcePoint.x),
					(int) (endPoint.y + shapeContext.sourcePoint.y));
		}
	}

}
