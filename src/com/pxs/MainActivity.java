package com.pxs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pxs.util.ACache;
import com.pxs.util.LinearLayoutSoftKeyboardDetect;

/**
 * @author pxs_623
 *
 */
@SuppressLint("NewApi")
public class MainActivity extends Activity implements MyInterface {
	private static final String TAG = MainActivity.class.getSimpleName();
	protected Dialog splashDialog;
	private MyWebView webview;
	private String launchUrl = "";
	private String title;
	private boolean needReload = true;
	private boolean isBackFlag = false;
	public static Bundle bundle;
	
	private ViewEntity ve;
	/**
	 *  disk cache
	 *  if memory has been cleared, get info from disk,so we can always load the history page 
	 */
	private ACache aCache;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		aCache = ACache.get(this);
		ViewManager.getManager().setContext(this);

		LinearLayout layout = (LinearLayoutSoftKeyboardDetect) getLayoutInflater()
				.inflate(R.layout.activity_main, null);
		LinearLayout relative = (LinearLayout) layout.findViewById(R.id.head);
		Intent i = getIntent();
		bundle = i.getExtras();
		if (bundle != null) {
			launchUrl = bundle.getString("url", "file:///android_asset/index.html");
			title = i.getStringExtra("title");
			needReload = i.getBooleanExtra("needReload", true);
			isBackFlag = i.getBooleanExtra("isBackFlag", false);
		} else {
			launchUrl = "file:///android_asset/index.html";
		}

		if (needReload && !isBackFlag) {// foward
			overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
			forward(layout, relative);
		} else if (needReload && isBackFlag) {// back and reload
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			ve = ViewManager.getManager().getValue(launchUrl);
			if (ve != null) {
				layout = ve.getLayout();
				((ViewGroup) layout.getParent()).removeAllViews();
				webview = (MyWebView) layout.getChildAt(1);
				layout.removeViewAt(1);
				webview.init();
				webview.clearView();// should clear view first
				webview.loadUrl(launchUrl);
				layout.addView(webview, 1);
				ViewEntity ve1 = new ViewEntity();
				ve1.setLayout(layout);
				ViewManager.getManager().putView(launchUrl, ve1);
			} else {// 加载历史页面失败时的处理
				viewBean vb = (viewBean) aCache.getAsObject(launchUrl);
				title = vb.getTitle();
				needReload = true;
				isBackFlag = false;
				forward(layout, relative);
			}
		} else {// back
			overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out);
			ve = ViewManager.getManager().getValue(launchUrl);
			if (ve != null) {
				layout = ve.getLayout();
				((ViewGroup) layout.getParent()).removeAllViews();
				webview = (MyWebView) layout.getChildAt(1);
				layout.removeViewAt(1);
				webview.init();
				layout.addView(webview, 1);
			} else {// 取不到memory cache时，取disk cache
					viewBean vb = (viewBean) aCache.getAsObject(launchUrl);
					System.out.println(vb);
					title = vb.getTitle();
					needReload = true;
					isBackFlag = false;
					forward(layout, relative);
			}
		}
		setContentView(layout);
	}

	/**
	 * 前进，加载页面
	 * 
	 * @param layout
	 * @param relative
	 */
	private void forward(LinearLayout layout, LinearLayout relative) {
		webview = (MyWebView) layout.findViewById(R.id.wv_web);
		webview.init();
		ImageButton back = (ImageButton) relative.findViewById(R.id.titleback);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (ViewManager.getManager().isBackFlag()) {
					return;
				}
				ViewManager.getManager().setBackFlag(true);
				webview.loadUrl("javascript:backs()");

			}
		});
		TextView tv =(TextView)relative.findViewById(R.id.title);
		tv.setText(title);
		webview.loadUrl(launchUrl);
		// cache current view
		ve = new ViewEntity();
		ve.setLayout(layout);
		ViewManager.getManager().putView(launchUrl, ve);
		// disk cache
		viewBean vb = new viewBean();
		vb.setTitle(title);
		aCache.put(launchUrl, vb);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (ViewManager.getManager().isBackFlag()) {
				return true;
			} else {
				ViewManager.getManager().setBackFlag(true);
			}
			if ("file:///android_asset/index.html".equals(launchUrl)) {
				AlertDialog.Builder builder = new Builder(this);
				builder.setMessage("Are you sure?");
				builder.setTitle("quitʾ");
				builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ViewManager.getManager().clear();
						System.exit(0);
						finish();
					}
				});
				builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
				ViewManager.getManager().setBackFlag(false);
			} else {
				webview.loadUrl("javascript:"
						+ "if (typeof(backs) != 'undefined') {"
						+"backs();}else{"
						+ "backward('index.html',false,true);"
						+ "}");
				}
    
			}
		return super.onKeyDown(keyCode, event);
	}

	public MyWebView getWebview() {
		return webview;
	}

	public void setWebview(MyWebView webview) {
		this.webview = webview;
	}

	@Override
	public Activity getActivity() {
		return this;
	}
}
