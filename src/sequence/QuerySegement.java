package sequence;

public class QuerySegement
{
	public static final int	TYPE_SMALL	= 0;
	public static final int	TYPE_LARGE	= 1;

	public int				startIndex;
	public int				endIndex;
	public int				type;

	public QuerySegement(int startIndex, int endIndex, int type)
	{
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.type = type;
	}

	public int getLength()
	{
		return endIndex - startIndex + 1;
	}

	public boolean isTypeEqual(QuerySegement querySegement)
	{
		return type == querySegement.type;
	}

	public void mergeSegement(QuerySegement querySegement)
	{
		int minStartIndex = startIndex < querySegement.startIndex ? startIndex : querySegement.startIndex;
		int maxEndIndex = endIndex > querySegement.endIndex ? endIndex : querySegement.endIndex;
		this.startIndex = minStartIndex;
		this.endIndex = maxEndIndex;
	}

	public void extend(int x)
	{
		extendFront(x);
		extendBack(x);
	}

	public void extendFront(int x)
	{
		startIndex -= x;
		startIndex = startIndex < 0 ? 0 : startIndex;
	}

	public void extendBack(int x)
	{
		endIndex += x;
	}

}
