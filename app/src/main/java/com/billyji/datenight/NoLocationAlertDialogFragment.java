package com.billyji.datenight;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.billyji.datenight.activities.MainActivity;


public class NoLocationAlertDialogFragment extends DialogFragment {
	public static NoLocationAlertDialogFragment newInstance(int title) {
		NoLocationAlertDialogFragment frag = new NoLocationAlertDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");

		return new AlertDialog.Builder(getActivity())
				.setIcon(R.mipmap.ic_alert_2)
				.setTitle(title)
				.setMessage(R.string.location_not_enabled_dialog_message)
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								((MainActivity)getActivity()).doPositiveClick();
							}
						}
				)
				.create();
	}


}
