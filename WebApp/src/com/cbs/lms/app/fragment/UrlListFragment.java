package com.cbs.lms.app.fragment;

import java.util.ArrayList;

import com.cbs.lms.app.R;
import com.cbs.lms.app.adapter.UrlAdapter;
import com.cbs.lms.app.db.DataHelperClass;
import com.cbs.lms.app.helper.ShowAlertInformation;
import com.cbs.lms.app.main.MainActivity;
import com.cbs.lms.app.pojos.UrlsDto;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class UrlListFragment extends Fragment implements View.OnClickListener {
	private ListView listview;
	private UrlAdapter adapter;
	private Button btn_add;
	private ArrayList<UrlsDto> dummy = new ArrayList<UrlsDto>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		View view = inflater.inflate(R.layout.frag_urllist, container, false);
		listview = (ListView) view.findViewById(R.id.list);
		btn_add = (Button) view.findViewById(R.id.btn_add);
		btn_add.setOnClickListener(this);
		setListData();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		 MainActivity.getMainScreenActivity().iv_logout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {

		case R.id.btn_add:

			addUrlDialog(getActivity());

			break;

		default:
			break;
		}

	}

	private void setListData() {
		DataHelperClass dbhelper = new DataHelperClass(getActivity());
		if (!dbhelper.isRecordExist()) {
			Toast.makeText(getActivity(), "No data to display", Toast.LENGTH_LONG).show();
			dummy.clear();
		} else {
			dummy = dbhelper.getUrlData();
		}
		adapter = new UrlAdapter(getActivity(), dummy);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
				if (MainActivity.getNetworkHelper().isOnline()) {
					Bundle bundle = new Bundle();
					bundle.putString("url", dummy.get(position).getUrl());
					WebFragment frg = new WebFragment();
					frg.setArguments(bundle);
					MainActivity.redirectToFragment(frg);
				} else {
					ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
				}
			}
		});

	}

	private void addUrlDialog(final Context mContext) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		LayoutInflater inflater = LayoutInflater.from(mContext);
		builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Save Url");
		View customDialogView = inflater.inflate(R.layout.dialog_product, null, false);
		final EditText edturl = (EditText) customDialogView.findViewById(R.id.edturl);
		builder.setView(customDialogView);
		final AlertDialog mAlertDialog = builder.create();
		mAlertDialog.setCancelable(false);
		Button btncancel = (Button) customDialogView.findViewById(R.id.btncancel);
		Button btnok = (Button) customDialogView.findViewById(R.id.btnok);
		btnok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				edturl.setError(null);
				if (edturl.getText().toString().equals("")) {
					edturl.setError("Url is mandatory.");
				} else {
					UrlsDto ulrItemDto = new UrlsDto();
					ulrItemDto.setUrl(edturl.getText().toString().trim());
					new DataHelperClass(getActivity()).addUrlData(ulrItemDto);
					setListData();
					mAlertDialog.dismiss();
				}
			}
		});
		btncancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAlertDialog.dismiss();

			}
		});

		mAlertDialog.show();

	}
}
