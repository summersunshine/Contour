package config;

import java.text.Format;

public class SampleConfig
{
	public static final String DATABASE_PATH = "C:\\Users\\XY\\Desktop\\Dataset\\";
	
	
	public static final String SHAPE_PATH = "shape\\";
	
	public static final String CHARCOAR_PATH = "charcoal1\\";
		
	public static String getPath(String name)
	{
		return DATABASE_PATH + SHAPE_PATH + name + "\\" + name + ".path";
	}
	
	public static String getEnds(String name)
	{
		return DATABASE_PATH + SHAPE_PATH + name + "\\" + name + ".ends";
	}
	
	public static String getStrokeTxtPath(String name,int index)
	{
		String last = String.format("_stroke%02d.txt", index);
		return DATABASE_PATH + SHAPE_PATH + name + "\\" + name + last;
	}
	
	public static String getStrokeImagePath(String name,int index)
	{
		String last = String.format("_stroke%02d.png", index);
		return DATABASE_PATH + SHAPE_PATH + name + "\\" + name + last;
	}
}
