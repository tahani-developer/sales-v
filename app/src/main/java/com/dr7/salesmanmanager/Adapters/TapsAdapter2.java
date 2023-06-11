package com.dr7.salesmanmanager.Adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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
import java.util.ArrayList;
import java.util.List;

public class TapsAdapter2 extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList();
    private final List <CharSequence>mFragmentTitleList = new ArrayList<>();

    public TapsAdapter2(FragmentManager manager){
      super(manager);
}

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFrag(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}