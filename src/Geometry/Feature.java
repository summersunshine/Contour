package Geometry;

import java.util.Vector;

import javax.security.auth.kerberos.KerberosKey;

public class Feature
{
	
	//��ǰstroke��ĵ�a���ʴ�
	public int a;
	
	//����stroke�ĵ�b������
	public int b;
	
	//��ǰ��֮ǰ��shapeConext
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
