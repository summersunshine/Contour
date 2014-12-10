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
	public static final Point[] OFFSET_POINTS = { new Point(1, 0), new Point(1, 1), new Point(0, 1), new Point(-1, 1), new Point(-1, 0), new Point(-1, -1),
			new Point(0, -1), new Point(1, -1) };

	public static final int[][] LAPLACE_MASK = { { 0, -1, 0 }, { -1, 4, -1 }, { 0, -1, 0 } };

	public static Vector<Point> points = new Vector<Point>();
	public static Vector<Point> leftCountourPoints = new Vector<Point>();
	public static Vector<Point> rightCountourPoints = new Vector<Point>();

	
	
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
	public static Vector<Point> getEdgePoints(BufferedImage edgeImage, Vector<Point> spinePoints)
	{
		points = spinePoints;

		Point beginPoint = getEndPoint(edgeImage, spinePoints.firstElement(), spinePoints.get(2));
		Point endPoint = getEndPoint(edgeImage, spinePoints.lastElement(), spinePoints.get(spinePoints.size() - 2));


		Point leftPoint = getContourPoint(edgeImage, beginPoint, endPoint, -1);
		Point rightPoint = getContourPoint(edgeImage, beginPoint, endPoint, 1);

		leftCountourPoints = traceContour(edgeImage, leftPoint, beginPoint, endPoint);
		rightCountourPoints = traceContour(edgeImage, rightPoint, beginPoint, endPoint);

		System.out.println(leftCountourPoints.size());
		System.out.println(rightCountourPoints.size());
		
		
		handleTurningPoint();
		makeContourSizeSame();
		twiceSample();
		
		drawAndSaveImage(edgeImage);


		System.out.println(leftCountourPoints.size());
		System.out.println(rightCountourPoints.size());
		return points;
	}

	public static void drawAndSaveImage(BufferedImage edgeImage)
	{
		BufferedImage outputImage = new BufferedImage(edgeImage.getWidth(), edgeImage.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = outputImage.createGraphics();
		
		graphics2d.setColor(new Color(255,0,0));
		for (int i = 0; i < points.size(); i++)
		{
			graphics2d.fillRect(points.get(i).getIntX(), points.get(i).getIntY(), 1, 1);
		}
		
		graphics2d.setColor(new Color(0,0,255));
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
	
	public static void handleTurningPoint()
	{
		int min;
		if (leftCountourPoints.size()<rightCountourPoints.size())
		{
			min = leftCountourPoints.size();
		}
		else
		{
			min = rightCountourPoints.size();
		}
		
		for (int i = 0; i < min; i++)
		{
			Point leftPoint = leftCountourPoints.get(i);
			Point rightPoint = rightCountourPoints.get(i);
			
			if (leftPoint.sub(rightPoint).length()>2*Global.BRUSH_WDITH+1)
			{
				if (leftCountourPoints.size() > rightCountourPoints.size())
				{
					leftCountourPoints.remove(i);
				}
				
				if (leftCountourPoints.size() < rightCountourPoints.size())
				{
					rightCountourPoints.remove(i);
				}
			}
			
			//System.out.println(leftPoint.sub(rightPoint).length());
		}
		

	}
	
	
	public static void makeContourSizeSame()
	{
		//保证具有相同的数目
		while (leftCountourPoints.size() != rightCountourPoints.size())
		{
			if (leftCountourPoints.size() < rightCountourPoints.size())
			{
				Random random = new Random();
				int index = random.nextInt(rightCountourPoints.size()-1);
				rightCountourPoints.remove(index);
			}
			if (leftCountourPoints.size() > rightCountourPoints.size())
			{
				Random random = new Random();
				int index = random.nextInt(leftCountourPoints.size()-1);
				leftCountourPoints.remove(index);
			}
		}
	}
	
	
	public static void twiceSample()
	{
		
		double ratio = leftCountourPoints.size()*1f/points.size();
		Vector<Point> leftPoints = new Vector<Point>();
		Vector<Point> righPoints = new Vector<Point>();
		
		for (int i = 0; i < points.size(); i++)
		{
			int index = (int) (i*ratio);
			leftPoints.add(leftCountourPoints.get(index));
			righPoints.add(rightCountourPoints.get(index));
		}
		leftCountourPoints.clear();
		rightCountourPoints.clear();
		leftCountourPoints.addAll(leftPoints);
		rightCountourPoints.addAll(righPoints);
		
	}
	

	public static Point getContourPoint(BufferedImage image, Point beginPoint, Point endPoint, int dir)
	{
		for (int i = 0; i < OFFSET_POINTS.length; i++)
		{

			Point testPoint = beginPoint.add(OFFSET_POINTS[i]);

			System.out.println(image.getRGB(testPoint.getIntX(), testPoint.getIntY()));

			if (image.getRGB(testPoint.getIntX(), testPoint.getIntY()) == Global.WHITE_VALUE)
			{
				if (dir < 0 && Geometry.isLeftSide(beginPoint, endPoint, testPoint))
				{
					return testPoint;
				}

				if (dir > 0 && Geometry.isRightSide(beginPoint, endPoint, testPoint))
				{
					return testPoint;
				}
			}
		}
		return beginPoint;
	}

	public static Vector<Point> traceContour(BufferedImage image, Point currPoint, Point lastPoint, Point endPoint)
	{
		Vector<Point> contourPoints = new Vector<Point>();

		contourPoints.add(currPoint);
		
		while (!currPoint.equal(endPoint))
		{
			Point tempPoint = currPoint;
			for (int i = 0; i < OFFSET_POINTS.length; i++)
			{
				tempPoint = currPoint.add(OFFSET_POINTS[i]);
				int x = tempPoint.getIntX();
				int y = tempPoint.getIntY();
				
				if (image.getRGB(x,y) == Global.WHITE_VALUE && 
						!tempPoint.equal(lastPoint))
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

	public static Point getEndPoint(BufferedImage image, Point firstPoint, Point secondPoint)
	{
		double dx = firstPoint.x - secondPoint.x;
		double dy = firstPoint.y - secondPoint.y;
		double num = Math.abs(dy) > Math.abs(dx) ? Math.abs(dy) : Math.abs(dx);
		double rateX = dx / num;
		double rateY = dy / num;


		for (int i = 0; i < 2*Global.BRUSH_WDITH; i++)
		{
			Point point = firstPoint.add(new Point(i * rateX, i * rateY));
//			if (i%Global.SAMPLE_DIST==(Global.SAMPLE_DIST-1))
//			{
//				points.add(point);
//			}
			
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
