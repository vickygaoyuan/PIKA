package com.example.vickygao.masproject;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
public class DetailCheckActivity extends FragmentActivity{
    TextView item;
    TextView status;
    TextView time;
    TextView givername;
    TextView givernum;
    TextView drivername;
    TextView drivernum;
    TextView fee;
    TextView pickaddress;
    TextView deliveraddress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_check);
        deliveraddress = (TextView)findViewById(R.id.detailDelivera);
        item = (TextView)findViewById(R.id.detailItem);
        pickaddress = (TextView)findViewById(R.id.detailPickupa);
        status = (TextView)findViewById(R.id.detailStatus);
        fee = (TextView)findViewById(R.id.detailFee);
        time = (TextView)findViewById(R.id.detailTime);
        givername = (TextView)findViewById(R.id.detailGiver);
        givernum = (TextView)findViewById(R.id.detailGivern);
        drivername = (TextView)findViewById(R.id.detailDriver);
        drivernum = (TextView)findViewById(R.id.detailDriverp);


        Bundle bundle=getIntent().getExtras();
        String i=bundle.getString("item");
        String da=bundle.getString("deliveryaddress");
        String pa=bundle.getString("pickupaddress");
        String s=bundle.getString("status");
        String f=Float.toString(bundle.getFloat("fee"));
        String g=bundle.getString("givername");
        String gn=bundle.getString("givernum");
        String d=bundle.getString("driver");
        String dn=bundle.getString("drivernum");
        String t=bundle.getString("pickupTime");

        Log.d("yume", "This is Debuginfo."+t+s);
       // String select = "select setter,hint from game_info where gameid=? AND step=?";
       // db = new DBHelper(getApplicationContext(), "store.db", null, 1);
       // sDatabase = db.getWritableDatabase();
        //Cursor seCursor = sDatabase.rawQuery(select, new String[]{t,s});
        //seCursor.moveToFirst();
        item.setText(i);
        time.setText(t);
        deliveraddress.setText(da);
        pickaddress .setText(pa);
        status.setText(s);
        fee.setText(f);
        givername.setText(g);
        givernum.setText(gn);
        drivername.setText(d);
        drivernum.setText(dn);
        //hint.setText(seCursor.getString(1));
       // seCursor.close();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

}