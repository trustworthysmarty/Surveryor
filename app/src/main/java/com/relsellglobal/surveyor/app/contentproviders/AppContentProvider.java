package com.relsellglobal.surveyor.app.contentproviders;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.HashMap;

/**
 * Created by anilkukreti on 02/03/16.
 */
public class AppContentProvider extends ContentProvider {

    public static final String PROVIDER_NAME = "com.relsellglobal.surveyor.app.contentproviders.AppContentProvider";



    public static class QuestionTable {
        public static final String URL = "content://" + PROVIDER_NAME + "/questiontable";
        public static final Uri CONTENT_URI = Uri.parse(URL);
        public static final String _ID = "_id";
        public static final String QUESTION_ID = "question_id";
        public static final String QUESTION = "question";
        public static final String QUESTION_ORDER = "question_order";
        public static final String QUESTION_BASE="question_base";
        public static final String QUESTION_RESPONSE_TYPE = "question_response_type";
        public static final String QUESTION_RANDOMIZE = "question_randomize";
        public static final String QUESTION_OTHER="question_other";
        public static final String QUESTION_NONE="question_none";
        public static final String QUESTION_NOTSURE="question_notsure";
        public static final String QUESTION_DECLINE="question_decline";
        public static final String QUESTION_RESPONSES_STR="question_responses_str";


    }

    public static class AnswersTable {
        public static final String URL = "content://" + PROVIDER_NAME + "/answerstable";
        public static final Uri CONTENT_URI = Uri.parse(URL);
        public static final String _ID = "_id";
        public static final String OP_ID = "op_id";
        public static final String SURVEY_ID = "survey_id";
        public static final String ANSWER = "answer";
    }
    public static class SurveyTable {
        public static final String URL = "content://" + PROVIDER_NAME + "/surveytable";
        public static final Uri CONTENT_URI = Uri.parse(URL);
        public static final String _ID = "_id";
        public static final String SURVEY_NAME = "survey_name";
        public static final String SURVEY_CATEGORY = "survey_category";
        public static final String SURVEY_BASE = "survey_base";
        public static final String SURVEY_START_DATE = "survey_start_date";
        public static final String SURVEY_END_DATE = "survey_end_date";
        public static final String SURVEY_ACTIVE = "survey_active";
        public static final String SURVEY_AWARD = "survey_award";
    }

    public static class UserTable {
        public static final String URL = "content://" + PROVIDER_NAME + "/usertable";
        public static final Uri CONTENT_URI = Uri.parse(URL);
        public static final String _ID = "_id";
        public static final String PHONE_ID = "phone_id";
        public static final String OP_ID = "op_id";
        public static final String PASSWORD = "password";
        public static final String FB_ID = "fb_id";
        public static final String GO_ID = "go_id";
        public static final String FULL_NAME = "full_name";
        public static final String PHONE_NO = "phone_no";
        public static final String EMAIL = "email";
        public static final String DOB = "dob";
        public static final String GENDER = "gender";
        public static final String CITY = "city";
        public static final String STATE = "state";
        public static final String EDUCATION = "education";
        public static final String INCOME = "income";
        public static final String EMPLOYMENT = "employment";
        public static final String INDUSTRY = "industry";
        public static final String MARITAL = "marital";
        public static final String ETHNICITY = "ethnicity";
        public static final String RELIGION = "religion";

    }




    private static HashMap<String, String> QUESTION_PROJECTION_MAP;
    private static HashMap<String, String> ANSWERS_PROJECTION_MAP;
    private static HashMap<String, String> SURVEY_PROJECTION_MAP;
    private static HashMap<String, String> USERS_PROJECTION_MAP;


    static final int QUESTION = 1;
    static final int ANSWERS = 2;
    static final int SURVEY = 3;
    static final int USER = 4;



    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "questiontable", QUESTION);
        uriMatcher.addURI(PROVIDER_NAME, "answerstable", ANSWERS);
        uriMatcher.addURI(PROVIDER_NAME, "surveytable", SURVEY);
        uriMatcher.addURI(PROVIDER_NAME, "usertable", USER);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;
    public static final String DATABASE_NAME = "SurveyorDb";
    public static final String QUESTION_TABLE_NAME = "questiontable";
    public static final String ANSWERS_TABLE_NAME = "answerstable";
    public static final String SURVEY_TABLE_NAME = "surveytable";
    public static final String USER_TABLE_NAME = "usertable";

    static final int DATABASE_VERSION = 1;



    static final String CREATE_SURVEY_DB_TABLE =
            " CREATE TABLE " + SURVEY_TABLE_NAME +
                    " (" + SurveyTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SurveyTable.SURVEY_NAME + " TEXT NOT NULL, " +
                    SurveyTable.SURVEY_CATEGORY + " TEXT NOT NULL, " +
                    SurveyTable.SURVEY_BASE + " TEXT NOT NULL, " +
                    SurveyTable.SURVEY_START_DATE + " TEXT NOT NULL, " +
                    SurveyTable.SURVEY_END_DATE + " TEXT NOT NULL, " +
                    SurveyTable.SURVEY_ACTIVE + " TEXT NOT NULL, " +
                    SurveyTable.SURVEY_AWARD + "  TEXT NOT NULL);";



    static final String CREATE_USER_DB_TABLE =
            " CREATE TABLE " + USER_TABLE_NAME +
                    " (" + UserTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    UserTable.PHONE_ID + " TEXT NOT NULL, " +
                    UserTable.OP_ID + " TEXT NULL, " +
                    UserTable.PASSWORD + " TEXT NULL, " +
                    UserTable.FB_ID + " TEXT NULL, " +
                    UserTable.GO_ID + " TEXT NULL, " +
                    UserTable.FULL_NAME + " TEXT NULL, " +
                    UserTable.PHONE_NO + " TEXT NULL, " +
                    UserTable.EMAIL + " TEXT NULL, " +
                    UserTable.DOB + " TEXT NULL, " +
                    UserTable.GENDER + " TEXT NULL, " +
                    UserTable.CITY + " TEXT NULL, " +
                    UserTable.STATE + " TEXT NULL, " +
                    UserTable.EDUCATION + " TEXT NULL, " +
                    UserTable.INCOME + " TEXT NULL, " +
                    UserTable.EMPLOYMENT + " TEXT NULL, " +
                    UserTable.INDUSTRY + " TEXT NULL, " +
                    UserTable.MARITAL + " TEXT NULL, " +
                    UserTable.RELIGION + " TEXT NULL, " +
                    UserTable.ETHNICITY + "  TEXT NULL);";


    static final String CREATE_ANSWERS_DB_TABLE =
            " CREATE TABLE " + ANSWERS_TABLE_NAME +
                    " (" + AnswersTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    AnswersTable.OP_ID + " TEXT NOT NULL, " +
                    AnswersTable.SURVEY_ID + " TEXT NOT NULL, " +
                    AnswersTable.ANSWER + "  TEXT NOT NULL);";



    static final String CREATE_QUESTION_DB_TABLE =
            " CREATE TABLE " + QUESTION_TABLE_NAME +
                    " (" + QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QuestionTable.QUESTION_ID + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_ORDER + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_BASE + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_RESPONSE_TYPE + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_RANDOMIZE + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_OTHER + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_NONE + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_NOTSURE + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_RESPONSES_STR + " TEXT NOT NULL, " +
                    QuestionTable.QUESTION_DECLINE + "  TEXT NOT NULL);";



    /**
     * Helper class that actually creates and manages
     * the provider's underlying data repository.
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_QUESTION_DB_TABLE);
            db.execSQL(CREATE_ANSWERS_DB_TABLE);
            db.execSQL(CREATE_SURVEY_DB_TABLE);
            db.execSQL(CREATE_USER_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + QUESTION_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ANSWERS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + SURVEY_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        /**
         * Create a write able database which will trigger its
         * creation if it doesn't already exist.
         */
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /**
         * Add a new student record
         */
        String tableName = getTableName(uri);

       if(tableName.equalsIgnoreCase(QUESTION_TABLE_NAME)) {
           long rowID = db.insert(tableName, "", values);
           /**
            * If record is added successfully
            */
           if (rowID > 0) {
               Uri _uri = ContentUris.withAppendedId(QuestionTable.CONTENT_URI, rowID);
               ContentResolver cr = getContext().getContentResolver();
               if(cr != null) {
                   cr.notifyChange(_uri, null);
               }
               return _uri;
           }
           throw new SQLException("Failed to add a record into " + uri);
       } else if(tableName.equalsIgnoreCase(ANSWERS_TABLE_NAME)) {
           long rowID = db.insert(tableName, "", values);
           /**
            * If record is added successfully
            */
           if (rowID > 0) {
               Uri _uri = ContentUris.withAppendedId(AnswersTable.CONTENT_URI, rowID);
               ContentResolver cr = getContext().getContentResolver();
               if(cr != null) {
                   cr.notifyChange(_uri, null);
               }
               return _uri;
           }
           throw new SQLException("Failed to add a record into " + uri);
       }  else if(tableName.equalsIgnoreCase(SURVEY_TABLE_NAME)) {
           long rowID = db.insert(tableName, "", values);
           /**
            * If record is added successfully
            */
           if (rowID > 0) {
               Uri _uri = ContentUris.withAppendedId(SurveyTable.CONTENT_URI, rowID);
               ContentResolver cr = getContext().getContentResolver();
               if(cr != null) {
                   cr.notifyChange(_uri, null);
               }
               return _uri;
           }
           throw new SQLException("Failed to add a record into " + uri);
       }  else if(tableName.equalsIgnoreCase(USER_TABLE_NAME)) {
           long rowID = db.insert(tableName, "", values);
           /**
            * If record is added successfully
            */
           if (rowID > 0) {
               Uri _uri = ContentUris.withAppendedId(UserTable.CONTENT_URI, rowID);
               ContentResolver cr = getContext().getContentResolver();
               if(cr != null) {
                   cr.notifyChange(_uri, null);
               }
               return _uri;
           }
           throw new SQLException("Failed to add a record into " + uri);
       }

        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        switch (uriMatcher.match(uri)) {
            case QUESTION:
                qb.setTables(QUESTION_TABLE_NAME);
                qb.setProjectionMap(QUESTION_PROJECTION_MAP);
                break;
            case ANSWERS:
                qb.setTables(ANSWERS_TABLE_NAME);
                qb.setProjectionMap(ANSWERS_PROJECTION_MAP);
                break;
            case SURVEY:
                qb.setTables(SURVEY_TABLE_NAME);
                qb.setProjectionMap(SURVEY_PROJECTION_MAP);
                break;
            case USER:
                qb.setTables(USER_TABLE_NAME);
                qb.setProjectionMap(USERS_PROJECTION_MAP);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI While query" + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            /**
             * By default sort on student names
             */
            sortOrder = QuestionTable._ID;
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs,
                null, null, sortOrder);
        /**
         * register to watch a content URI for changes
         */
        c.setNotificationUri(getContext().getContentResolver(), uri);

        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        String tableName = null;
        switch (uriMatcher.match(uri)) {
            case QUESTION:
                count = db.delete(QUESTION_TABLE_NAME, selection, selectionArgs);
                break;
            case ANSWERS:
                count = db.delete(ANSWERS_TABLE_NAME, selection, selectionArgs);
                break;
            case SURVEY:
                count = db.delete(SURVEY_TABLE_NAME, selection, selectionArgs);
                break;
            case USER:
                count = db.delete(USER_TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case QUESTION:
                count = db.update(QUESTION_TABLE_NAME, values,
                        selection, selectionArgs);
            case SURVEY:
                count = db.update(SURVEY_TABLE_NAME, values,
                        selection, selectionArgs);
            case USER:
                count = db.update(USER_TABLE_NAME, values,
                        selection, selectionArgs);
            case ANSWERS:
                count = db.update(ANSWERS_TABLE_NAME, values,
                        selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI while updating" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    public String getTableName(Uri uri) {
        String result = null;
        int res = uriMatcher.match(uri);
        switch (res) {
            case QUESTION:
                result = QUESTION_TABLE_NAME;
                break;
            case ANSWERS:
                result = ANSWERS_TABLE_NAME;
                break;
            case SURVEY:
                result = SURVEY_TABLE_NAME;
                break;
            case USER:
                result = USER_TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI Name in table: " + uri);
        }
        return result;
    }
}
