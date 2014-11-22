package sample;

import java.util.Vector;

public class StrokeInfo
{
	public int startIndex;
	public int endIndex;
	
	public Vector<C> cs;

	public StrokeInfo(int startIndex)
	{
		this.cs = new Vector<C>();
		this.startIndex = startIndex;
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

}
