package sample;


import geometry.PixelGrabber;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import config.Penalty;
import config.SampleConfig;

public class Segement
{
	public int lastIndexOfLib;
	public int startIndexOfQuery;
	public int endIndexOfQuery;
	public int startIndexOfLib;
	public int endIndexOfLib;

	public Vector<C> cs;

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
		while (this.cs.lastElement().b < lastIndexOfLib)
		{
			this.cs.add(new C(a, b + count + 1));
			count ++;
		}

		
	}

	public void addBack(int size)
	{
		int a = this.cs.lastElement().a;
		int b = this.cs.lastElement().b;

		for (int i = 0; i < size; i++)
		{
			if (this.cs.lastElement().b < lastIndexOfLib)
			{
				this.cs.add(new C(a, b + i + 1));
				System.out.println("add Back a: " + cs.lastElement().a + " b: " + cs.lastElement().b);
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
				this.cs.add(0, new C(a, b - i - 1));
				System.out.println("add Front a: " + cs.firstElement().a + " b: " + cs.firstElement().b);
			}
		}
	}

	public void removeBack(int size)
	{
		for (int i = 0; i < size; i++)
		{
			if (!this.cs.isEmpty())
			{
				this.cs.remove(this.cs.lastElement());
				System.out.println("remove Back a: " + cs.lastElement().a + " b: " + cs.lastElement().b);
			}

		}
	}

	public void removeFront(int size)
	{
		for (int i = 0; i < size; i++)
		{
			if (!this.cs.isEmpty())
			{
				this.cs.remove(this.cs.firstElement());
				System.out.println("remove Front a: " + cs.firstElement().a + " b: " + cs.firstElement().b);

			}

		}
	}

	public void addBack(Segement strokeInfo)
	{
		for (int i = 0; i < strokeInfo.cs.size(); i++)
		{
			this.cs.add(strokeInfo.cs.get(i));
		}
		endStroke(strokeInfo.endIndexOfQuery);
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

}
