package com.example.basepop.basepop.base.myTab;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyBasePagerAdapter extends FragmentPagerAdapter {
    private  ArrayList<Fragment> mFragments;

    public MyBasePagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments )
    {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.mFragments=mFragments;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}