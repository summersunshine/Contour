package config;


public class SampleConfig
{
	
	
	
	public static final int K = 50;
	
	
	public static final String OUTPUT_PATH = "C:\\Users\\XY\\Desktop\\Output\\";
	
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
	
	public static String getStrokeTightMaskPath(String name,int index)
	{
		String last = String.format("_stroke%02d_tightmask.png", index);
		return DATABASE_PATH + SHAPE_PATH + name + "\\" + name + last;
	}
	
	public static String getStrokeAlphaMaskPath(String name,int index)
	{
		String last = String.format("_stroke%02d_alphamask.png", index);
		return DATABASE_PATH + SHAPE_PATH + name + "\\" + name + last;
	}
	
}
