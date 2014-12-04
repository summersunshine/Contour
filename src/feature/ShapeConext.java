package feature;

import geometry.Geometry;
import geometry.LogPolar;
import geometry.Point;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import config.SampleConfig;

public class ShapeConext
{
	public static final int TYPE_HISTORY = 0;
	public static final int TYPE_FUTURE = 1;
	public static final int TYPE_NORMAL = 2;

	// ���ڴ�С
	public static final int winSize = 30;

	// ����ָ��������Ŀ
	public static final int pBins = 6;

	// �Ƕȷ���ָ��������Ŀ
	public static final int angleBins = 6;

	// ԭ����±꣨������
	private int sourceIndex;

	// ԭ�������
	private Point sourcePoint;

	// ������Ķ���������
	private Vector<LogPolar> logPolars;

	// �������xy����
	private Vector<Point> points;
	
	//�ο�����ϵ�ĽǶ�
	private double angle;

	// ͳ������
	private int[][] statistics = new int[pBins][angleBins];

	private float averageAngle;
	private int meaningfulCount;
	private int type;
	private Point startPoint;

	/**
	 * ���캯��
	 * 
	 * @param points
	 * @param sourceIndex
	 * @param dir
	 * 
	 *            ���dir>0��ֻͳ��sourceIndex֮��ĵ� ���dir<0, ֻͳ��souceIndex֮ǰ�ĵ� ���dir=0,
	 *            ��ͳ�����еĵ�
	 * */
	public ShapeConext(Vector<Point> points,double angle, int sourceIndex, int dir)
	{
		this.logPolars = new Vector<LogPolar>();

		this.angle = angle;
		
		this.points = points;

		this.sourcePoint = points.get(sourceIndex);

		this.sourceIndex = sourceIndex;

		this.averageAngle = 0;

		this.meaningfulCount = 0;

		if (dir > 0)
		{
			type = TYPE_FUTURE;
			countForFuture();
		}
		else if (dir < 0)
		{
			type = TYPE_HISTORY;
			countForHistory();
		}
		else
		{
			type = TYPE_NORMAL;
			countForAll();
		}
		if (meaningfulCount > 0)
		{
			this.averageAngle /= meaningfulCount;
		}

		this.startPoint = getStartPoint();

	}

	/**
	 * ���㵱ǰ��֮���shape context
	 * */
	private void countForFuture()
	{
		int begin = sourceIndex + 1;
		int end = sourceIndex + winSize > points.size() ? points.size() : sourceIndex + winSize;
		for (int i = begin; i < end; i++)
		{
			count(i);
		}
	}

	/**
	 * ���㵱ǰ��֮ǰ��shape context
	 * */
	private void countForHistory()
	{
		int begin = sourceIndex - winSize < 0 ? 0 : sourceIndex - winSize;
		int end = sourceIndex;
		for (int i = begin; i < end; i++)
		{
			count(i);
		}
	}

	/**
	 * �������е�
	 * */
	private void countForAll()
	{
		for (int i = 0; i < points.size(); i++)
		{
			if (sourceIndex != i)
			{
				count(i);
			}
		}
	}

	/**
	 * �����±�i�ĵ�λ���ĸ�������
	 * */
	private void count(int i)
	{
		LogPolar logPolar = new LogPolar(points.get(i).sub(sourcePoint),angle);
		logPolars.addElement(logPolar);

		int x = (int) (logPolar.p);
		int y = (int) (angleBins - 1 - (int) (logPolar.angle * angleBins / 360));

		if (x < pBins)
		{
			statistics[x][y]++;
			meaningfulCount++;
			averageAngle += logPolar.angle;
		}

	}

	/**
	 * ��shape context����Ϣ���ı��ķ�ʽ������ļ���
	 * 
	 * @param path
	 * */
	public void createShapeContextText(String path)
	{
		File file = new File(path);
		try
		{
			FileWriter fileWriter = new FileWriter(file);

			for (int i = 0; i < statistics.length; i++)
			{
				for (int j = 0; j < statistics[i].length; j++)
				{
					fileWriter.write(statistics[statistics.length - i - 1][j] + " ");
				}

				fileWriter.write("\r\n");
			}

			fileWriter.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * ��shape context����Ϣ��ͼ��ķ�ʽ������ļ���
	 * 
	 * @param path
	 * */
	public void createShapeContextImage(String path)
	{
		BufferedImage image = new BufferedImage(30 * angleBins, 30 * pBins, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				int value = 245 - statistics[i][j] * 10;
				value = value < 0 ? 0 : value;

				graphics2d.setColor(new Color(value, value, value));
				graphics2d.fillRect(j * 30, (pBins - 1 - i) * 30, 30, 30);

			}
		}

		File file = new File(path);
		System.out.println(file.getPath());
		try
		{
			ImageIO.write(image, "JPG", file);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * ��shape context���Ƴ���
	 * 
	 * @param graphics2d
	 * */
	public void drawShapeContext(Graphics2D graphics2d)
	{

		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				int value = 245 - statistics[i][j] * 10;
				value = value < 0 ? 0 : value;

				graphics2d.setColor(new Color(value, value, value));
				graphics2d.fillRect((int) startPoint.x + j * 30, (int) startPoint.y - i * 30, 30, 30);

			}
		}

		this.drawDescribe(graphics2d);
	}

	/**
	 * ��shape context���ı��������Ƴ���
	 * 
	 * @param graphics2d
	 * */
	public void drawDescribe(Graphics2D graphics2d)
	{
		Font f = new Font("����", Font.BOLD, 16);
		Color[] colors = { Color.ORANGE, Color.LIGHT_GRAY };
		graphics2d.setFont(f);
		graphics2d.setPaint(colors[0]);
		graphics2d.drawString(getDescribe(), (int) startPoint.x + 50, (int) startPoint.y + 50);
	}

	private String getDescribe()
	{
		if (type == TYPE_FUTURE)
		{
			return "Future";
		}
		else if (type == TYPE_HISTORY)
		{
			return "HISTORY";
		}
		else
		{
			return "WHOLE";
		}
	}

	private Point getStartPoint()
	{
		if (type == TYPE_FUTURE)
		{
			return new Point(0, 600);
		}
		else
		{
			return new Point(0, 400);
		}
	}
	
	
	/**
	 * ���ƶ�������������ϵ
	 * 
	 * @param graphics2d
	 * */
	public void drawCoordinateSystem()
	{
		double radius =  Math.exp(pBins) * 2;
		
		BufferedImage image = new BufferedImage((int)radius, (int)radius, BufferedImage.TYPE_INT_ARGB);
		
		int halfWidth = image.getWidth()/2;
		int halfHeight = image.getHeight()/2;
		
		for (int y = 0; y < image.getHeight(); y++)
		{
			for (int x = 0; x < image.getWidth(); x++)
			{
				image.setRGB(x, y, 0x00ffffff);
			}
		}
		
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setStroke(new BasicStroke(3));
		graphics2d.setColor(new Color(255, 0, 0));

		// sourcePoint.print();
		for (int i = 1; i <= pBins; i++)
		{
			radius = Math.exp(i) * 2;
			int x = (int) (halfWidth - radius / 2);
			int y = (int) (halfHeight - radius / 2);
			graphics2d.drawOval(x, y, (int) radius, (int) radius);
		}

		for (int i = 0; i < angleBins; i++)
		{
			float endX = (float) (Math.exp(pBins) * Math.cos(i * 2f / angleBins * Math.PI));
			float endY = (float) (Math.exp(pBins) * Math.sin(i * 2f / angleBins * Math.PI));
			Point endPoint = new Point(endX,endY);
			graphics2d.drawLine((int) radius/2, (int) radius/2, (int) (endPoint.x + radius/2), (int) (endPoint.y + radius/2));
		}
		
		File file = new File(SampleConfig.OUTPUT_PATH +"coordinate.png");
		System.out.println(file.getPath());
		try
		{
			ImageIO.write(image, "PNG", file);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	

	/**
	 * ���ƶ�������������ϵ
	 * 
	 * @param graphics2d
	 * */
	public void drawCoordinateSystem(Graphics2D graphics2d)
	{
		graphics2d.setColor(new Color(255, 0, 0));

		// sourcePoint.print();
		for (int i = 1; i <= pBins; i++)
		{
			double radius = Math.exp(i) * 2;
			graphics2d.drawOval((int) (sourcePoint.x - radius / 2), (int) (sourcePoint.y - radius / 2), (int) radius, (int) radius);
		}

		for (int i = 0; i < angleBins; i++)
		{
			float endX = (float) (Math.exp(pBins) * Math.cos(i * 2f / angleBins * Math.PI));
			float endY = (float) (Math.exp(pBins) * Math.sin(i * 2f / angleBins * Math.PI));
			Point endPoint = new Point(endX,endY);
			endPoint = Geometry.getRotatePoint(endPoint, angle);
			
			graphics2d.setStroke(new BasicStroke(3));
			//graphics2d.setColor(new Color(i*30,i*30,i*30));
			graphics2d.drawLine((int) sourcePoint.x, (int) sourcePoint.y, (int) (endPoint.x + sourcePoint.x), (int) (endPoint.y + sourcePoint.y));
		}
	}

	/**
	 * �����پ���
	 * */
	public int getDistanceL1(ShapeConext shapeConext)
	{
		int distance = 0;

		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				distance += Math.abs(shapeConext.statistics[i][j] - statistics[i][j]);
			}
		}
		return distance;
	}

	/**
	 * ŷ������
	 * */
	public float getDistanceL2(ShapeConext shapeConext)
	{
		float distance = 0;
		int offset = 0;
		// if (isAngleClose(shapeConext))
		// {
		// if (averageAngle > shapeConext.averageAngle)
		// {
		// offset = -1;
		// }
		// else if (averageAngle + 360-shapeConext.averageAngle<30)
		// {
		// offset = -1;
		// }
		// else
		// {
		// offset = 1;
		// }
		// }
		//

		for (int i = 0; i < statistics.length; i++)
		{
			for (int j = 0; j < statistics[i].length; j++)
			{
				distance += Math.pow(shapeConext.statistics[i][j] - statistics[i][j], 2);
			}
		}
		return (float) Math.sqrt(distance);
	}

	public boolean isAngleClose(ShapeConext shapeConext)
	{
		float diff = Math.abs(shapeConext.averageAngle - averageAngle);
		diff = diff > 180 ? 360 - diff : diff;
		return diff < 30;
	}

	public float getAngleDistance(ShapeConext shapeConext)
	{
		float diff = Math.abs(shapeConext.averageAngle - averageAngle);
		diff = diff > 180 ? 360 - diff : diff;
		diff/=180;
		return diff*diff;
	}
}
