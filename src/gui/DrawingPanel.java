package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JPanel;

import Geometry.Geometry;
import Geometry.Histogram;
import Geometry.Point;

public class DrawingPanel extends JPanel implements MouseListener,MouseMotionListener
{
	
	private Vector<Point>	points;
	private Vector<Integer> pointXs;
	private Vector<Integer> pointYs;
//	private Point currpPoint;
	private int count;
	
	public DrawingPanel()
	{
		
		this.setBounds(Config.DRAWING_RECTANGLE);
		this.setVisible(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}
	
	public void clear()
	{
		getGraphics().clearRect(0, 0, 1280, 720);
	}
	
	public int [] getPointX()
	{
		int [] pointX = new int[pointXs.size()];
		
		for (int i = 0; i < pointX.length; i++)
		{
			pointX[i] = pointXs.get(i).intValue();
		}
		return pointX;
	}
	public int [] getPointY()
	{
		int [] pointX = new int[pointYs.size()];
		
		for (int i = 0; i < pointX.length; i++)
		{
			pointX[i] = pointYs.get(i).intValue();
		}
		return pointX;
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
		
		Vector<Point> contourPoints1 = Geometry.getContourPoints(points, 15.0f,false);
		Vector<Point> contourPoints2 = Geometry.getContourPoints(points, -15.0f,false);
		graphics2d.setColor(Color.RED);
		graphics2d.drawPolyline(getPointX(), getPointY(), points.size());
		

		
		graphics2d.setColor(Color.GREEN);
		graphics2d.drawPolyline(getPointX(contourPoints1), getPointY(contourPoints1), contourPoints1.size());
		graphics2d.drawPolyline(getPointX(contourPoints2), getPointY(contourPoints2), contourPoints2.size());
		

		
		graphics2d.setColor(Color.BLACK);
		for (int i = 0; i < points.size(); i++)
		{
			//graphics2d.fillRect((int)points.get(i).x-2, (int)points.get(i).y-2, 4, 4);
			//graphics2d.drawLine((int)points.get(i).x, (int)points.get(i).y, (int)contourPoints1.get(i).x, (int)contourPoints1.get(i).y);
			graphics2d.fillRect((int)contourPoints1.get(i).x-2, (int)contourPoints1.get(i).y-2, 4, 4);
			//graphics2d.drawLine((int)points.get(i).x, (int)points.get(i).y, (int)contourPoints2.get(i).x, (int)contourPoints2.get(i).y);
			graphics2d.fillRect((int)contourPoints2.get(i).x-2, (int)contourPoints2.get(i).y-2, 4,4);
		}
		
		contourPoints1.addAll(contourPoints2);
		Histogram histogram  = new Histogram(contourPoints1, contourPoints1.size()/2);
		
		histogram.drawCoordinateSystem(graphics2d);
		
		histogram.drawHistogram(graphics2d);
		
	}
	

	public void initPoint()
	{
		count = 0;
		points = new Vector<Point>();
		pointXs = new Vector<Integer>();
		pointYs = new Vector<Integer>();
	}
	
	public void addPoint(int x,int y)
	{
		if (!points.isEmpty() && 
				points.lastElement().x == x && 
				points.lastElement().y == y)
		{
			return;
		}
		points.add(new Point(x,y));
		pointXs.add(x);
		pointYs.add(y);
	}

	@Override
	public void mouseDragged(MouseEvent event)
	{
		// TODO Auto-generated method stub
		if (count++%2==0 || true)
		{
			addPoint(event.getX(),event.getY());
		}
		
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
		//addPoint(event.getX(),event.getY());

		
	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		// TODO Auto-generated method stub
		
		
		drawSpine((Graphics2D)getGraphics());
	}
	
	
	
	
	
}
