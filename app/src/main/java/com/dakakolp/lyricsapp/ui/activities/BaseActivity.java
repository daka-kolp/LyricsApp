package com.dakakolp.lyricsapp.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dakakolp.lyricsapp.R;

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog mProgressLoading;

    protected void showProgress() {
        if (mProgressLoading == null) {
            mProgressLoading = ProgressDialog.show(this, null, null, false, true);
            mProgressLoading.setContentView(R.layout.dialog_progress_bar);
            if (mProgressLoading.getWindow() != null) {
                mProgressLoading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    protected void hideProgress() {
        if (mProgressLoading != null && mProgressLoading.isShowing()) {
            mProgressLoading.dismiss();
            mProgressLoading = null;
        }
    }

    protected void closeKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgress();
    }
}
