package gui;

import geometry.Geometry;
import geometry.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.security.MessageDigest;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import sample.LibParser;
import sample.QueryStroke;
import sample.ShapeConext;
import config.GuiConfig;

public class DrawingPanel extends JPanel implements MouseListener,MouseMotionListener
{
	//当前点的索引
	public static int currentIndex;
	
	private LibParser libParser;
	private QueryStroke queryStroke;
	
	private Vector<Point> points;
	private Vector<Point> leftContourPoints;
	private Vector<Point> rightContourPoints;
	
	private boolean clearFlag;
	

	private JLabel posLabel;
	
	public DrawingPanel()
	{
		initPosLabel();
		this.setBounds(GuiConfig.DRAWING_RECTANGLE);
		this.setVisible(true);
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.clearFlag = true;
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
		clearFlag = true;
		getGraphics().clearRect(0, 0, 1280, 720);
	}
	
	public void moveToPoint()
	{

		Graphics2D graphics2d = (Graphics2D)getGraphics();
		
		graphics2d.clearRect(0, 0, 1280, 720);
		
		this.drawStrokeSample(graphics2d);
		
		this.queryStroke.drawShapeContext(graphics2d, currentIndex);
	}
	
	public void moveToNextPoint(int dir)
	{
		if (clearFlag)
		{
			JOptionPane.showMessageDialog(null, "画布为空,画了笔触再说");
			return;
		}
		
		
		Graphics2D graphics2d = (Graphics2D)getGraphics();
		
		graphics2d.clearRect(0, 0, 1280, 720);
		
		this.drawStrokeSample(graphics2d);
		
		this.currentIndex = (this.currentIndex + dir)%points.size();
		
		this.queryStroke.drawShapeContext(graphics2d, currentIndex);
	}
	
	/**
	 * 设置主路径与轮廓点
	 * */
	public void setPoints()
	{
		points = Geometry.normalize(points,6);
		points = Geometry.removeClose(points,6);	
		
		leftContourPoints = Geometry.getContourPoints(points, 20.0f,true);
		rightContourPoints = Geometry.getContourPoints(points, -20.0f,true);
		
	
		//依据points的数目
		MainFrame.getInstance().initScorllBar(points.size());
	
	}
	
	
	/**
	 * 在鼠标释放之后
	 * 1.重新设置所有的路径与轮廓点
	 * 2.重绘制这些点
	 * 3.重新生成所有点的shape context
	 * 4.绘制第一个点的shape context
	 * */
	public void drawAfterMouseRelease(Graphics2D graphics2d)
	{
	//如果没有被清除，那么不做处理
		if (!clearFlag)
		{
			return;
		}
		
		this.setPoints();

		this.drawStrokeSample(graphics2d);
				
		queryStroke = new QueryStroke(points,rightContourPoints,leftContourPoints);
		
		queryStroke.drawShapeContext(graphics2d, 0);
		
		libParser.compareWithQueryStroke(queryStroke);
		
		clearFlag = false;
	}
	
	
	/**
	 * 画笔触的采样点
	 * */
	public  void drawStrokeSample(Graphics2D graphics2d)
	{
		
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

	}
	
	/**
	 * 添加新的点
	 * */
	private void addPoint(int x,int y)
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

	private void initPosLabel()
	{
		posLabel = new JLabel();
		posLabel.setBounds(900,0,100,50);
		this.add(posLabel);
		posLabel.repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent event)
	{
		addPoint(event.getX(),event.getY());
	}

	@Override
	public void mouseMoved(MouseEvent event)
	{
		posLabel.setText("x:" + event.getX() + "y:"  + event.getY());
		posLabel.repaint();
	}

	@Override
	public void mouseClicked(MouseEvent event)
	{
		
	}

	@Override
	public void mouseEntered(MouseEvent event)
	{
		
	}

	@Override
	public void mouseExited(MouseEvent event)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent event)
	{
		initPoint();

	}

	@Override
	public void mouseReleased(MouseEvent event)
	{
		drawAfterMouseRelease((Graphics2D)getGraphics());
	}
}
