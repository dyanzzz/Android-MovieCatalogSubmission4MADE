package com.submission.moviecatalogsubmission4made;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.submission.moviecatalogsubmission4made.fragment.MainFragment;
import com.submission.moviecatalogsubmission4made.fragment.FavoriteFragment;
import com.submission.moviecatalogsubmission4made.model.MainViewModel;

import static com.submission.moviecatalogsubmission4made.fragment.MainFragment.EXTRA_OBJECT;

public class MainActivity extends AppCompatActivity {

    final Fragment favoriteFragment = new FavoriteFragment();
    final FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        if (savedInstanceState == null) {
            navView.setSelectedItemId(R.id.navigation_movie);
        }

        MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mainViewModel.init();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment mainFragment = new MainFragment();
            Bundle args = new Bundle();

            switch (menuItem.getItemId()) {
                case R.id.navigation_movie:
                    args.putString(EXTRA_OBJECT, "movies");
                    mainFragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.main_content, mainFragment).commit();
                    return true;
                case R.id.navigation_tvshow:
                    args.putString(EXTRA_OBJECT, "tv_show");
                    mainFragment.setArguments(args);
                    fragmentManager.beginTransaction().replace(R.id.main_content, mainFragment).commit();
                    return true;
                default:
                    fragmentManager.beginTransaction().replace(R.id.main_content, favoriteFragment).commit();
                    return true;
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings) {
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}