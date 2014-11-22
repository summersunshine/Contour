package sample;

import java.util.Vector;

public class StrokeInfo
{
	public int lastIndex;
	public int startIndex;
	public int endIndex;
	
	public Vector<C> cs;

	public StrokeInfo(int startIndex)
	{
		this.cs = new Vector<C>();
		this.startIndex = startIndex;
	}
	
	public void addBack(int size)
	{
		int a = this.cs.lastElement().a;
		int b = this.cs.lastElement().b;
		
		for(int i = 0; i < size;i++)
		{
			if (this.cs.lastElement().b <= lastIndex)
			{
				this.cs.add(new C(a, b+i+1));
			}
			
		}
	}
	
	public void addFront(int size)
	{
		int a = this.cs.lastElement().a;
		int b = this.cs.lastElement().b;
		
		for(int i = 0; i < size;i++)
		{
			this.cs.add(0,new C(a, b-i-1));
		}
	}
	
	
	public void remove(int size)
	{
		for(int i = 0; i < size;i++)
		{
			if (!this.cs.isEmpty())
			{
				this.cs.remove(this.cs.lastElement());
			}
			
		}
	}
	
	public void addBack(StrokeInfo strokeInfo)
	{
		for (int i = 0; i < strokeInfo.cs.size(); i++)
		{
			this.cs.add(strokeInfo.cs.get(i));
		}
		endStroke(strokeInfo.endIndex);
	}
	
	public void addFront(StrokeInfo strokeInfo)
	{
		for (int i = 0; i < strokeInfo.cs.size(); i++)
		{
			this.cs.addAll(0, strokeInfo.cs);
		}
		this.startIndex = strokeInfo.startIndex;
	}
	

	public void add(C c)
	{
		this.cs.add(c);
	}
	
	public void endStroke(int endIndex)
	{
		this.endIndex = endIndex;
	}
	
	public int getL()
	{
		return this.cs.size();
	}
	
	public boolean isReachEnd()
	{
		return cs.lastElement().b == lastIndex;
		
	}

}
