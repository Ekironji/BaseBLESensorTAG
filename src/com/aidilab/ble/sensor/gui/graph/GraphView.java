package com.aidilab.ble.sensor.gui.graph;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

public class GraphView extends View{
	
	int MAX_SIZE;
	Paint linePaint = new Paint();
	
	int height, width;	
	DataPath d1;
	
	ArrayList<Float> datas = new ArrayList<Float>();
	ArrayList<Point> pts = new ArrayList<Point>();
	
	public GraphView(Context context) {
		super(context);	

		d1 = new DataPath(Color.RED, 100, 500, 500);
		linePaint.setColor(d1.getColor());
		
		for(int i=0; i<10; i++){
			pts.add(new Point(0,0));
		}
	}
	
	public GraphView(Context context, AttributeSet attrs) {
		super(context, attrs);	
		
		d1 = new DataPath(Color.RED, 100, 500, 500);
		linePaint.setColor(d1.getColor());
		linePaint.setStrokeWidth(3);		
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);		

		width  = getWidth();
		height = width / 2;
		MAX_SIZE = 100;
		
		datas = d1.getData();
		floatDatasToPoints();		
		
		for(int i=0; i<pts.size()-1; i++){
			canvas.drawLine(pts.get(i).x, pts.get(i).y, pts.get(i+1).x, pts.get(i+1).y, linePaint);			
		}		
	}
	

	public void addData(float data){
		d1.addData(data);
		invalidate();
	}
	
	
	public float getMin(){
		return d1.getMin();
	}
	
	public float getMax(){
		return d1.getMax();
	}
	
	public float getLocalMin(){
		return d1.getLocalMin();
	}
	
	public float getLocalMax(){
		return d1.getLocalMax();
	}
		
	public DataPath getDataPath(){
		return d1;
	}
	
	private ArrayList<Point> floatDatasToPoints(){		
		// Svuoto il vettore dei punti
		pts.clear();
		int x, y;
		
		for(int i=0; i<datas.size(); i++){
			x = (width / datas.size()) * i;
			y = (int)((height / (d1.getMax() - d1.getMin())) * datas.get(i));
			pts.add(new Point(x,y));
		}

		return pts;
	}
	

}
