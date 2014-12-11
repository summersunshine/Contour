package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import stroke.LibStroke;
import config.SampleConfig;

public class LibStrokeUtil
{

	/**
	 * »­±Ê´¥µÄ²ÉÑùÍ¼Ïñ
	 * */
	public static void createStrokeSampleImage(LibStroke libStroke)
	{
		System.out.println("Stroke.drawStroke()");
		BufferedImage image = new BufferedImage(libStroke.width, libStroke.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = (Graphics2D) image.getGraphics();
		for (int i = 0; i < libStroke.points.size(); i++)
		{
			libStroke.points.get(i).drawPoint(graphics2d, Color.RED);
			libStroke.rightContourPoints.get(i).drawPoint(graphics2d, Color.GREEN);
			libStroke.leftContourPoints.get(i).drawPoint(graphics2d, Color.BLUE);
		}

		String path = SampleConfig.OUTPUT_PATH + libStroke.index + "\\" + "sample.jpg";

		ImageUtil.saveImage(image, path);

	}

}
