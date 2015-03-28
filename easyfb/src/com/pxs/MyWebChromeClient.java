package com.pxs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

public class MyWebChromeClient extends WebChromeClient {

	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			final JsResult result) {
		//need to get current  context
		final AlertDialog.Builder builder = new AlertDialog.Builder(ViewManager
				.getManager().getContext());
		builder.setTitle("tip").setMessage(message)
				.setPositiveButton("ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				});
		builder.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
				return true;
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				result.cancel();
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		return true;
	}
	
	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			final JsResult result) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(ViewManager
				.getManager().getContext());
		builder.setTitle("tip").setMessage(message)
				.setPositiveButton("ok", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						result.confirm();
					}
				}).setNeutralButton("cancel", new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						result.cancel();
					}
				});

		builder.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				result.cancel();
			}
		});

		builder.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				Log.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
				return true;
			}
		});
		AlertDialog dialog = builder.create();
		dialog.show();
		return true;
	}

	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, final JsPromptResult result) {
		final AlertDialog.Builder builder = new AlertDialog.Builder(ViewManager
				.getManager().getContext());
		builder.setTitle("tip").setMessage(message);
		final EditText et = new EditText(builder.getContext());
		et.setSingleLine();
		et.setText(defaultValue);
		builder.setView(et);
		builder.setPositiveButton("ok", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				result.confirm(et.getText().toString());
			}
		}).setNeutralButton("cancel", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				result.cancel();
			}
		});

		et.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				Log.v("onJsPrompt", "keyCode==" + keyCode + "event=" + event);
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					result.cancel();
				}
				return false;
			}

		});

		AlertDialog dialog = builder.create();
		dialog.show();
		return true;
	}
}