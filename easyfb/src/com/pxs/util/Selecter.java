package com.pxs.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pxs.MainActivity;
import com.pxs.MyInterface;
import com.pxs.MyWebView;

/**
 * select控件
 * 
 * @author peng_xuesong
 * 
 */
public class Selecter implements PluginInterface {
	private static final String TAG = Selecter.class.getSimpleName();
	private AlertDialog.Builder builder;
	private AlertDialog dialog;
	private MyInterface iface;
	private MyWebView webview;
	private String key;
	private DomEntity domEntity;
	private int seletedItemIdex;
	public static boolean finish = false;

	public Selecter(String key, MyInterface cordova) {
		this.iface = cordova;
		this.key = key;
	}

	public void show(final JSONObject data) {
		if (finish) {
			return;
		}
		final Runnable runnable;
		final Context currentCtx = iface.getActivity();
		webview = ((MainActivity) iface.getActivity()).getWebview();
		runnable = new Runnable() {
			public void run() {
				builder = new AlertDialog.Builder(currentCtx);
				domEntity = new DomEntity();
				List<String> alist = new ArrayList<String>();
				try {
					JSONArray ja = data.getJSONArray("options");
					Log.i(TAG, ja.toString());
					for (int i = 0; i < ja.length(); i++) {
						JSONObject jo = (JSONObject) ja.get(i);
						if (jo.getBoolean("selected")) {
							seletedItemIdex = i;
						}
						alist.add(jo.getString("text"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				final ListView listView = new ListView(currentCtx);
				listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				final ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						currentCtx, R.layout.simple_list_item_1, alist);
				adapter.setDropDownViewResource(android.R.layout.select_dialog_item);
				// 设置下拉列表的风�?				listView.setAdapter(adapter);
				// listView.setSelected(true);;
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						try {
							if (position != seletedItemIdex) {
								String onChange = data.getString("onChange");
								domEntity.setOnChange(onChange);
								domEntity.setKey(String.valueOf(position));
								domEntity.setValue(adapter.getItem(position));
								sendResult(key, domEntity);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						} catch (Exception e) {
							e.printStackTrace();
						}
						dialog.dismiss();
						finish = false;
					}

				});
				builder.setView(listView);

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						finish = false;
					}
				});
				dialog = builder.create();
				dialog.show();
				finish = true;
			}
		};

		iface.getActivity().runOnUiThread(runnable);
		return;
	}

	@Override
	public void sendResult(String key, DomEntity domEntity) throws Exception {
		String valuejs = "$('#" + key + ">span').text('" + domEntity.getValue()+"');";
		String removejs = "$('#" + key +" ol[select=true]').removeAttr('select');";
		String index = domEntity.getKey();
		String selectjs = "$('#" + key + ">ol').eq(" + index+ ").attr('select','true');";
		String changejs = domEntity.getOnChange().replace("INDEX",domEntity.getKey())+ ";";
		webview.loadUrl("javascript:" + valuejs+ removejs+ selectjs+ changejs);
	}
}