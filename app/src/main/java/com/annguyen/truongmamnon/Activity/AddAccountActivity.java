package com.annguyen.truongmamnon.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.annguyen.truongmamnon.Adapter.MainViewPagerAdapter;
import com.annguyen.truongmamnon.Fragment.FragmentAddAccountExist;
import com.annguyen.truongmamnon.Fragment.FragmentAddNewAccount;
import com.annguyen.truongmamnon.R;
import com.google.android.material.tabs.TabLayout;

public class AddAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    ImageView backMainActivity;
    public static boolean kiemTraKetNoiInternet = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);
        AnhXa();
        setUpViewPager(viewPager);
    }

    private void AnhXa() {
        viewPager = findViewById(R.id.viewPagerAddAcountLayout);
        tabLayout = findViewById(R.id.tabLayoutAddAcount);
        backMainActivity = findViewById(R.id.backMainActivityAtAddAccountActivity);

        findViewById(R.id.backMainActivityAtAddAccountActivity).setOnClickListener(this);
    }

    private void setUpViewPager(ViewPager viewPager){
        //MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getChildFragmentManager());
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPagerAdapter.addFragment(new FragmentAddNewAccount(),"Thêm mới");
        mainViewPagerAdapter.addFragment(new FragmentAddAccountExist(),"Thêm phụ huynh");
        viewPager.setAdapter(mainViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backMainActivityAtAddAccountActivity:
                onBackPressed();
                break;
        }
    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
                NetworkInfo networkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.CONNECTED){
                    kiemTraKetNoiInternet = true;
                }else if (networkInfo != null && networkInfo.getDetailedState() == NetworkInfo.DetailedState.DISCONNECTED){
                    kiemTraKetNoiInternet = false;
                    Toast.makeText(AddAccountActivity.this,"Lỗi Intertnet, Vui lòng kiểm tra lại!",Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastReceiver,intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }
}
