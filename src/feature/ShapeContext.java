package feature;

import geometry.Geometry;
import geometry.LogPolar;
import geometry.Point;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import config.SampleConfig;

public class ShapeContext
{
	public static final double[] POW_DOUBLES = { 1.0, 0.5, 0.25, 0.125, 0.0625, 0.03125, 0.015625 };

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
	public Point sourcePoint;

	// 其他点的对数极坐标
	private Vector<LogPolar> logPolars;

	// 其他点的xy坐标
	private Vector<Point> points;

	public double maxR;

	// 参考坐标系的角度
	public double angle;

	// 统计数据
	public int[][] statistics = new int[pBins][angleBins];

	private float averageAngle;
	private int meaningfulCount;
	private int type;
	public Point startPoint;

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
	public ShapeContext(Vector<Point> points, double angle, double maxR, int sourceIndex, int dir)
	{
		this.logPolars = new Vector<LogPolar>();

		this.angle = angle;

		this.maxR = maxR;

		this.points = points;

		this.sourcePoint = points.get(sourceIndex);

		this.sourceIndex = sourceIndex;

		this.averageAngle = 0;

		this.meaningfulCount = 0;

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
		if (meaningfulCount > 0)
		{
			this.averageAngle /= meaningfulCount;
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
		Point point = points.get(i).sub(sourcePoint);
		LogPolar logPolar = new LogPolar(point, angle);
		logPolars.addElement(logPolar);

		int x = getXIndex(maxR, logPolar.p);
		int y = (int) (angleBins - 1 - (int) (logPolar.angle * angleBins / 360));

		if (x < pBins)
		{
			statistics[x][y]++;
			meaningfulCount++;
			averageAngle += logPolar.angle;
		}

	}

	private int getXIndex(double maxR, double p)
	{
		for (int i = 0; i < pBins; i++)
		{
			if (maxR * POW_DOUBLES[i] > p && p > maxR * POW_DOUBLES[i + 1])
			{
				return pBins - i - 1;
			}
		}
		return pBins;
	}

	public String getDescribe()
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
	 * 曼哈顿距离
	 * */
	public int getDistanceL1(ShapeContext shapeConext)
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
	public float getDistanceL2(ShapeContext shapeConext)
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

	/**
	 * 卡方距离
	 * */
	public float getChiSquare(ShapeContext shapeConext)
	{
		float distance = 0;
		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				int diff = shapeConext.statistics[i][j] - statistics[i][j];
				int sum = shapeConext.statistics[i][j] + statistics[i][j];
				if (sum != 0)
				{
					distance += Math.pow(diff, 2) * 1f / sum;
				}

			}
		}
		return distance;

	}

	public boolean isAngleClose(ShapeContext shapeConext)
	{
		float diff = Math.abs(shapeConext.averageAngle - averageAngle);
		diff = diff > 180 ? 360 - diff : diff;
		return diff < 30;
	}

	public float getAngleDistance(ShapeContext shapeConext)
	{
		float diff = Math.abs(shapeConext.averageAngle - averageAngle);
		diff = diff > 180 ? 360 - diff : diff;
		diff /= 180;
		return diff * diff;
	}
}
