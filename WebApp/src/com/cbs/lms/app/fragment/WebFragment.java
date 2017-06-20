package com.cbs.lms.app.fragment;

import com.cbs.lms.app.R;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebFragment extends Fragment {
	private WebView webView;
	private String url = "";
	private ProgressDialog progressDialog;
	private String baseurl = "";
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case 1: {
				webViewGoBack();
			}
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle args) {

		View view = inflater.inflate(R.layout.frag_webview, container, false);
		webView = (WebView) view.findViewById(R.id.webView);
		if (null != getArguments())
			url = getArguments().getString("url");
		baseurl = url;

		// if (!url.startsWith("www.") && !url.startsWith("http://") &&
		// !url.startsWith("https://")) {
		// url = "www." + url;
		// }
		if (!url.startsWith("http://") && !url.startsWith("https://")) {
			url = "http://" + url;
		}
		webView.setWebViewClient(new myWebClient());
		webView.setWebChromeClient(new WebChromeClient());
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setVerticalScrollBarEnabled(true);
		webView.setHorizontalScrollBarEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.getSettings().setDisplayZoomControls(false);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			// chromium, enable hardware acceleration
			webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		} else {
			// older android version, disable hardware acceleration
			webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}

		webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		webView.loadUrl(url);
		webView.setOnKeyListener(new OnKeyListener() {

			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_UP
						&& webView.canGoBack()) {
					handler.sendEmptyMessage(1);
					return true;
				}

				return false;
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

	public class myWebClient extends WebViewClient {

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			if (progressDialog != null && progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(getActivity());
				progressDialog.setMessage("Loading...");
				progressDialog.show();
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// if (MainActivity.getMainScreenActivity().isLoggedIn()) {
			// view.loadUrl(url);
			// } else

			if (url.toLowerCase().startsWith(baseurl.toLowerCase())) {
				view.loadUrl(url);
			}
			// else if(new
			// DataHelperClass(getActivity()).isValidUrl(url.toLowerCase().trim())){
			// view.loadUrl(url);
			// }
			else {
				Toast.makeText(getActivity(), "This url is blocked by admin.", Toast.LENGTH_LONG).show();
			}
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			//super.onPageFinished(view, url);

			try {
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
					progressDialog = null;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		}
	}

	private void webViewGoBack() {
		if (webView.canGoBack())
			webView.goBack();
	}
}
