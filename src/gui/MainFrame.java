package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.UIManager;

public class MainFrame extends JFrame implements ActionListener
{

	private DrawingPanel drawingPanel;
	private JButton clearButton;
	
	public MainFrame()
	{
		initSetting();
		initDrawingPanel();
		initClearButton();
	}
	
	
	public void initSetting()
	{
		this.setSize(Config.FRAME_DIMENSION.width, Config.FRAME_DIMENSION.height);
		this.setVisible(true);
		this.setLayout(null);
		this.setResizable(false);
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
	

	@Override
	public void actionPerformed(ActionEvent event)
	{
		// TODO Auto-generated method stub
		if (event.getSource() == clearButton)
		{
			drawingPanel.clear();
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