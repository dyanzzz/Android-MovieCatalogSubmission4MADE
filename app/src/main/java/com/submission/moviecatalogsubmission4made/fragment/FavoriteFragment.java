package com.submission.moviecatalogsubmission4made.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.submission.moviecatalogsubmission4made.R;
import com.submission.moviecatalogsubmission4made.adapter.PagerAdapter;

import java.util.Objects;

public class FavoriteFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorite_fragment, container, false);

        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager());
        ViewPager viewPager = view.findViewById(R.id.viewpager_favorite);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = view.findViewById(R.id.tab_favorite);
        tabLayout.setupWithViewPager(viewPager);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setText(R.string.tab_text_favorite_1);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText(R.string.tab_text_favorite_2);

        setHasOptionsMenu(true);
        return view;
    }
}
