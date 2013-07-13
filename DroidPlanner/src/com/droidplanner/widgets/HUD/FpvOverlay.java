package com.droidplanner.widgets.HUD;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import com.droidplanner.widgets.FPV.MjpegInputStream;

public class FpvOverlay {
	
	public enum DisplayMode {
		SIZE_STANDARD,SIZE_BEST_FIT,SIZE_FULLSCREEN
	}
	
	public DisplayMode displayMode;
	public MjpegInputStream mIn;
	public Bitmap bm;

	public FpvOverlay(DisplayMode displayMode) {
		this.displayMode = displayMode;
	}

	public void setSource(MjpegInputStream source) {
		Log.d("FPV", "setSource");
		mIn = source;		
	}

	void drawFPV(Canvas canvas, int canvasWidth, int canvasHeight) {
		try {
			if (mIn != null) {
				bm = mIn.readMjpegFrame();
	            Rect destRect = destRect(bm, canvasWidth,canvasHeight);
				canvas.drawBitmap(bm, null, destRect, null);
				bm.recycle();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Rect destRect(Bitmap picture, int canvasWidth, int canvasHeight) {
    	int bmw = picture.getWidth();
		int bmh = picture.getHeight();
		
		switch (displayMode) {
		default:
		case SIZE_STANDARD:
			return new Rect(-(bmw / 2), -(bmh / 2),(bmw / 2), (bmh / 2));
		case SIZE_BEST_FIT:
			float bmasp = (float) bmw / (float) bmh;
			bmw = canvasWidth;
			bmh = (int) (canvasWidth / bmasp);
			if (bmh > canvasHeight) {
				bmh = canvasHeight;
				bmw = (int) (canvasHeight * bmasp);
			}
			return new Rect(-(bmw / 2), -(bmh / 2),(bmw / 2), (bmh / 2));
		case SIZE_FULLSCREEN:
			return new Rect(-canvasWidth/2, -canvasHeight/2, canvasWidth/2, canvasHeight/2);
		}
	}
}
