package com.cbs.lms.app.fragment;

import com.cbs.lms.app.R;
import com.cbs.lms.app.main.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends Fragment {

	private Button btnlogin;
	private EditText edtpwd;

	public LoginFragment() {
		super();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		View view = inflater.inflate(R.layout.frag_login, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		btnlogin = (Button) view.findViewById(R.id.btnLogin);
		edtpwd = (EditText) view.findViewById(R.id.edtpwd);
		btnlogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String mobile = edtpwd.getText().toString().trim();
				edtpwd.setError(null);
				if (mobile.equals("")) {
					edtpwd.setError("Please enter password");
				} else if (!mobile.equals(MainActivity.getMainScreenActivity().getUserID())) {
					edtpwd.setError("Please enter valid password");
				} else {
					MainActivity.getMainScreenActivity().setSharPreferancename(" ",
							MainActivity.getMainScreenActivity().getUserID(), "", true);
					MainActivity.getMainScreenActivity().changeNavigationContentFragment(new UrlListFragment(), false);
				}

			}
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

}
