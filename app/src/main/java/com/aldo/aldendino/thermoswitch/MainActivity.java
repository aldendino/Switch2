package com.aldo.aldendino.thermoswitch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private ArrayList<Converter.ConversionType> types;

    public static final String TYPE_KEY = "type";

    private boolean smoothScrollEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager);
        getSupportActionBar().getThemedContext();

        types = new ArrayList<>();
        types.add(Converter.ConversionType.TEMPERATURE);
        types.add(Converter.ConversionType.DISTANCE);
        types.add(Converter.ConversionType.WEIGHT);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final ActionBar mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                mPager.setCurrentItem(tab.getPosition(), smoothScrollEnabled);
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };
        for(Converter.ConversionType type : types) {
            mActionBar.addTab(
                    mActionBar.newTab()
                            .setText(type.name())
                            .setTabListener(tabListener));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_switch) {
            switchDirection();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchDirection() {
        ScreenSlidePagerAdapter sspa = (ScreenSlidePagerAdapter) mPagerAdapter;
        int current =  mPager.getCurrentItem();
        ConversionFragment cf = (ConversionFragment) sspa.getItem(current);
        if(cf != null) cf.switchDirection();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        private SparseArray<ConversionFragment> frags = new SparseArray<>();

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position >= types.size() || position < 0) return null;
            ConversionFragment fragment = frags.get(position);
            if(fragment == null) {
                Bundle bundle = new Bundle();
                bundle.putString(TYPE_KEY, types.get(position).name());
                fragment = new ConversionFragment();
                fragment.setArguments(bundle);
                frags.put(position, fragment);
            }
            return fragment;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            frags.put(position, (ConversionFragment) fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            frags.removeAt(position);
        }

        @Override
        public int getCount() {
            return types.size();
        }
    }
}
