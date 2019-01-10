package com.yashoid.uxapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.widget.Toast;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TabChangeReceiver {

    private Toolbar mToolbar;
    private Tabbar mTabbar;

    private SparseArray<Fragment> mFragments = new SparseArray<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (System.currentTimeMillis() > 1535217723000L + 7L*24L*60L*60L*1000L) {
//            Toast.makeText(this, "مدت تست نسخه دمو اپلیکیشن هامون تمام شده است.", Toast.LENGTH_LONG).show();
//            finish();

//            Intent intent = new Intent(Intent.ACTION_DELETE, Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
//            startActivity(intent);

//            return;
        }

        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.toolbar);
        mTabbar = findViewById(R.id.tabbar);

        switchTab();
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public void onTabChanged(int tab) {
        mToolbar.reset();

        switchTab();
    }

    private void switchTab() {
        int tab = mTabbar.getTab();

        switch (tab) {
            case TAB_HOME:
                switchTabFragment(tab, HomeFragment.class);
                return;
            case TAB_RAPID_TRANSACTION:
                switchTabFragment(tab, RapidTransactionsFragment.class);
                return;
            case TAB_MESSAGES:
                switchTabFragment(tab, MessagesFragment.class);
                return;
            default:
                removeContent();
                return;
        }
    }

    private void switchTabFragment(int tab, Class<? extends Fragment> clazz) {
        removeContent();

        try {
            FragmentManager fm = getSupportFragmentManager();

            Fragment fragment = mFragments.get(tab);

            if (fragment != null) {
                fm.beginTransaction().attach(fragment).commitNow();
            }
            else {
                fragment = clazz.getConstructor().newInstance();

                mFragments.put(tab, fragment);

                fm.beginTransaction().add(R.id.content, fragment).commitNow();
            }
        } catch (Throwable t) {

        }
    }

    private void removeContent() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().detach(fragment).commitNow();
        }
    }

}
