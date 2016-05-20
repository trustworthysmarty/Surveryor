package com.relsellglobal.surveyor.app.facebookinteraction;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.relsellglobal.surveyor.app.R;
import com.relsellglobal.surveyor.app.TempResponse;
import com.relsellglobal.surveyor.app.util.MyAppUtility;

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

// Add this to the header of your file:

/**
 * Created by anilkukreti on 25/11/15.
 */
public class ProcessActivity extends AppCompatActivity {


    CallbackManager callbackManager;

    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;

    ProfileTracker profileTracker;



    private EditText mUserNameET;
    private EditText mEmailET;
    private EditText mFBUserId;
    private Button mLoginButtonForTorrins;
    private LoginButton mLoginFBButton;
    private boolean mFbLoginDone;
    private String mFbLoginName;
    private String mFbLoginEmail;
    private String mFBuserIDRetrieved;
    private String mFbToken;

    private String phoneId;



    // Updated your class body:
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.facebook_interaction_layout);
        this.setFinishOnTouchOutside(false);

        FacebookSdk.setApplicationId(getString(R.string.facebook_app_id));

        mUserNameET = (EditText)findViewById(R.id.name);
        mEmailET = (EditText)findViewById(R.id.email);
        mLoginFBButton = (LoginButton)findViewById(R.id.login_button);
        mFBUserId = (EditText)findViewById(R.id.userId);

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        phoneId = tm.getDeviceId();











        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using
                // currentAccessToken when it's loaded or set.
                if(currentAccessToken == null) {

                }
            }
        };
        // If the access token is available already assign it.
        accessToken = AccessToken.getCurrentAccessToken();

        if(accessToken != null) {
            mLoginFBButton.setVisibility(View.GONE);
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            String act =  accessToken.getToken();
                            System.out.println("Access Token "+act);
                            // Application code
                            System.out.println("GraphResponse: " + "-------------" + object.toString());

                            try {

                                String name = object.getString("name");
                                String email = object.getString("email");
                                mFBuserIDRetrieved = object.getString("id");
                                System.out.println("NAME "+name);
                                System.out.println("Email "+email);

                                mFbLoginDone = true;
                                mFbLoginEmail = email;
                                mFbLoginName = name;
                                mFbToken = accessToken.getToken();

                                mUserNameET.setText(name);
                                mEmailET.setText(email);
                                mFBUserId.setText(mFBuserIDRetrieved);


                      } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            if(mFbLoginEmail == null) {
                                mFbLoginEmail = mFBuserIDRetrieved;
                            }

                            HashMap<String, String> map = new HashMap<>();
                            map.put("method","loginFB");
                            map.put("email",mFbLoginEmail);
                            map.put("phoneid",phoneId);
                            map.put("verified","1");
                            map.put("fb_id",mFBuserIDRetrieved);
                            map.put("fb_access",act);

                            new LoginFBUserTask(map).execute();



                        }
                    });




            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,gender,email");
            request.setParameters(parameters);
            request.executeAsync();

        } else {
            mLoginFBButton.setVisibility(View.VISIBLE);
        }



        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(
                    Profile oldProfile,
                    Profile currentProfile) {
                getUserData();
            }
        };







        mLoginFBButton = (LoginButton)findViewById(R.id.login_button);

        mLoginFBButton.setReadPermissions(Arrays.asList("public_profile"));


        // Other app specific specialization

        // Callback registration
        mLoginFBButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                // App code
                System.out.println("Login Successful");

                final AccessToken accessTokenLocal = loginResult.getAccessToken();

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                String act =  accessTokenLocal.getToken();
                                System.out.println("Access Token "+act);
                                // Application code
                                System.out.println("GraphResponse: " + "-------------" + object
                                        .toString());

                                try {

                                    String name = object.getString("name");
                                    String email = object.getString("email");
                                    String userId = object.getString("id");

                                    System.out.println("NAME "+name);
                                    System.out.println("Email "+email);

                                    mFbLoginDone = true;
                                    mFbLoginEmail = email;
                                    mFbLoginName = name;

                                    mUserNameET.setText(name);
                                    mEmailET.setText(email);
                                    mFBUserId.setText(userId);


                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("method","loginFB");
                                    map.put("email",email);
                                    map.put("password","xyz");
                                    map.put("phoneid",phoneId);
                                    map.put("verified","1");
                                    map.put("fb_id",userId);
                                    map.put("fb_access",act);

                                    new LoginFBUserTask(map).execute();









                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,email");
                request.setParameters(parameters);
                request.executeAsync();



            }

            @Override
            public void onCancel() {
                // App code
                System.out.println("Login Cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                System.out.println("Login Failure "+exception);
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        /*MyAppUtility.getInstance().getModifiedLessonsFragmentCallBack().loginImplementationForFB(1);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }


    public void getUserData() {

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/{user-id}",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
            /* handle the result */
                       JSONObject user = response.getJSONObject();

                        System.out.println(response);




                    }
                }
        ).executeAsync();

    }



    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }






    public String getPostDataStr(HashMap<String, String> map) {
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

    public String getPostData(HashMap<String, String> incomingHm) throws UnsupportedEncodingException {

        String result = "";

            Set<String> keys = incomingHm.keySet();
            HashMap<String, String> map = new HashMap<String, String>();
            for (String key : keys) {
                map.put(key, URLEncoder.encode(incomingHm.get(key), "UTF-8"));
            }
            result = getPostDataStr(map);

        return result;
    }



    public class LoginFBUserTask extends AsyncTask<Void, Integer, Boolean> {


        StringBuffer responseString = new StringBuffer("");
        int mConnectionCode;
        HashMap<String, String> hmVars;

        public LoginFBUserTask(HashMap<String, String> hmVars) {

            this.hmVars = hmVars;
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

                URL url = new URL(urlToHit);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");


                connection.setDoInput(true);

                String postData = getPostData(hmVars);

                if (postData.substring(postData.length() - 1).equalsIgnoreCase("&")) {
                    postData = postData.substring(0, (postData.length() - 1));
                }

                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(postData);
                writer.flush();
                writer.close();
                os.close();








                mConnectionCode = connection.getResponseCode();

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
                if (responseString != null && !responseString.toString().equalsIgnoreCase("")) {


                    Intent tempResponseIntent = new Intent(ProcessActivity.this, TempResponse.class);
                    tempResponseIntent.putExtra("responseString",responseString.toString());
                    startActivity(tempResponseIntent);
                    finish();


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



}
