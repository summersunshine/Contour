package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import sample.LibParser;
import stroke.LibStroke;
import stroke.QueryStroke;
import config.Global;
import config.SampleConfig;
import edge.PixelGrabber;

public class LibParserUtil
{

	public static void saveTxt(String content, String path)
	{
		File file = new File(path);

		try
		{
			FileWriter fileWriter = new FileWriter(file);

			fileWriter.write(content);

			fileWriter.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void drawStrokeSegements(String dir)
	{
		File file = new File(SampleConfig.OUTPUT_PATH + dir);

		if (!file.exists())
		{
			file.mkdir();
		}

		drawStrokeSegements();

		PixelGrabber.saveResultImage(SampleConfig.OUTPUT_PATH + "After\\alpha");

		if (LibParser.drawNum != 0)
		{
			addStrokeToResult(PixelGrabber.alphaImage);
		}
		else
		{
			LibParser.resultImage = ImageUtil.getConverterImage(PixelGrabber.alphaImage, Global.BRUSH_COLOR);

		}
		LibParser.drawNum++;
		ImageUtil.saveImage(LibParser.resultImage, SampleConfig.OUTPUT_PATH + "After\\result.jpg");

	}

	public static void addStrokeToResult(BufferedImage alphaImage)
	{
		for (int i = 0; i < Global.height; i++)
		{
			for (int j = 0; j < Global.width; j++)
			{
				int rgb = alphaImage.getRGB(j, i);
				if (rgb != Global.BLACK_VALUE)
				{
					LibParser.resultImage.setRGB(j, i, ImageUtil.grayToColor(rgb));
				}
			}
		}
	}

	public static void drawStrokeSegements()
	{

		for (int i = 0; i < LibParser.segements.size(); i++)
		{
			int strokeId = LibParser.segements.get(i).strokeId;
			int width = LibParser.libStrokes.get(strokeId).width;
			int height = LibParser.libStrokes.get(strokeId).height;

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2d = (Graphics2D) image.getGraphics();

			for (int j = 0; j < LibParser.segements.get(i).libPoints.size(); j++)
			{
				LibParser.segements.get(i).libPoints.get(j).drawPoint(graphics2d, Color.RED);
				LibParser.segements.get(i).libLeftPoints.get(j).drawPoint(graphics2d, Color.GREEN);
				LibParser.segements.get(i).libRightPoints.get(j).drawPoint(graphics2d, Color.BLUE);
			}
			ImageUtil.saveImage(image, SampleConfig.OUTPUT_PATH + "After\\" + i + "_" + strokeId + "sample.jpg");
		}

	}

	public static void drawPoints(Graphics2D graphics2d, Vector<LibStroke> libStrokes, int a, int b)
	{

		libStrokes.get(a).points.get(b).drawPoint(graphics2d, Color.RED);
		libStrokes.get(a).rightContourPoints.get(b).drawPoint(graphics2d, Color.GREEN);
		libStrokes.get(a).leftContourPoints.get(b).drawPoint(graphics2d, Color.BLUE);
	}

	public static String getLineContent(Vector<LibStroke> libStrokes, QueryStroke queryStroke, int a, int b, int queryIndex)
	{
		String content = "";
		content += (int) libStrokes.get(a).points.get(b).x + " " + (int) libStrokes.get(a).points.get(b).y + " " + (int) queryStroke.points.get(queryIndex).x
				+ " " + (int) queryStroke.points.get(queryIndex).y + "\r\n";

		content += (int) libStrokes.get(a).leftContourPoints.get(b).x + " " + (int) libStrokes.get(a).leftContourPoints.get(b).y + " "
				+ (int) queryStroke.leftContourPoints.get(queryIndex).x + " " + (int) queryStroke.leftContourPoints.get(queryIndex).y + "\r\n";

		content += (int) libStrokes.get(a).rightContourPoints.get(b).x + " " + (int) libStrokes.get(a).rightContourPoints.get(b).y + " "
				+ (int) queryStroke.rightContourPoints.get(queryIndex).x + " " + (int) queryStroke.rightContourPoints.get(queryIndex).y + "\r\n";

		return content;

	}

}
