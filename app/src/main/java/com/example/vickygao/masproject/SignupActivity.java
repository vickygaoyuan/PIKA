package com.example.vickygao.masproject;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.app.AlertDialog;
import android.util.Log;

import android.app.ProgressDialog;
import android.content.Intent;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;


public class SignupActivity extends FragmentActivity {
    EditText email;
    EditText password;
    EditText name;
    EditText card;
    EditText phone;
    String namee="";
    String emaill;
    public static final String PREFS_NAME = "MyPrefsFile";
    String cardd;
    String phonee;
    public ProgressDialog pd;
    String pass="";
    DBHelper db;
    Button btnSignup;
    SQLiteDatabase sDatabase = null;
    private Context context;
    public SignupActivity(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        ////    db = new DBHelper(context, "store.db", null, 1);
        //     sDatabase = db.getWritableDatabase();
        setContentView(R.layout.activity_signup);
        email = (EditText)findViewById(R.id.signupEmail);
        password = (EditText)findViewById(R.id.signupPassword);
        name = (EditText)findViewById(R.id.signupName);
        card = (EditText)findViewById(R.id.signupCard);
        phone = (EditText)findViewById(R.id.signupPhone);
        btnSignup = (Button)findViewById(R.id.signupButton);
        btnSignup.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pd = new ProgressDialog(SignupActivity.this);
                pd.setMessage("Logging in...");
                pd.show();
                //  String i = email.getText().toString();
                emaill = email.getText().toString();
                pass = password.getText().toString();
                namee = name.getText().toString();
                phonee = phone.getText().toString();
                cardd = card.getText().toString();
                context= SignupActivity.this;

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

                if (namee.equals("") || pass.equals("")||phonee.equals("")||namee.equals("")||cardd.equals("") )
                {
                    new AlertDialog.Builder(context).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("Alarm").setMessage("You have to enter all the personal information").create().show();
                    pd.dismiss();
                }
                //   else if (!(email.getText().toString().equals(userName) && password
                //         .getText().toString().equals(userPw)))
                //{
                //  new AlertDialog.Builder(context).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("Alarm").setMessage("Not Match！").create().show();
                //  pd.dismiss();
                //  }
                //					ȫ����ȷ��ת����������
                else
                {


                    ServerAccess sa=new ServerAccess();
                    sa.setOnTaskFinishedEvent(new ServerAccess.OnTaskExecutionFinished() {
                        @Override
                        public void OnTaskFinishedEvent(ArrayList<String> result) {
                            Log.d("error",result.get(0).trim().toLowerCase());
                            if (result.get(0).trim().toLowerCase().equals("{\"user\":{\"username\":[\"this field must be unique.\"]}}")) {
                                new AlertDialog.Builder(SignupActivity.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("Alarm").setMessage("Email is already existed!" ).create().show();
                                pd.dismiss();
                            } else {

                                ServerAccess sa2=new ServerAccess();
                                sa2.setOnTaskFinishedEvent(new ServerAccess.OnTaskExecutionFinished() {
                                    @Override
                                    public void OnTaskFinishedEvent(ArrayList<String> result2) {
                                        if (result2.get(0).trim().toLowerCase().equals("error")) {
                                            new AlertDialog.Builder(SignupActivity.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("Alarm").setMessage("Error!" + result2.get(0)).create().show();
                                            pd.dismiss();
                                        } else {
                                            try {
                                                JSONObject object = new JSONObject(result2.get(0));

                                                String token = object.getString("token");
                                                pd.dismiss();
                                                Intent intent = new Intent();
                                                intent.putExtra("token",token);
                                                intent.putExtra("username",emaill);

                                                intent.setClass(SignupActivity.this, MainActivity.class);

                                                startActivity(intent);

                                            } catch (JSONException e) {
                                                // TODO Auto-generated catch block
                                                e.printStackTrace();
                                            }


                                        }
                                    }
                                });
                                try {
                                    JSONObject object2 = new JSONObject();
                                    object2.put("username",emaill);
                                    object2.put("password",pass);
                                    String posttoken = object2.toString();
                                    ArrayList<String> url2=new ArrayList<String>();
                                    url2.add("https://173.236.252.231/customer/?format=json");
                                    url2.add("posttoken");
                                    url2.add(posttoken);
                                    sa2.execute(url2);

                                }catch(Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                    try {
                    JSONObject user = new JSONObject();
                    user.put("username", emaill);
                    user.put("password", pass);
                    JSONObject device = new JSONObject();
                    device.put("deviceType", "android");
                    device.put("deviceId", "xxxxx");
                    JSONObject object = new JSONObject();
                    object.put("user",user);
                    object.put("phoneNumber","1122213333");
                    object.put("device",device);
                        object.put("bankName","BOA");
                        object.put("accountNumber","12345");
                        object.put("routingNumber","12345");
                        object.put("license","12345");
                        object.put("accountNumber","12345");
                        String postnew = object.toString();

                            ArrayList<String> url=new ArrayList<String>();
                                url.add("https://173.236.252.231/customer/?format=json");
                              url.add("postnew");
                            url.add(postnew);
                        sa.execute(url);


                    }catch(Exception e) {
                        e.printStackTrace();
                    }







               //     ArrayList<String> url=new ArrayList<String>();
            //        url.add("https://173.236.252.231/customer/?format=json");
              //      url.add("post");
                //    url.add("username");
                  //  url.add(namee);
                  //  url.add("password");
                   // url.add(pass);
                    //sa.execute(url);


                //     cursor.close();

                //    Toast.makeText(context, "Successfully log in", Toast.LENGTH_SHORT).show();




            }
            }});
    }
}

//  @Override
// public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
//   getMenuInflater().inflate(R.menu.menu_layout, menu);
// return true;
//}

//@Override
//public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
//  int id = item.getItemId();

//noinspection SimplifiableIfStatement
//if (id == R.id.action_settings) {
//   return true;
//}

//   return super.onOptionsItemSelected(item);
//}

