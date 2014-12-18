package sample;

import java.awt.image.BufferedImage;
import java.util.Vector;

import sequence.QuerySegement;
import sequence.Segement;
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
	public static final int			STOKE_NUM	= 14;

	// 库中笔触数列
	public static Vector<LibStroke>	libStrokes;

	public static QueryStroke		queryStroke;

	public static Vector<Segement>	segements;

	public static BufferedImage		resultImage;

	private Segement				currSegement;

	public Cost						lastCost;

	public int						currIndex;

	public static int				drawNum		= 0;

	public LibParser()
	{
		this.initStrokeInfos();
		LibParser.loadLibary();
	}

	public void initStrokeInfos()
	{
		this.currSegement = null;
		segements = new Vector<Segement>();
	}

	/**
	 * 载入笔触库中笔触
	 * */
	public static void loadLibary()
	{
		// System.out.println("LibParser.initLibStrokes() begin");
		LibParser.libStrokes = new Vector<LibStroke>();
		int sampleDistSum = 0, sampleBrushWidthSum = 0;
		for (int i = 0; i < STOKE_NUM; i++)
		{
			LibStroke libStroke = new LibStroke(Global.Libary, i);
			sampleDistSum += libStroke.sampleDist;
			sampleBrushWidthSum += libStroke.sampleBrushWidth;
			LibParser.libStrokes.add(libStroke);
			System.out.println("parse stroke " + i);
		}
		// Global.BRUSH_WDITH = (int) (sampleBrushWidthSum / STOKE_NUM);
		// Global.SAMPLE_DIST = (int) (sampleDistSum / STOKE_NUM);
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

		this.matchSegements();

		// this.addTransitionData();

		// this.optimization();

		LibParserUtil.drawStrokeSegements("After\\");

	}

	/**
	 * 匹配段落
	 * */
	public void matchSegements()
	{
		for (int i = 0; i < LibParser.queryStroke.querySegements.size(); i++)
		{
			matchSegement(LibParser.queryStroke.querySegements.get(i));
		}
	}

	public void matchSegement(QuerySegement querySegement)
	{
		boolean flag = false;
		int targetI = 0, targetJ = 0;
		int size;
		int count;

		float minSum = 0;

		for (int i = 0; i < LibParser.libStrokes.size(); i++)
		{
			size = libStrokes.get(i).libSamples.size();
			count = size - querySegement.getLength();
			if (count < 0)
			{
				continue;
			}
			else
			{
				if (!flag)
				{
					minSum = getFeatureSum(querySegement.startIndex, querySegement.endIndex, i, 0);
					targetI = i;
					flag = true;
				}
			}
			// minSum = getFeatureSum(i, 0, length);
			for (int j = 0; j < count; j++)
			{
				float sum = getFeatureSum(querySegement.startIndex, querySegement.endIndex, i, j);
				// System.out.println("i: " + i + " j: " + j + " sum: " + sum);
				if (minSum > sum)
				{
					targetI = i;
					targetJ = j;
					minSum = sum;
				}
			}
		}
		System.out.println();
		segements.add(new Segement(targetI, targetJ, querySegement.startIndex));
		segements.lastElement().addBack(querySegement.getLength());

	}

	public float getFeatureSum(int startIndex, int endIndex, int a, int startB)
	{
		float sum = 0;
		for (int count = 0, i = startIndex; i <= endIndex; i++, count++)
		{
			sum += LibParser.queryStroke.getFeatureDistance(i, a, startB + count);
		}
		return sum;

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

			// queryStroke.querySamples.get(i).sort();
			// queryStroke.querySamples.get(i).printKNN();

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
			// queryStroke.querySamples.get(currIndex).printKNN();

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

		float ratio = currSegement.getSize() < Penalty.Lmin ? 1 : Penalty.Lmin / 1.33f / currSegement.getSize() + 0.25f;

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

		if (currSegement != null)
		{

			segements.add(currSegement);
		}

		currSegement = new Segement(a, b, queryIndex);

	}

	// 在stroke后加点
	private void addSegementPoints(int a, int b, int queryIndex)
	{
		currSegement.addBack(1);

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

		for (int i = 0; i < segements.size(); i++)
		{
			if (i != 0)
			{
				segements.get(i).addFront(Penalty.TranArea);

			}

			if (i != segements.size() - 1)
			{
				segements.get(i).addBack(Penalty.TranArea);
			}
		}
	}

	public void handEndPoint()
	{

		for (int i = 0; i < segements.size(); i++)
		{
			if (i != segements.size() - 1)
			{
				if (segements.get(i).isReachEnd())
				{
					// segementInfos.get(i).removeBack(Penalty.EndArea);
					segements.get(i + 1).addFront(4);
				}
			}

			if (!segements.get(i).isReachEnd() && i == (segements.size() - 1))
			{
				segements.get(i).addBack();
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
					segements.get(i - 1).addBack(segements.get(i).getSize());
					segements.remove(i--);
				}
			}
		}
	}

	/********************************* 结束优化序列 *************************************************/

}
