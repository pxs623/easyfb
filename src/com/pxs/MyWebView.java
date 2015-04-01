package com.pxs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyWebView extends WebView {

	public static final String TAG = "MyWebView";

	/** Activities and other important classes **/
	private MyInterface iface;

	private MyWebViewClient viewClient = null;
	private MyWebChromeClient webChromeClient = null;

	// The URL passed to loadUrl(), not necessarily the URL of the current page.
	String loadedUrl;

	public MyWebView(Context context) {
		this(context, null);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void init() {
		Context context = ViewManager.getManager().getContext();
		this.iface = (MyInterface) context;
		webChromeClient = new MyWebChromeClient();
		viewClient = new MyWebViewClient();
		WebSettings webSet = this.getSettings();
		this.setInitialScale(0);
		//this.setVerticalScrollBarEnabled(true);
		this.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
		webSet.setJavaScriptEnabled(true);
		webSet.setJavaScriptCanOpenWindowsAutomatically(true);
		webSet.setLayoutAlgorithm(LayoutAlgorithm.NORMAL);
		webSet.setUseWideViewPort(true);
		webSet.setLoadWithOverviewMode(true);
		webSet.setAppCacheEnabled(true);
		webSet.setAppCacheMaxSize(1024 * 1024 * 8);
		webSet.setCacheMode(WebSettings.LOAD_DEFAULT);
		String cacheDirPath = context.getCacheDir().getAbsolutePath();
		webSet.setAppCachePath(cacheDirPath);
		webSet.setAllowFileAccess(true);
		// webSet.setAllowContentAccess(true);
		webSet.setDomStorageEnabled(true);
		webSet.setGeolocationEnabled(true);
		webSet.setDatabaseEnabled(true);
		webSet.setDatabasePath(context.getFilesDir().getParentFile().getPath()
				+ "/databases/");
		this.addJavascriptInterface(new JavaScriptInterface(), "android");
		this.addJavascriptInterface(new LocalStorageJavaScriptInterface(),
				"LocalStorage");
		this.setWebChromeClient(webChromeClient);
		this.setWebViewClient(viewClient);
	}

	
	/**
	 * Go to previous page in history. (We manage our own history)
	 * 
	 * @return true if we went back, false if we are already at top
	 */
	public boolean backHistory() {
		// Check webview first to see if there is a history
		// This is needed to support curPage#diffLink, since they are added to
		// appView's history, but not our history url array (JQMobile behavior)
		if (super.canGoBack()) {
			super.goBack();
			return true;
		}
		return false;
	}


	public MyInterface getIface() {
		return iface;
	}  

	public void setIface(MyInterface iface) {
		this.iface = iface;
	}
}
