package com.annguyen.truongmamnon.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annguyen.truongmamnon.Adapter.MainViewPagerAdapter;
import com.annguyen.truongmamnon.R;
import com.google.android.material.tabs.TabLayout;

public class FragmentDiaryTap extends Fragment {
    static View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diary_tap,container,false);
        setHasOptionsMenu(true);

        AnhXa();
        setUpViewPager(viewPager);
        return view;
    }

    private void setUpViewPager(ViewPager viewPager) {
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getChildFragmentManager());
        mainViewPagerAdapter.addFragment(new FragmentDanhSachChamThe(),"Danh sách chạm thẻ");
        mainViewPagerAdapter.addFragment(new FragmentDanhSachChuaChamThe(),"Danh sách chưa chạm thẻ");
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void AnhXa() {
        viewPager = view.findViewById(R.id.viewPagerNhatKyLayout);
        tabLayout = view.findViewById(R.id.tabLayoutNhatLayout);
    }

}
