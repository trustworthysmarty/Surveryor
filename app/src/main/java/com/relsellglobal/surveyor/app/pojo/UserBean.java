package com.relsellglobal.surveyor.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by anilkukreti on 14/05/16.
 */
public class UserBean implements Parcelable {
    private String phoneId;
    private String opId;
    private String password;
    private String fbId;
    private String goId;
    private String fullName;
    private String phoneNo;
    private String email;
    /// second screen
    private String dob;
    private String gender;
    private String city;
    private String state;
    private String education;
    private String income;
    private String employment;
    private String industry;
    private String marital;
    private String etnicity;
    private String religion;


    public UserBean() {

    }


    public UserBean(Parcel in) {
        phoneId = in.readString();
        opId = in.readString();
        password = in.readString();
        fbId = in.readString();
        goId = in.readString();
        fullName = in.readString();
        phoneNo = in.readString();
        email = in.readString();
        dob = in.readString();
        gender = in.readString();
        city = in.readString();
        state = in.readString();
        education = in.readString();
        income = in.readString();
        employment = in.readString();
        industry = in.readString();
        marital = in.readString();
        etnicity = in.readString();
        religion = in.readString();
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel in) {
            return new UserBean(in);
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(String phoneId) {
        this.phoneId = phoneId;
    }

    public String getOpId() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId = opId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFbId() {
        return fbId;
    }

    public void setFbId(String fbId) {
        this.fbId = fbId;
    }

    public String getGoId() {
        return goId;
    }

    public void setGoId(String goId) {
        this.goId = goId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getEmployment() {
        return employment;
    }

    public void setEmployment(String employment) {
        this.employment = employment;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getMarital() {
        return marital;
    }

    public void setMarital(String marital) {
        this.marital = marital;
    }

    public String getEtnicity() {
        return etnicity;
    }

    public void setEtnicity(String etnicity) {
        this.etnicity = etnicity;
    }


    @Override
    public String toString() {
        return "UserBean{" +
                "phoneId='" + phoneId + '\'' +
                ", opId='" + opId + '\'' +
                ", password='" + password + '\'' +
                ", fbId='" + fbId + '\'' +
                ", goId='" + goId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phoneNo='" + phoneNo + '\'' +
                ", email='" + email + '\'' +
                ", dob='" + dob + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", education='" + education + '\'' +
                ", income='" + income + '\'' +
                ", employment='" + employment + '\'' +
                ", industry='" + industry + '\'' +
                ", marital='" + marital + '\'' +
                ", etnicity='" + etnicity + '\'' +
                ", religion='" + religion + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phoneId);
        dest.writeString(opId);
        dest.writeString(password);
        dest.writeString(fbId);
        dest.writeString(goId);
        dest.writeString(fullName);
        dest.writeString(phoneNo);
        dest.writeString(email);
        dest.writeString(dob);
        dest.writeString(gender);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(education);
        dest.writeString(income);
        dest.writeString(employment);
        dest.writeString(industry);
        dest.writeString(marital);
        dest.writeString(etnicity);
        dest.writeString(religion);
    }
}
