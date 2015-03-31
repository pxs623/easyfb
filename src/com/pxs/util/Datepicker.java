package com.pxs.util;

import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.widget.DatePicker;

import com.pxs.MainActivity;
import com.pxs.MyInterface;
import com.pxs.MyWebView;

/**
 * 时间选择控件
 * 
 * @author peng_xuesong
 * 
 */
public class Datepicker implements PluginInterface {
	private static final String TAG = Datepicker.class.getSimpleName();
	private AlertDialog.Builder builder;

	private static final String ACTION_DATE = "date";
	private static final String ACTION_TIME = "time";
	private static final String SUFFIX = "-";
	private MyInterface iface;
	private MyWebView webview;
	private String key;
	public static boolean finish = false;

	public Datepicker(String key, MyInterface cordova) {
		this.iface = cordova;
		this.key = key;
	}

	public void show(final JSONObject data) {
		if (finish) {
			return;
		}
		final Calendar c = Calendar.getInstance();
		final Runnable runnable;
		final Context currentCtx = iface.getActivity();
		webview = ((MainActivity) iface.getActivity()).getWebview();
		String action = "date";
		int month = -1, day = -1, year = -1, hour = -1, min = -1;
		try {
			action = data.getString("mode");

			String optionDate = data.getString("date");
			if (!"".equals(optionDate)) {
				String[] datePart = optionDate.split(SUFFIX);
				if (datePart.length > 1) {
					year = Integer.parseInt(datePart[0]);
					month = Integer.parseInt(datePart[1]);
					day = Integer.parseInt(datePart[2]);
					// hour = Integer.parseInt(datePart[3]);
					// min = Integer.parseInt(datePart[4]);
				}
			}

		} catch (JSONException e) {
			LOG.e(TAG, "parse error ", e);
			;
		}
		// By default initalize these fields to 'now'
		final int mYear = year == -1 ? c.get(Calendar.YEAR) : year;
		final int mMonth = month == -1 ? c.get(Calendar.MONTH) : month - 1;
		final int mDay = day == -1 ? c.get(Calendar.DAY_OF_MONTH) : day;
		// final int mHour = hour == -1 ? c.get(Calendar.HOUR_OF_DAY) : hour;
		// final int mMinutes = min == -1 ? c.get(Calendar.MINUTE) : min;

		if (ACTION_TIME.equalsIgnoreCase(action)) {
			runnable = new Runnable() {
				public void run() {
				}
			};

		} else if (ACTION_DATE.equalsIgnoreCase(action)) {
			runnable = new Runnable() {
				public void run() {
					builder = new AlertDialog.Builder(currentCtx);
					builder.setTitle("");
					final DatePicker datePicker = new DatePicker(currentCtx);
					datePicker.setCalendarViewShown(false);
					datePicker.init(mYear, mMonth, mDay, null);
					datePicker.updateDate(mYear, mMonth, mDay);
					builder.setView(datePicker);
					builder.setPositiveButton("ok",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									int year = datePicker.getYear();
									int month = datePicker.getMonth();
									int dayOfMonth = datePicker.getDayOfMonth();

									StringBuilder stringBuilder = new StringBuilder();
									stringBuilder.append(year);
									int newMonth = month + 1;
									if (newMonth < 10) {
										stringBuilder.append(SUFFIX)
												.append("0").append(newMonth);
									} else {
										stringBuilder.append(SUFFIX).append(
												newMonth);
									}
									if (dayOfMonth < 10) {
										stringBuilder.append(SUFFIX)
												.append("0").append(dayOfMonth);
									} else {
										stringBuilder.append(SUFFIX).append(
												dayOfMonth);
									}
									DomEntity de = new DomEntity();
									de.setValue(stringBuilder.toString());
									sendResult(key, de);
									finish = false;
								}
							});
					builder.setNegativeButton("cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									finish = false;
								}

							});
					builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							finish = false;
						}
					});
					builder.show();
					finish = true;
				}
			};

		} else {
			Log.d(TAG,
					"Unknown action. Only 'date' or 'time' are valid actions");
			return;
		}
		iface.getActivity().runOnUiThread(runnable);
		return;
	}

	public void sendResult(String key, DomEntity domEntity) {
		String valuejs="document.getElementById('" + key+ "').innerHTML='" + domEntity.getValue() + "';";
		webview.loadUrl("javascript:"+valuejs);
	}
}