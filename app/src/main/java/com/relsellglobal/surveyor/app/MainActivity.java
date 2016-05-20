package com.relsellglobal.surveyor.app;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.relsellglobal.surveyor.app.contentproviders.AppContentProvider;
import com.relsellglobal.surveyor.app.pojo.QuestionBean;
import com.relsellglobal.surveyor.app.pojo.ResponseBean;
import com.relsellglobal.surveyor.app.pojo.UserBean;
import com.relsellglobal.surveyor.app.util.MyAppUtility;
import com.relsellglobal.surveyor.app.ux.SurveyReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private EditText mPhoneId;
    private EditText mFullName;
    private EditText mPhoneNo;
    private EditText mEmail;
    private Button mButton;
    private UserBean userObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initUI();
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String phoneId = tm.getDeviceId();

        mPhoneId.setText(phoneId);
        mPhoneId.setEnabled(false);




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isEmpty = false;
                HashMap<String, String> map = new HashMap<>();
                map.put("method","createUser");
                map.put("phone_id",mPhoneId.getText().toString());
                map.put("full_name",mFullName.getText().toString());
                map.put("phone_no",mPhoneNo.getText().toString());
                map.put("email",mEmail.getText().toString());


                Set<String> keys = map.keySet();
                for(String key : keys) {
                    String value = map.get(key);
                    if(value == null || (value != null && value.equalsIgnoreCase("")) ) {
                        isEmpty = true;
                        break;
                    }
                }

                if(!isEmpty) {
                    new RegisterUserTask(map).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }
        });
    }

    public void initUI() {
        mPhoneId = (EditText) findViewById(R.id.phoneId_et);
        mButton = (Button)findViewById(R.id.submit_button);
        mFullName = (EditText)findViewById(R.id.Full_Name_et);
        mPhoneNo  = (EditText)findViewById(R.id.Phone_no_et);
        mEmail = (EditText)findViewById(R.id.email_et);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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


    public class RegisterUserTask extends AsyncTask<Void, Integer, Boolean> {


        StringBuffer responseString = new StringBuffer("");
        int mConnectionCode;
        HashMap<String, String> hmVars;


        public RegisterUserTask(HashMap<String, String> hm) {
            this.hmVars = hm;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {

                String server = MyAppUtility.getInstance().getServer();
                String port = MyAppUtility.getInstance().getPort();
                String urlToHit = "http://" + server + ":" + port + "/index.php";
                String urlData = getUrlStringData(hmVars);

                if (urlData.substring(urlData.length() - 1).equalsIgnoreCase("&")) {
                    urlData = urlData.substring(0, (urlData.length() - 1));
                }
                urlToHit +="?"+urlData;

                URL url = new URL(urlToHit);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.setDoInput(true);



                mConnectionCode = connection.getResponseCode();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (mConnectionCode == HttpURLConnection.HTTP_OK) {

                    InputStream inputStream = connection.getInputStream();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));

                    String line = "";

                    while ((line = rd.readLine()) != null) {
                        responseString.append(line);
                    }

                    return true;
                }
            } catch (IOException e) {
                Log.v("Message", e.getMessage());
                e.printStackTrace();
                return false;

            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean success) {

            if (success) {
                System.out.println("Response String "+responseString);

                if (responseString != null && !responseString.toString().equalsIgnoreCase("")) {

                    // put partial data user db
                   // JSONObject jsonObject = null;
                    //try {
                        //jsonObject = new JSONObject(responseString.toString());
                        //Object opId = jsonObject.get("op_id");
                        // update data in table for phone Id


/*
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }*/

                    userObj = new UserBean();
                    userObj.setPhoneId(mPhoneId.getText().toString());
                    userObj.setPhoneNo(mPhoneNo.getText().toString());
                    userObj.setFullName(mFullName.getText().toString());
                    userObj.setOpId(mEmail.getText().toString());
                    userObj.setEmail(mEmail.getText().toString());

                    Intent i = new Intent(MainActivity.this,SurveyReader.class);
                    i.putExtra("userObj",userObj);
                    startActivity(i);




                } else if (mConnectionCode >= HttpURLConnection.HTTP_BAD_REQUEST && mConnectionCode < HttpURLConnection.HTTP_INTERNAL_ERROR) {


                } else if (mConnectionCode >= HttpURLConnection.HTTP_MULT_CHOICE && mConnectionCode < HttpURLConnection.HTTP_BAD_REQUEST) {


                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);


        }
    }

    public String getUrlStringDataStr(HashMap<String, String> map) {
        String result = "";

        Set<String> myset = map.keySet();
        int setSize = myset != null ? myset.size() : 0;
        int i = 0;

        for (String key : myset) {
            String value = map.get(key);
            result += key + "=" + value;
            if (i <= setSize - 1)
                result += "&";
            i++;
        }
        return result;
    }

    public String getUrlStringData(HashMap<String, String> incomingHm) throws UnsupportedEncodingException {

        String result = "";

        Set<String> keys = incomingHm.keySet();
        HashMap<String, String> map = new HashMap<String, String>();
        for (String key : keys) {
            map.put(key, URLEncoder.encode(incomingHm.get(key), "UTF-8"));
        }
        result = getUrlStringDataStr(map);

        System.out.println(result);

        return result;
    }

}
