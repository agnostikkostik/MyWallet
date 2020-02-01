package space.krya.newkurs;

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

public class registration extends Fragment {

    private TextView result;
    private EditText temp;
    private String name;
    private String email;
    private String phone;
    View rootView;

    class QueryTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            String response = null;

            try
            {
                response = FNS.Registration(strings[0], strings[1], strings[2]);

                switch (response)
                {
                    case "204":
                        response = "Введите код из смс.";
                        break;
                    case "400":
                        response = "Некорректный адрес электронной почты";
                        break;
                    case "409":
                        response = "Введите код из смс.";
                        FNS.RestorePassword(strings[2]);
                        break;
                    case "500":
                        response = "Некорректный номер телефона";
                        break;
                }
            }
            catch (IOException e)
            {
                try
                {
                    response = FNS.RestorePassword(strings[2]);
                }
                catch (IOException ex)
                {
                    response = "Одно из значений указано неверно или нет соединения с сервером.";
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response)
        {
            if (response == "Введите код из смс.")
            {
                getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, new login())
                    .addToBackStack(null)
                    .commit();
            }
            else
                result.setText(response);
        }
    }

    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.activity_registration, container, false);

        Button btn = (Button) rootView.findViewById(R.id.button1);
        result = rootView.findViewById(R.id.tv_r);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                temp = rootView.findViewById(R.id.et_phone);
                phone = "+" + temp.getText().toString();
                temp = rootView.findViewById(R.id.et_name);
                name = temp.getText().toString();
                temp = rootView.findViewById(R.id.et_email);
                email = temp.getText().toString();

                new QueryTask().execute(email, name, phone);
            }
        });

        MainActivity.auth = true;
        return rootView;
    }
}
