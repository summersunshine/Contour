package util;

import geometry.Point;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class WarpingImage 
{
	
	public static void drawImage(BufferedImage image,Vector<Point> points,Vector<Point> contourPoints,double [][] matrix,Graphics2D graphics2d)
	{
		graphics2d.setColor(new Color(255,255,255));
		for (int i = 0; i < points.size(); i++) {
			
			points.get(i).print();
			
			int m = matrix.length-3;
			double x = (int)points.get(i).x;
			double y = (int)points.get(i).y;
			double dx = matrix[m+0][0] + matrix[m+1][0]*x + matrix[m+2][0]*y;
			double dy = matrix[m+0][1] + matrix[m+1][1]*x + matrix[m+2][1]*y;
			
			System.out.print("dx: " + dx +  "dy: " + dy);
			
			for (int j = 0; j < m; j++) {
				double offsetX = (int)contourPoints.get(j).x - (int)points.get(i).x;
				double offsetY = (int)contourPoints.get(j).y - (int)points.get(i).y;
				//Point offsetPoint = contourPoints.get(j).sub(points.get(i));
				double d = baseFunc(offsetX*offsetX+offsetY*offsetY);
				dx += matrix[j][0]*d;
				dy += matrix[j][1]*d;
			}
			
			System.out.println( "  dx: " + dx +  "dy: " + dy);
			Point newPoint = points.get(i).add(new Point(dx,dy));
			
			graphics2d.setColor(new Color(image.getRGB((int)points.get(i).x, (int)points.get(i).y)));
			
			if (newPoint.x>=0 && newPoint.x<1280 && newPoint.y>=0 && newPoint.y<720) {
				graphics2d.drawRect((int)newPoint.x, (int)newPoint.y, 1, 1);
			}
			else
			{
				System.out.print("point out");
				newPoint.print();
			}
			
		}
		
	}
	
	
	  private static  double baseFunc(double r2)
	  {
	    // same as r*r * log(r), but for r^2:
	    return ( r2==0 )
	      ? 0.0 // function limit at 0
	      : r2 * Math.log(r2) * 0.217147241; // = 1/(2*log(10))
	  }
}
