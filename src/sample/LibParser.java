package sample;

import java.awt.image.BufferedImage;
import java.util.Vector;

import sequence.C;
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
	// ���бʴ�����Ŀ
	public static final int STOKE_NUM = 14;

	// ���бʴ�����
	public static Vector<LibStroke> libStrokes;

	public static QueryStroke queryStroke;

	public static Vector<Segement> segements;

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
		segements = new Vector<Segement>();
		segements.add(new Segement(0));
	}

	/**
	 * ����ʴ����бʴ�
	 * */
	public void initLibStrokes()
	{
		System.out.println("LibParser.initLibStrokes() begin");
		LibParser.libStrokes = new Vector<LibStroke>();
		for (int i = 0; i < STOKE_NUM; i++)
		{
			LibParser.libStrokes.add(new LibStroke(Global.Libary, i));
			System.out.println("parse stroke " + i);
		}

		Feature.isLoadBegin = false;
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
		LibParser.queryStroke = queryStroke;

		this.initStrokeInfos();

		this.addFeatureData();


		this.addTransitionData();


		this.optimization();

		LibParserUtil.drawStrokeSegements("After\\", segements, libStrokes, queryStroke);

	}

	/**
	 * ����feature����
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
	 * ����Transition ����
	 * 
	 * @param otherSamples
	 * */
	public void addTransitionData()
	{
		System.out.println("transition cal begin");

		for (currIndex = 1; currIndex < queryStroke.querySamples.size(); currIndex++)
		{
			lastCost = getQuerySampleCost(currIndex - 1, 0);

			for (int j = 0; j < queryStroke.querySamples.get(currIndex).costs.size(); j++)
			{
				Cost cost = getQuerySampleCost(currIndex, j);
				float Et = getTransitionPenalty(cost.a, cost.b);
				queryStroke.querySamples.get(currIndex).costs.get(j).addEt(Et);
			}

			queryStroke.querySamples.get(currIndex).sort();
			queryStroke.querySamples.get(currIndex).printKNN();

			addSegements(lastCost.a, lastCost.b);
		}

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

	/********************************* ������ɳͷ� *************************************************/

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

		float ratio = count < Penalty.Lmin ? 1 : Penalty.Lmin / 1.33f / count + 0.25f;

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

	/********************************* �������ɳͷ� *************************************************/

	/********************************* ����segement *************************************************/

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

	// ����һ���µ�stroke
	private void createNewSegements(int a, int b)
	{
		count = 0;

		Segement segement = new Segement(currIndex);
		segement.add(new C(a, b));
		segements.add(segement);
	}

	// ��stroke��ӵ�
	private void addSegementPoints(int a, int b)
	{
		count++;

		// ÿ�ζ�Ҫ����һ�����һ�������Ϣ
		segements.lastElement().add(new C(a, b));
		segements.lastElement().lastIndexOfLib = libStrokes.get(a).points.size() - 1;
		segements.lastElement().endStroke(currIndex);
	}

	/********************************* ��������segement *************************************************/

	/********************************* ��ʼ�Ż����� *************************************************/

	// �Ż�����
	public void optimization()
	{
		handStortSegement();
		//handEndPoint();
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
					segements.get(i).removeBack(Penalty.EndArea);
					segements.get(i + 1).addFront(4);
				}
			}

			if (!segements.get(i).isReachEnd() && i == (segements.size() - 1))
			{
				segements.get(i).addToEnd();
			}

			if (segements.get(i).startIndexOfQuery >= segements.get(i).endIndexOfQuery)
			{
				segements.remove(i);
			}

		}
	}

	public void handStortSegement()
	{
		for (int i = 0; i < segements.size(); i++)
		{
			if (segements.get(i).isEmpty())
			{
				segements.remove(i--);

			}
		}

		for (int i = 1; i < segements.size(); i++)
		{
			if (segements.get(i).isShort())
			{
				// �������һ��
				if (i == segements.size() - 1)
				{
					segements.get(i).addFront(Penalty.TranArea);

				}
				// ��������һ�����Ͳ�ɾ������
				else
				{
					segements.get(i - 1).addBack(segements.get(i).getL());
					segements.remove(i--);
				}
			}
		}
	}

	// public void handStortSegement(int beginIndex, int endIndex)
	// {
	// int counts = 0;
	// for (int j = beginIndex; j < endIndex; j++)
	// {
	// counts += segements.get(j).getL();
	// }
	//
	// if (beginIndex - 1 >= 0)
	// {
	// segements.get(beginIndex - 1).addBack(counts);
	// }
	//
	// if (endIndex < segements.size())
	// {
	// segements.get(endIndex).addFront(counts);
	// }
	//
	// for (int j = beginIndex; j < endIndex; j++)
	// {
	// segements.remove(beginIndex);
	// }
	// }

	/********************************* �����Ż����� *************************************************/

}
