package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import config.SampleConfig;
import sequence.Segement;
import stroke.LibStroke;
import stroke.QueryStroke;

public class LibParserUtil
{
	public  static void saveResultImage(QueryStroke queryStroke,String path)
	{
		//BufferedImage image = MergeImage.getImage(partImages);
		BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for (int i = 0; i < queryStroke.points.size(); i++)
		{
			queryStroke.points.get(i).drawPoint(graphics2d, Color.RED);
			queryStroke.rightContourPoints.get(i).drawPoint(graphics2d, Color.GREEN);
			queryStroke.leftContourPoints.get(i).drawPoint(graphics2d, Color.BLUE);
		}
		
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
	
	
	public static void saveSampleImage(BufferedImage image, String path)
	{
		File file = new File(path);

		if (!file.exists())
		{
			file.mkdirs();
		}

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

	public  static void saveImage(LibStroke libStroke,Segement segement, String path)
	{
		
		int start = segement.cs.firstElement().b;
		int end = segement.cs.lastElement().b;
		
		BufferedImage image = PixelGrabberUtil.getIamge(libStroke,start,end);
		
		
		File file = new File(path);

		if (!file.exists())
		{
			file.mkdirs();
		}

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
	
	
	
	public static void drawStrokeSegements(String dir,Vector<Segement> segements,Vector<LibStroke> libStrokes,QueryStroke queryStroke)
	{
		File file = new File(SampleConfig.OUTPUT_PATH + dir);

		
		if (file.exists())
		{
			file.delete();
		}
		for (int i = 0; i < segements.size(); i++)
		{
			
			drawStrokeSegement(i, dir,segements,libStrokes,queryStroke);
		}
		
		
		LibParserUtil.saveResultImage(queryStroke,SampleConfig.OUTPUT_PATH + dir + "result.jpg");
	}

	
	public static void drawStrokeSegement(int i, String dir,Vector<Segement> segements,Vector<LibStroke> libStrokes,QueryStroke queryStroke)
	{
		int index = segements.get(i).cs.get(0).a;
		int width = libStrokes.get(index).width;
		int height = libStrokes.get(index).height;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();

		
		int startIndex = segements.get(i).startIndexOfQuery;
		String context="";
		for (int j = 0; j < segements.get(i).cs.size(); j++)
		{
			int a = segements.get(i).cs.get(j).a;
			int b = segements.get(i).cs.get(j).b;

			context += (int)libStrokes.get(a).points.get(b).x + " " + (int)libStrokes.get(a).points.get(b).y + " " + 
					(int)queryStroke.points.get(startIndex+j).x + " " + (int)queryStroke.points.get(startIndex+j).y + "\r\n";
			
			context += (int)libStrokes.get(a).leftContourPoints.get(b).x + " " + (int)libStrokes.get(a).leftContourPoints.get(b).y + " " + 
					(int)queryStroke.leftContourPoints.get(startIndex+j).x + " " + (int)queryStroke.leftContourPoints.get(startIndex+j).y + "\r\n";
			
			context += (int)libStrokes.get(a).rightContourPoints.get(b).x + " " + (int)libStrokes.get(a).rightContourPoints.get(b).y + " " + 
					(int)queryStroke.rightContourPoints.get(startIndex+j).x + " " + (int)queryStroke.rightContourPoints.get(startIndex+j).y + "\r\n";
	
			libStrokes.get(a).points.get(b).drawPoint(graphics2d, Color.RED);
			libStrokes.get(a).rightContourPoints.get(b).drawPoint(graphics2d, Color.GREEN);
			libStrokes.get(a).leftContourPoints.get(b).drawPoint(graphics2d, Color.BLUE);

		}


		
		LibParserUtil.saveSampleImage(image, SampleConfig.OUTPUT_PATH + dir + i + "_" + index + "sample.jpg");
		LibParserUtil.saveTxt(context, SampleConfig.OUTPUT_PATH + dir + i + ".txt");
		
		if (dir == "After\\")
		{
			String path =SampleConfig.OUTPUT_PATH + dir + i + "_" + index + ".jpg";
			LibParserUtil.saveImage(libStrokes.get(index), segements.get(i), path);
		}
		
	}

	
}
