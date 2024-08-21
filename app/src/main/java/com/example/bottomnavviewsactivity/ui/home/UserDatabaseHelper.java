package com.example.bottomnavviewsactivity.ui.home;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private Context context;
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "user_db";

    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";
    private static final String TABLE_LOGIN_CREDENTIALS = "login_credentials";
    private static final String LOGIN_KEY_ID = "login_id";
    private static final String LOGIN_KEY_USERNAME = "username";
    private static final String LOGIN_KEY_PASSWORD = "password";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_USERNAME + " TEXT,"
                + KEY_START_DATE + " TEXT,"
                + KEY_END_DATE + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);

        String CREATE_LOGIN_CREDENTIALS_TABLE = "CREATE TABLE " + TABLE_LOGIN_CREDENTIALS + "("
                + LOGIN_KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LOGIN_KEY_USERNAME + " TEXT,"
                + LOGIN_KEY_PASSWORD + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_CREDENTIALS_TABLE);

        // Insert default admin credentials
        ContentValues values = new ContentValues();
        values.put(LOGIN_KEY_USERNAME, "admin");
        values.put(LOGIN_KEY_PASSWORD, "admin");
        db.insert(TABLE_LOGIN_CREDENTIALS, null, values);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_START_DATE, user.getStartDate());
        values.put(KEY_END_DATE, user.getEndDate());

        long id = db.insert(TABLE_USERS, null, values);
        return id != -1;
    }

    public List<User> showUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String select = "SELECT * FROM " + TABLE_USERS;
        Cursor cursor = null;

        try {
            cursor = sqLiteDatabase.rawQuery(select, null);
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME));
                    String lastName = cursor.getString(cursor.getColumnIndexOrThrow(KEY_LAST_NAME));
                    String email = cursor.getString(cursor.getColumnIndexOrThrow(KEY_EMAIL));
                    String username = cursor.getString(cursor.getColumnIndexOrThrow(KEY_USERNAME));
                    String startDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_START_DATE));
                    String endDate = cursor.getString(cursor.getColumnIndexOrThrow(KEY_END_DATE));

                    User user = new User(id, name, lastName, email, username, startDate, endDate);
                    users.add(user);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("showUsers", "Error retrieving data: " + e.getMessage());
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return users;
    }

    public boolean deleteUserById(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_USERS, KEY_ID + " = ?", new String[]{String.valueOf(userId)});
        return rowsAffected > 0;
    }

    public boolean updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_USERNAME, user.getUsername());
        values.put(KEY_START_DATE, user.getStartDate());
        values.put(KEY_END_DATE, user.getEndDate());

        int rowsAffected = db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});
        return rowsAffected > 0;
    }

    public boolean isValidCredentials(String username, String password) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {LOGIN_KEY_ID};
        String selection = LOGIN_KEY_USERNAME + " = ? AND " + LOGIN_KEY_PASSWORD + " = ?";
        String[] selectionArgs = {username, password};


        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_LOGIN_CREDENTIALS, columns, selection, selectionArgs, null, null, null);
            // go to first row
            if (cursor != null && cursor.moveToFirst()) {
                return true; // Credentials valid
            }
        } catch (Exception e) {
            Toast.makeText(context, "Wrong credentials" + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false; // Credentials not valid
    }

    public boolean doesUsernameExist(String username, int userId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selection;
        String[] selectionArgs;

        if (userId != -1) {
            selection = "LOWER(username) = LOWER(?) AND id != ?";
            selectionArgs = new String[]{username, String.valueOf(userId)};
        } else {
            selection = "LOWER(username) = LOWER(?)";
            selectionArgs = new String[]{username};
        }

        Cursor cursor = db.query(
                "users",
                new String[]{"username"},
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        db.close();
        return exists;
    }

}
