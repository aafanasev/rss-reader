package com.jokuskay.rss_reader.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.ContextThemeWrapper;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.jokuskay.rss_reader.R;

public class AddRssDialog extends DialogFragment {

    private OnAddRssDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAddRssDialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must be implement OnAddRssDialogListener interface");
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mListener.onAddRssDialogClose();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mListener.onAddRssDialogOpen();

        ContextThemeWrapper themedContext;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.L) {
            themedContext = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Material_Dialog_NoActionBar);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
            themedContext = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar);
        } else {
            themedContext = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Light_NoTitleBar);
        }

        final FrameLayout inputLayout = (FrameLayout) getActivity().getLayoutInflater().inflate(R.layout.dialog_add_rss, null);

        AlertDialog.Builder adb = new AlertDialog.Builder(themedContext)
                .setView(inputLayout)
                .setTitle("Add new RSS feed")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText input = (EditText) inputLayout.getChildAt(0);
                        mListener.onAddRssDialogAction(input.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", null);

        return adb.create();
    }

    public static interface OnAddRssDialogListener {
        public abstract void onAddRssDialogOpen();

        public abstract void onAddRssDialogClose();

        public abstract void onAddRssDialogAction(String url);
    }

}
