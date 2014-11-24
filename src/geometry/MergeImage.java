package geometry;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class MergeImage
{
	public static BufferedImage  getImage(Vector<BufferedImage> partImages)
	{
		BufferedImage image = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
		
		for (int i = 0; i < partImages.size(); i++)
		{
			for (int y = 0; y < 720; y++)
			{
				for (int x = 0; x < 1280; x++)
				{
					
					image.setRGB(x, y, image.getRGB(x, y) + partImages.get(i).getRGB(x, y));
					
//					int rgb1 = image.getRGB(x, y);
//					int rgb2 = partImages.get(i).getRGB(x, y);
//					Color color1 = new Color(rgb1);
//					Color color2 = new Color(rgb2);
//					
//					if (color1.getRed() != color2.getRed())
//					{
//						
//						Color color = new Color(color1.getRed()/2+color2.getRed()/2, 
//								color1.getGreen()/2+color2.getGreen()/2, 
//								color1.getBlue()/2+color2.getBlue()/2);
//						
//						image.setRGB(x, y, color.getRGB());
//					}
//					else
//					{
//						image.setRGB(x, y, -1);
//					}
				}
			}
		}
		return image;
		
	}
}
