package com.relsellglobal.surveyor.app.pojo;

import java.util.ArrayList;

/**
 * Created by anilkukreti on 11/05/16.
 */
public class QuestionBean {
    private String questionId;
    private String question;
    private String questionOrder;
    private String base;
    private String responseType;
    private String randomize;
    private String other;
    private String none;
    private String notsure;
    private String decline;
    private String responsesString;
    private ArrayList<ResponseBean> mList;


    public ArrayList<ResponseBean> getmList() {
        return mList;
    }

    public void setmList(ArrayList<ResponseBean> mList) {
        this.mList = mList;
    }

    public String getResponsesString() {
        return responsesString;
    }

    public void setResponsesString(String responsesString) {
        this.responsesString = responsesString;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getQuestionOrder() {
        return questionOrder;
    }

    public void setQuestionOrder(String questionOrder) {
        this.questionOrder = questionOrder;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public String getRandomize() {
        return randomize;
    }

    public void setRandomize(String randomize) {
        this.randomize = randomize;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getNone() {
        return none;
    }

    public void setNone(String none) {
        this.none = none;
    }

    public String getNotsure() {
        return notsure;
    }

    public void setNotsure(String notsure) {
        this.notsure = notsure;
    }

    public String getDecline() {
        return decline;
    }

    public void setDecline(String decline) {
        this.decline = decline;
    }

    @Override
    public String toString() {
        return "QuestionBean{" +
                "questionId='" + questionId + '\'' +
                ", question='" + question + '\'' +
                ", questionOrder='" + questionOrder + '\'' +
                ", base='" + base + '\'' +
                ", responseType='" + responseType + '\'' +
                ", randomize='" + randomize + '\'' +
                ", other='" + other + '\'' +
                ", none='" + none + '\'' +
                ", notsure='" + notsure + '\'' +
                ", decline='" + decline + '\'' +
                '}';
    }
}
