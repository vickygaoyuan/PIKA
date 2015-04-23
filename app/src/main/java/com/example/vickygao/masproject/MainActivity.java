package com.example.vickygao.masproject;

import android.app.Activity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.content.Intent;



/** Called when the user clicks the Send button */



public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    public final static String EXTRA_MESSAGE = "com.example.vickygao.masproject.MESSAGE";
    private static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    private List<infocard> mGist =new ArrayList<infocard>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        PickupAdapter pAdapter = new PickupAdapter(this,mGist);
        mRecyclerView.setAdapter(pAdapter);
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Bundle bundle=getIntent().getExtras();
        final String token=bundle.getString("token");
        Log.d("token1",token);
        final String username=bundle.getString("username");

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        findViewById(R.id.newtask).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewTask.class);
                intent.putExtra("token",token);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        findViewById(R.id.noti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mNotificationManager.notify(NOTIFICATION_ID, createNotification(
                        true));
            }
        });




        findViewById(R.id.fresh).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                intent.putExtra("token",token);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        ServerAccess sa=new ServerAccess();
        sa.setOnTaskFinishedEvent(new ServerAccess.OnTaskExecutionFinished(){
            @Override
            public void OnTaskFinishedEvent(ArrayList<String> result){
                try {
                    JSONObject object = new JSONObject(result.get(0));
                    JSONArray array = object.getJSONArray("results");
                    for(int i=0;i<array.length();i++){
                        //
                        int m = array.length()-i-1;
                        String driver =((JSONObject)array.get(m)).getString("driver");
                        String customer=((JSONObject)array.get(m)).getString("customer");
                        String itemname=((JSONObject)array.get(m)).getString("itemName");
                        String status=((JSONObject)array.get(m)).getString("status");
                        String giver=((JSONObject)array.get(m)).getString("giverName");
                        String destinationAddress=((JSONObject)array.get(m)).getString("destinationAddress");
                        String cost=((JSONObject)array.get(m)).getString("cost");
                        String giverNumber=((JSONObject)array.get(m)).getString("giverNumber");
                        String originAddress=((JSONObject)array.get(m)).getString("originAddress");
                        String pickupTime=((JSONObject)array.get(m)).getString("pickupTime");

                                               //
                        Log.d(driver,customer);
                        infocard map = new infocard();
                        map.setUserName(customer);
                        map.setGiver(giver);
                        map.setDeliverAddress(destinationAddress);
                        map.setPickupAddress(originAddress);
                        map.setFee(Float.parseFloat(cost));
                        map.setGiverNum(giverNumber);
                        map.setRequestTime(pickupTime);

                        map.setDriver(driver);
                        map.setName(itemname);
                        map.setStatus(status);
                        mGist.add(map);
                    }
   initView();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        });


        ArrayList<String> url=new ArrayList<String>();
        url.add("https://173.236.252.231/rides/?format=json");
        //url.add("http://192.168.122.1/rides/?format=json");
        url.add("get");
        url.add("Authorization");
        Log.d("token",token);
        url.add("Token "+token);
        sa.execute(url);


    }


    Notification createNotification(boolean makeHeadsUpNotification){
        Notification.Builder notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.drawable.logo)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentTitle("Pika")
                .setContentText("Your request status has changed.");
        if (makeHeadsUpNotification) {
            Intent push = new Intent();
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            push.setClass(this,MainActivity.class);

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    push, PendingIntent.FLAG_CANCEL_CURRENT);
            notificationBuilder
                    .setContentText("Your request status has changed.")
                    .setFullScreenIntent(fullScreenPendingIntent, true);
        }
        return notificationBuilder.build();
    }

    public void initView(){
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        PickupAdapter pAdapter = new PickupAdapter(this,mGist);
        mRecyclerView.setAdapter(pAdapter);
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }


    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    private List<infocard> createList(int size) {
        List<infocard> result = new ArrayList<infocard>();
        for (int i=1; i <= size; i++) {
            infocard ci = new infocard();
            ci.setName("item"+i);
            ci.setGiver("giver"+i);
            ci.setUserName("user"+i);
            ci.setStatus("status"+i);
            result.add(ci);
        }
        return result;
    }

}
