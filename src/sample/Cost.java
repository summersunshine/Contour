package sample;

import java.util.Comparator;

public class Cost
{
	// 当前stroke库的第a个笔触
	public int a;

	// 属于stroke的第b个样例
	public int b;

	// 查询的第i个样例
	public int i;

	// 特征的惩罚
	public int ef;

	// 转换的惩罚
	public int et;

	// 短段落的惩罚
	public int es;

	public Cost(int a, int b, int i, int distance)
	{
		this.a = a;
		this.b = b;
		this.i = i;
		this.ef = distance;
	}
	
	public void print()
	{
		System.out.println("a: " +a + " b: " +b + " i: " + i + " distance " + ef);
		
	}

	static final class CostComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			Cost s1 = (Cost) o1;
			Cost s2 = (Cost) o2;

			if (s1.ef < s2.ef)
			{
				return 1;
			}
			else if(s1.ef > s2.ef)
			{
				return -1;
			}
			else
			{
				return 0;
			}
		}
	}
	

}
