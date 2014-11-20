package sample;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import config.SampleConfig;
import Geometry.ImgUtil;
import Geometry.Point;
import Geometry.Position;

public class LibStroke extends Stroke
{
	//数据中每一行的数据量
	public static final int digitNums = 12;
	
//	public static final int width = 1200;
//	public static final int height = 200;
	
	
	private int num;
	private String name;
	private int width;
	private int height;
	
	//库中的第index个笔触
	private int index;
	
	
	private String txtPathString;
	private String imgPathString;
	
	
	public Vector<LibSample> libSamples;
	
	public LibStroke(String name,int index)
	{
		super();
		this.num= 0;
		this.name = name;
		this.index = index;
		this.txtPathString = SampleConfig.getStrokeTxtPath(name, index);
		this.imgPathString = SampleConfig.getStrokeImagePath(name, index);
		this.readImage();
		this.readFile();
		
		this.initLibSample();
	}
	
	private void initLibSample()
	{
		this.libSamples  = new Vector<LibSample>();
		
		for (int i = 0; i < points.size(); i++)
		{
			libSamples.add(new LibSample(points, index, i));
		}
	}
	
	public void readImage()
	{
		BufferedImage image =  ImgUtil.getImg(imgPathString);
		width = image.getWidth();
		height = image.getHeight();
		
//		width = 600;
//		height = 400;
	}
	
	public void readFile()
	{
		File file = new File(txtPathString);
		if (!file.exists())
		{
			return;
		}
		
		FileReader fileReader = null;
		BufferedReader bufferedReader;
		
		try
		{
			fileReader = new FileReader(txtPathString);
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
				addPosition(line);
				line = bufferedReader.readLine();
			}
			
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 依据每一行的数据添加位置
	 * 
	 * @param line
	 * */
	private void addPosition(String line)
	{
		String [] numsStrings = line.split(" ");
	
		setPoints(getFloats(numsStrings));
	}
	
	
	private float[] getFloats(String [] strings)
	{
		float[] floats = new float[digitNums];
		for (int i = 0,count = 0; i < strings.length; i++)
		{
			if (!strings[i].equals(""))
			{
				floats[count++] = Float.parseFloat(strings[i]);
			}
		}
		return floats;
	}
	
	private void setPoints(float[] floats)
	{

		points.add(new Point(floats[0]*width,floats[1]*height));
		rightCountourPoints.add(new Point(floats[2]*width,floats[3]*height));
		leftCountourPoints.add(new Point(floats[4]*width,floats[5]*height));

	}
	
//	
//	public void drawStroke(Graphics2D graphics2d)
//	{
//		System.out.println("Stroke.drawStroke()");
//
//		for (int i = 0; i < relativePositions.size(); i++)
//		{
//			relativePositions.get(i).drawPosition(graphics2d);
//		}
//	}
}
