package test.badoo.sniper.badootest.activities;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.badoo.sniper.badootest.R;
import test.badoo.sniper.badootest.utils.HeightAnimation;

/**
 * Created by sniper on 1/9/16.
 */
public class MyBaseActivity extends AppCompatActivity {

//    private ConnectivityChangeBroadcastReceiver mReceiver;// internet connection checker
    protected ProgressDialog progressDialog;

    public ProgressDialog getProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
        }
        return progressDialog;
    }
}