package com.example.ld_street_lights_maintenance.fragment.popwindow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.view.MySlidingTabLayout;

import java.util.ArrayList;

public class PopWindosSettingsFragment extends Fragment {


    private final String[] luxoptionss = {"基础配置", "详细配置"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private View rootView;
    private MyPagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_popwindos_settings, null);
        initView();
        return rootView;
    }

    ViewPager vp;

    private void initView() {
        // 日常照度选择
       /* for (String title : luxoptionss) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }*/
        mFragments.add(PeacetimeLuxFragment.getInstance(luxoptionss[0]));
        mFragments.add(HolidayLuxFragment.getInstance(luxoptionss[1]));

        vp = rootView.findViewById(R.id.vp_line_chart);
        mAdapter = new MyPagerAdapter(getChildFragmentManager(),3);
        vp.setAdapter(mAdapter);
        MySlidingTabLayout tabLayout_10 = rootView.findViewById(R.id.tl_10);
        tabLayout_10.setViewPager(vp);

        vp.setCurrentItem(0);
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return luxoptionss[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }


}
