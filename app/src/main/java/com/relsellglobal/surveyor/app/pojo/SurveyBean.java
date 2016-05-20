package com.relsellglobal.surveyor.app.pojo;

import java.util.ArrayList;

/**
 * Created by anilkukreti on 14/05/16.
 */
public class SurveyBean {

    private String mSurveyId;
    private String mSurveyName;
    private String mSurveyCategory;
    private String mSurveyBase;
    private String mSurveyStartDate;
    private String mSurveyEndDate;
    private String mSurveyActive;
    private String mSurveyAward;
    private ArrayList<QuestionBean> mQuestionsList;


    public String getmSurveyId() {
        return mSurveyId;
    }

    public void setmSurveyId(String mSurveyId) {
        this.mSurveyId = mSurveyId;
    }

    public String getmSurveyName() {
        return mSurveyName;
    }

    public void setmSurveyName(String mSurveyName) {
        this.mSurveyName = mSurveyName;
    }

    public String getmSurveyCategory() {
        return mSurveyCategory;
    }

    public void setmSurveyCategory(String mSurveyCategory) {
        this.mSurveyCategory = mSurveyCategory;
    }

    public String getmSurveyBase() {
        return mSurveyBase;
    }

    public void setmSurveyBase(String mSurveyBase) {
        this.mSurveyBase = mSurveyBase;
    }

    public String getmSurveyStartDate() {
        return mSurveyStartDate;
    }

    public void setmSurveyStartDate(String mSurveyStartDate) {
        this.mSurveyStartDate = mSurveyStartDate;
    }

    public String getmSurveyEndDate() {
        return mSurveyEndDate;
    }

    public void setmSurveyEndDate(String mSurveyEndDate) {
        this.mSurveyEndDate = mSurveyEndDate;
    }

    public String getmSurveyActive() {
        return mSurveyActive;
    }

    public void setmSurveyActive(String mSurveyActive) {
        this.mSurveyActive = mSurveyActive;
    }

    public String getmSurveyAward() {
        return mSurveyAward;
    }

    public void setmSurveyAward(String mSurveyAward) {
        this.mSurveyAward = mSurveyAward;
    }

    public ArrayList<QuestionBean> getmQuestionsList() {
        return mQuestionsList;
    }

    public void setmQuestionsList(ArrayList<QuestionBean> mQuestionsList) {
        this.mQuestionsList = mQuestionsList;
    }
}
