package libary;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import Geometry.Point;

public class Stroke
{
	public static final int digitNums = 12;
	
	private String filePath;
	
	private int num;
	
	private Vector<Position> positions;
	
	private Vector<Position> relativePositions;
	
	
	public Stroke(String filePath)
	{
		this.num= 0;
		this.filePath = filePath;
		this.positions = new Vector<Position>();
		this.relativePositions= new Vector<Position>();
		
		this.readFile();
	}
	
	public void readFile()
	{
		File file = new File(filePath);
		if (!file.exists())
		{
			return;
		}
		
		FileReader fileReader = null;
		BufferedReader bufferedReader;
		
		try
		{
			fileReader = new FileReader(filePath);
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bufferedReader = new BufferedReader(fileReader);
		
		try
		{
			String line = bufferedReader.readLine();
			num = Integer.parseInt(line);
			
			line = bufferedReader.readLine();
			while (line!=null)
			{
				String [] numsStrings = line.split(" ");
				float[] floats = getFloats(numsStrings);
				Point[] points = getPoints(floats);
				positions.add(new Position(points[0], points[1], points[2]));
				relativePositions.add(new Position(points[3], points[4], points[5]));
				
				line = bufferedReader.readLine();
			}
			
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	private float[] getFloats(String [] strings)
	{
		float[] floats = new float[digitNums];
		for (int i = 0,count = 0; i < strings.length; i++)
		{
			if (!strings[i].equals(""))
			{
				///System.out.println(strings[i]);
				floats[count++] = Float.parseFloat(strings[i]);
			}
		}
		return floats;
	}
	
	private Point[] getPoints(float[] floats)
	{
		Point[] points = new Point[digitNums];
		for (int i = 0; i < digitNums; i+=2)
		{
			points[i/2] = new Point(floats[i]*600,floats[i+1]*100);
			
		}
		return points;
	}
	
	
	public void drawStroke(Graphics2D graphics2d)
	{
		System.out.println("Stroke.drawStroke()");

		for (int i = 0; i < relativePositions.size(); i++)
		{
			relativePositions.get(i).drawPosition(graphics2d);
		}
	}
}
