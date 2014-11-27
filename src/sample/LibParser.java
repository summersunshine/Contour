package sample;

import feature.Cost;
import feature.Feature;
import geometry.Geometry;
import geometry.MergeImage;
import geometry.PixelGrabber;
import geometry.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.lang.model.element.Element;

import sequence.C;
import sequence.Segement;
import stroke.LibStroke;
import stroke.QueryStroke;
import config.Penalty;
import config.SampleConfig;

public class LibParser
{
	// 库中笔触的数目
	public static final int STOKE_NUM = 14;

	// 库中笔触数列
	public static Vector<LibStroke> libStrokes;

	public static QueryStroke queryStroke;

	public Vector<Segement> segements;

	public boolean isRollBack;

	public Cost lastCost;

	public int currIndex;

	public int count;
	
	public Vector<BufferedImage> partImages;

	public LibParser()
	{
		this.isRollBack = false;
		this.partImages = new Vector<BufferedImage>();
		this.initStrokeInfos();
		this.initLibStrokes();
	}

	public void initStrokeInfos()
	{
		this.count = 0;
		this.segements = new Vector<Segement>();
		this.segements.add(new Segement(0));
	}

	/**
	 * 载入笔触库中笔触
	 * */
	public void initLibStrokes()
	{
		System.out.println("LibParser.initLibStrokes() begin");
		LibParser.libStrokes = new Vector<LibStroke>();
		for (int i = 0; i < STOKE_NUM; i++)
		{
			LibParser.libStrokes.add(new LibStroke("charcoal1", i));
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
		LibParser.queryStroke = queryStroke;

		this.initStrokeInfos();

		this.addFeatureData();

		//this.addBeginData();

		this.addTransitionData();

		this.drawStrokeSegements("Before\\");

		this.optimization();

		this.drawStrokeSegements("After\\");
		
	
		
	}

	/**
	 * 加上feature数据
	 * */
	public void addFeatureData()
	{
		System.out.println("feature cal begin");

		for (int j = 0; j < libStrokes.size(); j++)
		{
			queryStroke.querySamples.get(0).addDistanceData(libStrokes.get(j).libSamples);

		}
		
		for (int i = 1; i < queryStroke.querySamples.size(); i++)
		{
			for (int j = 0; j < libStrokes.size(); j++)
			{
				queryStroke.querySamples.get(i).addDistanceData(libStrokes.get(j).libSamples);

			}

			queryStroke.querySamples.get(i).sort();
			queryStroke.querySamples.get(i).printKNN();
			//addSegements(queryStroke.querySamples.get(i-1).a, queryStroke.querySamples.get(i-1).b);
		}
		
		System.out.println("feature cal end");
	}

	/**
	 * 处理起始的端点问题 数据
	 * 
	 * @param otherSamples
	 * */
	public void addBeginData()
	{
		System.out.println("begin cal begin");

		for (int j = 0; j < queryStroke.querySamples.get(0).costs.size(); j++)
		{
			if (queryStroke.querySamples.get(0).costs.get(j).b != 0)
			{
				queryStroke.querySamples.get(0).costs.get(j).addEe(10);
			}
			addSegements(lastCost.a, lastCost.b);
		}

		queryStroke.querySamples.get(0).sort();
		queryStroke.querySamples.get(0).printKNN();

		System.out.println("begin cal end");
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
				float Et = getTransitionPenalty(j, a, b);
				// int Ee = getEndPenalty(j, a, b);
				queryStroke.querySamples.get(currIndex).costs.get(j).addEt(Et);
				// queryStroke.querySamples.get(currIndex).costs.get(j).addEe(Ee);
			}

			queryStroke.querySamples.get(currIndex).sort();
			queryStroke.querySamples.get(currIndex).printKNN();
			
			addSegements(lastCost.a, lastCost.b);
		}

		System.out.println("transition cal end");
	}

	public float getTransitionPenalty(int j, int a, int b)
	{

		float penalty = 0;
		if (a == lastCost.a)
		{
			if (b == lastCost.b + 1)
			{
				penalty = 0;
			}
			else if (b == lastCost.b)
			{
				penalty = Penalty.Cr * (getRepeatTime(b, currIndex));
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

	public int getEndPenalty(int j, int a, int b)
	{
		float percent1 = currIndex / queryStroke.getSampleNumber();
		float percent2 = b / libStrokes.get(a).getSampleNumber();
		float diff = Math.abs(percent1 - percent2);

		if (percent1 < 0.25 || percent1 > 0.75)
		{
			return (int) (Penalty.Ce * diff);
		}
		else
		{
			return (int) (2 * Penalty.Ce * diff * diff);
		}

	}

	
	public int getRepeatTime(int b, int index)
	{
		int count = 1;
		for (int i = index - 1; i > 0; i--)
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

	public float getJumpPenalty(int a, int b, int lastA, int lastB)
	{
		Point v1 = libStrokes.get(lastA).libSamples.get(lastB).velocity;
		Point v2 = libStrokes.get(a).libSamples.get(b).velocity;

		float ratio = count < Penalty.Lmin ? 1 : Penalty.Lmin / 1.33f / count + 0.25f;

		return (float) ((Penalty.Ct  + Penalty.Cp * (1 - Geometry.getCos(v1, v2)))*ratio);

	}

	public int getJumpPenaltyForSameStroke(int a, int b, int lastA, int lastB)
	{
		Point v1 = libStrokes.get(lastA).libSamples.get(lastB).velocity;
		Point v2 = libStrokes.get(a).libSamples.get(b).velocity;

		return (int) (Penalty.Ct + Penalty.Cp * (1 - Geometry.getCos(v1, v2)));

	}

	public void addSegements(int lastA, int lastB)
	{
		int a = queryStroke.querySamples.get(currIndex).costs.get(0).a;
		int b = queryStroke.querySamples.get(currIndex).costs.get(0).b;

		if (a != lastA)
		{
			createNewSegements(a, b);
		}
		else
		{
			addSegementPoints(a, b);
		}

	}

	// 创建一段新的stroke
	private void createNewSegements(int a, int b)
	{
		count = 0;

		Segement segement = new Segement(currIndex);
		segement.add(new C(a, b));
		segements.add(segement);
	}

	// 在stroke后加点
	private void addSegementPoints(int a, int b)
	{
		count++;

		// 每次都要更新一下最后一个点的信息
		segements.lastElement().add(new C(a, b));
		segements.lastElement().lastIndexOfLib = libStrokes.get(a).points.size() - 1;
		segements.lastElement().endStroke(currIndex);
	}


	// 优化序列
	public void optimization()
	{
		handStortSegement();
		handEndPoint();
		
	}

	public void handEndPoint()
	{

		for (int i = 0; i < segements.size(); i++)
		{
			if (i!= segements.size() - 1)
			{
				if (segements.get(i).isReachEnd())
				{
					segements.get(i).removeBack(Penalty.EndArea);
					segements.get(i + 1).addFront(4);
				}
//				else
//				{
//					segements.get(i).addBack(4);
//				}
				//segements.get(i + 1).addFront(4);
				//continue;
			}
			
			

			if (!segements.get(i).isReachEnd() && i == (segements.size() - 1))
			{
				//segements.get(i).addToEnd();
			}
			
			if (segements.get(i).startIndexOfQuery>=segements.get(i).endIndexOfQuery)
			{
				segements.remove(i);
			}

		}
	}

	public void handStortSegement()
	{
		for (int i = 1; i < segements.size(); i++)
		{
			if (segements.get(i).isShort())
			{
				// 不是最后一个
				if (i == segements.size() - 1)
				{
					segements.get(i).addFront(Penalty.TranArea);

				}
				// 如果是最后一个，就不删除他了
				else
				{
					segements.get(i - 1).addBack(segements.get(i).getL());
					segements.remove(i);
				}

			}

		}

	}

	public void handStortSegement(int beginIndex, int endIndex)
	{
		int counts = 0;
		for (int j = beginIndex; j < endIndex; j++)
		{
			counts += segements.get(j).getL();
		}

		if (beginIndex - 1 >= 0)
		{
			segements.get(beginIndex - 1).addBack(counts);
		}

		if (endIndex < segements.size())
		{
			segements.get(endIndex).addFront(counts);
		}

		for (int j = beginIndex; j < endIndex; j++)
		{
			segements.remove(beginIndex);
		}
	}

	
	public void drawStrokeSegements(String dir)
	{
		File file = new File(SampleConfig.OUTPUT_PATH + dir);

		partImages.clear();
		
		if (file.exists())
		{
			file.delete();
		}
		for (int i = 0; i < segements.size(); i++)
		{
			
			drawStrokeSegement(i, dir);
		}
		
		
		saveResultImage(SampleConfig.OUTPUT_PATH + dir + "result.jpg");
	}

	public void drawStrokeSegement(int i, String dir)
	{
		int index = segements.get(i).cs.get(0).a;
		int width = libStrokes.get(index).width;
		int height = libStrokes.get(index).height;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();

		String context = segements.get(i).startIndexOfQuery +" " + segements.get(i).endIndexOfQuery + "\r\n";
		for (int j = 0; j < segements.get(i).cs.size(); j++)
		{
			int a = segements.get(i).cs.get(j).a;
			int b = segements.get(i).cs.get(j).b;
			context += "a: " + a + " b:" + b + "\r\n";
			libStrokes.get(a).points.get(b).drawPoint(graphics2d, Color.RED);
			libStrokes.get(a).rightContourPoints.get(b).drawPoint(graphics2d, Color.GREEN);
			libStrokes.get(a).leftContourPoints.get(b).drawPoint(graphics2d, Color.BLUE);

		}


		
		saveSampleImage(image, SampleConfig.OUTPUT_PATH + dir + i + "_" + index + "sample.jpg");
		saveTxt(context, SampleConfig.OUTPUT_PATH + dir + i + "_" + index + ".txt");
		
		if (dir == "After\\")
		{
			saveImage(index, i, SampleConfig.OUTPUT_PATH + dir + i + "_" + index + ".jpg");
		}
		
	}

	private void saveTxt(String content, String path)
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

	private void saveImage(int index,int i, String path)
	{
		LibStroke libStroke = libStrokes.get(index);
		
		int start = segements.get(i).cs.firstElement().b;
		int end = segements.get(i).cs.lastElement().b;
		
		BufferedImage image = PixelGrabber.getIamge(libStroke,start,end);
		
		image = segements.get(i).rotate(image);
		
		partImages.add(image);
		
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
	
	private void saveSampleImage(BufferedImage image, String path)
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

	
	private void saveResultImage(String path)
	{
		BufferedImage image = MergeImage.getImage(partImages);
		
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
	

	
}
