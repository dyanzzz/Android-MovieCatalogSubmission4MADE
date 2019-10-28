package com.submission.moviecatalogsubmission4made.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.submission.moviecatalogsubmission4made.fragment.MainFragment;
import com.submission.moviecatalogsubmission4made.fragment.FavoriteListFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new FavoriteListFragment();
        Bundle args = new Bundle();

        switch (position) {
            case 0:
                args.putString(MainFragment.EXTRA_OBJECT, "movies");
                break;
            case 1:
                args.putString(MainFragment.EXTRA_OBJECT, "tv_show");
                break;
        }
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
