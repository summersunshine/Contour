package geometry;

import java.awt.Color;
import java.awt.Graphics2D;

public class Position
{
	public Point	spinePoint;

	public Point	rightPoint;

	public Point	leftPoint;

	public Position(Point spinePoint, Point rightPoint, Point leftPoint)
	{
		this.spinePoint = spinePoint;
		this.rightPoint = rightPoint;
		this.leftPoint = leftPoint;
	}

	public void drawPosition(Graphics2D graphics2d)
	{
		// System.out.println("Position.drawPosition()");
		spinePoint.drawPoint(graphics2d, Color.RED);
		rightPoint.drawPoint(graphics2d, Color.BLUE);
		leftPoint.drawPoint(graphics2d, Color.GREEN);
	}

}
