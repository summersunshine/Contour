package edge;

import geometry.CoordDiff;
import geometry.Geometry;
import geometry.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.Vector;

import sample.LibParser;
import sample.LibParserUtil;
import sequence.SegementInfo;
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
	public static final Point[]	OFFSET_POINTS	= { new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

	public static Vector<Point>	points			= new Vector<Point>();

	public static boolean[][]	flag			= new boolean[Global.width][Global.height];

	public static BufferedImage	alphaImage;

	public static void saveResultImage(String path, boolean isAlphaMerge)
	{

		alphaImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
		Graphics2D mainGraphics2d = (Graphics2D) alphaImage.getGraphics();

		Vector<BufferedImage> alphaImages = new Vector<BufferedImage>();
		for (int i = 0; i < LibParser.segements.size(); i++)
		{
			BufferedImage bufferedImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2d = (Graphics2D) bufferedImage.getGraphics();
			System.out.println("segement" + i);
			int start = LibParser.segements.get(i).getStartIndexOfLib();
			int end = LibParser.segements.get(i).getEndIndexOfLib();
			int index = LibParser.segements.get(i).getIndexofLibStroke();
			LibStroke libStroke = LibParser.libStrokes.get(index);
			Vector<CoordDiff> coordDiffs = LibParserUtil.vectors.get(i);
			setSamplePoints(libStroke, index, start, end);
			drawWarpingImage(libStroke.alphaImage, coordDiffs, graphics2d, isAlphaMerge, (short) (i + 1));

			ImageUtil.saveImage(bufferedImage, path + i + ".jpg");

			alphaImages.add(bufferedImage);
		}

		flag = new boolean[Global.width][Global.height];

		for (int i = 0; i < alphaImages.size(); i++)
		{
			for (int y = 0; y < Global.height; y++)
			{
				for (int x = 0; x < Global.width; x++)
				{

					if (alphaImages.get(i).getRGB(x, y) != Global.BLACK_VALUE)
					{
						if (flag[x][y])
						{
							Color color = new Color(alphaImages.get(i).getRGB(x, y));
							Color srcColor = new Color(alphaImage.getRGB(x, y));
							Color mergeColor = ColorUtil.getAlphaMergeColor(color, srcColor);
							mainGraphics2d.setColor(mergeColor);
						}
						else
						{
							Color color = new Color(alphaImages.get(i).getRGB(x, y));
							mainGraphics2d.setColor(color);
						}
						flag[x][y] = true;
						mainGraphics2d.fillRect(x, y, 1, 1);
					}

				}
			}
		}

		ImageUtil.saveImage(alphaImage, path + ".jpg");

	}

	public static void saveResultImage(String path)
	{
		flag = new boolean[Global.width][Global.height];

		alphaImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
		Graphics2D mainGraphics2d = (Graphics2D) alphaImage.getGraphics();
		Vector<BufferedImage> alphaImages = new Vector<BufferedImage>();
		for (int i = 0; i < LibParser.segementInfos.size(); i++)
		{
			BufferedImage bufferedImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2d = (Graphics2D) bufferedImage.getGraphics();

			SegementInfo segementInfo = LibParser.segementInfos.get(i);
			int strokeId = segementInfo.strokeId;
			LibStroke libStroke = LibParser.libStrokes.get(strokeId);

			setSamplePoints(libStroke, segementInfo);
			drawWarpingImage(libStroke.alphaImage, segementInfo, graphics2d, true, (short) (i + 1));

			ImageUtil.saveImage(bufferedImage, path + i + ".jpg");

			alphaImages.add(bufferedImage);
		}

		flag = new boolean[Global.width][Global.height];

		for (int i = 0; i < alphaImages.size(); i++)
		{
			for (int y = 0; y < Global.height; y++)
			{
				for (int x = 0; x < Global.width; x++)
				{

					if (alphaImages.get(i).getRGB(x, y) != Global.BLACK_VALUE)
					{
						if (flag[x][y])
						{
							Color color = new Color(alphaImages.get(i).getRGB(x, y));
							Color srcColor = new Color(alphaImage.getRGB(x, y));
							Color mergeColor = ColorUtil.getAlphaMergeColor(color, srcColor);
							mainGraphics2d.setColor(mergeColor);
						}
						else
						{
							Color color = new Color(alphaImages.get(i).getRGB(x, y));
							mainGraphics2d.setColor(color);
						}
						flag[x][y] = true;
						mainGraphics2d.fillRect(x, y, 1, 1);
					}

				}
			}
		}

		ImageUtil.saveImage(alphaImage, path + ".jpg");

	}

	public static void drawWarpingImage(BufferedImage strokealphaImage, SegementInfo segementInfo, Graphics2D graphics2d, boolean isAlphaMerge, short index)
	{
		segementInfo.calCoorDiff();

		// TODO Auto-generated method stub
		BufferedImage image = ImageUtil.getCloneImage(strokealphaImage);

		TPSMorpher tpsMorpher = new TPSMorpher(segementInfo.coordDiffs, 0.15, 1);
		Vector<CoordDiff> samples = tpsMorpher.morphPoints(points);
		System.out.println("sample size " + samples.size());
		for (int i = 0; i < samples.size(); i++)
		{
			CoordDiff sampleCoordDiff = samples.get(i);

			int x = sampleCoordDiff.getIntX();
			int y = sampleCoordDiff.getIntY();
			int x2 = sampleCoordDiff.getIntX2();
			int y2 = sampleCoordDiff.getIntY2();

			// if (flag[x2][y2] != index && isAlphaMerge)
			// {
			// Color color1 = new Color(alphaImage.getRGB(x2, y2));
			// Color color2 = new Color(image.getRGB(x, y));
			// Color mergeColor = ColorUtil.getAlphaMergeColor(color1, color2);
			//
			// graphics2d.setColor(mergeColor);
			//
			// flag[x2][y2] = index;
			// }
			// else
			// {
			// flag[x2][y2] = index;
			//
			// }
			graphics2d.setColor(new Color(image.getRGB(x, y)));
			graphics2d.drawRect(x2, y2, 1, 1);

		}

	}

	public static void drawWarpingImage(BufferedImage strokealphaImage, Vector<CoordDiff> coordDiffs, Graphics2D graphics2d, boolean isAlphaMerge, short index)
	{
		// TODO Auto-generated method stub
		BufferedImage image = ImageUtil.getCloneImage(strokealphaImage);

		TPSMorpher tpsMorpher = new TPSMorpher(coordDiffs, 0.15, 1);
		Vector<CoordDiff> samples = tpsMorpher.morphPoints(points);
		System.out.println("sample size " + samples.size());
		for (int i = 0; i < samples.size(); i++)
		{
			CoordDiff sampleCoordDiff = samples.get(i);

			int x = sampleCoordDiff.getIntX();
			int y = sampleCoordDiff.getIntY();
			int x2 = sampleCoordDiff.getIntX2();
			int y2 = sampleCoordDiff.getIntY2();

			// if (flag[x2][y2] != index && isAlphaMerge)
			// {
			// Color color1 = new Color(alphaImage.getRGB(x2, y2));
			// Color color2 = new Color(image.getRGB(x, y));
			// Color mergeColor = ColorUtil.getAlphaMergeColor(color1, color2);
			//
			// graphics2d.setColor(mergeColor);
			//
			// flag[x2][y2] = index;
			// }
			// else
			// {
			// flag[x2][y2] = index;
			graphics2d.setColor(new Color(image.getRGB(x, y)));
			// }

			graphics2d.drawRect(x2, y2, 1, 1);

		}

	}

	@SuppressWarnings("unchecked")
	public static Vector<Point> getClockWisePoints(Vector<Point> points)
	{
		Point midPoint = new Point();

		for (int i = 0; i < points.size(); i++)
		{
			midPoint.add(points.get(i));
		}

		midPoint = midPoint.div(points.size());

		for (int i = 0; i < points.size(); i++)
		{
			points.get(i).sub(midPoint);
		}

		Collections.sort(points, new Point.AngleComparator());

		for (int i = 0; i < points.size(); i++)
		{
			points.get(i).add(midPoint);
		}

		return points;

	}

	/**
	 * 抓到需要绘制的点
	 * */
	public static void setSamplePoints(LibStroke libStroke, SegementInfo segementInfo)
	{
		boolean[][] mask = new boolean[libStroke.width][libStroke.height];
		BufferedImage strokeImage = new BufferedImage(libStroke.width, libStroke.height, BufferedImage.TYPE_INT_RGB);
		// Graphics2D strokeGraphics2d = strokeImage.createGraphics();

		Point point12 = segementInfo.libLeftPoints.get(0);
		Point point22 = segementInfo.libLeftPoints.get(0 + 1);

		Point point32 = segementInfo.libRightPoints.get(0 + 1);
		Point point42 = segementInfo.libRightPoints.get(0);

		int minX2 = (int) Math.floor(Math.min(Math.min(Math.min(point12.x, point22.x), point32.x), point42.x));
		int maxX2 = (int) Math.ceil(Math.max(Math.max(Math.max(point12.x, point22.x), point32.x), point42.x));
		int minY2 = (int) Math.floor(Math.min(Math.min(Math.min(point12.y, point22.y), point32.y), point42.y));
		int maxY2 = (int) Math.ceil(Math.max(Math.max(Math.max(point12.y, point22.y), point32.y), point42.y));

		points.clear();
		for (int i = 0; i < segementInfo.libPoints.size(); i++)
		{
			Point point1 = segementInfo.libLeftPoints.get(0);
			Point point2 = segementInfo.libLeftPoints.get(0 + 1);

			Point point3 = segementInfo.libRightPoints.get(0 + 1);
			Point point4 = segementInfo.libRightPoints.get(0);

			// 获取所有四个点的最大最小的x与y值
			int minX = (int) Math.floor(Math.min(Math.min(Math.min(point1.x, point2.x), point3.x), point4.x));
			int maxX = (int) Math.ceil(Math.max(Math.max(Math.max(point1.x, point2.x), point3.x), point4.x));
			int minY = (int) Math.floor(Math.min(Math.min(Math.min(point1.y, point2.y), point3.y), point4.y));
			int maxY = (int) Math.ceil(Math.max(Math.max(Math.max(point1.y, point2.y), point3.y), point4.y));

			minX = minX < 0 ? 0 : minX;
			maxX = maxX > strokeImage.getWidth() - 1 ? strokeImage.getWidth() - 1 : maxX;
			minY = minY < 0 ? 0 : minY;
			maxY = maxY > strokeImage.getHeight() - 1 ? strokeImage.getHeight() - 1 : maxY;

			minX2 = minX2 > minX ? minX : minX2;
			maxX2 = maxX2 < maxX ? maxX : maxX2;
			minY2 = minY2 > minY ? minY : minY2;
			maxY2 = minY2 < maxY ? maxY : maxY2;

			if (Math.abs(minX - maxX) > 100 || Math.abs(minY - maxY) > 100)
			{
				System.out.println("gao mao!!!!!");
			}

			// 循环，判断是否在四边形内
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					if (!mask[x][y] && libStroke.tightImage.getRGB(x, y) == Global.BLACK_VALUE)
					{
						// strokeGraphics2d.setColor(new Color(255, 255, 255));
						// strokeGraphics2d.fillRect(x, y, 1, 1);
						points.add(new Point(x, y));
						mask[x][y] = true;
					}
				}
			}
		}
	}

	/**
	 * 抓到需要绘制的点
	 * */
	public static void setSamplePoints(LibStroke libStroke, int index, int start, int end)
	{
		boolean[][] mask = new boolean[libStroke.width][libStroke.height];
		BufferedImage strokeImage = new BufferedImage(libStroke.width, libStroke.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D strokeGraphics2d = strokeImage.createGraphics();

		Point point12 = libStroke.leftContourPoints.get(0);
		Point point22 = libStroke.leftContourPoints.get(0 + 1);

		Point point32 = libStroke.rightContourPoints.get(0 + 1);
		Point point42 = libStroke.rightContourPoints.get(0);

		int minX2 = (int) Math.floor(Math.min(Math.min(Math.min(point12.x, point22.x), point32.x), point42.x));
		int maxX2 = (int) Math.ceil(Math.max(Math.max(Math.max(point12.x, point22.x), point32.x), point42.x));
		int minY2 = (int) Math.floor(Math.min(Math.min(Math.min(point12.y, point22.y), point32.y), point42.y));
		int maxY2 = (int) Math.ceil(Math.max(Math.max(Math.max(point12.y, point22.y), point32.y), point42.y));

		points.clear();
		for (int i = start + 1; i < end - 1; i++)
		{
			Point point1 = libStroke.leftContourPoints.get(i);
			Point point2 = libStroke.leftContourPoints.get(i + 1);

			Point point3 = libStroke.rightContourPoints.get(i + 1);
			Point point4 = libStroke.rightContourPoints.get(i);

			// 获取所有四个点的最大最小的x与y值
			int minX = (int) Math.floor(Math.min(Math.min(Math.min(point1.x, point2.x), point3.x), point4.x));
			int maxX = (int) Math.ceil(Math.max(Math.max(Math.max(point1.x, point2.x), point3.x), point4.x));
			int minY = (int) Math.floor(Math.min(Math.min(Math.min(point1.y, point2.y), point3.y), point4.y));
			int maxY = (int) Math.ceil(Math.max(Math.max(Math.max(point1.y, point2.y), point3.y), point4.y));

			minX = minX < 0 ? 0 : minX;
			maxX = maxX > strokeImage.getWidth() - 1 ? strokeImage.getWidth() - 1 : maxX;
			minY = minY < 0 ? 0 : minY;
			maxY = maxY > strokeImage.getHeight() - 1 ? strokeImage.getHeight() - 1 : maxY;

			minX2 = minX2 > minX ? minX : minX2;
			maxX2 = maxX2 < maxX ? maxX : maxX2;
			minY2 = minY2 > minY ? minY : minY2;
			maxY2 = minY2 < maxY ? maxY : maxY2;

			if (Math.abs(minX - maxX) > 100 || Math.abs(minY - maxY) > 100)
			{
				System.out.println("gao mao!!!!!");
			}

			// 循环，判断是否在四边形内
			for (int y = minY; y <= maxY; y++)
			{
				for (int x = minX; x <= maxX; x++)
				{
					if (!mask[x][y] && libStroke.tightImage.getRGB(x, y) == Global.BLACK_VALUE)
					{
						strokeGraphics2d.setColor(new Color(255, 255, 255));
						strokeGraphics2d.fillRect(x, y, 1, 1);
						points.add(new Point(x, y));
						mask[x][y] = true;
					}
				}
			}
		}
	}

	public static boolean isPointInPolygon(Point point, Vector<Point> polygon)
	{
		double angle = 0;

		for (int i = 0; i < polygon.size() - 1; i++)
		{
			Point point1 = polygon.get(i).sub(point);
			Point point2 = polygon.get(i + 1).sub(point);
			double temp = Geometry.getAngle(point1, point2) * 180 / Math.PI;
			angle += temp;
		}

		System.out.println("angle :" + angle);
		return Math.abs(angle - 360) < 60;
	}

	public static boolean isIntersect(double px1, double py1, double px2, double py2, double px3, double py3, double px4, double py4)
	{
		boolean flag = false;
		double d = (px2 - px1) * (py4 - py3) - (py2 - py1) * (px4 - px3);
		if (d != 0)
		{
			double r = ((py1 - py3) * (px4 - px3) - (px1 - px3) * (py4 - py3)) / d;
			double s = ((py1 - py3) * (px2 - px1) - (px1 - px3) * (py2 - py1)) / d;
			if ((r >= 0) && (r <= 1) && (s >= 0) && (s <= 1))
			{
				flag = true;
			}
		}
		return flag;
	}

	public static boolean isPointOnLine(double px0, double py0, double px1, double py1, double px2, double py2)
	{
		boolean flag = false;
		double ESP = 1e-9;
		if ((Math.abs(Multiply(px0, py0, px1, py1, px2, py2)) < ESP) && ((px0 - px1) * (px0 - px2) <= 0) && ((py0 - py1) * (py0 - py2) <= 0))
		{
			flag = true;
		}
		return flag;
	}

	public static double Multiply(double px0, double py0, double px1, double py1, double px2, double py2)
	{
		return ((px1 - px0) * (py2 - py0) - (px2 - px0) * (py1 - py0));
	}
}
