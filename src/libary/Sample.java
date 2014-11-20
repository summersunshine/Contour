package libary;

import java.awt.Graphics2D;

import sample.Stroke;
import config.SampleConfig;

public class Sample
{

	Stroke stroke;
	
	public Sample()
	{
//		String strokePath = SampleConfig.getStrokeTxtPath("charcoal1", 0);
//		stroke = new Stroke(strokePath);
		
	}
	
	public void drawSample(Graphics2D graphics2d,int index)
	{
		//System.out.println("drawSample");
		
//		String strokePath = SampleConfig.getStrokeTxtPath("charcoal1", index);
//		
//		stroke = new Stroke(strokePath);
//		
//		stroke.drawStroke(graphics2d);
	}
	
	public static void read()
	{
	
		for (int i = 0; i <= 15; i++)
		{
			String strokePath = SampleConfig.getStrokeTxtPath("charcoal1", i);
			
		}
	}
}
