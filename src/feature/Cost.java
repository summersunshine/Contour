package feature;

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
	public float ef;

	// 转换的惩罚
	public float et;

	// 短段落的惩罚
	public float es;

	// 端点的惩罚
	public float ee;

	public float totalPenalty;

	public Cost(int a, int b, int i, float distance)
	{
		this.a = a;
		this.b = b;
		this.i = i;
		this.ef = distance;
		this.totalPenalty = distance;
	}

	public void print()
	{
		// System.out.println("a: " + a + " b: " + b + " penalty " +
		// totalPenalty);
		System.out.println("a: " + a + " b: " + b + " feature " + ef + " transition " + et + "");
	}

	public void addEe(float ee)
	{
		this.ee = ee;
		this.totalPenalty += ee;
	}

	public void addEt(float et)
	{
		this.et = et;
		this.totalPenalty += et;
	}

	public void addEs(float es)
	{
		this.es = es;
		this.totalPenalty += es;
	}

	@SuppressWarnings("rawtypes")
	public static final class CostComparator implements Comparator
	{
		public int compare(Object o1, Object o2)
		{
			Cost s1 = (Cost) o1;
			Cost s2 = (Cost) o2;

			if (s1.totalPenalty > s2.totalPenalty)
			{
				return 1;
			}
			else if (s1.totalPenalty < s2.totalPenalty)
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
