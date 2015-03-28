package com.pxs;

import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MyWebViewClient extends WebViewClient {

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return true;
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		super.onPageStarted(view, url, favicon);
	}

	@Override
	public void onPageFinished(WebView view, String url) {
		ViewManager.getManager().setBackFlag(false);
		super.onPageFinished(view, url);
	}

	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
	}
	
	@Override
	public void onReceivedError(WebView view, int errorCode,
			String description, String failingUrl) {
		view.loadUrl("about:blank");
		Toast.makeText(view.getContext(), "load error" + description,
				Toast.LENGTH_LONG).show();
	}
}
