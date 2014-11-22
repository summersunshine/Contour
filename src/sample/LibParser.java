package sample;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.lang.model.element.Element;

import Geometry.Geometry;
import Geometry.Point;
import config.Penalty;
import config.SampleConfig;

public class LibParser
{
	// 库中笔触的数目
	public static final int STOKE_NUM = 14;

	// 库中笔触数列
	public Vector<LibStroke> libStrokes;

	public QueryStroke queryStroke;

	public Vector<StrokeInfo> strokeInfos;

	public boolean isRollBack;
	
	public Cost lastCost;
	
	public int currIndex;
	
	public int count;

	public LibParser()
	{
		this.isRollBack = false;
		
		this.initStrokeInfos();
		initLibStrokes();
	}

	public void initStrokeInfos()
	{
		this.count = 0;
		this.strokeInfos = new Vector<StrokeInfo>();
		this.strokeInfos.add(new StrokeInfo(0));
	}

	/**
	 * 载入笔触库中笔触
	 * */
	public void initLibStrokes()
	{
		System.out.println("LibParser.initLibStrokes() begin");
		libStrokes = new Vector<LibStroke>();
		for (int i = 0; i < STOKE_NUM; i++)
		{
			libStrokes.add(new LibStroke("charcoal1", i));
			System.out.println("parse stroke " + i);
		}

		Feature.isLoadBegin = false;
		System.out.println("LibParser.initLibStrokes() end");
	}

	/**
	 * Waring!!!!!
	 * 
	 * 效率应该非常低下，但是好像暂时没有其他的办法！
	 * 
	 * 循环了三层，太可耻了！
	 * */
	public void compareWithQueryStroke(QueryStroke queryStroke)
	{
		this.queryStroke = queryStroke;
		this.initStrokeInfos();

		System.out.println("feature cal begin");

		for (int i = 0; i < queryStroke.querySamples.size(); i++)
		{
			for (int j = 0; j < libStrokes.size(); j++)
			{
				queryStroke.querySamples.get(i).addDistanceData(libStrokes.get(j).libSamples);

			}

			queryStroke.querySamples.get(i).sort();
			queryStroke.querySamples.get(i).printKNN();
		}

		System.out.println("feature cal end");

		addTransitionData();

		drawStrokeSegements();
	}

	/**
	 * 加上Transition 数据
	 * 
	 * @param otherSamples
	 * */
	public void addTransitionData()
	{
		System.out.println("transition cal begin");
		
		for (currIndex = 1; currIndex < queryStroke.querySamples.size(); currIndex++)
		{
			lastCost = queryStroke.querySamples.get(currIndex - 1).costs.get(0);

			for (int j = 0; j < queryStroke.querySamples.get(currIndex).costs.size(); j++)
			{
				int a = queryStroke.querySamples.get(currIndex).costs.get(j).a;
				int b = queryStroke.querySamples.get(currIndex).costs.get(j).b;
				int Et = getTransitionPenalty(j,a,b);
				int Ee = getEndPenalty(j, a, b);
				queryStroke.querySamples.get(currIndex).costs.get(j).addEt(Et);
				//queryStroke.querySamples.get(currIndex).costs.get(j).addEe(Ee);
			}

			queryStroke.querySamples.get(currIndex).sort();
			queryStroke.querySamples.get(currIndex).printKNN();
			addStrokeInfo(lastCost.a, lastCost.b);

		}

		System.out.println("transition cal end");
	}
	
	public int getTransitionPenalty(int j,int a,int b)
	{

		int penalty = 0;
		if (a == lastCost.a)
		{
			if (b == lastCost.b + 1)
			{
				penalty = 0;
			}
			else if (b == lastCost.b)
			{
				penalty = Penalty.Cr*(getRepeatTime(b, currIndex));
			}
			else if (b == lastCost.b + 2)
			{
				penalty = Penalty.Cs;
			}
			else
			{
				penalty = getJumpPenaltyForSameStroke(a, b, lastCost.a, lastCost.b);
			}
		}
		else
		{
			penalty = getJumpPenalty(a, b, lastCost.a, lastCost.b);
		}
		
		return penalty;
	}
	
	public int getEndPenalty(int j,int a,int b)
	{
		float percent1 = currIndex/(queryStroke.getSampleNumber()-currIndex);
		float percent2;
		
		if (percent1 > 1)
		{
			percent1 =currIndex/queryStroke.getSampleNumber();
			
			percent2 = b/libStrokes.get(a).getSampleNumber();
			
		}
		else
		{
			 percent2 = b/(libStrokes.get(a).getSampleNumber()-b);
		}
		float diff = Math.abs(percent1 - percent2);
		
		return (int) (Penalty.Ce*diff*diff);
	}
	
	public int getRepeatTime(int b,int index)
	{
		int count = 1;
		for (int i = index-1; i > 0; i--)
		{
			if (queryStroke.querySamples.get(i).costs.get(0).b == b)
			{
				count++;
			}
			else
			{
				break;
			}
		}
		return count;
	}

	public int getJumpPenalty(int a, int b, int lastA, int lastB)
	{
		Point v1 = libStrokes.get(lastA).libSamples.get(lastB).velocity;
		Point v2 = libStrokes.get(a).libSamples.get(b).velocity;

		float ratio = count < Penalty.Lmin ? 1 : Penalty.Lmin / 4.0f / count + 0.75f;

		return (int) (Penalty.Ct * ratio + Penalty.Cp * (1 - Geometry.getCos(v1, v2) ));

	}

	public int getJumpPenaltyForSameStroke(int a, int b, int lastA, int lastB)
	{
		Point v1 = libStrokes.get(lastA).libSamples.get(lastB).velocity;
		Point v2 = libStrokes.get(a).libSamples.get(b).velocity;

		return (int) (Penalty.Ct + Penalty.Cp * (1 - Geometry.getCos(v1, v2)));

	}

	public void addStrokeInfo(int lastA, int lastB)
	{
		int a = queryStroke.querySamples.get(currIndex).costs.get(0).a;
		int b = queryStroke.querySamples.get(currIndex).costs.get(0).b;
		
		
		if (a != lastA )
		{
			createNewStrokeInfo(a,b);
			
			//createNewStrokeInfo(a,b);
//			if (count >= Penalty.Lmin)
//			{
//				
//			}
//			else
//			{
//				//回滚
//				rollbackLastStrokeInfo();
//			}
		}
		else
		{
			strokeInfos.lastElement().add(new C(a, b));
			count++;
			//addNewPointToStroke(a,b);
		}
		
	}
	
	//创建一段新的stroke
	private void createNewStrokeInfo(int a,int b)
	{
		count = 0;
		strokeInfos.lastElement().endStroke(currIndex);

		
		StrokeInfo strokeInfo = new StrokeInfo(currIndex);
		strokeInfo.add(new C(a, b));
		strokeInfos.add(strokeInfo);
	}
	
	
	//创建一段新的stroke
	private void addNewPointToStroke(int a,int b)
	{
		
		if (strokeInfos.isEmpty())
		{
			initStrokeInfos();
		}

		strokeInfos.lastElement().add(new C(a, b));
		count++;
	}

	//回滚到上以stroke
	private void rollbackLastStrokeInfo()
	{
		currIndex = strokeInfos.lastElement().startIndex;
		
		for (int j = 0; j < queryStroke.querySamples.get(currIndex).costs.size(); j++)
		{
			//保障连续性
			if (queryStroke.querySamples.get(currIndex).costs.get(j).a == strokeInfos.lastElement().cs.lastElement().a)
			{
				//int p = (int) Math.pow(Penalty.Cl + Penalty.Lmin - strokeInfos.lastElement().getL(), 2);
				int p = (int) Penalty.Cl + Penalty.Lmin - strokeInfos.lastElement().getL();
				queryStroke.querySamples.get(currIndex).costs.get(j).addEs(-Penalty.Cl);
			}
			
		}
		queryStroke.querySamples.get(currIndex).sort();
		
		strokeInfos.remove(strokeInfos.lastElement());
		
		if (strokeInfos.isEmpty())
		{
			initStrokeInfos();
		}
		else
		{
			count = strokeInfos.lastElement().getL();
		}
	}
	
	public void drawStrokeSegements()
	{
		File file = new File(SampleConfig.OUTPUT_PATH + "Result\\");

		if (file.exists())
		{
			file.delete();
		}
		for (int i = 0; i < strokeInfos.size(); i++)
		{
			drawStrokeSegement(i);
		}
	}

	public void drawStrokeSegement(int i)
	{
		int index = strokeInfos.get(i).cs.get(0).a;
		int width = libStrokes.get(index).width;
		int height = libStrokes.get(index).height;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for (int j = 0; j < strokeInfos.get(i).cs.size(); j++)
		{
			int a = strokeInfos.get(i).cs.get(j).a;
			int b = strokeInfos.get(i).cs.get(j).b;

			libStrokes.get(a).points.get(b).drawPoint(graphics2d, Color.RED);
			libStrokes.get(a).rightCountourPoints.get(b).drawPoint(graphics2d, Color.GREEN);
			libStrokes.get(a).leftCountourPoints.get(b).drawPoint(graphics2d, Color.BLUE);

		}

		saveImage(image, SampleConfig.OUTPUT_PATH + "Result\\" + i + "_" + index + ".jpg");

	}
	
	private void saveImage(BufferedImage image,String path)
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

}
