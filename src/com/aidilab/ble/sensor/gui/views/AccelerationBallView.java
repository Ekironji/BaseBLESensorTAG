package com.aidilab.ble.sensor.gui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class AccelerationBallView extends View{

	// View sets
	int ballColor = Color.RED;
	int bkgColor = Color.BLACK;
	int gridColor = Color.GRAY;
	
	// View styles
	Paint ballPaint = new Paint();
	Paint gridPaint = new Paint();
	
	// View sizes
	int height = 100, width = 100;	
	
	int startingBallDiameter = 100;
	int maxBallDiameter = 200;
	int minBallDiameter = 50;
	
	float maxValueRange = 30;	

	float x, y, ballSize;
	
	public AccelerationBallView(Context context, AttributeSet attrs) {
		super(context, attrs);	
		
		ballPaint.setColor(ballColor);
		gridPaint.setColor(gridColor);
		gridPaint.setStrokeWidth(1);
		
		try {
			width  = getWidth();
			height = getHeight();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);		

		width  = getWidth();
		height = getHeight();
			
		canvas.drawColor(bkgColor);
		
		canvas.drawCircle(x, y, ballSize, ballPaint);
		canvas.drawLine( width/2, width/2 , 0       , height  , gridPaint);
		canvas.drawLine( 0      , width   , height/2, height/2, gridPaint);
				
	}
		
	
	public void setMaxRange(float max){
		maxValueRange = max;
	}
	
	public void updateView(float x, float y, float z){
		this.x = map(x, -maxValueRange, maxValueRange, 0, width  );
		this.y = map(y, -maxValueRange, maxValueRange, 0, height );
		this.ballSize = map(z, -maxValueRange, maxValueRange, minBallDiameter, maxBallDiameter );
		
		postInvalidate();		
	}
	
	
	float map(float x, float in_min, float in_max, float out_min, float out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}

}
