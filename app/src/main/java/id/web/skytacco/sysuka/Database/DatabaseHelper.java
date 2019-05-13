package id.web.skytacco.sysuka.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import id.web.skytacco.sysuka.entity.FavoriteItem;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AddtoFav";
    private static final String TABLE_NAME = "Favorite";
    private static final String KEY_ID = "id";
    private static final String KEY_CATID = "catid";
    private static final String KEY_CID = "cid";
    private static final String KEY_CATEGORYNAME = "categoryname";
    private static final String KEY_NEWSHEADING = "newsheading";
    private static final String KEY_NEWSIMAGE = "newsimage";
    private static final String KEY_NEWSDESC = "newsdesc";
    private static final String KEY_NEWSDATE = "newsdate";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CATID + " TEXT,"
                + KEY_CID + " TEXT,"
                + KEY_CATEGORYNAME + " TEXT,"
                + KEY_NEWSHEADING + " TEXT,"
                + KEY_NEWSIMAGE + " TEXT,"
                + KEY_NEWSDESC + " TEXT,"
                + KEY_NEWSDATE + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    //Adding Record in Database
    public void AddtoFavorite(FavoriteItem mFavoriteItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_CATID, mFavoriteItem.getCatId());
        values.put(KEY_CID, mFavoriteItem.getCId());
        values.put(KEY_CATEGORYNAME, mFavoriteItem.getCategoryName());
        values.put(KEY_NEWSHEADING, mFavoriteItem.getNewsHeading());
        values.put(KEY_NEWSIMAGE, mFavoriteItem.getNewsImage());
        values.put(KEY_NEWSDESC, mFavoriteItem.getNewsDesc());
        values.put(KEY_NEWSDATE, mFavoriteItem.getNewsDate());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Data
    public List<FavoriteItem> getAllData() {
        List<FavoriteItem> dataList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " ORDER BY id DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavoriteItem mFavoriteItem = new FavoriteItem();
                mFavoriteItem.setId(Integer.parseInt(cursor.getString(0)));
                mFavoriteItem.setCatId(cursor.getString(1));
                mFavoriteItem.setCId(cursor.getString(2));
                mFavoriteItem.setCategoryName(cursor.getString(3));
                mFavoriteItem.setNewsHeading(cursor.getString(4));
                mFavoriteItem.setNewsImage(cursor.getString(5));
                mFavoriteItem.setNewsDesc(cursor.getString(6));
                mFavoriteItem.setNewsDate(cursor.getString(7));
                // Adding contact to list
                dataList.add(mFavoriteItem);
            } while (cursor.moveToNext());
        }
        // return contact list
        return dataList;
    }

    //getting single row
    public List<FavoriteItem> getFavRow(String id) {
        List<FavoriteItem> dataList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE catid=" + id;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FavoriteItem mFavoriteItem = new FavoriteItem();
                mFavoriteItem.setId(Integer.parseInt(cursor.getString(0)));
                mFavoriteItem.setCatId(cursor.getString(1));
                mFavoriteItem.setCId(cursor.getString(2));
                mFavoriteItem.setCategoryName(cursor.getString(3));
                mFavoriteItem.setNewsHeading(cursor.getString(4));
                mFavoriteItem.setNewsImage(cursor.getString(5));
                mFavoriteItem.setNewsDesc(cursor.getString(6));
                mFavoriteItem.setNewsDate(cursor.getString(7));

                // Adding contact to list
                dataList.add(mFavoriteItem);
            } while (cursor.moveToNext());
        }
        // return contact list
        return dataList;
    }

    //for remove favorite
    public void RemoveFav(FavoriteItem contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_CATID + " = ?",
                new String[]{String.valueOf(contact.getCatId())});
        db.close();
    }

    public enum DatabaseManager {
        INSTANCE;
        DatabaseHelper dbHelper;
        private SQLiteDatabase db;
        private boolean isDbClosed = true;

        public void init(Context context) {
            dbHelper = new DatabaseHelper(context);
            if (isDbClosed) {
                isDbClosed = false;
                this.db = dbHelper.getWritableDatabase();
            }
        }

        public boolean isDatabaseClosed() {
            return isDbClosed;
        }

        public void closeDatabase() {
            if (!isDbClosed && db != null) {
                isDbClosed = true;
                db.close();
                dbHelper.close();
            }
        }
    }
}
