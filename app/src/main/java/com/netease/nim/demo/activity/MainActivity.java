package com.netease.nim.demo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.netease.nim.demo.R;
import com.netease.nim.uikit.business.contact.ContactsFragment;
import com.netease.nim.uikit.business.recent.RecentContactsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViewPager();
    }

    private void setupViewPager() {
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        TempViewPagerAdapter adapter = new TempViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    private class TempViewPagerAdapter extends FragmentPagerAdapter {

        String[] tabs = new String[]{"会话", "通讯录"};

        public TempViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return new RecentContactsFragment();
            }
            return new ContactsFragment();
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
}
