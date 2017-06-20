package com.cbs.lms.app.main;

import java.util.HashMap;
import java.util.Stack;

import com.cbs.lms.app.R;
import com.cbs.lms.app.db.DBOpenHelperClass;
import com.cbs.lms.app.fragment.UrlFragment;
import com.cbs.lms.app.fragment.UrlListFragment;
import com.cbs.lms.app.helper.NetworkHelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	private Boolean exit = false;
	private static MainActivity mainActivity;
	private static NetworkHelper networkHelper;
	public static final String MyPREFERENCES = "AppPref";
	private static SharedPreferences sharedpreferences;

	protected Fragment mFrag;
	protected Fragment cFrag, rootFragment;
	private HashMap<String, Stack<Fragment>> mFragmentsStack;
	public TextView actionBarTitle;

	private final String IS_LOGIN = "IsLoggedIn";
	private final String KEY_EMAIL = "email";
	private final String KEY_ACTIVE_USER_ID = "userName";
	private final String KEY_USER_ID = "userID";
	protected static final String CONTENT_TAG = "contenFragments";
	public DBOpenHelperClass dbHandler;
	public ImageView iv_logout;

	public static MainActivity getMainScreenActivity() {
		return mainActivity;
	}

	public static NetworkHelper getNetworkHelper() {
		return networkHelper;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainActivity = this;
		networkHelper = new NetworkHelper(this);
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		mFragmentsStack = new HashMap<String, Stack<Fragment>>();
		mFragmentsStack.put(CONTENT_TAG, new Stack<Fragment>());

		ActionBarSetup();
		dbHandler = DBOpenHelperClass.getSharedObject(this);
		if (isLoggedIn()) {
			changeNavigationContentFragment(new UrlListFragment(), false);
		} else
			changeNavigationContentFragment(new UrlFragment(), false);

		onNewIntent(getIntent());

	}

	private void ActionBarSetup() {
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setCustomView(R.layout.custom_actionbar_view);
		getSupportActionBar().setElevation(0);
		View v = getSupportActionBar().getCustomView();

		actionBarTitle = (TextView) v.findViewById(R.id.title);
		iv_logout = (ImageView) v.findViewById(R.id.iv_logout);
		iv_logout.setOnClickListener(logoClik);
		Toolbar parent = (Toolbar) v.getParent();
		parent.setContentInsetsAbsolute(0, 0);
		// actionBarTitle.setText(getString(R.string.app_name));

	}

	public void changeNavigationContentFragment(Fragment frgm, boolean shouldAdd) {

		if (shouldAdd) {

			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			if (null != fm.findFragmentById(R.id.main_fragment))
				ft.hide(fm.findFragmentById(R.id.main_fragment));
			ft.add(R.id.main_fragment, frgm, frgm.getClass().getSimpleName());
			ft.addToBackStack(null);
			// ft.commitAllowingStateLoss();
			ft.commit();
			mFragmentsStack.get(CONTENT_TAG).push(frgm);
		} else {
			mFragmentsStack.get(CONTENT_TAG).clear();
			FragmentManager manager = getSupportFragmentManager();
			manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
			manager.beginTransaction().replace(R.id.main_fragment, frgm).commitAllowingStateLoss();
			rootFragment = frgm;
		}

		cFrag = frgm;

	}

	// Checking exitFlag on Back press, if exitFlag is true removing all
	// fragments from back stack and exiting app.
	// if exitFlag is false then allowing default behavior. That is removing
	// current Fragment and loading previous
	// Fragment.
	@Override
	public void onBackPressed() {
		removeFragment();
	}

	public void removeFragment() {
		int statckSize = mFragmentsStack.get(CONTENT_TAG).size();
		if (statckSize == 0) {
			if (exit) {
				finish(); // finish activity
			} else {
				Toast.makeText(this, "Press Back again to Exit.", Toast.LENGTH_SHORT).show();
				exit = true;
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						exit = false;
					}
				}, 3 * 1000);

			}

		} else {
			Fragment fragment;
			if (statckSize > 1)
				fragment = mFragmentsStack.get(CONTENT_TAG).elementAt(statckSize - 2);
			else
				fragment = mFragmentsStack.get(CONTENT_TAG).elementAt(statckSize - 1);
			mFragmentsStack.get(CONTENT_TAG).pop();
			FragmentManager fm = this.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.detach(cFrag);
			ft.commitAllowingStateLoss();
			if (statckSize > 1) {
				cFrag = fragment;
				fragment.onResume();
			} else {
				cFrag = rootFragment;
				rootFragment.onResume();
			}
			super.onBackPressed();
		}

	}

	public static SharedPreferences getSharePreferance() {
		return sharedpreferences;
	}

	public String getActiveUserID() {
		return sharedpreferences.getString(KEY_ACTIVE_USER_ID, "");
	}

	public String getUserID() {
		return sharedpreferences.getString(KEY_USER_ID, "9001");
	}

	public boolean isLoggedIn() {
		return sharedpreferences.getBoolean(IS_LOGIN, false);
	}

	public void setSharPreferancename(String act_user_id, String userID, String mobileNo, boolean isLogin) {
		Editor editor = sharedpreferences.edit();
		editor.putString(KEY_ACTIVE_USER_ID, act_user_id);
		editor.putString(KEY_USER_ID, userID);
		editor.putString(KEY_EMAIL, mobileNo);
		editor.putBoolean(IS_LOGIN, isLogin);
		editor.commit();
	}

	public static void redirectToFragment(Fragment fragment) {
		Fragment VF = fragment;
		MainActivity.getMainScreenActivity().changeNavigationContentFragment(VF, true);

	}

	public void restartActivity() {
		Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	OnClickListener logoClik = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {

			// case R.id.btn_exit:
			// new
			// AlertDialog.Builder(mainActivity).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
			// .setMessage("Are you sure you want to exit?")
			// .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// finish();
			// }
			// }).setNegativeButton("No", null).show();
			// break;
			case R.id.iv_logout:
				SharedPreferences.Editor editor = MainActivity.getSharePreferance().edit();
				editor.clear();
				editor.commit();
				restartActivity();
				break;
			default:
				break;
			}
		}
	};
}
