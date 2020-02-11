package com.dr7.salesmanmanager;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;


import android.text.Layout;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class Transaction_Fragment extends Fragment {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SendTransactionFragment sendFrag;
    public Transaction_Fragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_transaction_, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        sendFrag = new SendTransactionFragment();
        fragmentTransaction.add(R.id.fragment_container, sendFrag).commit();

        tabLayout.addTab(tabLayout.newTab().setText("  Send  "));
        tabLayout.addTab(tabLayout.newTab().setText("  Recive  "));
        tabLayout.setTabMode(TabLayout.INDICATOR_GRAVITY_CENTER);
        ViewGroup.LayoutParams mParams = tabLayout.getLayoutParams();
        mParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        tabLayout.setLayoutParams(mParams);
        //**********************
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position) {
                    case 0:
                        Toast.makeText(getActivity().getBaseContext(), "send", Toast.LENGTH_SHORT).show();
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, sendFrag).addToBackStack(null);
                        fragmentTransaction.commit();



                        break;
                    case 1:
                        ReciveTransactionFragment recivefragment=new ReciveTransactionFragment();
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, recivefragment).addToBackStack(null);
                        fragmentTransaction.commit();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

}
/*TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        switch (tab.getPosition()) {
            case 1:
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ChildFragment()).commit();
                break;
            // Continue for each tab in TabLayout
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
});*/