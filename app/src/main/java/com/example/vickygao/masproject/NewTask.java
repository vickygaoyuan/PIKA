package com.example.vickygao.masproject;

import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalItem;
import com.paypal.android.sdk.payments.PayPalOAuthScopes;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalPaymentDetails;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.paypal.android.sdk.payments.ShippingAddress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Config;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NewTask extends FragmentActivity {
    private static final String TAG = "paymentExample";
    public String item = "";
    public String status = "";
    public String pickaddress = "";
    public static final String PREFS_NAME = "MyPrefsFile";
    public String deliveryaddress = "";
    public String fee = "";
    public String givername = "";
    public String givernum = "";
    public String time = "";
    public String newDate = "";
    public String fulltime = "";
    public String newTime = "";
    public String verifynum="";
    double lat=0;
    double lng=0;
    double dlat;
    String rideID;
    public String shipfee;

    double dlng;
    EditText nameField;
    EditText fieldB;
    EditText fieldC;
    EditText fieldD;
    EditText fieldE ;
    EditText fieldF;
    double price=0;
    EditText fieldG;
    String token="";
    String username="";
    private boolean validInput = false;

    //paypal status codes:
    /*
    * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test credentials
    * from https://developer.paypal.com
    *
    * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
    * without communicating to PayPal's servers.
    */
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

    private static final String CONFIG_CLIENT_ID = "AU6gWzqLqliEBfi-I2xWRYLD7ro591oDHfUE4x7qJC0uMxQE0rCjRIrxEvozeU3wZv5Lqz4o6iBFiZje";

    private static final int REQUEST_CODE_PAYMENT = 1;
    //set the paypal environment
    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        findViewById(R.id.submittask).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLatLongFromAddress();

            }
        });
        nameField = (EditText)findViewById(R.id.newProductName);
        fieldB = (EditText)findViewById(R.id.newPickAddress);
        fieldC = (EditText)findViewById(R.id.newDelAddress);
        fieldD = (EditText)findViewById(R.id.newDelDate);
        fieldE = (EditText)findViewById(R.id.newDelTime);

        fieldF = (EditText)findViewById(R.id.newGiverName);
        fieldG = (EditText)findViewById(R.id.newGiverNum);

        // Starting PayPal service
        Bundle bundle=getIntent().getExtras();
        token=bundle.getString("token");
        username=bundle.getString("username");
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showPriceDialog(){
        updateInputTask();



        ServerAccess sa2=new ServerAccess();
        sa2.setOnTaskFinishedEvent(new ServerAccess.OnTaskExecutionFinished() {
            @Override
            public void OnTaskFinishedEvent(ArrayList<String> result2) {
                try {
                    JSONObject object = new JSONObject(result2.get(0));

                    shipfee = object.getString("cost");
                    rideID= object.getString("id");
                    Log.d("token cost",shipfee);
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewTask.this);

                    String message ="Your total price for this is $"+ shipfee;
                    builder.setMessage(message);
                    builder.setTitle("Confirmation");

                    builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {





                            //if(validInput){
                            //  sendInputTask();
                            //}
                            //OK

                            //start paypal payment process;
                            launchPayPalPayment();
                /*
                Intent intent = new Intent();
                intent.setClass(NewTask.this, SuccessSubmitActivity.class);


                intent.putExtra("item",item);
                intent.putExtra("deliveryaddress",deliveryaddress);
                intent.putExtra("fee",fee);
                intent.putExtra("pickaddress",pickaddress);
                intent.putExtra("givername",givername);
                intent.putExtra("time",time);
                intent.putExtra("givernum",givernum);
                startActivity(intent);
                */








                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Cancel
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();

                }
                Log.d("error257",result2.get(0).trim().toLowerCase());


            }
        });





        try {

            Log.d("lat1",Double.toString(dlat));
            Log.d("lng1",Double.toString(dlng));
            Log.d("lat",Double.toString(lat));
            Log.d("lng",Double.toString(lng));

            JSONObject object2 = new JSONObject();
            object2.put("cost",price);
            object2.put("itemName",item);
            object2.put("itemDescription","none");
            object2.put("customer",1);
            object2.put("destinationAddress",deliveryaddress);
            object2.put("destinationCoordinateLat",dlat);
            object2.put("destinationCoordinateLong",dlng);
            object2.put("distance",dlat-lat+dlng-lng);
            object2.put("driver",null);
            object2.put("giverName",givername);
            object2.put("giverNumber",givernum);
            object2.put("originAddress",pickaddress);
            object2.put("destinationAddress",deliveryaddress);
            object2.put("originCoordinateLat",lat);
            object2.put("originCoordinateLong",lng);
            object2.put("pickupTime",fulltime);
            object2.put("status","N/A");
            object2.put("timeStamp","2015-04-06T16:52:27Z");

            String posttoken = object2.toString();
            ArrayList<String> url2=new ArrayList<String>();
            url2.add("https://173.236.252.231/rides/?format=json");
            url2.add("newrequest");
            url2.add("Authorization");
            Log.d("token",token);
            Log.d("token",posttoken);

            url2.add("Token "+token);
            url2.add("Content-Type");
            url2.add("application/json");
            url2.add(posttoken);
            sa2.execute(url2);

        }catch(Exception e) {
            e.printStackTrace();
        }



    }

    public void updateInputTask(){

        item = nameField.getText().toString();
        status = "No Deliver";
        pickaddress = fieldB.getText().toString();
        deliveryaddress = fieldC.getText().toString();
        newDate = fieldD.getText().toString();
        newTime = fieldE.getText().toString();
        int x1 = newDate.indexOf("/");

        int x2 = newDate.indexOf("/",x1+1);
time= newDate+ newTime;
        String mchange="";
        String dchange="";

        if (newDate.substring(0,x1).length()==1)
        {mchange="0"+newDate.substring(0,x1);}
        else mchange =newDate.substring(0,x1);
        if (newDate.substring(x1+1,x2).length()==1)
        {dchange="0"+newDate.substring(x1+1,x2);}
        else dchange =newDate.substring(x1+1,x2);
        fulltime=newDate.substring(x2+1,newDate.length())+"-"+dchange+"-"+mchange+"T"+newTime;
        givername = fieldF.getText().toString();
        givernum = fieldG.getText().toString();
        fee = "$10";

        if (newDate.length() > 0 && newTime.length() > 0) {
            time = newDate + "  " + newTime;
            validInput = true;
        }else{
            validInput = false;
        }
    }

    public void sendInputTask(){

        Intent intent = new Intent();
        intent.setClass(NewTask.this, SuccessSubmitActivity.class);
        intent.putExtra("item",item);
        intent.putExtra("deliveryaddress",deliveryaddress);
        intent.putExtra("fee",shipfee);
        intent.putExtra("pickaddress",pickaddress);
        intent.putExtra("givername",givername);
        intent.putExtra("time",time);
        intent.putExtra("givernum",givernum);
        intent.putExtra("token",token);
        intent.putExtra("username",username);
        startActivity(intent);

    }
    /**
     * Preparing final cart amount that needs to be sent to PayPal for payment
     **/
    private PayPalPayment getThingToBuy(String paymentIntent) {
        return new PayPalPayment(new BigDecimal(shipfee), "USD", "Pika delivery fee",
                paymentIntent);
    }

    /**
     * Launching PalPay payment activity to complete the payment
     **/
    /*
         * PAYMENT_INTENT_SALE will cause the payment to complete immediately.
         * Change PAYMENT_INTENT_SALE to
         *   - PAYMENT_INTENT_AUTHORIZE to only authorize payment and capture funds later.
         *   - PAYMENT_INTENT_ORDER to create a payment for authorization and capture
         *     later via calls from your server.
         *
         * Also, to include additional payment details and an item list, see getStuffToBuy() below.
         */
    private void launchPayPalPayment() {

        PayPalPayment thingsToBuy = getThingToBuy(PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(NewTask.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingsToBuy);
        startActivityForResult(intent, REQUEST_CODE_PAYMENT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_task, menu);
        return true;
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

    private PayPalOAuthScopes getOauthScopes() {
        /* create the set of required scopes
         * Note: see https://developer.paypal.com/docs/integration/direct/identity/attributes/ for mapping between the
         * attributes you select for this app in the PayPal developer portal and the scopes required here.
         */
        Set<String> scopes = new HashSet<String>(
                Arrays.asList(PayPalOAuthScopes.PAYPAL_SCOPE_EMAIL, PayPalOAuthScopes.PAYPAL_SCOPE_ADDRESS) );
        return new PayPalOAuthScopes(scopes);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentConfirmation confirm =
                        data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4));
                        Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
                        JSONObject resp =  confirm.toJSONObject().getJSONObject("response");

                        verifynum = resp.get("id").toString();
                        Log.i(TAG, verifynum);
                        String a[] = verifynum.split("-");
                        Log.d("trid",verifynum);
                        Log.d("rideID",rideID);




                        ServerAccess sa2=new ServerAccess();
                        sa2.setOnTaskFinishedEvent(new ServerAccess.OnTaskExecutionFinished() {
                                                       @Override
                                                       public void OnTaskFinishedEvent(ArrayList<String> result2) {
                                                           Log.d("error",result2.get(0).trim().toLowerCase());}});
                        try {
                            JSONObject object2 = new JSONObject();
                            object2.put("rideID",Integer.parseInt(rideID));
                            object2.put("transactionID",verifynum);
                            String posttoken = object2.toString();
                            ArrayList<String> url2=new ArrayList<String>();
                            url2.add("https://173.236.252.231/customer/?format=json");
                            url2.add("postid");
                            url2.add(posttoken);
                            url2.add("Authorization");
                            Log.d("token",token);
                            url2.add("Token "+token);
                            Log.d("trid",verifynum);
                            Log.d("rideID",rideID);
                            sa2.execute(url2);

                        }catch(Exception e) {
                            e.printStackTrace();
                        }









                        //hhtp post with tid
                        /**
                         *  TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.
                         *
                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */
                        Toast.makeText(
                                getApplicationContext(),
                                "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG)
                                .show();
                        sendInputTask();









                    } catch (JSONException e) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                        TAG,
                        "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
            }
        }
    }

    @Override
    public void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public  void getLatLongFromAddress() {

        pickaddress = fieldB.getText().toString();
        deliveryaddress = fieldC.getText().toString();

        String youraddress1 = pickaddress;
        String youraddress2 = deliveryaddress;

        try {
            youraddress1 = URLEncoder.encode(youraddress1, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            youraddress2 = URLEncoder.encode(youraddress2, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        String uri1 = "http://www.maps.google.com/maps/api/geocode/json?address=" +
                youraddress1 + "&sensor="+URLEncoder.encode("false");
        String uri2 = "http://www.maps.google.com/maps/api/geocode/json?address=" +
                youraddress2 + "&sensor="+URLEncoder.encode("false");

        ServerAccess sa2=new ServerAccess();
        sa2.setOnTaskFinishedEvent(new ServerAccess.OnTaskExecutionFinished() {
            @Override
            public void OnTaskFinishedEvent(ArrayList<String> result2) {
                Log.d("error",result2.get(0).trim().toLowerCase());
                    try {
                        JSONObject jsonObject = new JSONObject(result2.get(0));
                        JSONArray array = jsonObject.getJSONArray("results");

                        lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lng");
                        Log.d("lng",Double.toString(lng));

                        lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                                .getJSONObject("geometry").getJSONObject("location")
                                .getDouble("lat");
                        Log.d("lat",Double.toString(lat));


                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



            }
        });
        try {
            ArrayList<String> url2=new ArrayList<String>();
            url2.add("https://173.236.252.231/customer/?format=json");
            url2.add("getlocation");
            url2.add(uri1);
            sa2.execute(url2);

        }catch(Exception e) {
            e.printStackTrace();
        }
        ServerAccess sa3=new ServerAccess();
        sa3.setOnTaskFinishedEvent(new ServerAccess.OnTaskExecutionFinished() {
            @Override
            public void OnTaskFinishedEvent(ArrayList<String> result2) {
                Log.d("error",result2.get(0).trim().toLowerCase());
                try {
                    JSONObject jsonObject = new JSONObject(result2.get(0));
                    JSONArray array = jsonObject.getJSONArray("results");

                    dlng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lng");
                    Log.d("dlng",Double.toString(dlng));

                    dlat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                            .getJSONObject("geometry").getJSONObject("location")
                            .getDouble("lat");
                    Log.d("dlat",Double.toString(dlat));
                    showPriceDialog();


                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }




            }
        });
        try {
            ArrayList<String> url3=new ArrayList<String>();
            url3.add("https://173.236.252.231/customer/?format=json");
            url3.add("getlocation");
            url3.add(uri2);
            sa3.execute(url3);

        }catch(Exception e) {
            e.printStackTrace();
        }




    }





}
