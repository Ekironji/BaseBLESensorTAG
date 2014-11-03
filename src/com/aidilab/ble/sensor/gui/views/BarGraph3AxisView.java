package com.aidilab.ble.sensor.gui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class BarGraph3AxisView extends View {

	private int x = 0;
	private int y = 1;
	private int z = 2;
	
	int MAX = 30;
	int height, width;
	float[] values    = {0,0,0};
	int[]   axisColor = {0,0,0};
	
    Paint paint = new Paint();
	
	public BarGraph3AxisView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	/**
	 * Usage:
	 * mBarGraph.setRange(MAX);
	 * mBarGraph.setBarColors(Color.RED, Color.GREEN, Color.BLUE );
	 * 
	 * at runtime
	 * mBarGraph.setData(x,y,z);
	 * 
	 */
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		height = getHeight();
		width  = getWidth();
		
        paint.setStrokeWidth(0);
		
		for (int i = 0; i < 3; i++) {
			values[i] = Math.abs(values[i]);
			values[i] = map(values[i], 0, MAX, 0, width);
			paint.setColor(axisColor[i]);
			canvas.drawRect(0, (height / 3) * i, values[i], (height / 3) * (i + 1), paint);
		}
		
	}
	
	public void setRange(int max){
		MAX = max;
	}
	
	public void setBarColors(int xBar, int yBar, int zBar){
		axisColor[x] = xBar;
		axisColor[y] = yBar;
		axisColor[z] = zBar;
	}
	
	public void setData(float x, float y, float z){
		values[this.x] = x;
		values[this.y] = y;
		values[this.z] = z;
		postInvalidate();
	}
	
	public void setData(double x, double y, double z){
		values[this.x] = (float) x;
		values[this.y] = (float) y;
		values[this.z] = (float) z;
		postInvalidate();
	}
	
	float map(float x, float in_min, float in_max, float out_min, float out_max) {
		return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
	}
}
