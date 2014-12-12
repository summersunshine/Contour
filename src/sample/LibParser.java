package sample;

import java.awt.image.BufferedImage;
import java.util.Vector;

import sequence.SegementInfo;
import stroke.LibStroke;
import stroke.QueryStroke;
import config.Global;
import config.Penalty;
import feature.Cost;
import feature.Feature;
import geometry.Geometry;
import geometry.Point;

public class LibParser
{
	// 库中笔触的数目
	public static final int				STOKE_NUM	= 14;

	// 库中笔触数列
	public static Vector<LibStroke>		libStrokes;

	public static QueryStroke			queryStroke;

	public static Vector<SegementInfo>	segementInfos;

	public static BufferedImage			resultImage;

	private SegementInfo				currSegementInfo;


	public Cost							lastCost;

	public int							currIndex;


	public LibParser()
	{
		this.initStrokeInfos();
		LibParser.loadLibary();
	}

	public void initStrokeInfos()
	{
		this.currSegementInfo = null;
		segementInfos = new Vector<SegementInfo>();
	}

	/**
	 * 载入笔触库中笔触
	 * */
	public static void loadLibary()
	{
		// System.out.println("LibParser.initLibStrokes() begin");
		LibParser.libStrokes = new Vector<LibStroke>();
		int sum = 0;
		for (int i = 0; i < STOKE_NUM; i++)
		{
			LibStroke libStroke = new LibStroke(Global.Libary, i);
			sum += libStroke.sampleDist;
			LibParser.libStrokes.add(libStroke);
			System.out.println("parse stroke " + i);
		}

		Global.SAMPLE_DIST = (int) (sum / STOKE_NUM / 1.5);
		Feature.isLoadBegin = false;
		System.out.println(Global.SAMPLE_DIST);
		// System.out.println("LibParser.initLibStrokes() end");
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

		this.addTransitionData();

		this.optimization();

		LibParserUtil.drawStrokeSegements("After\\");

	}

	/**
	 * 加上feature数据
	 * */
	public void addFeatureData()
	{
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
	}

	/**
	 * 加上Transition 数据
	 * 
	 * @param otherSamples
	 * */
	public void addTransitionData()
	{
		System.out.println("transition cal begin");

		lastCost = getQuerySampleCost(0, 0);

		createNewSegements(lastCost.a, lastCost.b, 0);

		for (currIndex = 1; currIndex < queryStroke.querySamples.size(); currIndex++)
		{
			for (int j = 0; j < queryStroke.querySamples.get(currIndex).costs.size(); j++)
			{
				Cost cost = getQuerySampleCost(currIndex, j);
				float Et = getTransitionPenalty(cost.a, cost.b);
				queryStroke.querySamples.get(currIndex).costs.get(j).addEt(Et);
			}

			queryStroke.querySamples.get(currIndex).sort();
			queryStroke.querySamples.get(currIndex).printKNN();

			addSegements(lastCost.a, lastCost.b);

			lastCost = getQuerySampleCost(currIndex, 0);
		}
		createNewSegements(lastCost.a, lastCost.b, currIndex);
		System.out.println("transition cal end");
	}

	public float getTransitionPenalty(int a, int b)
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

	private Cost getQuerySampleCost(int index, int rank)
	{
		return queryStroke.querySamples.get(index).costs.get(rank);
	}

	/********************************* 处理过渡惩罚 *************************************************/

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
		Point v1 = getLibSampleVelocity(lastA, lastB);
		Point v2 = getLibSampleVelocity(a, b);

		float ratio = currSegementInfo.getSize() < Penalty.Lmin ? 1 : Penalty.Lmin / 1.33f / currSegementInfo.getSize() + 0.25f;

		return (float) ((Penalty.Ct + Penalty.Cp * (1 - Geometry.getCos(v1, v2))) * ratio);

	}

	public int getJumpPenaltyForSameStroke(int a, int b, int lastA, int lastB)
	{
		Point v1 = getLibSampleVelocity(lastA, lastB);
		Point v2 = getLibSampleVelocity(a, b);

		return (int) (Penalty.Ct + Penalty.Cp * (1 - Geometry.getCos(v1, v2)));

	}

	private Point getLibSampleVelocity(int a, int b)
	{
		return libStrokes.get(a).libSamples.get(b).velocity;
	}

	/********************************* 结束过渡惩罚 *************************************************/

	/********************************* 处理segement *************************************************/

	public void addSegements(int lastA, int lastB)
	{
		int a = queryStroke.querySamples.get(currIndex).costs.get(0).a;
		int b = queryStroke.querySamples.get(currIndex).costs.get(0).b;

		if (a != lastA)
		{
			createNewSegements(a, b, currIndex);
		}
		else
		{
			addSegementPoints(a, b, currIndex);
		}

	}

	// 创建一段新的stroke
	private void createNewSegements(int a, int b, int queryIndex)
	{

		if (currSegementInfo != null)
		{

			segementInfos.add(currSegementInfo);
		}

		currSegementInfo = new SegementInfo(a, b, queryIndex);

	}

	// 在stroke后加点
	private void addSegementPoints(int a, int b, int queryIndex)
	{
		currSegementInfo.addBack(1);

	}

	/********************************* 结束处理segement *************************************************/

	/********************************* 开始优化序列 *************************************************/

	// 优化序列
	public void optimization()
	{
		// handStortSegement();
		// handEndPoint();
		extent();

	}

	public void extent()
	{

		for (int i = 0; i < segementInfos.size(); i++)
		{
			if (i != 0)
			{
				segementInfos.get(i).addFront(Penalty.TranArea);

			}

			if (i != segementInfos.size() - 1)
			{
				segementInfos.get(i).addBack(Penalty.TranArea);
			}
		}
	}

	public void handEndPoint()
	{

		for (int i = 0; i < segementInfos.size(); i++)
		{
			if (i != segementInfos.size() - 1)
			{
				if (segementInfos.get(i).isReachEnd())
				{
					// segementInfos.get(i).removeBack(Penalty.EndArea);
					segementInfos.get(i + 1).addFront(4);
				}
			}

			if (!segementInfos.get(i).isReachEnd() && i == (segementInfos.size() - 1))
			{
				segementInfos.get(i).addBack();
			}


		}
	}

	public void handStortSegement()
	{

		for (int i = 1; i < segementInfos.size(); i++)
		{
			if (segementInfos.get(i).isShort())
			{
				// 不是最后一个
				if (i == segementInfos.size() - 1)
				{
					segementInfos.get(i).addFront(Penalty.TranArea);

				}
				// 如果是最后一个，就不删除他了
				else
				{
					segementInfos.get(i - 1).addBack(segementInfos.get(i).getSize());
					segementInfos.remove(i--);
				}
			}
		}
	}



	/********************************* 结束优化序列 *************************************************/

}
