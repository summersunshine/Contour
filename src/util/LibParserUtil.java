package util;

import geometry.CoordDiff;
import geometry.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import config.SampleConfig;
import sample.LibParser;
import sequence.Segement;
import stroke.LibStroke;
import stroke.QueryStroke;

public class LibParserUtil
{

	
	public static Vector<Vector<CoordDiff>> vectors = new Vector<Vector<CoordDiff>>();

	public static void saveImage(BufferedImage image, String path)
	{
		File file = new File(path);

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

	public static void saveResultImage(String path)
	{
		BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for (int i = 0; i < LibParser.segements.size(); i++)
		{
			int start = LibParser.segements.get(i).cs.firstElement().b;
			int end = LibParser.segements.get(i).cs.lastElement().b;

			int index = LibParser.segements.get(i).getIndexofLibStroke();
			System.out.println("Stroke Index: " + index);
			PixelGrabberUtil.drawWarpImage(LibParser.libStrokes.get(index), i, start, end, graphics2d);

		}
		saveImage(image, path);
	}

	public static void drawStrokeSegements(String dir, Vector<Segement> segements, Vector<LibStroke> libStrokes, QueryStroke queryStroke)
	{
		File file = new File(SampleConfig.OUTPUT_PATH + dir);

		if (!file.exists())
		{
			file.mkdir();
		}

		vectors.clear();
		for (int i = 0; i < segements.size(); i++)
		{

			drawStrokeSegement(i, dir, segements, libStrokes, queryStroke);
		}

		saveTxt(segements.size() + "\r\n", SampleConfig.OUTPUT_PATH + dir + "num.txt");
		// LibParserUtil.saveResultImage(queryStroke,SampleConfig.OUTPUT_PATH +
		// dir + "result.jpg");
		
		saveResultImage(SampleConfig.OUTPUT_PATH + "After\\result.jpg");
	}

	public static void drawStrokeSegement(int i, String dir, Vector<Segement> segements, Vector<LibStroke> libStrokes, QueryStroke queryStroke)
	{
		int index = segements.get(i).cs.get(0).a;
		int width = libStrokes.get(index).width;
		int height = libStrokes.get(index).height;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();

		vectors.add(new Vector<CoordDiff>());

		int startIndex = segements.get(i).startIndexOfQuery;
		String context = "";
		for (int j = 0; j < segements.get(i).cs.size(); j++)
		{
			int a = segements.get(i).cs.get(j).a;
			int b = segements.get(i).cs.get(j).b;

			context += getLineContent(libStrokes, queryStroke, a, b, startIndex + j);

			drawPoints(graphics2d, libStrokes, a, b);

		}

		LibParserUtil.saveImage(image, SampleConfig.OUTPUT_PATH + dir + i + "_" + index + "sample.jpg");

		LibParserUtil.saveTxt(context, SampleConfig.OUTPUT_PATH + dir + i + ".txt");

		//
	}

	private static void drawPoints(Graphics2D graphics2d, Vector<LibStroke> libStrokes, int a, int b)
	{

		libStrokes.get(a).points.get(b).drawPoint(graphics2d, Color.RED);
		libStrokes.get(a).rightContourPoints.get(b).drawPoint(graphics2d, Color.GREEN);
		libStrokes.get(a).leftContourPoints.get(b).drawPoint(graphics2d, Color.BLUE);
	}

	private static String getLineContent(Vector<LibStroke> libStrokes, QueryStroke queryStroke, int a, int b, int queryIndex)
	{
		String content = "";
		content += (int) libStrokes.get(a).points.get(b).x + " " + (int) libStrokes.get(a).points.get(b).y + " " + (int) queryStroke.points.get(queryIndex).x
				+ " " + (int) queryStroke.points.get(queryIndex).y + "\r\n";

		content += (int) libStrokes.get(a).leftContourPoints.get(b).x + " " + (int) libStrokes.get(a).leftContourPoints.get(b).y + " "
				+ (int) queryStroke.leftContourPoints.get(queryIndex).x + " " + (int) queryStroke.leftContourPoints.get(queryIndex).y + "\r\n";

		content += (int) libStrokes.get(a).rightContourPoints.get(b).x + " " + (int) libStrokes.get(a).rightContourPoints.get(b).y + " "
				+ (int) queryStroke.rightContourPoints.get(queryIndex).x + " " + (int) queryStroke.rightContourPoints.get(queryIndex).y + "\r\n";

		
		vectors.lastElement().add(new CoordDiff(libStrokes.get(a).leftContourPoints.get(b),queryStroke.leftContourPoints.get(queryIndex)));
		vectors.lastElement().add(new CoordDiff(libStrokes.get(a).rightContourPoints.get(b),queryStroke.rightContourPoints.get(queryIndex)));
		return content;

	}

}
