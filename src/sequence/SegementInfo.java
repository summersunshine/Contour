package sequence;

import geometry.CoordDiff;
import geometry.Point;

import java.util.Vector;

import sample.LibParser;

public class SegementInfo
{

	public static final int		FRONT	= 1;
	public static final int		BACK	= -1;

	// stroke's id
	public int					strokeId;

	public Vector<Point>		libPoints;

	public Vector<Point>		libLeftPoints;

	public Vector<Point>		libRightPoints;

	public Vector<Point>		queryPoints;

	public Vector<Point>		queryLeftPoints;

	public Vector<Point>		queryRightPoints;

	public Vector<CoordDiff>	coordDiffs;

	public int					libIndexSize;
	public int					queryIndexSize;
	public int					startLibIndex;
	public int					startQueryIndex;
	public int					endLibIndex;
	public int					endQueryIndex;
	public int					frontStartOverlayIndex;
	public int					frontEndOverLayIndex;
	public int					backStartOverlayIndex;
	public int					backEndOverlayIndex;

	public SegementInfo(int strokeId, int startLibIndex, int startQueryIndex)
	{
		this.strokeId = strokeId;
		this.startLibIndex = startLibIndex;
		this.endLibIndex = startLibIndex;
		this.startQueryIndex = startQueryIndex;
		this.endQueryIndex = startQueryIndex;

		this.libIndexSize = LibParser.libStrokes.get(strokeId).getSize();
		this.queryIndexSize = LibParser.queryStroke.getSize();

		this.frontStartOverlayIndex = 0;
		this.frontEndOverLayIndex = 0;
		this.backStartOverlayIndex = 0;
		this.backEndOverlayIndex = 0;

		this.libPoints = new Vector<Point>();
		this.libLeftPoints = new Vector<Point>();
		this.libRightPoints = new Vector<Point>();
		this.queryPoints = new Vector<Point>();
		this.queryLeftPoints = new Vector<Point>();
		this.queryRightPoints = new Vector<Point>();

		this.coordDiffs = new Vector<CoordDiff>();
	}

	public void addFront()
	{

		while (startLibIndex > 0 && startQueryIndex > 0)
		{
			startLibIndex--;
			startQueryIndex--;
			addLibPoint(strokeId, startLibIndex, FRONT);
			addQueryPoint(startQueryIndex, FRONT);

		}

	}

	public void addBack()
	{

		while (endLibIndex < libIndexSize - 1 && endQueryIndex < queryIndexSize - 1)
		{
			endLibIndex++;
			endQueryIndex++;
			addLibPoint(strokeId, endLibIndex, BACK);
			addQueryPoint(endQueryIndex, BACK);
		}

	}

	public void addFront(int num)
	{
		while (num-- > 0)
		{
			startLibIndex--;
			startQueryIndex--;
			if (startLibIndex > 0 && startQueryIndex > 0)
			{
				addLibPoint(strokeId, startLibIndex, FRONT);
				addQueryPoint(startQueryIndex, FRONT);
			}
			else
			{
				break;
			}
		}
	}

	public void addBack(int num)
	{
		while (num-- > 0)
		{
			endLibIndex++;
			endQueryIndex++;
			if (endLibIndex < libIndexSize - 1 && endQueryIndex < queryIndexSize - 1)
			{
				addLibPoint(strokeId, endLibIndex, BACK);
				addQueryPoint(endQueryIndex, BACK);

			}
			else
			{
				break;
			}
		}
	}

	public void calCoorDiff()
	{
		for (int i = 0; i < libPoints.size(); i++)
		{
			coordDiffs.add(new CoordDiff(libLeftPoints.get(i), queryLeftPoints.get(i)));
			coordDiffs.add(new CoordDiff(libRightPoints.get(i), queryRightPoints.get(i)));
		}

	}

	public void addLibPoint(int a, int b, int dir)
	{
		Point point = LibParser.libStrokes.get(a).points.get(b);
		Point leftPoint = LibParser.libStrokes.get(a).leftContourPoints.get(b);
		Point rightPoint = LibParser.libStrokes.get(a).rightContourPoints.get(b);

		if (dir > 0)
		{
			addLibPointFront(point, leftPoint, rightPoint);
		}
		else
		{
			addLibPointBack(point, leftPoint, rightPoint);
		}

	}

	public void addLibPointFront(Point point, Point leftPoint, Point rightPoint)
	{
		libPoints.add(0, point);
		libLeftPoints.add(0, leftPoint);
		libRightPoints.add(0, rightPoint);
	}

	public void addLibPointBack(Point point, Point leftPoint, Point rightPoint)
	{
		libPoints.add(point);
		libLeftPoints.add(leftPoint);
		libRightPoints.add(rightPoint);
	}

	public void addQueryPoint(int index, int dir)
	{
		Point point = LibParser.queryStroke.points.get(index);
		Point leftPoint = LibParser.queryStroke.leftContourPoints.get(index);
		Point rightPoint = LibParser.queryStroke.rightContourPoints.get(index);
		if (dir > 0)
		{
			addQueryPointFront(point, leftPoint, rightPoint);
		}
		else
		{
			addQueryPointBack(point, leftPoint, rightPoint);
		}

	}

	public void addQueryPointFront(Point point, Point leftPoint, Point rightPoint)
	{
		queryPoints.add(0, point);
		queryLeftPoints.add(0, leftPoint);
		queryRightPoints.add(0, rightPoint);
	}

	public void addQueryPointBack(Point point, Point leftPoint, Point rightPoint)
	{
		queryPoints.add(point);
		queryLeftPoints.add(leftPoint);
		queryRightPoints.add(rightPoint);
	}

	/**
	 * set the overlay area's start index and end index
	 * */
	public static void setOverLayArea(Vector<SegementInfo> segementInfos)
	{
		for (int i = 0; i < segementInfos.size(); i++)
		{
			segementInfos.get(i).frontStartOverlayIndex = segementInfos.get(i).startQueryIndex;
			segementInfos.get(i).backEndOverlayIndex = segementInfos.get(i).endQueryIndex;
			if (i != segementInfos.size() - 1)
			{
				segementInfos.get(i).backStartOverlayIndex = segementInfos.get(i + 1).startQueryIndex;
			}
			if (i != 0)
			{
				segementInfos.get(i).frontEndOverLayIndex = segementInfos.get(i - 1).endQueryIndex;
			}

			if (i == segementInfos.size() - 1)
			{
				segementInfos.get(i).backStartOverlayIndex = segementInfos.get(i + 1).endQueryIndex;
			}

			if (i != 0)
			{
				segementInfos.get(i).frontEndOverLayIndex = 0;
			}
		}
	}
}
