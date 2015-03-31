package com.pxs;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;

/**
 *  view manager
 * 
 */
public class ViewManager {
	private static  Map<String, ViewEntity> viewList;
	private Context context;
	public static ViewManager manager =null;
	private boolean backFlag = false; // avoid backButton double clicked
	private int menuLevel =0;//record deep level

	public synchronized  boolean isBackFlag() {
		return backFlag;
	}

	public void setBackFlag(boolean backFlag) {
			synchronized(this){
			this.backFlag = backFlag;
			}
	}

	private ViewManager() {
		viewList = new ConcurrentHashMap<String, ViewEntity>();
	}

	public static ViewManager getManager() {
		if(manager == null){
			manager = new ViewManager();
		}
		return manager;
	}

	public void setContext(Context context) {
		synchronized(this){
			this.context = context;
		}
	}

	public synchronized Context getContext() {
		return context;
	}

	public void putView(String key, ViewEntity view) {
		if ("file:///android_asset/index.html".equals(key)) {
			view.setMenuLevel(0);
		} else if (null == viewList.get(key)) {
			view.setMenuLevel(menuLevel + 1);
		} else {
			view.setMenuLevel(viewList.get(key).getMenuLevel());
		}
		menuLevel = view.getMenuLevel();
		clearView(key);
		viewList.put(key, view);
	}

	public ViewEntity getValue(String key) {
		if(null !=viewList.get(key)){
		menuLevel = viewList.get(key).getMenuLevel();
		clearView(key);
		}
		return viewList.get(key);
	}
	
	/** clear useless view ,when we goback
	 * @param key
	 */
	private void clearView(String key){
		Iterator<String> it = viewList.keySet().iterator(); 
		while (it.hasNext()){
			String str = it.next();
			if (!str.equals(key)
					&& viewList.get(str) != null && (viewList.get(str).getMenuLevel() >= menuLevel)) {
				it.remove();
			}
		} 
	}
	
	public void clear() {
		viewList.clear();
	}
	
	public int getMenuLevel() {
		return menuLevel;
	}

	public void setMenuLevel(int menuLevel) {
		this.menuLevel = menuLevel;
	}
}
