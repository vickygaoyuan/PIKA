package com.example.vickygao.masproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public  class ServerAccess extends AsyncTask<ArrayList<String>,Void,ArrayList<String>>{

    private OnTaskExecutionFinished _task_finished_event;

    public interface OnTaskExecutionFinished{
        public void OnTaskFinishedEvent(ArrayList<String> result);
    }

    public void setOnTaskFinishedEvent(OnTaskExecutionFinished onTaskExecutionFinished)
    {
        if(onTaskExecutionFinished != null)this._task_finished_event = onTaskExecutionFinished;
    }

    @Override
    protected ArrayList<String> doInBackground(ArrayList<String>... params) {
        HttpClient   httpclient = getNewHttpClient();

        HttpResponse response;
        String type=params[0].get(1);

        // Depends on your web service
        //httppost.setHeader("Content-type", "application/json");
        String result="";
        InputStream inputStream = null;

        try {
            if(type=="get"){

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                for(int i=2;i< params[0].size()-1;i+=2){
                    nameValuePairs.add(new BasicNameValuePair(params[0].get(i),params[0].get(i+1)));
                }
                String paramString = URLEncodedUtils.format(nameValuePairs, "utf-8");
                String url=params[0].get(0);
                Log.d("AS",url);
                HttpGet httppost = new HttpGet(url);
                httppost.setHeader(params[0].get(2), params[0].get(3));

                response = httpclient.execute(httppost);
            }
            else if(type=="postnew"){
                HttpPost post = new HttpPost("https://173.236.252.231/customer/?format=json");
                StringEntity se = new StringEntity( params[0].get(2));
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                response = httpclient.execute(post);
            }
            else if(type=="posttoken"){
                HttpPost post = new HttpPost("https://173.236.252.231/api-token-auth/?format=json");
                StringEntity se = new StringEntity( params[0].get(2));
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                response = httpclient.execute(post);
            }
            else if(type=="newrequest"){
                HttpPost post = new HttpPost("https://173.236.252.231/rides/?format=json");
                StringEntity se = new StringEntity( params[0].get(6));
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                post.setHeader(params[0].get(2), params[0].get(3));
                post.setHeader(params[0].get(4), params[0].get(5));

                response = httpclient.execute(post);
            }
   else if(type=="getlocation"){
                String url=params[0].get(2);
                Log.d("AS",url);
                HttpGet httppost = new HttpGet(url);
                response = httpclient.execute(httppost);
            }

            else if(type=="postid"){
                HttpPost post = new HttpPost("https://173.236.252.231/paypal/?format=json");
                StringEntity se = new StringEntity( params[0].get(2));
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                post.setEntity(se);
                post.setHeader(params[0].get(3), params[0].get(4));
                response = httpclient.execute(post);
            }
            else{
                HttpPost httppost= new HttpPost(params[0].get(0));
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                for(int i=2;i< params[0].size()-1;i++){

                    nameValuePairs.add(new BasicNameValuePair(params[0].get(i),params[0].get(i+1)));
                }
                httppost.setHeader(params[0].get(2), params[0].get(3));
                httppost.setHeader(params[0].get(4),params[0].get(5));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                response = httpclient.execute(httppost);
            }
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();

        }catch (ClientProtocolException e) {
            result ="client protocol error";
        }  catch (IOException e) {
            Log.d("IOERROR", e.getMessage());
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        ArrayList<String> a= new ArrayList<String>();
        a.add(result);
        return a;
    }
    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
    public String parseTheJSON(String responseText){
        Log.d("what",responseText);
        try {
            JSONObject json = new JSONObject(responseText);
            String str_value=json.getString("state");
            return str_value;
        } catch (JSONException e) {

            e.printStackTrace();
            return "";
        }

    }

    @Override
    protected void onPostExecute(ArrayList<String> result)
    {
        super.onPostExecute(result);
        if(this._task_finished_event != null)
        {
            for (String s : result) Log.d("what did u got", s);
            this._task_finished_event.OnTaskFinishedEvent(result);
        }
        else
        {
            Log.d("SomeClass", "task_finished even is null");
        }
    }
}
