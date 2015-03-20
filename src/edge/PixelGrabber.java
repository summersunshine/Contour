package edge;

import geometry.CoordDiff;
import geometry.Point;
import geometry.TpsPoint;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

import sample.LibParser;
import sequence.Segement;
import stroke.LibStroke;
import tps.TPSMorpher;
import util.ColorUtil;
import util.ImageUtil;
import config.Global;

/**
 * 抓取像素
 * */
public class PixelGrabber
{
	// 空的，无需填充
	public static final int			EMPTY			= 0;
	// 填充过了
	public static final int			FILLED			= 1;
	// 等待填充
	public static final int			WAIT_FILL		= 2;

	public static final Point[]	OFFSET_POINTS	= { new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

	public static Vector<Point>	samplePoints	= new Vector<Point>();
	public static Vector<Point>		querySamplePoints	= new Vector<Point>();
	public static Vector<Float>		alphas			= new Vector<Float>();

	public static Vector<TpsPoint>	tpsPoints		= new Vector<TpsPoint>();

	// public static boolean[][] flag = new
	// boolean[Global.width][Global.height];

	public static int[][]			flag			= new int[Global.width][Global.height];

	public static BufferedImage	alphaImage;



	public static void saveResultImage(String path)
	{
		// flag = new boolean[Global.width][Global.height];

		Vector<BufferedImage> alphaImages = new Vector<BufferedImage>();

		Segement.setOverLayArea(LibParser.segements);

		for (int i = 0; i < LibParser.segements.size(); i++)
		{
			LibParser.segements.get(i).calCoorDiff();
			setSamplePoints(LibParser.segements.get(i));
			// setQuerySamplePoints(LibParser.segements.get(i));
			BufferedImage image = getWarpingImage(LibParser.segements.get(i));
			ImageUtil.saveImage(image, path + i + "_before.jpg");
			// fill(image);
			// ImageUtil.saveImage(image, path + i + "_after.jpg");

			alphaImages.add(image);
		}


		mergeMerge(alphaImages,path);

	}
	

	
	public static void mergeMerge(Vector<BufferedImage> alphaImages,String path)
	{
		System.out.println("PixelGrabber.mergeMerge()");
		alphaImage = new BufferedImage(Global.width, Global.height,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D mainGraphics2d = (Graphics2D) alphaImage.getGraphics();
		
		flag = new int[Global.width][Global.height];


		for (int y = 0; y < Global.height; y++)
		{
			for (int x = 0; x < Global.width; x++)
			{

				Vector<Integer> colors = new Vector<Integer>();
				for (int i = 0; i < alphaImages.size(); i++)
				{
					Color color = new Color(alphaImages.get(i).getRGB(x, y));
					if (color.getRed() != 0 && color.getBlue() != 0
							&& color.getGreen() != 0)
					{
						// System.out.println("x: " + x + " y: " + y);
						colors.add(alphaImages.get(i).getRGB(x, y));
					}
				}

				if (colors.size() > 0)
				{
					Color mergeColor = ColorUtil.getAlphaMergeColor(colors);
					mainGraphics2d.setColor(mergeColor);
					mainGraphics2d.fillRect(x, y, 1, 1);
				}

			}
		}
		ImageUtil.saveImage(alphaImage, path + ".jpg");
		

	}

	public static BufferedImage getWarpingImage(Segement segementInfo)
	{


		BufferedImage bufferedImage = new BufferedImage(1280, 720,
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2d = (Graphics2D) bufferedImage.getGraphics();

		int strokeId = segementInfo.strokeId;
		LibStroke libStroke = LibParser.libStrokes.get(strokeId);

		// TODO Auto-generated method stub
		BufferedImage sourceImage = ImageUtil.getCloneImage(libStroke.sourceImage);
		BufferedImage alphaImage = ImageUtil.getCloneImage(libStroke.alphaImage);
		TPSMorpher tpsMorpher = new TPSMorpher(segementInfo.coordDiffs, 0.15, 1);
		Vector<CoordDiff> samples = tpsMorpher.morphPoints(samplePoints);

		for (int i = 0; i < samples.size(); i++)
		{
			CoordDiff sampleCoordDiff = samples.get(i);

			int x = sampleCoordDiff.getIntX();
			int y = sampleCoordDiff.getIntY();
			int x2 = sampleCoordDiff.getIntX2();
			int y2 = sampleCoordDiff.getIntY2();
			Color sourceColor = new Color(sourceImage.getRGB(x, y));
			Color alphaColor = new Color(alphaImage.getRGB(x, y));

			// int alpha = (alphaColor.getRed() + alphaColor.getBlue() +
			// alphaColor
			// .getGreen()) / 3;
			// alpha = 255 - alpha;
			// if (alpha != 255)
			// {
			// System.out.println("PixelGrabber.getWarpingImage()" + alpha);
			// }
			// int alpha = (int) (alphas.get(i).floatValue() *
			// (alphaColor.getRed() + alphaColor.getGreen() +
			// alphaColor.getBlue()) / 3);
			int alpha = (alphaColor.getRed() + alphaColor.getGreen() + alphaColor.getBlue()) / 3;

			// System.out.println("PixelGrabber.getWarpingImage()" + alpha);
			// int r = color.getRed() * alpha / 255;
			// int g = color.getGreen() * alpha / 255;
			// int b = color.getBlue() * alpha / 255;
			// Color compositeColor = new Color(255 - r, 255 - g, 255 - b);
			int r = 255 - sourceColor.getRed();
			int g = 255 - sourceColor.getGreen();
			int b = 255 - sourceColor.getBlue();
			Color compositeColor = new Color(r, g, b, alpha);
			graphics2d.setColor(compositeColor);
			graphics2d.drawRect(x2, y2, 1, 1);
			flag[x2][y2] = FILLED;

		}
		return bufferedImage;
	}

	public static void fill(BufferedImage image)
	{
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for (int i = 0; i < querySamplePoints.size(); i++)
		{
			int x = querySamplePoints.get(i).getIntX();
			int y = querySamplePoints.get(i).getIntY();
			if (flag[x][y] != FILLED)
			{
				Vector<Integer> colors = new Vector<Integer>();
				for (int j = 0; j < OFFSET_POINTS.length; j++)
				{
					int x1 = x + OFFSET_POINTS[j].getIntX();
					int y1 = y + OFFSET_POINTS[j].getIntY();

					// if (flag[x1][y1] != FILLED)
					// {
					// // System.out.println("x: " + x + " y: " + y);
					// colors.add(image.getRGB(x1, y1));
					// }
					colors.add(image.getRGB(x1, y1));
				}

				if (colors.size() > 0)
				{
					Color mergeColor = ColorUtil.getAlphaMergeColor(colors);
					graphics2d.setColor(mergeColor);
					graphics2d.fillRect(x, y, 1, 1);
					flag[x][y] = FILLED;
				}
			}
		}
	}



	/**
	 * 抓到需要绘制的点
	 * */
	public static void setQuerySamplePoints(Segement segementInfo)
	{

		querySamplePoints.clear();
		boolean[][] mask = new boolean[Global.width][Global.height];

		for (int i = 0; i < segementInfo.coordDiffs.size() - 1; i++)
		{
			Point point1 = segementInfo.coordDiffs.get(i).getPoint2();
			Point point2 = segementInfo.coordDiffs.get(i + 1).getPoint2();

			Point point3 = segementInfo.coordDiffs.get(i + 1).getPoint2();
			Point point4 = segementInfo.coordDiffs.get(i).getPoint2();

			// 获取所有四个点的最大最小的x与y值
			int minX = (int) Math.floor(Math.min(Math.min(Math.min(point1.x, point2.x), point3.x), point4.x));
			int maxX = (int) Math.ceil(Math.max(Math.max(Math.max(point1.x, point2.x), point3.x), point4.x));
			int minY = (int) Math.floor(Math.min(Math.min(Math.min(point1.y, point2.y), point3.y), point4.y));
			int maxY = (int) Math.ceil(Math.max(Math.max(Math.max(point1.y, point2.y), point3.y), point4.y));

			minX = minX < 0 ? 0 : minX;
			maxX = maxX > Global.width - 1 ? Global.width - 1 : maxX;
			minY = minY < 0 ? 0 : minY;
			maxY = maxY > Global.height - 1 ? Global.height - 1 : maxY;

			// 循环，判断是否在四边形内
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					if (!mask[x][y] && LibParser.maskImage.getRGB(x, y) != Global.BLACK_VALUE)
					{
						querySamplePoints.add(new Point(x, y));

						mask[x][y] = true;

					}
				}
			}
		}
	}

	public static float getAlpha(Point point1, Point point2, Point point3, Point point4, Point point)
	{
		Point point5 = Point.getMidPoint(point1, point2);
		Point point6 = Point.getMidPoint(point3, point4);
		double dist1 = Point.getDistance(point1, point);
		double dist2 = Point.getDistance(point2, point);
		double dist3 = Point.getDistance(point3, point);
		double dist4 = Point.getDistance(point4, point);
		double dist5 = Point.getDistance(point5, point);
		double dist6 = Point.getDistance(point6, point);
		double min1 = Math.min(dist1, Math.min(dist2, dist3));
		double min2 = Math.min(dist4, Math.min(dist5, dist6));
		return (float) (min1 / (min1 + min2));
		// + Point.getDistance(point2, point);
		// double dist2 = Point.getDistance(point2, point) +
		// Point.getDistance(point3, point);
		// return (float) (dist1 / (dist1 + dist2));
	}

	/**
	 * 抓到需要绘制的点
	 * */
	public static void setSamplePoints(Segement segementInfo)
	{
		int strokeId = segementInfo.strokeId;
		LibStroke libStroke = LibParser.libStrokes.get(strokeId);
		boolean[][] mask = new boolean[libStroke.width][libStroke.height];

		BufferedImage strokeImage = new BufferedImage(libStroke.width, libStroke.height, BufferedImage.TYPE_INT_RGB);

		samplePoints.clear();
		alphas.clear();
		tpsPoints.clear();

		// Point frontLeftPoint = segementInfo.libLeftPoints.firstElement();
		// Point frontRightPoint = segementInfo.libRightPoints.firstElement();
		// Point backLeftPoint = segementInfo.libLeftPoints.lastElement();
		// Point backRightPoint = segementInfo.libRightPoints.lastElement();
		// Point frontLeftBoundaryPoint =
		// segementInfo.libLeftPoints.get(segementInfo.frontOverlayLength);
		// Point frontRightBoundaryPoint =
		// segementInfo.libRightPoints.get(segementInfo.frontOverlayLength);
		// Point backLeftBoundaryPoint =
		// segementInfo.libLeftPoints.get(segementInfo.backOverlayLength);
		// Point backRightBoundaryPoint =
		// segementInfo.libRightPoints.get(segementInfo.backOverlayLength);

		for (int i = 0; i < segementInfo.libPoints.size()-1; i++)
		{
			Point point1 = segementInfo.libLeftPoints.get(i);
			Point point2 = segementInfo.libLeftPoints.get(i + 1);

			Point point3 = segementInfo.libRightPoints.get(i + 1);
			Point point4 = segementInfo.libRightPoints.get(i);

			// 获取所有四个点的最大最小的x与y值
			int minX = (int) Math.floor(Math.min(Math.min(Math.min(point1.x, point2.x), point3.x), point4.x));
			int maxX = (int) Math.ceil(Math.max(Math.max(Math.max(point1.x, point2.x), point3.x), point4.x));
			int minY = (int) Math.floor(Math.min(Math.min(Math.min(point1.y, point2.y), point3.y), point4.y));
			int maxY = (int) Math.ceil(Math.max(Math.max(Math.max(point1.y, point2.y), point3.y), point4.y));

			minX = minX < 0 ? 0 : minX;
			maxX = maxX > strokeImage.getWidth() - 1 ? strokeImage.getWidth() - 1 : maxX;
			minY = minY < 0 ? 0 : minY;
			maxY = maxY > strokeImage.getHeight() - 1 ? strokeImage.getHeight() - 1 : maxY;



			// System.out.println("minx : " + minX + "maxx : " + maxX +
			// "miny : " + minY + "maxY : " + maxY);
			// if (Math.abs(minX - maxX) > 100 || Math.abs(minY - maxY) > 100)
			// {
			// System.out.println("gao mao!!!!!");
			// }

			// 循环，判断是否在四边形内
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					if (!mask[x][y] && libStroke.tightImage.getRGB(x, y) == Global.BLACK_VALUE)
					{
						samplePoints.add(new Point(x, y));

						// TpsPoint tpsPoint = new TpsPoint(x, y);
						mask[x][y] = true;

						// 点在前段重叠的部分
						if (i < segementInfo.frontOverlayLength)
						{
							// float alpha = getAlpha(frontLeftPoint,
							// frontRightPoint, frontRightBoundaryPoint,
							// frontLeftBoundaryPoint,
							// samplePoints.lastElement());
							// TpsPoint tpsPoint = new TpsPoint(x, y, alpha);
							// tpsPoints.add(tpsPoint);
							float alpha = (i + 1) * 1f / segementInfo.frontOverlayLength;
							alphas.add(alpha / 2 + 0.5f);
							System.out.println("front" + alphas.lastElement());
						}

						// 点在后段重叠部分
						else if (i + segementInfo.backOverlayLength > segementInfo.libPoints.size() - 1)
						{
							// float alpha = getAlpha(backLeftPoint,
							// backRightPoint, backRightBoundaryPoint,
							// backLeftBoundaryPoint,
							// samplePoints.lastElement());
							// TpsPoint tpsPoint = new TpsPoint(x, y, alpha);
							// tpsPoints.add(tpsPoint);
							float alpha = (segementInfo.libPoints.size() - 1 - i) * 1f / (segementInfo.backOverlayLength);
							alphas.add(alpha / 2 + 0.5f);
							System.out.println("back" + alphas.lastElement());
						}
						else
						{
							// TpsPoint tpsPoint = new TpsPoint(x, y, 1);
							// tpsPoints.add(tpsPoint);
							alphas.add(1f);
						}


					}
				}
			}
		}
	}
}
