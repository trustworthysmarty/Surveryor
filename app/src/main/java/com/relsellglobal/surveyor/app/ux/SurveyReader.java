package com.relsellglobal.surveyor.app.ux;


import android.content.ContentValues;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.relsellglobal.surveyor.app.MainActivity;
import com.relsellglobal.surveyor.app.R;
import com.relsellglobal.surveyor.app.TempResponse;
import com.relsellglobal.surveyor.app.contentproviders.AppContentProvider;
import com.relsellglobal.surveyor.app.pojo.QuestionBean;
import com.relsellglobal.surveyor.app.pojo.ResponseBean;
import com.relsellglobal.surveyor.app.pojo.SurveyBean;
import com.relsellglobal.surveyor.app.pojo.UserBean;
import com.relsellglobal.surveyor.app.util.MyAppUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by anilkukreti on 11/05/16.
 */
public class SurveyReader extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    LinearLayout mLinearLayout;
    private SurveyBean surveyObj;
    private Button submitButton;
    private String phoneId;
    private UserBean userObj;
    private HashMap<String,String> responsesCollectorMap;
    private HashMap<String,RadioGroup> mRadioGroupMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.survey_main);

        Toolbar toolBar = (Toolbar)findViewById(R.id.toolbar);
        toolBar.setTitle("DemoGraphics");

        userObj = getIntent().getParcelableExtra("userObj");

        mLinearLayout = (LinearLayout) findViewById(R.id.dynamicLayout);
        submitButton = (Button)findViewById(R.id.submit_button);


        mRadioGroupMap = new HashMap<>();
        responsesCollectorMap = new HashMap<>();


        HashMap<String, String> map = new HashMap<>();
        map.put("method", "getSurvey");
        map.put("survey_id", "1");

        new GetSurvey(map).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Set<String> keys = mRadioGroupMap.keySet();
                for(String key : keys) {
                    RadioGroup rg1 = mRadioGroupMap.get(key);
                    int selected=rg1.getCheckedRadioButtonId();
                    RadioButton rb1=(RadioButton)findViewById(selected);
                    responsesCollectorMap.put(key,rb1.getTag().toString());
                }
                System.out.println("response collector "+responsesCollectorMap);

                Set<String> keys2 = responsesCollectorMap.keySet();

                for(String key : keys2) {

                    String value = responsesCollectorMap.get(key);
                    if(key.equalsIgnoreCase("3")) {
                        userObj.setState(value);
                    } else if(key.equalsIgnoreCase("2")) {
                        userObj.setDob(value);
                    } else if (key.equalsIgnoreCase("1")) {
                        userObj.setGender(value);
                    } else if(key.equalsIgnoreCase("10")) {
                        userObj.setReligion(value);
                    } else if(key.equalsIgnoreCase("7")) {
                        userObj.setEmployment(value);
                    } else if(key.equalsIgnoreCase("5")) {
                        userObj.setEducation(value);
                    } else if(key.equalsIgnoreCase("4")) {
                        userObj.setIncome(value);
                    } else if(key.equalsIgnoreCase("9")) {
                        userObj.setCity(value);
                    } else if(key.equalsIgnoreCase("8")) {
                        userObj.setIndustry(value);
                    } else if(key.equalsIgnoreCase("11")) {
                        userObj.setMarital(value);
                    }

                }

                System.out.println("user Object : "+userObj);

                // write userObj to db Usertable

                ContentValues values = new ContentValues();
                values.put(AppContentProvider.UserTable.OP_ID,userObj.getOpId());
                values.put(AppContentProvider.UserTable.PHONE_ID,userObj.getPhoneId());
                values.put(AppContentProvider.UserTable.PASSWORD,userObj.getPassword());
                values.put(AppContentProvider.UserTable.FB_ID,userObj.getFbId());
                values.put(AppContentProvider.UserTable.GO_ID,userObj.getGoId());
                values.put(AppContentProvider.UserTable.FULL_NAME,userObj.getFullName());
                values.put(AppContentProvider.UserTable.PHONE_NO,userObj.getPhoneNo());
                values.put(AppContentProvider.UserTable.DOB,userObj.getDob());
                values.put(AppContentProvider.UserTable.GENDER,userObj.getGender());
                values.put(AppContentProvider.UserTable.CITY,userObj.getCity());
                values.put(AppContentProvider.UserTable.STATE,userObj.getState());
                values.put(AppContentProvider.UserTable.EDUCATION,userObj.getEducation());
                values.put(AppContentProvider.UserTable.INCOME,userObj.getIncome());
                values.put(AppContentProvider.UserTable.EMPLOYMENT,userObj.getEmployment());
                values.put(AppContentProvider.UserTable.INDUSTRY,userObj.getIndustry());
                values.put(AppContentProvider.UserTable.MARITAL,userObj.getMarital());
                values.put(AppContentProvider.UserTable.RELIGION,userObj.getReligion());
                values.put(AppContentProvider.UserTable.ETHNICITY,userObj.getEtnicity());

                SurveyReader.this.getContentResolver().insert(AppContentProvider.UserTable.CONTENT_URI,values);

                MyAppUtility.getInstance().exportDB();


            }
        });


    }

    public void getDataFromDB(int queryNo) {
        Log.v("TAG", "inside get data from db");
        Bundle b = new Bundle();
        if (queryNo == 1) {
            b.putStringArray("projection", new String[]{
                    AppContentProvider.QuestionTable._ID,
                    AppContentProvider.QuestionTable.QUESTION,
                    AppContentProvider.QuestionTable.QUESTION_RESPONSE_TYPE,
            });
            b.putString("selection", null);
            b.putStringArray("selectionArgs", null);
            b.putString("sortOrder", null);
        }
        Loader local = getSupportLoaderManager().initLoader(queryNo, b, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v("TAG", "inside on create loader");
        Uri CONTENT_URI = null;
        String[] projection = args.getStringArray("projection");
        String selection = args.getString("selection");
        String[] selectionArgs = args.getStringArray("selectionArgs");
        String sortOrder = args.getString("sortOrder");
        if (id == 1) {
            CONTENT_URI = AppContentProvider.QuestionTable.CONTENT_URI;
        }
        return new CursorLoader(this, CONTENT_URI, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {


        if (loader.getId() == 1) {

            if (data.getCount() != 0) {
                while (data.moveToNext()) {

                    String question = data.getString(data.getColumnIndexOrThrow(AppContentProvider.QuestionTable.QUESTION));
                    String responseType = data.getString(data.getColumnIndexOrThrow(AppContentProvider.QuestionTable.QUESTION_RESPONSE_TYPE));

                    if (Integer.parseInt(responseType) == 1) {

                        TextView tv = new TextView(SurveyReader.this);
                        tv.setText(question);
                        RadioGroup rg = new RadioGroup(SurveyReader.this);
                        RadioButton rb = new RadioButton(SurveyReader.this);
                        rb.setText("Male");
                        rg.addView(rb);
                        mLinearLayout.addView(tv);
                        mLinearLayout.addView(rg);
                    }


                }
            }

        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    // get data from DB and display in the survey


    public class GetSurvey extends AsyncTask<Void, Integer, Boolean> {


        StringBuffer responseString = new StringBuffer("");
        int mConnectionCode;
        HashMap<String, String> hmVars;


        public GetSurvey(HashMap<String, String> hm) {
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
                urlToHit += "?" + urlData;

                URL url = new URL(urlToHit);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                connection.setDoInput(true);


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

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
            ArrayList<QuestionBean> questionBeanArrayList = new ArrayList<>();

            if (success) {
                System.out.println("Response String " + responseString);


                if (responseString != null && !responseString.toString().equalsIgnoreCase("")) {
                    surveyObj = new SurveyBean();
                    // Now parse response string and put that in DB

                    try {
                        JSONObject jsonObject = new JSONObject(responseString.toString());

                        JSONArray jsonArray = jsonObject.getJSONArray("questions");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject obj = (JSONObject) jsonArray.get(i);
                            String questionId = obj.getString("id");
                            String questionOrder = obj.getString("question_order");
                            String question = obj.getString("question");
                            JSONArray responsesArray = obj.getJSONArray("responses");
                            String base = obj.getString("base");
                            String responseType = obj.getString("response_type");
                            String randomize = obj.getString("randomize");
                            String other = obj.getString("other");
                            String none = obj.getString("none");
                            String notsure = obj.getString("notsure");
                            String decline = obj.getString("decline");

                            String responseArrayStr = "";

                            ArrayList<ResponseBean> rbList = new ArrayList<>();

                            for (int j = 0; j < responsesArray.length(); j++) {

                                ResponseBean rb = new ResponseBean();

                                JSONObject respObject = responsesArray.getJSONObject(j);
                                responseArrayStr += "code=" + respObject.getString("code") + ",";
                                responseArrayStr += "wording=" + respObject.getString("wording") + ",";
                                responseArrayStr += "next=" + respObject.getString("next");
                                responseArrayStr += "|";
                                rb.setNext(respObject.getString("next"));
                                rb.setCode(respObject.getString("code"));
                                rb.setWording(respObject.getString("wording"));
                                rbList.add(rb);
                            }

                            QuestionBean questionBean = new QuestionBean();
                            questionBean.setQuestionId(questionId);
                            questionBean.setQuestionOrder(questionOrder);
                            questionBean.setQuestion(question);
                            questionBean.setBase(base);
                            questionBean.setResponseType(responseType);
                            questionBean.setRandomize(randomize);
                            questionBean.setOther(other);
                            questionBean.setNone(none);
                            questionBean.setNotsure(notsure);
                            questionBean.setDecline(decline);
                            questionBean.setResponsesString(responseArrayStr);
                            questionBean.setmList(rbList);
                            questionBeanArrayList.add(questionBean);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    System.out.println(questionBeanArrayList);


                    surveyObj.setmQuestionsList(questionBeanArrayList);

                    // show this is UI


                    for (final QuestionBean localObj : questionBeanArrayList) {

                        String question = localObj.getQuestion();

                        String responseType = localObj.getResponseType();

                        if (Integer.parseInt(responseType) == 1) {

                            TextView tv = new TextView(SurveyReader.this);
                            tv.setText(question);
                            RadioGroup rg = new RadioGroup(SurveyReader.this);

                            ArrayList<ResponseBean> responseBeanArrayList = localObj.getmList();
                            for (ResponseBean rbObj : responseBeanArrayList) {
                                RadioButton rb = new RadioButton(SurveyReader.this);
                                rb.setTag(rbObj.getWording());
                                rb.setText(rbObj.getWording());
                                rg.addView(rb);
                            }

                            mRadioGroupMap.put(localObj.getQuestionId(),rg);
                            mLinearLayout.addView(tv);
                            mLinearLayout.addView(rg);
                        } else if (Integer.parseInt(responseType) == 3) {
                            TextView tv = new TextView(SurveyReader.this);
                            tv.setText(question);

                            Spinner spinner = new Spinner(SurveyReader.this);

                            final ArrayList<ResponseBean> responseBeanArrayList = localObj.getmList();
                            String[] spinnerArray = new String[responseBeanArrayList.size()];
                            int k = 0;
                            for (ResponseBean obj : responseBeanArrayList) {
                                spinnerArray[k] = obj.getWording();
                                k++;
                            }

                            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    ResponseBean object = responseBeanArrayList.get(position);
                                    responsesCollectorMap.put(localObj.getQuestionId(),object.getWording());

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SurveyReader.this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinner.setAdapter(spinnerArrayAdapter);



                            mLinearLayout.addView(tv);
                            mLinearLayout.addView(spinner);


                        } else if (Integer.parseInt(responseType) == 4) {
                            TextView tv = new TextView(SurveyReader.this);
                            tv.setText(question);

                            AutoCompleteTextView atv = new AutoCompleteTextView(SurveyReader.this);

                            final ArrayList<ResponseBean> responseBeanArrayList = localObj.getmList();
                            String[] spinnerArray = new String[responseBeanArrayList.size()];
                            int k = 0;
                            for (ResponseBean obj : responseBeanArrayList) {
                                spinnerArray[k] = obj.getWording();
                                k++;
                            }



                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(SurveyReader.this, android.R.layout.simple_spinner_item, spinnerArray); //selected item will look like a spinner set from XML
                            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            atv.setAdapter(spinnerArrayAdapter);

                            atv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    ResponseBean object = responseBeanArrayList.get(position);
                                    responsesCollectorMap.put(localObj.getQuestionId(),object.getWording());
                                }
                            });




                            mLinearLayout.addView(tv);
                            mLinearLayout.addView(atv);


                        }

                    }


                    submitButton.setVisibility(View.VISIBLE);


                    //writeToDb(questionBeanArrayList);

                    //MyAppUtility.getInstance().exportDB();


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


    public void writeToDb(ArrayList<QuestionBean> questionBeanArrayList) {

        SurveyReader.this.getContentResolver().delete(AppContentProvider.QuestionTable.CONTENT_URI, null, null);


        for (QuestionBean obj : questionBeanArrayList) {
            ContentValues values = new ContentValues();
            values.put(AppContentProvider.QuestionTable.QUESTION_ID, obj.getQuestionId());
            values.put(AppContentProvider.QuestionTable.QUESTION, obj.getQuestion());
            values.put(AppContentProvider.QuestionTable.QUESTION_ORDER, obj.getQuestionOrder());
            values.put(AppContentProvider.QuestionTable.QUESTION_BASE, obj.getBase());
            values.put(AppContentProvider.QuestionTable.QUESTION_RESPONSE_TYPE, obj.getResponseType());
            values.put(AppContentProvider.QuestionTable.QUESTION_RANDOMIZE, obj.getRandomize());
            values.put(AppContentProvider.QuestionTable.QUESTION_OTHER, obj.getOther());
            values.put(AppContentProvider.QuestionTable.QUESTION_NONE, obj.getNone());
            values.put(AppContentProvider.QuestionTable.QUESTION_RESPONSES_STR, obj.getResponsesString());
            values.put(AppContentProvider.QuestionTable.QUESTION_NOTSURE, obj.getNotsure());
            values.put(AppContentProvider.QuestionTable.QUESTION_DECLINE, obj.getDecline());
            Uri uri = SurveyReader.this.getContentResolver().insert(AppContentProvider.QuestionTable.CONTENT_URI,
                    values);
        }
    }

}
