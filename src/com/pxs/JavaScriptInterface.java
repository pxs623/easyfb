package com.pxs;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.pxs.util.Datepicker;
import com.pxs.util.LOG;
import com.pxs.util.PluginInterface;
import com.pxs.util.Selecter;

/**
 * js-android bridge
 * 
 */
public class JavaScriptInterface {
	public static final String TAG = "JavaScriptInterface";
	private ProgressDialog spinnerDialog;
	private MyInterface iface = null;

	public JavaScriptInterface() {

	}

	JavaScriptInterface(MyInterface myInterface) {
		this.iface = myInterface;
	}

	@JavascriptInterface
	public void action(final String message) {
		iface = (MyInterface) ViewManager.getManager().getContext();
		iface.getActivity().runOnUiThread(new Runnable() {
			public void run() {
				LOG.i(TAG, message);
				String temp = "";
				String[] msgs = message.split(":", 2);
				temp = msgs[0];
				if (temp.equals("hide")) {
					if (spinnerDialog != null && spinnerDialog.isShowing()) {
						spinnerDialog.dismiss();
						spinnerDialog = null;
					}
					return;
				} else if (temp.equals("show")) {
					spinnerDialog = new ProgressDialog(iface.getActivity());
					spinnerDialog.requestWindowFeature(Window.FEATURE_PROGRESS);
					spinnerDialog.setCanceledOnTouchOutside(false);
					spinnerDialog.setMessage("loading...");
					spinnerDialog.show();
					return;
				} else if (temp.equals("forward")) {
					String json = msgs[1];
					JSONObject jsonobj;
					try {
						jsonobj = new JSONObject(json);
						String url = jsonobj.getString("url");
						if (!url.isEmpty()) {
							url = "file:///android_asset/" + jsonobj.getString("url");
							Intent intent = new Intent(iface.getActivity(), MainActivity.class);
							intent.putExtra("url", url);
							intent.putExtra("title", jsonobj.getString("title"));
							intent.putExtra("needReload", true);
							iface.getActivity().startActivity(intent);
							iface.getActivity().finish();
						}

					} catch (JSONException e) {
						LOG.e(TAG, "jsonparse error", e);
					}
				} else if ("back".equals(temp)) {//
					String json = msgs[1];
					JSONObject jsonobj;
					try {
						jsonobj = new JSONObject(json);
						String url = jsonobj.getString("url");
						if (!url.isEmpty()) {
							url = "file:///android_asset/" + jsonobj.getString("url");
							boolean needReload = jsonobj.getBoolean("needReload");
							boolean isBackFlag = jsonobj.getBoolean("isBackFlag");
							Intent intent = new Intent(iface.getActivity(), MainActivity.class);
							intent.putExtra("url", url);
							intent.putExtra("needReload", needReload);
							intent.putExtra("isBackFlag", isBackFlag);
							if (ViewManager.getManager().isBackFlag() || isBackFlag) {
								iface.getActivity().startActivity(intent);
								iface.getActivity().finish();
								ViewManager.getManager().setBackFlag(false);
							}
						}
					} catch (JSONException e) {
						LOG.e(TAG, "jsonparse error", e);
					}
				} else if (temp.equals("toast")) {
					Toast.makeText(iface.getActivity(), msgs[1], Toast.LENGTH_LONG).show();
					return;
				} 
			}
		});
	}

	/**
	 * @param type
	 * @param message
	 */
	@JavascriptInterface
	public void callPlugin(final String type, String message) {
		iface = (MyInterface) ViewManager.getManager().getContext();
		PluginInterface pi;
		try {
			JSONObject jsonobj = new JSONObject(message);
			if ("datePicker".equals(type)) {
				String id = jsonobj.getString("id");
				pi = new Datepicker(id, iface);
				pi.show(jsonobj);
			}
			if ("selecter".equals(type)) {
				String id = jsonobj.getString("id");
				pi = new Selecter(id, iface);
				pi.show(jsonobj);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
