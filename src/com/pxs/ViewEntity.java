package com.pxs;

import android.widget.LinearLayout;

public class ViewEntity {

	private LinearLayout layout;
	private int menuLevel=0;
	public LinearLayout getLayout() {
		return layout;
	}

	public void setLayout(LinearLayout layout) {
		this.layout = layout;
	}

	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}


}
