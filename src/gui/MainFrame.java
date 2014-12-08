package gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollBar;
import javax.swing.UIManager;

import util.LibParserUtil;
import config.GuiConfig;
import config.SampleConfig;

public class MainFrame extends JFrame implements ActionListener, AdjustmentListener, MouseMotionListener
{
	private DrawingPanel drawingPanel;
	private JButton saveButton;
	private JButton clearButton;
	private JButton lastButton;
	private JButton nextButton;

	private JScrollBar indexScrollBar;
	private JLabel indexLabel;

	public MainFrame()
	{
		initSetting();
		initDrawingPanel();
		initClearButton();
		initSaveButton();
		// initLastButton();
		// initNextButton();
		this.addMouseMotionListener(this);
	}

	private void initSetting()
	{
		this.setSize(GuiConfig.FRAME_DIMENSION.width, GuiConfig.FRAME_DIMENSION.height);
		this.setVisible(true);
		this.setLayout(null);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.white);
	}

	private void initDrawingPanel()
	{
		drawingPanel = new DrawingPanel();
		getContentPane().add(drawingPanel);
		drawingPanel.setBackground(Color.WHITE);

	}

	private void initClearButton()
	{
		clearButton = new JButton("清除");
		clearButton.setBounds(0, 0, 100, 50);
		getContentPane().add(clearButton);
		clearButton.addActionListener(this);
		clearButton.repaint();
	}

	private void initSaveButton()
	{
		saveButton = new JButton("保存");
		saveButton.setBounds(100, 0, 100, 50);
		getContentPane().add(saveButton);
		saveButton.addActionListener(this);
		saveButton.repaint();
	}

	// private void initLastButton()
	// {
	// lastButton = new JButton("上一点");
	// lastButton.setBounds(100, 0, 100, 50);
	// getContentPane().add(lastButton);
	// lastButton.addActionListener(this);
	// lastButton.repaint();
	// }
	//
	// private void initNextButton()
	// {
	// nextButton = new JButton("下一点");
	// nextButton.setBounds(200, 0, 100, 50);
	// getContentPane().add(nextButton);
	// nextButton.addActionListener(this);
	// nextButton.repaint();
	// }
	//
	// 依据点的数目创建
	public void initScorllBar(int size)
	{
		indexLabel = new JLabel("第0个点");
		indexLabel.setBounds(300, 0, 100, 50);

		indexScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1, 0, size);
		indexScrollBar.setBounds(400, 0, 300, 50);
		indexScrollBar.setUnitIncrement(1);
		indexScrollBar.setBlockIncrement(1);
		indexScrollBar.addAdjustmentListener(this);

		getContentPane().add(indexLabel);
		getContentPane().add(indexScrollBar);
		indexLabel.repaint();
		indexScrollBar.repaint();

		setVisible(true);

	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		// TODO Auto-generated method stub
		if (event.getSource() == clearButton)
		{
			drawingPanel.clear();

			this.remove(indexLabel);
			this.remove(indexScrollBar);
			this.repaint();
		}

		if (event.getSource() == saveButton)
		{
			LibParserUtil.saveResultImage(SampleConfig.OUTPUT_PATH + "After\\result.jpg");
		}

		if (event.getSource() == nextButton)
		{
			drawingPanel.moveToNextPoint(1);
		}

		if (event.getSource() == lastButton)
		{
			drawingPanel.moveToNextPoint(-1);
		}
	}

	@Override
	public void adjustmentValueChanged(AdjustmentEvent event)
	{
		// TODO Auto-generated method stub
		if (event.getSource() == indexScrollBar)
		{
			DrawingPanel.currentIndex = event.getValue();
			indexLabel.setText("第" + DrawingPanel.currentIndex + "个点");
			drawingPanel.moveToPoint();
		}

	}

	private static MainFrame mainFrame;

	public static MainFrame getInstance()
	{
		if (mainFrame == null)
		{
			mainFrame = new MainFrame();
		}
		return mainFrame;
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

		MainFrame.getInstance();

	}

	@Override
	public void mouseDragged(MouseEvent arg0)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// TODO Auto-generated method stub

	}

}
