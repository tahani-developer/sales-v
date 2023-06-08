package com.dr7.salesmanmanager.Adapters;


import android.content.Context;
import android.os.Handler;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dr7.salesmanmanager.Fragments.BlankFragment;
import com.dr7.salesmanmanager.Fragments.CustomersettFragment;
import com.dr7.salesmanmanager.Fragments.GenralsettFragment;
import com.dr7.salesmanmanager.Fragments.VoucherssettFragment;
import com.dr7.salesmanmanager.GeneralMethod;
import com.dr7.salesmanmanager.R;

public class TapsAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TapsAdapter( FragmentManager fm) {
        super(fm);

        Log.e("TapsAdapter","TapsAdapter");
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        Log.e("TapsAdapter","getItem "+position);
        switch (position) {
            case 0:
                BlankFragment sportFragment1 = new BlankFragment();
                return    sportFragment1;


//            case 1:
//                Log.e(" case 1","getItem "+position);
//                VoucherssettFragment sportFragment = new VoucherssettFragment();
//                return sportFragment;
//            case 2:
//                Log.e("case 2","getItem "+position);
//                CustomersettFragment homeFragment = new CustomersettFragment();
//                return homeFragment;

            default:
                return null;
        }

    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return 1;
    }


}