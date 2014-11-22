package sample;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import Geometry.LogPolar;
import Geometry.Point;

public class ShapeConext
{
	public static final int TYPE_HISTORY = 0;
	public static final int TYPE_FUTURE = 1;
	public static final int TYPE_NORMAL = 2;

	// 窗口大小
	public static final int winSize = 30;

	// 径向分割的区域数目
	public static final int pBins = 5;

	// 角度方向分割的区域数目
	public static final int angleBins = 6;

	// 原点的下标（索引）
	private int sourceIndex;

	// 原点的坐标
	private Point sourcePoint;

	// 其他点的对数极坐标
	private Vector<LogPolar> logPolars;

	// 其他点的xy坐标
	private Vector<Point> points;

	// 统计数据
	private int[][] statistics = new int[pBins][angleBins];

	private int type;
	private Point startPoint;

	/**
	 * 构造函数
	 * 
	 * @param points
	 * @param sourceIndex
	 * @param dir
	 * 
	 *            如果dir>0，只统计sourceIndex之后的点 如果dir<0, 只统计souceIndex之前的点 如果dir=0,
	 *            则统计所有的点
	 * */
	public ShapeConext(Vector<Point> points, int sourceIndex, int dir)
	{
		this.logPolars = new Vector<LogPolar>();

		this.points = points;

		this.sourcePoint = points.get(sourceIndex);

		this.sourceIndex = sourceIndex;

		if (dir > 0)
		{
			type = TYPE_FUTURE;
			countForFuture();
		}
		else if (dir < 0)
		{
			type = TYPE_HISTORY;
			countForHistory();
		}
		else
		{
			type = TYPE_NORMAL;
			countForAll();
		}

		this.startPoint = getStartPoint();

	}

	/**
	 * 计算当前点之后的shape context
	 * */
	private void countForFuture()
	{
		int begin = sourceIndex + 1;
		int end = sourceIndex + winSize > points.size() ? points.size() : sourceIndex + winSize;
		for (int i = begin; i < end; i++)
		{
			count(i);
		}
	}

	/**
	 * 计算当前点之前的shape context
	 * */
	private void countForHistory()
	{
		int begin = sourceIndex - winSize < 0 ? 0 : sourceIndex - winSize;
		int end = sourceIndex;
		for (int i = begin; i < end; i++)
		{
			count(i);
		}
	}

	/**
	 * 计算所有点
	 * */
	private void countForAll()
	{
		for (int i = 0; i < points.size(); i++)
		{
			if (sourceIndex != i)
			{
				count(i);
			}
		}
	}

	/**
	 * 计算下标i的点位于哪个格子中
	 * */
	private void count(int i)
	{
		LogPolar logPolar = new LogPolar(points.get(i).sub(sourcePoint));
		logPolars.addElement(logPolar);

		int x = (int) (logPolar.p);
		int y = (int) (angleBins - 1 - (int) logPolar.angle * angleBins / 360);

		if (x < pBins)
		{
			statistics[x][y]++;
		}
	}

	/**
	 * 将shape context的信息以文本的方式输出到文件中
	 * 
	 * @param path
	 * */
	public void createShapeContextText(String path)
	{
		File file = new File(path);
		try
		{
			FileWriter fileWriter = new FileWriter(file);

			for (int i = 0; i < statistics.length; i++)
			{
				for (int j = 0; j < statistics[i].length; j++)
				{
					fileWriter.write(statistics[statistics.length - i - 1][j] + " ");
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
	public void createShapeContextImage(String path)
	{
		BufferedImage image = new BufferedImage(30 * angleBins, 30 * pBins, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				int value = 245 - statistics[i][j] * 10;
				value = value < 0 ? 0 : value;

				graphics2d.setColor(new Color(value, value, value));
				graphics2d.fillRect(j * 30, (pBins - 1 - i) * 30, 30, 30);

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
	public void drawShapeContext(Graphics2D graphics2d)
	{

		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				int value = 245 - statistics[i][j] * 10;
				value = value < 0 ? 0 : value;

				graphics2d.setColor(new Color(value, value, value));
				graphics2d.fillRect((int) startPoint.x + j * 30, (int) startPoint.y - i * 30, 30, 30);

			}
		}

		this.drawDescribe(graphics2d);
	}

	/**
	 * 将shape context的文本描述绘制出来
	 * 
	 * @param graphics2d
	 * */
	public void drawDescribe(Graphics2D graphics2d)
	{
		Font f = new Font("宋体", Font.BOLD, 16);
		Color[] colors = { Color.ORANGE, Color.LIGHT_GRAY };
		graphics2d.setFont(f);
		graphics2d.setPaint(colors[0]);
		graphics2d.drawString(getDescribe(), (int) startPoint.x + 50, (int) startPoint.y + 50);
	}

	private String getDescribe()
	{
		if (type == TYPE_FUTURE)
		{
			return "Future";
		}
		else if (type == TYPE_HISTORY)
		{
			return "HISTORY";
		}
		else
		{
			return "WHOLE";
		}
	}

	private Point getStartPoint()
	{
		if (type == TYPE_FUTURE)
		{
			return new Point(0, 600);
		}
		else
		{
			return new Point(0, 400);
		}
	}

	/**
	 * 绘制对数极坐标坐标系
	 * 
	 * @param graphics2d
	 * */
	public void drawCoordinateSystem(Graphics2D graphics2d)
	{
		graphics2d.setColor(new Color(255, 0, 0));

		// sourcePoint.print();
		for (int i = 1; i <= pBins; i++)
		{
			double radius = Math.exp(i) * 2;
			graphics2d.drawOval((int) (sourcePoint.x - radius / 2), (int) (sourcePoint.y - radius / 2), (int) radius, (int) radius);
		}

		for (int i = 0; i < angleBins; i++)
		{
			float endX = (float) (Math.exp(pBins) * Math.cos(i * 2f / angleBins * Math.PI));
			float endY = (float) (Math.exp(pBins) * Math.sin(i * 2f / angleBins * Math.PI));
			graphics2d.drawLine((int) sourcePoint.x, (int) sourcePoint.y, (int) (endX + sourcePoint.x), (int) (endY + sourcePoint.y));
		}
	}

	/**
	 * 曼哈顿距离
	 * */
	public int getDistanceL1(ShapeConext shapeConext)
	{
		int distance = 0;

		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				distance += Math.abs(shapeConext.statistics[i][j] - statistics[i][j]);
			}
		}
		return distance;
	}

	/**
	 * 欧拉距离
	 * */
	public float getDistanceL2(ShapeConext shapeConext)
	{
		float distance = 0;

		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				distance += Math.pow(shapeConext.statistics[i][j] - statistics[i][j], 2);
			}
		}
		return (float) Math.sqrt(distance);
	}
}
