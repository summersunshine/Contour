package sequence;

import geometry.Point;

import java.util.Vector;

import sample.LibParser;
import config.Penalty;

public class Segement
{
	public int			lastIndexOfLib;
	public int			startIndexOfQuery;
	public int			endIndexOfQuery;
	public int			startIndexOfLib;
	public int			endIndexOfLib;

	public Vector<C>	cs;

	public Segement(int startIndex)
	{
		this.cs = new Vector<C>();
		this.startIndexOfQuery = startIndex;
	}

	public int getIndexofLibStroke()
	{
		return cs.firstElement().a;
	}

	public int getStartIndexOfLib()
	{
		return cs.firstElement().b;
	}

	public int getEndIndexOfLib()
	{
		return cs.lastElement().b;
	}

	public void addToEnd()
	{
		int a = this.cs.lastElement().a;
		int b = this.cs.lastElement().b;
		int count = 0;
		while (this.cs.lastElement().b < lastIndexOfLib - 1)
		{
			startIndexOfQuery--;
			this.cs.add(new C(a, b + count + 1));
			count++;
		}

	}

	public void addBack(int size)
	{
		int a = this.cs.lastElement().a;
		int b = this.cs.lastElement().b;

		for (int i = 0; i < size; i++)
		{
			if (this.cs.lastElement().b < lastIndexOfLib - 1)
			{
				endIndexOfQuery++;
				this.cs.add(new C(a, b + i + 1));
				// System.out.println("add Back a: " + cs.lastElement().a +
				// " b: " + cs.lastElement().b);
			}

		}
	}

	public void addFront(int size)
	{
		int a = this.cs.firstElement().a;
		int b = this.cs.firstElement().b;

		for (int i = 0; i < size; i++)
		{
			if (this.cs.firstElement().b > 0)
			{
				startIndexOfQuery--;
				this.cs.add(0, new C(a, b - i - 1));
				// System.out.println("add Front a: " + cs.firstElement().a +
				// " b: " + cs.firstElement().b);
			}
		}
	}

	public void removeBack(int size)
	{
		for (int i = 0; i < size; i++)
		{
			if (!this.cs.isEmpty())
			{
				endIndexOfQuery--;
				this.cs.remove(this.cs.lastElement());
				// System.out.println("remove Back a: " + cs.lastElement().a +
				// " b: " + cs.lastElement().b);
			}

		}
	}

	public void removeFront(int size)
	{
		for (int i = 0; i < size; i++)
		{
			if (!this.cs.isEmpty())
			{
				startIndexOfQuery++;
				this.cs.remove(this.cs.firstElement());
				// System.out.println("remove Front a: " + cs.firstElement().a +
				// " b: " + cs.firstElement().b);

			}

		}
	}

	public void addBack(Segement strokeInfo)
	{
		for (int i = 0; i < strokeInfo.cs.size(); i++)
		{
			this.cs.add(strokeInfo.cs.get(i));
		}

		this.endIndexOfQuery = strokeInfo.endIndexOfQuery;
	}

	public void addFront(Segement strokeInfo)
	{
		for (int i = 0; i < strokeInfo.cs.size(); i++)
		{
			this.cs.addAll(0, strokeInfo.cs);
		}
		this.startIndexOfQuery = strokeInfo.startIndexOfQuery;
	}

	public void add(C c)
	{
		this.cs.add(c);
	}

	public void endStroke(int endIndex)
	{
		this.endIndexOfQuery = endIndex;
	}

	public int getL()
	{
		return this.cs.size();
	}

	public boolean isReachEnd()
	{
		return Math.abs(cs.lastElement().b - lastIndexOfLib) <= Penalty.EndArea;

	}

	public boolean isShort()
	{
		return getL() < Penalty.Lmin;
	}

	public boolean isEmpty()
	{
		return startIndexOfQuery >= endIndexOfQuery;
	}

	public Vector<Point> getQueryPoints()
	{
		Vector<Point> queryPoints = new Vector<Point>();

		for (int i = startIndexOfQuery; i < endIndexOfQuery; i++)
		{
			if (i < LibParser.queryStroke.points.size())
			{
				queryPoints.add(LibParser.queryStroke.points.get(i));
			}

		}
		return queryPoints;
	}

	public Vector<Point> getLibPoints()
	{
		Vector<Point> libPoints = new Vector<Point>();

		for (int i = getStartIndexOfLib(); i < getEndIndexOfLib(); i++)
		{
			if (i < LibParser.libStrokes.get(getIndexofLibStroke()).points.size())
			{
				libPoints.add(LibParser.libStrokes.get(getIndexofLibStroke()).points.get(i));
			}

		}
		return libPoints;
	}

}
