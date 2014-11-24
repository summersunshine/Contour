package geometry;

import java.awt.image.BufferedImage;
import java.util.Vector;

public class ContourImage
{
	
	public BufferedImage image;
	
	public ContourImage(Vector<Point> points,int width)
	{
		image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_ARGB);
		
		for (int i = 0; i < points.size(); i++)
		{
			updateImage((int)points.get(i).x,(int)points.get(i).y,width);
		}
	}
	

	/**
	 * ����Ӧ����ԴͼƬ�ϵ�Ч��������ԭͼƬ������£���������ΪԲ������
	 * 
	 * @param sourceImage
	 *            ԭͼƬ
	 * @param centerX
	 *            ���������ĵ�X����
	 * @param centerY
	 *            ���������ĵ�Y����
	 * @param radius
	 *            �����İ뾶
	 * */
	public void updateImage(int centerX, int centerY, int radius)
	{
		int startX = centerX - radius;
		int startY = centerY - radius;
		int endX = centerX + radius;
		int endY = centerY + radius;

		startX = startX < 0 ? 0 : startX;
		startY = startY < 0 ? 0 : startY;
		endX = endX > image.getWidth() ? image.getWidth() : endX;
		endY = endY > image.getHeight() ? image.getHeight() : endY;

		for (int y = startY; y < endY; y++)
		{
			for (int x = startX; x < endX; x++)
			{
				if (Geometry.getDistance(x, y, centerX, centerY) < radius)
				{
					image.setRGB(x, y, 0xfffffff);
				}
			}
		}
	}
	
	
	public void createCountourPoint()
	{
		for (int y = 0; y < image.getHeight(); y++)
		{
			boolean isInside = false;
			for (int x = 0; x < image.getWidth(); x++)
			{
				if (image.getRGB(x, y)==0xfffffff)
				{
					isInside = true;
					
				}
			}
		}

	}
}
