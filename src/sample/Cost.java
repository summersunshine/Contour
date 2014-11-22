package sample;

import java.util.Comparator;

public class Cost
{
	// ��ǰstroke��ĵ�a���ʴ�
	public int a;

	// ����stroke�ĵ�b������
	public int b;

	// ��ѯ�ĵ�i������
	public int i;

	// �����ĳͷ�
	public int ef;

	// ת���ĳͷ�
	public int et;

	// �̶���ĳͷ�
	public int es;
	
	//�˵�ĳͷ�
	public int ee;

	public int totalPenalty;

	public Cost(int a, int b, int i, int distance)
	{
		this.a = a;
		this.b = b;
		this.i = i;
		this.ef = distance;
		this.totalPenalty = distance;
	}

	public void print()
	{
		System.out.println("a: " + a + " b: " + b + " penalty " + totalPenalty);

	}

	public void addEe(int ee)
	{
		this.ee = ee;
		this.totalPenalty += ee;
	}
	
	public void addEt(int et)
	{
		this.et = et;
		this.totalPenalty += et;
	}
	
	public void addEs(int es)
	{
		this.es = es;
		this.totalPenalty +=es;
	}

	@SuppressWarnings("rawtypes")
	static final class CostComparator implements Comparator
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
