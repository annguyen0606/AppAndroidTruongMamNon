package com.annguyen.truongmamnon.Fragment;

import android.os.Bundle;
import android.print.PrinterId;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.annguyen.truongmamnon.Adapter.MainViewPagerAdapter;
import com.annguyen.truongmamnon.R;
import com.google.android.material.tabs.TabLayout;

public class FragmentAddAccount extends Fragment {
    private View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_account,container,false);
        AnhXa();
        setUpViewPager(viewPager);
        return view;
    }

    private void AnhXa() {
        viewPager = view.findViewById(R.id.viewPagerAddAcountLayout);
        tabLayout = view.findViewById(R.id.tabLayoutAddAcount);

    }

    private void setUpViewPager(ViewPager viewPager){
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getChildFragmentManager());
        mainViewPagerAdapter.addFragment(new FragmentAddNewAccount(),"Thêm mới");
        mainViewPagerAdapter.addFragment(new FragmentAddAccountExist(),"Thêm phụ huynh");
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
