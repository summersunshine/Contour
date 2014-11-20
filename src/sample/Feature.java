package sample;

import java.io.File;
import java.util.Vector;

import javax.security.auth.kerberos.KerberosKey;

import config.SampleConfig;
import Geometry.Point;

public class Feature
{
	
	//��ǰstroke��ĵ�a���ʴ�
	public int a;
	
	//����stroke�ĵ�b������
	public int b;
	
	//��ǰ��֮ǰ��shapeConext
	public ShapeConext historyShapeConext;
	
	//��ǰ��֮���shapecontext
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
