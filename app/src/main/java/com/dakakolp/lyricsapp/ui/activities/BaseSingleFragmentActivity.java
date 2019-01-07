package com.dakakolp.lyricsapp.ui.activities;

import android.os.Bundle;

import com.dakakolp.lyricsapp.R;
import com.dakakolp.lyricsapp.ui.fragments.BaseFragment;

public abstract class BaseSingleFragmentActivity<T extends BaseFragment> extends BaseActivity {

    public static final String FRAGMENT_TAG = "_single_fragment_tag";

    private BaseFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        mFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag());
        if (mFragment == null) {
            mFragment = onCreateFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(getIdFragmentContainer(), mFragment, getFragmentTag())
                    .commit();
        }
    }

    protected String getFragmentTag() {
        return getClass().getSimpleName() + FRAGMENT_TAG;
    }

    protected abstract int getContentViewId();

    protected abstract int getIdFragmentContainer();

    protected abstract T onCreateFragment();

}
