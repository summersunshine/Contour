package sample;

import java.util.Vector;

import config.SampleConfig;

public class LibParser
{
	public static final int STOKE_NUM = 14;
	
	public Vector<LibStroke> libStrokes;
	
	
	public LibParser()
	{
		initLibStrokes();
	}
	
	public void initLibStrokes()
	{
		System.out.println("LibParser.initLibStrokes() begin");
		libStrokes = new Vector<LibStroke>();
		for (int i = 0; i < STOKE_NUM; i++)
		{
			libStrokes.add(new LibStroke("charcoal1",i));
			System.out.println("parse stroke " + i);
		}
		
		SampleConfig.isLoadBegin = false;
		System.out.println("LibParser.initLibStrokes() end");
	}
	
	
	/**
	 * Waring!!!!!
	 * 
	 * Ч��Ӧ�÷ǳ����£����Ǻ�����ʱû�������İ취��
	 * 
	 * ѭ�������㣬̫�ɳ��ˣ�
	 * */
	public void compareWithQueryStroke(QueryStroke queryStroke)
	{
		System.out.println("cal begin");
		
		for (int i = 0; i < queryStroke.querySamples.size(); i++)
		{
			for (int j = 0; j < libStrokes.size(); j++)
			{
				queryStroke.querySamples.get(i).getKNN(libStrokes.get(j).libSamples, SampleConfig.K);
						
			}
			
			queryStroke.querySamples.get(i).sort();
			queryStroke.querySamples.get(i).printKNN();
		}
		
		System.out.println("cal end");
		
		
	}
	
}
