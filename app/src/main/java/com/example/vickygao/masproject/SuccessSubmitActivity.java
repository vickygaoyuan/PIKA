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
public class SuccessSubmitActivity extends FragmentActivity{
    TextView item;
  //  TextView status;
    TextView time;
    TextView givername;
    TextView givernum;
 //   TextView drivername;
 //   TextView drivernum;
    TextView fee;
    TextView pickaddress;
    TextView deliveraddress;
    Button ok;

   // SQLiteDatabase sDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_submit);
        ok = (Button)findViewById(R.id.okBtn);
        deliveraddress = (TextView)findViewById(R.id.submitDelivera);
        item = (TextView)findViewById(R.id.submitItem);
        pickaddress = (TextView)findViewById(R.id.submitPickupa);
        fee = (TextView)findViewById(R.id.submitFee);
        time = (TextView)findViewById(R.id.submitTime);
        givername = (TextView)findViewById(R.id.submitGiver);
        givernum = (TextView)findViewById(R.id.submitGivern);

        Bundle bundle=getIntent().getExtras();
        final String i=bundle.getString("item");
        final String da=bundle.getString("deliveryadress");
        final String pa=bundle.getString("pickaddress");
        final String f=bundle.getString("fee");
        final String g=bundle.getString("givername");
        final String gn=bundle.getString("givernum");
        final String t=bundle.getString("time");
        final String token=bundle.getString("token");
        final String username =bundle.getString("username");

        Log.d("yume", "This is Debuginfo."+t);
        // String select = "select setter,hint from game_info where gameid=? AND step=?";
        // db = new DBHelper(getApplicationContext(), "store.db", null, 1);
        // sDatabase = db.getWritableDatabase();
        //Cursor seCursor = sDatabase.rawQuery(select, new String[]{t,s});
        //seCursor.moveToFirst();
        item.setText(i);
        time.setText(t);
        deliveraddress.setText(da);
        pickaddress .setText(pa);
        fee.setText(f);
        givername.setText(g);
        givernum.setText(gn);
        //hint.setText(seCursor.getString(1));
        // seCursor.close();
        ok.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {


                // String select_sql = "select username,password from user_info where username = '"
                //       + i + "'";
                //   Cursor cursor = sDatabase.rawQuery(select_sql, null);
                // cursor.moveToFirst();
                //    try
                // {
                //   userName = cursor.getString(0);
                // userPw = cursor.getString(1);
                //   } catch (Exception e)
                // {
                //   // TODO: handle exception
                // userName = "";
                //     userPw = "";
                // }

                    Intent intent = new Intent();
                    //   Bundle bundle = new Bundle();
                    //    bundle.putString("username",name);
                    //    intent.putExtras(bundle);
                    intent.setClass(SuccessSubmitActivity.this, MainActivity.class);
                    intent.putExtra("item",i);
                intent.putExtra("deliveryadress",da);
                intent.putExtra("pickaddress",pa);
                intent.putExtra("fee",f);
                intent.putExtra("givername",g);
                intent.putExtra("givernum",gn);
                intent.putExtra("time",t);

                intent.putExtra("token",token);
                intent.putExtra("username",username);
                    startActivity(intent);

                    //    Toast.makeText(context, "Successfully log in", Toast.LENGTH_SHORT).show();


                }
            });
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