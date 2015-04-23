package com.example.vickygao.masproject;
import java.util.Locale;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class LogTabActivity extends TabActivity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_log_tab);

/** TabHost will have Tabs */
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);

/** TabSpec used to create a new tab.
 * By using TabSpec only we can able to setContent to the tab.
 * By using TabSpec setIndicator() we can set name to tab. */

/** tid1 is firstTabSpec Id. Its used to access outside. */
        TabSpec firstTabSpec = tabHost.newTabSpec("tid1");
        TabSpec secondTabSpec = tabHost.newTabSpec("tid1");

/** TabSpec setIndicator() is used to set name for the tab. */
/** TabSpec setContent() is used to set content for a particular tab. */
        firstTabSpec.setIndicator("Log in").setContent(new Intent(this,LoginActivity.class));
        secondTabSpec.setIndicator("Sign up").setContent(new Intent(this,SignupActivity.class));

/** Add tabSpec to the TabHost to display. */
        tabHost.addTab(firstTabSpec);
        tabHost.addTab(secondTabSpec);

    }
}