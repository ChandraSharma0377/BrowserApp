package com.cbs.lms.app.adapter;

import java.util.ArrayList;

import com.cbs.lms.app.R;
import com.cbs.lms.app.db.DataHelperClass;
import com.cbs.lms.app.pojos.UrlsDto;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UrlAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<UrlsDto> urlItemDtos;

	public UrlAdapter(Context context, ArrayList<UrlsDto> urlItemDtos) {
		this.context = context;
		this.urlItemDtos = urlItemDtos;
	}

	@Override
	public int getCount() {
		return urlItemDtos.size();
	}

	@Override
	public Object getItem(int position) {
		return urlItemDtos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.url_list_item,parent, false);
			holder = new ViewHolder();
			holder.tvUrl = (TextView) convertView.findViewById(R.id.tvUrl);
			holder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvUrl.setText(urlItemDtos.get(position).getUrl());
		holder.ivDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertbuilder = new AlertDialog.Builder(context);
				alertbuilder.setMessage(R.string.delete_msg);
				alertbuilder.setCancelable(true);
				alertbuilder.setPositiveButton(R.string.deleteok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
						DataHelperClass DHC = new DataHelperClass(context);
						DHC.deleteRecord(urlItemDtos.get(position).getUniqueId());
						urlItemDtos.remove(position);
						notifyDataSetChanged();

					}
				});
				alertbuilder.setNegativeButton(R.string.deletecancel, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

				AlertDialog alert = alertbuilder.create();
				alert.show();

			}
		});

		if (position % 2 == 0) { // make the list color alternate
			convertView.setBackgroundColor(Color.WHITE);
		} else {
			convertView.setBackgroundColor(Color.parseColor("#e7eef2"));
		}

		return convertView;

	}

	static class ViewHolder {
		TextView tvUrl;
		ImageView ivDelete;

	}

}
