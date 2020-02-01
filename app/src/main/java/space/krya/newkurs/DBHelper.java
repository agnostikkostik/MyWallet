package space.krya.newkurs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATAVASE_VERSION = 2;
    public static final String DATABASE_NAME = "userDb";
    public static final String TABLE_USER = "user";

    public static final String KEY_ID = "_id";
    public static final String KEY_LOGIN = "login";
    public static final String KEY_FNS_PASSWORD = "FNS_password";


    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATAVASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_USER + "(" +
                KEY_ID + " integer primary key," +
                KEY_LOGIN + " text," +
                KEY_FNS_PASSWORD + " text" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + TABLE_USER);
        onCreate(db);
    }
}
