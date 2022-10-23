package edu.northeastern.driversafebc.a7atyourservice.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import edu.northeastern.driversafebc.R;

public class ShowAlertDialog {
    private static Dialog alertDialog;

    public static void closeImg() {
        if (anim != null) {
            anim.stop();
            anim = null;
        }
    }

    private static AnimationDrawable anim;

    public static Dialog loadingDialog(Context context) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.loading_dialog, null);

        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

        loadingDialog.setCancelable(true);
        loadingDialog.setContentView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }

}
