package stroke;

import geometry.Point;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import sample.LibSample;
import util.ImageUtil;
import config.SampleConfig;

public class LibStroke extends Stroke
{
	// 数据中每一行的数据量
	public static final int digitNums = 12;

	// 源图像的款
	public int width;
	// 源图像的高
	public int height;

	public BufferedImage sourceImage;

	public BufferedImage alphaImage;

	public BufferedImage tightImage;

	// 库中的第index个笔触
	public int index;

	// 输出的文本路径
	private String txtPathString;

	// 输出的图像路径
	private String imgPathString;

	// 图像掩码路径
	private String tightMaskString;

	private String alphaMaskString;

	public Vector<LibSample> libSamples;

	public LibStroke(String name, int index)
	{
		super();
		this.index = index;
		this.txtPathString = SampleConfig.getStrokeTxtPath(name, index);
		this.imgPathString = SampleConfig.getStrokeImagePath(name, index);
		this.tightMaskString = SampleConfig.getStrokeTightMaskPath(name, index);
		this.alphaMaskString = SampleConfig.getStrokeAlphaMaskPath(name, index);
		this.readImage();
		this.readFile();
		this.calDirAngle();
		this.setAverageR();
		// LibStrokeUtil.createStrokeSampleImage(this);
		this.initLibSample();

	}

	/**
	 * 初始化库中样例
	 * */
	private void initLibSample()
	{
		this.libSamples = new Vector<LibSample>();

		for (int i = 0; i < points.size(); i++)
		{
			libSamples.add(new LibSample(points, dirAngle.get(i), averageR, index, i));
		}
	}

	/**
	 * 读图像
	 * */
	public void readImage()
	{
		sourceImage = ImageUtil.getImage(imgPathString);
		width = sourceImage.getWidth();
		height = sourceImage.getHeight();
		tightImage = ImageUtil.getImage(tightMaskString);
		alphaImage = ImageUtil.getImage(alphaMaskString);
	}

	/**
	 * 读坐标文件
	 * */
	public void readFile()
	{
		File file = new File(txtPathString);
		if (!file.exists())
		{
			return;
		}

		FileReader fileReader = null;
		BufferedReader bufferedReader;

		try
		{
			fileReader = new FileReader(txtPathString);
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bufferedReader = new BufferedReader(fileReader);
		try
		{
			String line = bufferedReader.readLine();
			Integer.parseInt(line);

			line = bufferedReader.readLine();
			while (line != null)
			{
				addPosition(line);
				line = bufferedReader.readLine();
			}

		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 依据每一行的数据添加位置
	 * 
	 * @param line
	 * */
	private void addPosition(String line)
	{
		String[] numsStrings = line.split(" ");

		setPoints(getFloats(numsStrings));
	}

	/**
	 * 将读取的string转换成浮点数
	 * 
	 * @param strings
	 * */
	private float[] getFloats(String[] strings)
	{
		float[] floats = new float[digitNums];
		for (int i = 0, count = 0; i < strings.length; i++)
		{
			if (!strings[i].equals(""))
			{
				floats[count++] = Float.parseFloat(strings[i]);
			}
		}
		return floats;
	}

	/**
	 * 设置点
	 * 
	 * @param floats
	 * */
	private void setPoints(float[] floats)
	{

		rightContourPoints.add(new Point(floats[6] * width, floats[7] * height));
		points.add(new Point(floats[8] * width, floats[9] * height));
		leftContourPoints.add(new Point(floats[10] * width, floats[11] * height));

	}

}
