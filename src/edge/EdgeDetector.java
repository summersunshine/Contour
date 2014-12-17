package edge;

import geometry.Geometry;
import geometry.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;

import util.ImageUtil;
import config.Global;
import config.SampleConfig;

public class EdgeDetector
{
	public static final Point[]	OFFSET_POINTS		= { new Point(1, 1), new Point(-1, 1), new Point(-1, -1), new Point(1, -1), new Point(1, 0),
			new Point(0, 1), new Point(-1, 0), new Point(0, -1), };

	public static final int[][]	LAPLACE_MASK		= { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };

	public static Vector<Point>	points				= new Vector<Point>();
	public static Vector<Point>	leftCountourPoints	= new Vector<Point>();
	public static Vector<Point>	rightCountourPoints	= new Vector<Point>();

	/**
	 * 获取边缘检测图像
	 * 
	 * */
	public static BufferedImage getImage(BufferedImage image)
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

				outputImage.setRGB(x, y, ImageUtil.getRGB(gray, gray, gray));

			}
		}

		return outputImage;

	}

	/**
	 * 获取边缘检测图像
	 * 
	 * */
	public static Vector<Point> getEdgePoints(BufferedImage edgeImage, SpinePoints spinePoints)
	{
		points = spinePoints.spinePoints;

		Point beginPoint = getEndPoint(edgeImage, points.firstElement(), points.get(2), 1);
		Point endPoint = getEndPoint(edgeImage, points.lastElement(), points.get(points.size() - 2), -1);

		Point[] contourPoint = getContourPoint(edgeImage, beginPoint);

		leftCountourPoints = traceContour(edgeImage, contourPoint[0], beginPoint, endPoint);
		rightCountourPoints = traceContour(edgeImage, contourPoint[1], beginPoint, endPoint);

		// System.out.println(leftCountourPoints.size());
		// System.out.println(rightCountourPoints.size());

		// handleTurningPoint1(spinePoints);
		handleTurningPoint2();
		handleTurningPoint();
		makeContourSizeSame();
		twiceSample();

		drawAndSaveImage(edgeImage);

		// System.out.println(leftCountourPoints.size());
		// System.out.println(rightCountourPoints.size());
		return points;
	}

	public static void drawAndSaveImage(BufferedImage edgeImage)
	{
		BufferedImage outputImage = new BufferedImage(edgeImage.getWidth(), edgeImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = outputImage.createGraphics();

		graphics2d.setColor(new Color(255, 0, 0));
		for (int i = 0; i < points.size(); i++)
		{
			graphics2d.fillRect(points.get(i).getIntX(), points.get(i).getIntY(), 1, 1);
		}

		graphics2d.setColor(new Color(0, 0, 255));
		for (int i = 0; i < leftCountourPoints.size(); i++)
		{
			graphics2d.fillRect(leftCountourPoints.get(i).getIntX(), leftCountourPoints.get(i).getIntY(), 1, 1);
		}

		graphics2d.setColor(new Color(0, 255, 0));
		for (int i = 0; i < rightCountourPoints.size(); i++)
		{
			graphics2d.fillRect(rightCountourPoints.get(i).getIntX(), rightCountourPoints.get(i).getIntY(), 1, 1);
		}

		ImageUtil.saveImage(outputImage, SampleConfig.OUTPUT_PATH + "After\\contour points.jpg");
	}

	/**
	 * 处理因为拐点带来的采样数目的不同
	 * */
	public static void handleTurningPoint()
	{
		int count = 0;
		for (int i = 0; i < leftCountourPoints.size() && i < rightCountourPoints.size(); i++)
		{
			Point leftPoint = leftCountourPoints.get(i);
			Point rightPoint = rightCountourPoints.get(i);

			double length = leftPoint.sub(rightPoint).length();
			if (length > 2 * Global.BRUSH_WDITH + 2)
			{
				count = 0;
				Point nextRightPoint = rightCountourPoints.get(i + 1);
				Point nextLeftPoint = leftCountourPoints.get(i + 1);

				int deleteCount = 0;
				while (leftPoint.sub(nextRightPoint).length() < length)
				{
					rightCountourPoints.remove(i);
					length = leftPoint.sub(nextRightPoint).length();
					nextRightPoint = rightCountourPoints.get(i + 1);
					if (deleteCount++ > 2)
					{
						break;
					}

				}

				while (rightPoint.sub(nextLeftPoint).length() < length)
				{
					leftCountourPoints.remove(i);
					length = rightPoint.sub(nextLeftPoint).length();
					nextLeftPoint = leftCountourPoints.get(i + 1);

					if (deleteCount++ > 2)
					{
						break;
					}
				}

			}
			else
			{
				count++;
				if (count == 4)
				{
					int n = 3;
					while ((n--) > 0)
					{
						if (rightCountourPoints.size() > i)
						{
							rightCountourPoints.remove(i);
						}

					}
					n = 3;
					while ((n--) > 0)
					{
						if (leftCountourPoints.size() > i)
						{
							leftCountourPoints.remove(i);
						}

					}

					count = 0;
				}
			}

		}

	}

	/**
	 * 处理因为拐点带来的采样数目的不同
	 * */
	public static void handleTurningPoint2()
	{
		int[] differences = new int[points.size()];
		for (int i = 0; i < points.size(); i++)
		{
			int leftCount = getCountInBox(leftCountourPoints, points.get(i), (int) Global.BRUSH_WDITH * 4);
			int rightCount = getCountInBox(rightCountourPoints, points.get(i), (int) Global.BRUSH_WDITH * 4);
			differences[i] = leftCount - rightCount;
			System.out.println("leftCount: " + leftCount + " rightCount: " + rightCount);
			System.out.println("sum: " + (leftCount - rightCount) + " i: " + i);
		}

	}

	public static int getCountInBox(Vector<Point> points, Point point, int size)
	{
		int minX = (int) (point.x - size / 2);
		int maxX = (int) (point.x + size / 2);
		int minY = (int) (point.y - size / 2);
		int maxY = (int) (point.y + size / 2);
		return getCountInBox(points, minX, maxX, minY, maxY);
	}

	public static int getCountInBox(Vector<Point> points, int minX, int maxX, int minY, int maxY)
	{
		int count = 0;
		for (int i = 0; i < points.size(); i++)
		{
			if (Geometry.isInBox(minX, maxX, minY, maxY, points.get(i)))
			{
				count++;
			}
		}
		return count;
	}

	public static void handleTurningPoint1(SpinePoints spinePoints)
	{
		for (int i = 0; i < points.size(); i++)
		{
			while (leftCountourPoints.size() >= points.size())
			{
				Point leftPoint = leftCountourPoints.get(i);
				double leftLength = points.get(i).sub(leftPoint).length();
				if (leftLength > Global.BRUSH_WDITH * spinePoints.radius.get(i))
				{
					leftCountourPoints.remove(i);
				}
				else
				{
					break;
				}
			}

			while (rightCountourPoints.size() >= points.size())
			{
				Point rightPoint = rightCountourPoints.get(i);

				double rightLength = points.get(i).sub(rightPoint).length();

				if (rightLength > Global.BRUSH_WDITH * spinePoints.radius.get(i))
				{
					rightCountourPoints.remove(i);
				}
				else
				{
					break;
				}
			}
		}

	}

	/**
	 * 随机删除部分数据 保持左右轮廓点上的数目和脊柱上的数目一致
	 * */
	public static void makeContourSizeSame()
	{
		// 保证具有相同的数目
		while (leftCountourPoints.size() != rightCountourPoints.size())
		{
			if (leftCountourPoints.size() < rightCountourPoints.size())
			{
				Random random = new Random();
				int index = random.nextInt(rightCountourPoints.size() - 1);
				rightCountourPoints.remove(index);
			}
			if (leftCountourPoints.size() > rightCountourPoints.size())
			{
				Random random = new Random();
				int index = random.nextInt(leftCountourPoints.size() - 1);
				leftCountourPoints.remove(index);
			}
		}
	}

	/**
	 * 二次采样，减少轮廓点的数目到合适的范围
	 * */
	public static void twiceSample()
	{

		double ratio = leftCountourPoints.size() * 1f / points.size();
		Vector<Point> leftPoints = new Vector<Point>();
		Vector<Point> righPoints = new Vector<Point>();

		for (int i = 0; i < points.size(); i++)
		{
			int index = (int) (i * ratio);
			leftPoints.add(leftCountourPoints.get(index));
			righPoints.add(rightCountourPoints.get(index));
		}
		leftCountourPoints.clear();
		rightCountourPoints.clear();
		leftCountourPoints.addAll(leftPoints);
		rightCountourPoints.addAll(righPoints);

	}

	/**
	 * 获取轮廓上的与起始点响铃的点
	 * */
	public static Point[] getContourPoint(BufferedImage image, Point beginPoint)
	{
		Point[] contouPoints = new Point[2];
		int count = 0;
		for (int i = 0; i < OFFSET_POINTS.length; i++)
		{

			Point testPoint = beginPoint.add(OFFSET_POINTS[i]);

			// System.out.println(image.getRGB(testPoint.getIntX(),
			// testPoint.getIntY()));

			if (image.getRGB(testPoint.getIntX(), testPoint.getIntY()) == Global.WHITE_VALUE)
			{
				contouPoints[count++] = testPoint;
				if (count >= 2)
				{
					break;
				}
			}
		}

		return contouPoints;
	}

	/**
	 * 轮廓追踪
	 * */
	public static Vector<Point> traceContour(BufferedImage image, Point currPoint, Point lastPoint, Point endPoint)
	{
		Vector<Point> contourPoints = new Vector<Point>();

		// contourPoints.add(currPoint);

		while (!currPoint.equal(endPoint))
		{
			Point tempPoint = currPoint;
			for (int i = 0; i < OFFSET_POINTS.length; i++)
			{
				tempPoint = currPoint.add(OFFSET_POINTS[i]);
				int x = tempPoint.getIntX();
				int y = tempPoint.getIntY();

				if (image.getRGB(x, y) == Global.WHITE_VALUE && !tempPoint.equal(lastPoint))
				{
					contourPoints.add(tempPoint);

					lastPoint = currPoint;
					currPoint = tempPoint;
					break;
				}
			}
		}
		return contourPoints;
	}

	/**
	 * 找到轮廓上对应的端点
	 * */
	public static Point getEndPoint(BufferedImage image, Point firstPoint, Point secondPoint, int dir)
	{
		double dx = firstPoint.x - secondPoint.x;
		double dy = firstPoint.y - secondPoint.y;
		double num = Math.abs(dy) > Math.abs(dx) ? Math.abs(dy) : Math.abs(dx);
		double rateX = dx / num;
		double rateY = dy / num;

		for (int i = 0; i < 2 * Global.BRUSH_WDITH; i++)
		{
			Point point = firstPoint.add(new Point(i * rateX, i * rateY));

			for (int j = 0; j < OFFSET_POINTS.length; j++)
			{
				Point testPoint = point.add(OFFSET_POINTS[j]);
				if (image.getRGB(testPoint.getIntX(), testPoint.getIntY()) == Global.WHITE_VALUE)
				{
					return testPoint;
				}
			}
			// if (i % Global.SAMPLE_DIST == (Global.SAMPLE_DIST - 1))
			// {
			// if (dir > 0)
			// {
			// points.add(0, point);
			// }
			// else
			// {
			// points.add(point);
			// }
			// }

		}
		return firstPoint;

	}

	public static boolean isCloseToCountour(BufferedImage image, Point currPoint)
	{
		for (int i = 0; i < OFFSET_POINTS.length; i++)
		{
			Point testPoint = currPoint.add(OFFSET_POINTS[i]);
			if (image.getRGB(testPoint.getIntX(), testPoint.getIntY()) == Global.WHITE_VALUE)
			{
				return true;
			}
		}
		return false;
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
		sum = ImageUtil.clampIn255(sum);

		return sum;
	}
}
