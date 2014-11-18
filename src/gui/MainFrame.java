package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

import libary.Sample;
import config.GuiConfig;

public class MainFrame extends JFrame implements ActionListener
{
	private Sample sample;
	private DrawingPanel drawingPanel;
	private JButton clearButton;
	private	JButton showButton;
	
	public MainFrame()
	{
		initSetting();
		initDrawingPanel();
		initClearButton();
		initShowButton();
		//initSample();
	}
	
	
	public void initSetting()
	{
		this.setSize(GuiConfig.FRAME_DIMENSION.width, GuiConfig.FRAME_DIMENSION.height);
		this.setVisible(true);
		this.setLayout(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.white);
	}
	
	public void initDrawingPanel()
	{
		drawingPanel = new DrawingPanel();
		getContentPane().add(drawingPanel);
		drawingPanel.setBackground(Color.WHITE);
		
	}
	
	public void initClearButton()
	{
		clearButton = new JButton("Çå³ý");
		clearButton.setBounds(0, 0, 100, 50);
		getContentPane().add(clearButton);
		clearButton.addActionListener(this);
		
	}
	
	public void initShowButton()
	{
		showButton = new JButton("ÏÔÊ¾");
		showButton.setBounds(100, 0, 100, 50);
		getContentPane().add(showButton);
		showButton.addActionListener(this);
		
	}
	
	private void initSample()
	{
		// TODO Auto-generated method stub
		 sample = new Sample();
	}
	

	@Override
	public void actionPerformed(ActionEvent event)
	{
		// TODO Auto-generated method stub
		if (event.getSource() == clearButton)
		{
			drawingPanel.clear();
		}
		
		if (event.getSource() == showButton)
		{
			drawingPanel.drawSample();
			//sample.drawSample((Graphics2D)drawingPanel.getGraphics());
		}
	}
	


	public static void main(String args[])
	{
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
		}
		
		MainFrame mainFrame  = new MainFrame();
		
	}
}


//Point lastPoint = new Point(0,0);
//Point currPoint = new Point(1,1);
//Point nextPoint = new Point(2,0);
//
//Point contourPoint1 = Geometry.getContourPoint(lastPoint, currPoint, nextPoint, 1);
//
//contourPoint1.print();
//
//Point contourPoint2 = Geometry.getContourPoint(lastPoint, currPoint, 1);
//
//contourPoint2.print();
//
//Point diff = contourPoint2.sub(contourPoint1);
//
//diff.print();