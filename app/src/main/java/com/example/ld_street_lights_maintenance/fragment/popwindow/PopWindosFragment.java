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
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

public class PopWindosFragment extends Fragment {


    private final String[] luxoptionss = {"平时照度", "节假照度"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private View rootView;
    private MyPagerAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fr_popwindos, null);
     //   initView();
        return rootView;
    }


    private void initView() {
        // 日常照度选择
        for (String title : luxoptionss) {
            mFragments.add(SimpleCardFragment.getInstance(title));
        }
        ViewPager vp = rootView.findViewById(R.id.vp);
        mAdapter = new MyPagerAdapter(getFragmentManager(), 0);
        vp.setAdapter(mAdapter);
        SlidingTabLayout tabLayout_10 = rootView.findViewById(R.id.tl_10);
        tabLayout_10.setViewPager(vp);
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
