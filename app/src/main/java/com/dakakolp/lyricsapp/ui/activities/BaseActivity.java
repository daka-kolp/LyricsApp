package com.dakakolp.lyricsapp.ui.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.dakakolp.lyricsapp.R;

public abstract class BaseActivity extends AppCompatActivity{
    private ProgressDialog mProgressLoading;

    protected void showProgress() {
        if (mProgressLoading == null) {
            mProgressLoading = new ProgressDialog(this, R.style.ProgressDialogStyle);
            mProgressLoading.setCancelable(false);
            mProgressLoading.show();
        }
    }

    protected void hideProgress() {
        if (mProgressLoading != null) {
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

}
