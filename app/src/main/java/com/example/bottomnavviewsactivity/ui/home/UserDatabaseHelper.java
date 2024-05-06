package com.example.bottomnavviewsactivity.ui.home;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserDatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "user_db";

    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_USERNAME = "username";

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "("
                + KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                + KEY_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_EMAIL + " TEXT,"
                + KEY_USERNAME + " TEXT" + ")";
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getName());
        values.put(KEY_LAST_NAME, user.getLastName());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_USERNAME, user.getUsername());


        long id = db.insert("users", null, values);
        return id != -1;
    }

    public List<User> showUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String select = "SELECT * FROM users";
        Cursor cursor = sqLiteDatabase.rawQuery(select, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String lastName = cursor.getString(2);
                String email = cursor.getString(3);
                String username = cursor.getString(4);
                User user = new User(id, name, lastName, email, username);
                users.add(user);
            } while(cursor.moveToNext());
        }
        cursor.close();
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

        int rowsAffected = db.update(TABLE_USERS, values, KEY_ID + " = ?", new String[]{String.valueOf(user.getId())});

        return rowsAffected > 0;
    }



}

