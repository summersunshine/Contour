package sample;

import java.io.File;
import java.util.Vector;

import javax.security.auth.kerberos.KerberosKey;

import config.SampleConfig;
import Geometry.Point;

public class Feature
{
	
	//当前stroke库的第a个笔触
	public int a;
	
	//属于stroke的第b个样例
	public int b;
	
	//当前点之前的shapeConext
	public ShapeConext historyShapeConext;
	
	//当前点之后的shapecontext
	public ShapeConext futureShapeConext;
	
	public Vector<Feature> features;
	
	public Feature(Vector<Point> points,int a,int b)
	{
		this.a = a;
		this.b = b;
		this.historyShapeConext = new ShapeConext(points, b,-1);
		this.futureShapeConext = new ShapeConext(points, b, 1);
		
		if (a==-1 || SampleConfig.isLoadBegin)
		{
			this.createShapeConextImage();
		}
		
		
	}
	
	

	
	public float getDistance(Feature otherFeature)
	{
		float historyDistance = this.historyShapeConext.getDistanceL2(otherFeature.historyShapeConext);
		float futureDistance = this.futureShapeConext.getDistanceL2(otherFeature.futureShapeConext);
		
		return (float) Math.sqrt(historyDistance*historyDistance + futureDistance*futureDistance);
	}
	
	
	public void createShapeConextImage()
	{
		File file = new File(SampleConfig.OUTPUT_PATH + a + "\\");
		if (!file.exists())
		{
			file.mkdirs();
		}
		
		historyShapeConext.createHistogramImage(SampleConfig.OUTPUT_PATH + a + "\\" + b + "_histroy.jpg");
		futureShapeConext.createHistogramImage(SampleConfig.OUTPUT_PATH + a + "\\" + b + "_future.jpg");
	}
}
