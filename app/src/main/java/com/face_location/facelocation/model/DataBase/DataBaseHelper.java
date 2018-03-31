package com.face_location.facelocation.model.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by admin on 05.12.17.
 */

public class DataBaseHelper extends SQLiteOpenHelper{

    public static final String TAG = "DataBaseHelper";

    private static DataBaseHelper sInstance;

    private static final int DATABASE_VERSION = 1;

    public static final String APP_DATABASE = "application.db";

    public static final String USER_DATA_TABLE = "server_responses";

    public static final String ID = "ID";
    public static final String USER_ID = "USER_ID";
    public static final String FIRST_LOGIN = "FIRST_LOGIN";
    public static final String USER_EMAIL = "EMAIL";
    public static final String USER_ROLE = "ROLE";
    public static final String USER_STATUS = "STATUS";
    public static final String USER_TOKEN = "TOKEN";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_LASTNAME = "LASTNAME";
    public static final String USER_PHONE = "PHONE";
    public static final String USER_JOB = "JOB";
    public static final String USER_AVATAR_URL = "AVATAR_URL";


    public static synchronized DataBaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }


    private DataBaseHelper(Context context) {
        super(context, APP_DATABASE, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + USER_DATA_TABLE +
                " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_ID + " TEXT NULL, " +
                FIRST_LOGIN + " BOOLEAN NULL, " +
                USER_EMAIL + " TEXT NULL, " +
                USER_ROLE + " TEXT NULL, " +
                USER_STATUS + " INTEGER NULL, " +
                USER_TOKEN + " TEXT NULL, " +
                USER_NAME + " TEXT NULL, " +
                USER_LASTNAME + " TEXT NULL, " +
                USER_PHONE + " TEXT NULL, " +
                USER_JOB + " TEXT NULL, " +
                USER_AVATAR_URL + " TEXT NULL)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_DATA_TABLE);
        onCreate(db);
    }


    public boolean addFirstLogginData(String userID, Boolean firstLogin, String userEmail, String userRole, int userStatus, String userToken, String userAvatarURL)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_ID, userID);
        contentValues.put(FIRST_LOGIN, firstLogin);
        contentValues.put(USER_EMAIL, userEmail);
        contentValues.put(USER_ROLE, userRole);
        contentValues.put(USER_STATUS, userStatus);
        contentValues.put(USER_TOKEN, userToken);
        contentValues.put(USER_AVATAR_URL, userAvatarURL);

        long result = db.insert(USER_DATA_TABLE, null, contentValues);
        db.close();

        if (result == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean updateFirstLoginValue(String currentUserID){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(FIRST_LOGIN, false);

        // updating row
        long result = db.update(USER_DATA_TABLE, contentValues,USER_ID + " = ?", new String[] {currentUserID});

        if (result == -1){
            return false;
        } else {
            return true;
        }
    }

    public String[] retrieveFirstLoginValues(){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] userArrayData = null;

        String[] columns = {"*"};
        String selection =  FIRST_LOGIN + "=?";
        String[] selectionArgs = {"1"}; //true = 1 in SQLite
        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor cursor = db.query(USER_DATA_TABLE, columns, selection, selectionArgs, groupBy, having, orderBy);

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            userArrayData = new String[]{
                    cursor.getString(cursor.getColumnIndex(USER_ID)),           //0
                    cursor.getString(cursor.getColumnIndex(FIRST_LOGIN)),       //1
                    cursor.getString(cursor.getColumnIndex(USER_EMAIL)),        //2
                    cursor.getString(cursor.getColumnIndex(USER_ROLE)),         //3
                    cursor.getString(cursor.getColumnIndex(USER_STATUS)),       //4
                    cursor.getString(cursor.getColumnIndex(USER_TOKEN)),        //5
                    cursor.getString(cursor.getColumnIndex(USER_NAME)),         //6
                    cursor.getString(cursor.getColumnIndex(USER_LASTNAME)),     //7
                    cursor.getString(cursor.getColumnIndex(USER_PHONE)),        //8
                    cursor.getString(cursor.getColumnIndex(USER_JOB)),          //9
                    cursor.getString(cursor.getColumnIndex(USER_AVATAR_URL))    //10
            };
            cursor.close();
        }
        cursor.close();
        db.close();
        return userArrayData;
    }

    public boolean isUser(String userID){
        SQLiteDatabase db = this.getWritableDatabase();

        String[] columns = {"*"};
        String selection =  USER_ID + "=?";
        String[] selectionArgs = {userID};
        String groupBy = null;
        String having = null;
        String orderBy = null;

        Cursor cursor = db.query(USER_DATA_TABLE, columns, selection, selectionArgs, groupBy, having, orderBy);

        if (cursor.getCount() == 1){
            cursor.close();
            db.close();
            return true;
        } else {
            Log.i(TAG, "isUser: НЕ РАВНО 1: " + cursor.getCount());
            cursor.close();
            db.close();
            return false;
        }
    }

    public boolean updatesAfterLogin(String currentUserID, String userEmail, String userAvatarURL, String userToken){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_EMAIL, userEmail);
        contentValues.put(USER_AVATAR_URL, userAvatarURL);
        contentValues.put(USER_TOKEN, userToken);
        contentValues.put(FIRST_LOGIN, true);

        long result = db.update(USER_DATA_TABLE, contentValues,USER_ID + " = ?", new String[] {currentUserID});

        if (result == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }



    public boolean updateMyProfileData(String currentUserID, String userEmail, String userName, String userLastname, String userPhone, String userAvatar, String userJob)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_EMAIL, userEmail);
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_LASTNAME, userLastname);
        contentValues.put(USER_PHONE, userPhone);
        contentValues.put(USER_AVATAR_URL, userAvatar);
        contentValues.put(USER_JOB, userJob);

        long result = db.update(USER_DATA_TABLE, contentValues,USER_ID + " = ?", new String[] {currentUserID});

        if (result == -1){
            db.close();
            return false;
        } else {
            db.close();
            return true;
        }
    }
}
