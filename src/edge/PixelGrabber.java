package edge;

import geometry.CoordDiff;
import geometry.Point;

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
	public static final Point[]	OFFSET_POINTS	= { new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1) };

	public static Vector<Point>	samplePoints	= new Vector<Point>();

	public static boolean[][]	flag			= new boolean[Global.width][Global.height];

	public static BufferedImage	alphaImage;



	public static void saveResultImage(String path)
	{
		flag = new boolean[Global.width][Global.height];


		Vector<BufferedImage> alphaImages = new Vector<BufferedImage>();
		for (int i = 0; i < LibParser.segements.size(); i++)
		{
			setSamplePoints(LibParser.segements.get(i));
			BufferedImage image = getWarpingImage(LibParser.segements.get(i));
			ImageUtil.saveImage(image, path + i + ".jpg");

			alphaImages.add(image);
		}


		mergeMerge(alphaImages,path);

	}
	

	
	public static void mergeMerge(Vector<BufferedImage> alphaImages,String path)
	{
		alphaImage = new BufferedImage(Global.width, Global.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D mainGraphics2d = (Graphics2D) alphaImage.getGraphics();
		
		flag = new boolean[Global.width][Global.height];


		for (int y = 0; y < Global.height; y++)
		{
			for (int x = 0; x < Global.width; x++)
			{

				Vector<Integer> colors = new Vector<Integer>();
				for (int i = 0; i < alphaImages.size(); i++)
				{
					if (alphaImages.get(i).getRGB(x, y) != 0)
					{
						colors.add(alphaImages.get(i).getRGB(x, y));
					}
				}

				for (int i = 0; i < colors.size(); i++)
				{
					Color mergeColor = ColorUtil.getAlphaMergeColor(true, colors);
					mainGraphics2d.setColor(mergeColor);
					mainGraphics2d.fillRect(x, y, 1, 1);
				}
			}
		}
		ImageUtil.saveImage(alphaImage, path + ".jpg");
		

	}

	public static BufferedImage getWarpingImage(Segement segementInfo)
	{
		segementInfo.calCoorDiff();

		BufferedImage bufferedImage = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2d = (Graphics2D) bufferedImage.getGraphics();

		int strokeId = segementInfo.strokeId;
		LibStroke libStroke = LibParser.libStrokes.get(strokeId);

		// TODO Auto-generated method stub
		BufferedImage image = ImageUtil.getCloneImage(libStroke.sourceImage);
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
			Color color = new Color(image.getRGB(x, y));
			Color alphaColor = new Color(alphaImage.getRGB(x, y));

			int r = color.getRed() * alphaColor.getRed() / 255;
			int g = color.getGreen() * alphaColor.getRed() / 255;
			int b = color.getBlue() * alphaColor.getRed() / 255;
			// Color compositeColor = new Color(r, g, b,alphaColor.getRed());
			Color compositeColor = new Color(color.getRed(), color.getGreen(), color.getBlue(), alphaColor.getRed());
			graphics2d.setColor(compositeColor);
			graphics2d.drawRect(x2, y2, 1, 1);

		}
		return bufferedImage;
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
						mask[x][y] = true;
					}
				}
			}
		}
	}



}
