package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import sample.LibParser;
import sample.QueryStroke;
import sample.ShapeConext;
import libary.Sample;
import config.GuiConfig;
import Geometry.Geometry;
import Geometry.Point;

public class DrawingPanel extends JPanel implements MouseListener,MouseMotionListener
{
	private LibParser libParser;
	private QueryStroke queryStroke;
	private Vector<Point>	points;
	private Vector<Point> leftContourPoints;
	private Vector<Point> rightContourPoints;
	public DrawingPanel()
	{
		
		this.setBounds(GuiConfig.DRAWING_RECTANGLE);
		this.setVisible(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.initPoint();
		this.initLibParser();
	}
	
	
	private void initPoint()
	{
		points = new Vector<Point>();
	}
	

	private void initLibParser()
	{
		libParser = new LibParser();
	}
	
	public void clear()
	{
		getGraphics().clearRect(0, 0, 1280, 720);
	}
	
	public int [] getPointX(Vector<Point> points)
	{
		int [] pointX = new int[points.size()];
		
		for (int i = 0; i < pointX.length; i++)
		{
			pointX[i] = (int) points.get(i).x;
		}
		return pointX;
	}
	
	public int [] getPointY(Vector<Point> points)
	{
		int [] pointX = new int[points.size()];
		
		for (int i = 0; i < pointX.length; i++)
		{
			pointX[i] = (int) points.get(i).y;
		}
		return pointX;
	}
	
	
	
	public  void drawSpine(Graphics2D graphics2d)
	{
		// TODO Auto-generated method stub
		
		points = Geometry.normalize(points);
		points = Geometry.removeClose(points,10);
		
		
		leftContourPoints = Geometry.getContourPoints(points, 20.0f,true);
		rightContourPoints = Geometry.getContourPoints(points, -20.0f,true);
		

	
		graphics2d.setColor(Color.BLACK);
		for (int i = 0; i < points.size(); i++)
		{
			graphics2d.setColor(Color.RED);
			graphics2d.fillRect((int)points.get(i).x-2, (int)points.get(i).y-2, 4, 4);
		}
		for (int i = 0; i < leftContourPoints.size(); i++)
		{
			graphics2d.setColor(Color.GREEN);
			graphics2d.fillRect((int)leftContourPoints.get(i).x-2, (int)leftContourPoints.get(i).y-2, 4, 4);
		}
		
		for (int i = 0; i < rightContourPoints.size(); i++)
		{	graphics2d.setColor(Color.BLUE);
			graphics2d.fillRect((int)rightContourPoints.get(i).x-2, (int)rightContourPoints.get(i).y-2, 4,4);
		}
		
		ShapeConext shapeConext = new ShapeConext(points, points.size()/2,0);
		
		shapeConext.drawCoordinateSystem(graphics2d);
		shapeConext.drawHistogram(graphics2d);
		
		queryStroke = new QueryStroke(points,rightContourPoints,leftContourPoints);
		
		
		libParser.compareWithQueryStroke(queryStroke);
	}
	
	public void addPoint(int x,int y)
	{
		if (!points.isEmpty() && 
				(points.lastElement().x == x && 
				points.lastElement().y == y))
		{
			return;
		}
		
		if (points.size() > 1 &&
				(points.get(points.size()-2).x == x && 
				points.get(points.size()-2).y == y))
		{
			return;
		}
		
		points.add(new Point(x,y));
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		// TODO Auto-generated method stub
		addPoint(event.getX(),event.getY());
	}

	@Override
	public void mouseMoved(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		// TODO Auto-generated method stub
		initPoint();

	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		// TODO Auto-generated method stub	
		drawSpine((Graphics2D)getGraphics());
	}
}


//
//reverseIndex = Geometry.getReverseIndex(points);
//if (!reverseIndex.isEmpty())
//{
//	contourPoints1 = Geometry.removeIntersect(contourPoints1,reverseIndex.get(0));
//	contourPoints2 = Geometry.removeIntersect(contourPoints2,reverseIndex.get(0));
//}
//

//contourPoints1 = Geometry.normalize(contourPoints1);
//contourPoints1 = Geometry.removeClose(contourPoints1,5);

//contourPoints2 = Geometry.normalize(contourPoints2);
//contourPoints2 = Geometry.removeClose(contourPoints2,5);

//contourPoints1 = Geometry.removeIntersect(contourPoints1);
//contourPoints2 = Geometry.removeIntersect(contourPoints2);


//if (!reverseIndex.isEmpty())
//{
//	contourPoints1 = Geometry.removeIntersect(contourPoints1,reverseIndex.get(0));
//	contourPoints2 = Geometry.removeIntersect(contourPoints2,reverseIndex.get(0));
//}

//graphics2d.setColor(Color.RED);
//graphics2d.drawPolyline(getPointX(), getPointY(), points.size());



//graphics2d.setColor(Color.GREEN);
//graphics2d.drawPolyline(getPointX(contourPoints1), getPointY(contourPoints1), contourPoints1.size());
//graphics2d.setColor(Color.BLUE);
//graphics2d.drawPolyline(getPointX(contourPoints2), getPointY(contourPoints2), contourPoints2.size());



//contourPoints1.addAll(contourPoints2);
//Histogram histogram  = new Histogram(contourPoints1, contourPoints1.size()/2);

//histogram.drawCoordinateSystem(graphics2d);

//histogram.drawHistogram(graphics2d);
