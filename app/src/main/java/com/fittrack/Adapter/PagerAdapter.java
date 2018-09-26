package com.fittrack.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fittrack.Fragment.AvoidFragment;
import com.fittrack.Fragment.EatFragment;

/**
 * Created by kundan on 10/16/2015.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new AvoidFragment();
                break;
            case 1:
                frag = new EatFragment();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = " ";
        switch (position) {
            case 0:
                title = "TO AVOID";
                break;
            case 1:
                title = "TO EAT";
                break;

        }
        return title;
    }
}
