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
	public static final float		MINDIST				= 3;
	public static final Point[]		OFFSET_POINTS		= { new Point(1, 1), new Point(-1, 1), new Point(-1, -1), new Point(1, -1), new Point(1, 0),
			new Point(0, 1), new Point(-1, 0), new Point(0, -1), };

	public static final int[][]		LAPLACE_MASK		= { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };

	public static Vector<Point>		points				= new Vector<Point>();
	public static Vector<Point>		leftCountourPoints	= new Vector<Point>();
	public static Vector<Point>		rightCountourPoints	= new Vector<Point>();

	private static float[]			differences;
	private static int				boxSize;
	private static double			ratio;
	private static Vector<Point>	dirPoints;

	/**
	 * ��ȡ��Ե���ͼ��
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
	 * ��ȡ��Ե���ͼ��
	 * 
	 * */
	public static Vector<Point> getEdgePoints(BufferedImage edgeImage, SpinePath spinePoints)
	{
		points = spinePoints.spinePoints;

		dirPoints = Geometry.getDirPoints(points);
		Point beginPoint = getEndPoint(edgeImage, points.firstElement(), points.get(2), 1);
		Point endPoint = getEndPoint(edgeImage, points.lastElement(), points.get(points.size() - 2), -1);

		Point[] contourPoint = getContourPoint(edgeImage, beginPoint);

		leftCountourPoints = traceContour(edgeImage, contourPoint[0], beginPoint, endPoint);
		rightCountourPoints = traceContour(edgeImage, contourPoint[1], beginPoint, endPoint);

		System.out.println(leftCountourPoints.size());
		System.out.println(rightCountourPoints.size());

		handleTurningPoint();

		drawAndSaveImage(SampleConfig.OUTPUT_PATH + "After\\contour points.jpg");

		// System.out.println(leftCountourPoints.size());
		// System.out.println(rightCountourPoints.size());
		return points;
	}

	public static void drawAndSaveImage(String path)
	{
		BufferedImage outputImage = new BufferedImage(Global.width, Global.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = outputImage.createGraphics();

		for (int i = 0; i < points.size(); i++)
		{
			points.get(i).drawPoint(graphics2d, Color.red);
		}

		for (int i = 0; i < leftCountourPoints.size(); i++)
		{
			leftCountourPoints.get(i).drawPoint(graphics2d, Color.blue);
		}

		for (int i = 0; i < rightCountourPoints.size(); i++)
		{
			rightCountourPoints.get(i).drawPoint(graphics2d, Color.green);
		}

		ImageUtil.saveImage(outputImage, path);
	}

	/**
	 * ������Ϊ�յ�����Ĳ�����Ŀ�Ĳ�ͬ
	 * */
	public static void handleTurningPoint()
	{
		boxSize = (int) Global.BRUSH_WDITH * 3;
		differences = new float[points.size()];
		for (int i = 0; i < points.size(); i++)
		{
			int leftCount = getCountInBox(leftCountourPoints, points.get(i), boxSize);
			int rightCount = getCountInBox(rightCountourPoints, points.get(i), boxSize);
			differences[i] = (leftCount - rightCount) * 1f / (leftCount + rightCount);
			System.out.println("leftCount: " + leftCount + " rightCount: " + rightCount);
			System.out.println("differences: " + differences[i] + " i: " + i);
		}

		int firstIndex = findNoZeroAfterZero(differences);
		int lastIndex = findNoZeroAfterZeroOpposite(differences);

		twiceSample(firstIndex, lastIndex);
	}

	public static void twiceSample(int firstIndex, int lastIndex)
	{

		double leftRatio = leftCountourPoints.size() * 1f / points.size();
		double rightRatio = rightCountourPoints.size() * 1f / points.size();
		ratio = (leftRatio + rightRatio) / 2f;
		Vector<Point> leftPoints = new Vector<Point>();
		Vector<Point> righPoints = new Vector<Point>();

		int rightIndex = 0, leftIndex = 0;
		for (int i = 0; i < differences.length; i++)
		{
			if (i < firstIndex)
			{
				leftIndex = getMinDistIndex(leftCountourPoints, points.get(i), dirPoints.get(i), leftIndex);
				rightIndex = getMinDistIndex(rightCountourPoints, points.get(i), dirPoints.get(i), rightIndex);
			}
			else if (differences[i] > 0.1)
			{
				rightIndex += (ratio - MINDIST) * (1 - differences[i]) * (1 - differences[i]) + MINDIST;
				leftIndex = getLeftIndex(points.get(i), rightIndex, leftIndex);
			}
			else if (differences[i] < -0.1)
			{
				leftIndex += (ratio - MINDIST) * (1 + differences[i]) * (1 + differences[i]) + MINDIST;
				rightIndex = getRightIndex(points.get(i), rightIndex, leftIndex);
			}
			else
			{
				leftIndex = getMinDistIndex(leftCountourPoints, points.get(i), dirPoints.get(i), leftIndex);
				rightIndex = getMinDistIndex(rightCountourPoints, points.get(i), dirPoints.get(i), rightIndex);

			}
			System.out.println("i " + i + " leftIndex: " + leftIndex + " rightIndex: " + rightIndex + " differences: " + differences[i]);
			leftPoints.add(leftCountourPoints.get(leftIndex));
			righPoints.add(rightCountourPoints.get(rightIndex));
		}

		leftCountourPoints.clear();
		rightCountourPoints.clear();
		leftCountourPoints.addAll(leftPoints);
		rightCountourPoints.addAll(righPoints);
	}

	public static int getLeftIndex(Point currPoint, int rightIndex, int leftIndex)
	{
		Point diffPoint1 = currPoint.sub(rightCountourPoints.get(rightIndex));
		Point diffPoint2 = currPoint.sub(leftCountourPoints.get(leftIndex));
		double minCos = Geometry.getCos(diffPoint1, diffPoint2);
		for (int j = leftIndex + 1; j < leftCountourPoints.size(); j++)
		{
			diffPoint2 = currPoint.sub(leftCountourPoints.get(j));
			double cos = Geometry.getCos(diffPoint1, diffPoint2);
			if (minCos > cos)
			{
				minCos = cos;
			}
			else
			{
				leftIndex = j;
				break;
			}
		}
		return leftIndex;
	}

	public static int getRightIndex(Point currPoint, int rightIndex, int leftIndex)
	{
		Point diffPoint1 = currPoint.sub(leftCountourPoints.get(leftIndex));
		Point diffPoint2 = currPoint.sub(rightCountourPoints.get(rightIndex));
		double minCos = Geometry.getCos(diffPoint1, diffPoint2);
		for (int j = rightIndex + 1; j < rightCountourPoints.size(); j++)
		{
			diffPoint2 = currPoint.sub(rightCountourPoints.get(j));
			double cos = Geometry.getCos(diffPoint1, diffPoint2);
			if (minCos > cos)
			{
				minCos = cos;
			}
			else
			{
				rightIndex = j;
				break;
			}
		}
		return rightIndex;
	}

	public static int getMinDistIndex(Vector<Point> contourPoints, Point point, Point dirPoint, int startIndex)
	{
		int index = startIndex + 1;
		Point diffPoint = point.sub(contourPoints.get(index));
		double minCos = Math.abs(Geometry.getCos(diffPoint, dirPoint));
		for (int j = index; j < contourPoints.size() && j < startIndex + 2 * ratio; j++)
		{
			diffPoint = point.sub(contourPoints.get(j));
			double dis = Math.abs(Geometry.getCos(diffPoint, dirPoint));
			if (minCos > dis)
			{
				index = j;
				minCos = dis;
			}
		}
		return index;
	}

	public static int findNoZeroAfterZero(float[] differences)
	{
		boolean isZeroAppeared = false;
		for (int i = 0; i < differences.length; i++)
		{
			if (differences[i] == 0)
			{
				isZeroAppeared = true;
			}

			if (isZeroAppeared && differences[i] != 0)
			{
				return i;
			}
		}
		return -1;
	}

	public static int findNoZeroAfterZeroOpposite(float[] differences)
	{
		boolean isZeroAppeared = false;
		for (int i = differences.length - 1; i > 0; i--)
		{
			if (differences[i] == 0)
			{
				isZeroAppeared = true;
			}

			if (isZeroAppeared && differences[i] != 0)
			{
				return i;
			}
		}
		return -1;
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

	/**
	 * ���ɾ���������� ���������������ϵ���Ŀ�ͼ����ϵ���Ŀһ��
	 * */
	public static void makeContourSizeSame()
	{
		// ��֤������ͬ����Ŀ
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
	 * ��ȡ�����ϵ�����ʼ������ĵ�
	 * */
	public static Point[] getContourPoint(BufferedImage image, Point beginPoint)
	{
		Point[] contouPoints = new Point[2];
		int count = 0;
		for (int i = 0; i < OFFSET_POINTS.length; i++)
		{

			Point testPoint = beginPoint.add(OFFSET_POINTS[i]);

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
	 * ����׷��
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
	 * �ҵ������϶�Ӧ�Ķ˵�
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
