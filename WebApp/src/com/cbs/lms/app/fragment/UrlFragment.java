package com.cbs.lms.app.fragment;

import java.util.ArrayList;

import com.cbs.lms.app.R;
import com.cbs.lms.app.db.DataHelperClass;
import com.cbs.lms.app.helper.ShowAlertInformation;
import com.cbs.lms.app.main.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class UrlFragment extends Fragment {
	private Button btnlogin;
	private EditText edtMobile;
	private ImageView adminlogin;
	private Spinner spurls;
	private ArrayList<String> urlList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		View view = inflater.inflate(R.layout.frag_url, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		spurls = (Spinner) view.findViewById(R.id.spurls);
		btnlogin = (Button) view.findViewById(R.id.btnLogin);
		edtMobile = (EditText) view.findViewById(R.id.edtMobile);
		DataHelperClass dbhelper = new DataHelperClass(getActivity());
		urlList = dbhelper.getUrlList();
		urlList.add(0,"Select url from list");
		urlList.add(1,"http://acquairglobal.com");
		ArrayAdapter<String> adapter_country = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, urlList);
		spurls.setAdapter(adapter_country);
		spurls.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if (position > 0) {
					if (MainActivity.getNetworkHelper().isOnline()) {
						Bundle bundle = new Bundle();
						bundle.putString("url", urlList.get(position));
						WebFragment frg = new WebFragment();
						frg.setArguments(bundle);
						MainActivity.redirectToFragment(frg);
					} else {
						ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
					}
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		adminlogin = (ImageView) view.findViewById(R.id.adminlogin);
		adminlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				MainActivity.redirectToFragment(new LoginFragment());
			}
		});
		edtMobile.setText("http://acquairglobal.com");

		btnlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = edtMobile.getText().toString().trim();
				edtMobile.setError(null);
				if (url.equals("")) {

					edtMobile.setError("Please enter url.");
				} else {
					if (MainActivity.getNetworkHelper().isOnline()) {
						Bundle bundle = new Bundle();
						bundle.putString("url", url);
						WebFragment frg = new WebFragment();
						frg.setArguments(bundle);
						MainActivity.redirectToFragment(frg);
					} else {
						ShowAlertInformation.showDialog(getActivity(), "Network error", getString(R.string.offline));
					}
				}

			}
		});

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		// MainActivity.getMainScreenActivity().actionBarTitle.setText("Submit
		// Assets");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

	}
}
