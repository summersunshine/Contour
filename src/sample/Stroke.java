package sample;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import Geometry.Point;
import Geometry.Position;

public class Stroke
{
	public Vector<Point> points;
	public Vector<Point> rightCountourPoints;
	public Vector<Point> leftCountourPoints;
	
	public Stroke()
	{
		this.points = new Vector<Point>();
		this.rightCountourPoints = new Vector<Point>();
		this.leftCountourPoints = new Vector<Point>();
		
	}
}
