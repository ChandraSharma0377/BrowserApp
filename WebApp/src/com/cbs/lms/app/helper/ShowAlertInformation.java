package com.cbs.lms.app.helper;

import com.cbs.lms.app.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ShowAlertInformation {

	public static void showDialog(Context context, String Title, String Message) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		alertDialog.setTitle(Title);

		alertDialog.setMessage(Message);

		alertDialog.setIcon(R.drawable.ic_launcher);

		alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();
			}
		});

		alertDialog.show();
	}

}
