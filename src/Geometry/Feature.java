package Geometry;

import java.util.Vector;

import javax.security.auth.kerberos.KerberosKey;

public class Feature
{
	
	//当前stroke库的第a个笔触
	public int a;
	
	//属于stroke的第b个样例
	public int b;
	
	//当前点之前的shapeConext
	public ShapeConext historyShapeConext;
	public ShapeConext futureShapeConext;
	
	public Vector<Feature> features;
	
	public Feature(Vector<Point> points,int index)
	{
		this.a = index;
		this.historyShapeConext = new ShapeConext(points, index,-1);
		this.futureShapeConext = new ShapeConext(points, index, 1);
	}
	
	

	
	public float getDistance(Feature otherFeature)
	{
		float historyDistance = this.historyShapeConext.getDistanceL2(otherFeature.historyShapeConext);
		float futureDistance = this.futureShapeConext.getDistanceL2(otherFeature.futureShapeConext);
		
		return (float) Math.sqrt(historyDistance*historyDistance + futureDistance*futureDistance);
	}
}
