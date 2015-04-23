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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class LoginActivity extends FragmentActivity {
    EditText email;
    EditText password;
    String name="";
    public ProgressDialog pd;
    String pass="";
    public static final String PREFS_NAME = "MyPrefsFile";
    DBHelper db;
    Button btnLogin;
    SQLiteDatabase sDatabase = null;
    private Context context;
    public LoginActivity(){
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////    db = new DBHelper(context, "store.db", null, 1);
        //     sDatabase = db.getWritableDatabase();
        setContentView(R.layout.activity_login);
        email = (EditText)findViewById(R.id.loginEmail);
        password = (EditText)findViewById(R.id.loginPassword);
        btnLogin = (Button)findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pd = new ProgressDialog(LoginActivity.this);
                pd.setMessage("Logging in...");
                pd.show();
                //  String i = email.getText().toString();
                name = email.getText().toString();
                pass = password.getText().toString();
                context= LoginActivity.this;

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

                if (name.equals(""))
                {
                    new AlertDialog.Builder(context).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("Alarm").setMessage("You have to enter an username and a password").create().show();
                    pd.dismiss();
                }
                else if (pass.equals(""))
                {
                    new AlertDialog.Builder(context).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("Alarm").setMessage("You have to enter an username and a password").create().show();
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

                    ServerAccess sa2=new ServerAccess();
                    sa2.setOnTaskFinishedEvent(new ServerAccess.OnTaskExecutionFinished() {
                        @Override
                        public void OnTaskFinishedEvent(ArrayList<String> result2) {
                            Log.d("error",result2.get(0).trim().toLowerCase());

                            if (result2.get(0).trim().toLowerCase().equals("{\"non_field_errors\":[\"unable to log in with provided credentials.\"]}")) {
                                new AlertDialog.Builder(LoginActivity.this).setIcon(getResources().getDrawable(R.drawable.login_error_icon)).setTitle("Alarm").setMessage("The username and password are not matched!").create().show();
                                pd.dismiss();
                            } else {
                                try {
                                    JSONObject object = new JSONObject(result2.get(0));

                                    String token = object.getString("token");
                                    pd.dismiss();
                                    Intent intent = new Intent();
                                    intent.putExtra("token",token);
                                    intent.putExtra("username",name);

                                    intent.setClass(LoginActivity.this, MainActivity.class);

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
                        object2.put("username",name);
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

