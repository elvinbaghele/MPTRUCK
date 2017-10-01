package com.ycce.mptruck;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.logging.Logger;

import com.ycce.mptruck.CustomLayout.CustomTabLayout;
import com.ycce.mptruck.adapter.TabAdapter;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static java.security.AccessController.getContext;

public class TabActivity extends AppCompatActivity {

    ViewPager viewPager;
    CustomTabLayout tabLayout;




    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        tabLayout= (CustomTabLayout) findViewById(R.id.tab_layout);


        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(new TabAdapter(getSupportFragmentManager()));
    }
    protected void changeTabsFont(TabLayout tabLayout) {

        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof AppCompatTextView) {

                    Typeface type = Typeface.createFromAsset(getAssets(),"fonts/AppFont.ttf");
                    TextView viewChild = (TextView) tabViewChild;
                    viewChild.setTypeface(type);
                    viewChild.setAllCaps(false);
                }
            }
        }
    }
}