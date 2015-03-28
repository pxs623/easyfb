/*
       Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
*/
package com.pxs.util;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

import com.pxs.MainActivity;
import com.pxs.ViewManager;

/**
 * This class is used to detect when the soft keyboard is shown and hidden in the web view.
 */
public class LinearLayoutSoftKeyboardDetect extends LinearLayout {

    private static final String TAG = "SoftKeyboardDetect";

    private int oldHeight = 0;  // Need to save the old height as not to send redundant events
    private int oldWidth = 0; // Need to save old width for orientation change
    private int screenWidth = 0;
    private int screenHeight = 0;
	private MainActivity app = null;

    @Override
	protected void onFinishInflate() {
		// TODO Auto-generated method stub
		super.onFinishInflate();
	}

	public LinearLayoutSoftKeyboardDetect(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public LinearLayoutSoftKeyboardDetect(Context context, int width, int height) {
        super(context);
        screenWidth = width;
        screenHeight = height;
        app = (MainActivity) context;
    }

    @Override
    /**
     * Start listening to new measurement events.  Fire events when the height
     * gets smaller fire a show keyboard event and when height gets bigger fire
     * a hide keyboard event.
     *
     * Note: We are using app.postMessage so that this is more compatible with the API
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		app = (MainActivity) ViewManager.getManager().getContext();
        LOG.d(TAG, "We are in our onMeasure method");
        DisplayMetrics dm = new DisplayMetrics();
        app.getWindowManager().getDefaultDisplay().getMetrics(dm);
        final float density = dm.density;
        // Get the current height of the visible part of the screen.
        // This height will not included the status bar.\
        int width, height;

        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        LOG.d(TAG, "Old Height = %d", oldHeight);
        LOG.d(TAG, "Height = %d", height);
        LOG.d(TAG, "Old Width = %d", oldWidth);
        LOG.d(TAG, "Width = %d", width);
        int pixelHeightDiff = (int)((height - oldHeight) / density);

        // If the oldHeight = 0 then this is the first measure event as the app starts up.
        // If oldHeight == height then we got a measurement change that doesn't affect us.
        if (oldHeight == 0 || oldHeight == height) {
            LOG.d(TAG, "Ignore this event");
        }
        // Account for orientation change and ignore this event/Fire orientation change
        else if (screenHeight == width)
        {
            int tmp_var = screenHeight;
            screenHeight = screenWidth;
            screenWidth = tmp_var;
            LOG.d(TAG, "Orientation Change");
        }
        // If the height as gotten bigger then we will assume the soft keyboard has
        // gone away.
        else if(pixelHeightDiff>100) {
            if (app != null)
            	app.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						//app.getWebview().loadUrl("javascript:fixFooter()");
					}
				});
        }
        // If the height as gotten smaller then we will assume the soft keyboard has 
        // been displayed.
		else if (height < oldHeight) {
			if (app != null)
				app.runOnUiThread(new Runnable() {

					@Override
					public void run() {
					//app.getWebview().loadUrl("javascript:$('.footer').css('position','relative');");
					}
				});
		}

        // Update the old height for the next event
        oldHeight = height;
        oldWidth = width;
    }

}
