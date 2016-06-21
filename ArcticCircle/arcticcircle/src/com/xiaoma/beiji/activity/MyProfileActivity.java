package com.xiaoma.beiji.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.xiaoma.beiji.R;
import com.xiaoma.beiji.fragment.MyProfileFragment;

/**
 * Created by zhangqibo on 2016/6/21.
 */
public class MyProfileActivity extends FragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);
        FragmentManager mFragmentManager = getSupportFragmentManager();
        Fragment fragment =  Fragment.instantiate(this, MyProfileFragment.class.getName(), null);
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if(!fragment.isAdded()){
            ft.add(R.id.pageContainer, fragment, fragment.getClass().getSimpleName());
        }
        ft.show(fragment);
        ft.commitAllowingStateLoss();
    }
}
