package space.krya.newkurs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    DBHelper dbHelper;

    public static String forAllPhone;
    public static String forAllPassword;
    public static String forAllAndroidId;

    public static boolean auth = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forAllAndroidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        dbHelper = new DBHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.query(DBHelper.TABLE_USER, null, null, null, null, null, null);
        if (cursor.moveToFirst())
        {
            int phoneIndex = cursor.getColumnIndex((DBHelper.KEY_LOGIN));
            int passwordIndex = cursor.getColumnIndex((DBHelper.KEY_FNS_PASSWORD));
            forAllPhone = "+" + cursor.getString(phoneIndex);
            forAllPassword = cursor.getString(passwordIndex);
        }

        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNav);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new home()).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;

                switch (menuItem.getItemId()) {
                    case R.id.home:
                        fragment = new home();
                        break;

                    case R.id.scan:
                        fragment = new scan();
                        break;

                    case R.id.profile:
                        fragment = new profile();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (auth)
        {
            Fragment fragment = new profile();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
            auth = false;
        }
        else
            super.onBackPressed();
    }
}
