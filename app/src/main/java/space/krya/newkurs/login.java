package space.krya.newkurs;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class login extends Fragment {

    private TextView result;
    private EditText temp;
    private String phone;
    private String password;
    View rootView;
    DBHelper dbHelper;

    class QueryTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            String response = null;

            try
            {
                response = FNS.Login(strings[0], strings[1]);

                switch (response)
                {
                    case "200":
                        response = "Вошли успешно";
                        break;
                    case "403":
                        response = "Некорректный адрес электронной почты";
                        break;
                }
            }
            catch (IOException e)
            {
                response = "Одно из значений указано неверно или нет соединения с сервером.";
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response)
        {
            if (response.contains("Вошли успешно"))
            {
                temp = rootView.findViewById(R.id.et_phone);
                phone = "+" + temp.getText().toString();
                temp = rootView.findViewById(R.id.et_password);
                password = temp.getText().toString();

                dbHelper = new DBHelper(rootView.getContext());

                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_LOGIN, phone);
                contentValues.put(DBHelper.KEY_FNS_PASSWORD, password);

                database.insert(DBHelper.TABLE_USER, null, contentValues);

                MainActivity.forAllPhone = phone;
                MainActivity.forAllPassword = password;


            }
            result.setText(response);
        }
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.login, container, false);

        Button btn = (Button) rootView.findViewById(R.id.button2);
        result = rootView.findViewById(R.id.tv_R);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temp = rootView.findViewById(R.id.et_phone);
                phone = "+" + temp.getText().toString();
                temp = rootView.findViewById(R.id.et_password);
                password = temp.getText().toString();

                new QueryTask().execute(phone, password);
            }
        });

        MainActivity.auth = true;
        return rootView;
    }
}
