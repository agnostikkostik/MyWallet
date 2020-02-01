package space.krya.newkurs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class scan extends Fragment {
    View rootView;

    public static TextView textViewResult;
    public static TextView textViewAnswer;
    public static TextView infoR;

    public static String idRec;
    private static final int PERMISSION_REQUEST_CODE = 123;

    class QueryTask extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            String response = null;

            try
            {
                response = FNS.Check(strings[0]);

                switch (response)
                {
                    case "204":
                        response = "Чек существует";
                        break;
                    default:
                        response = "Произошла ошибка или чек не существует";
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
            textViewAnswer.setText(response);
        }
    }

    class QueryTask2 extends AsyncTask<String, Void, String>
    {

        @Override
        protected String doInBackground(String... strings)
        {
            String response = null;

            try
            {
                response = FNS.Receive(strings[0]);

                if (response != "")
                {
                    Uri uri = Uri.parse("http://mywallet.fun/parse" + response);
                    response = "Место покупки: " + uri.getQueryParameter("place") +
                            ". Куплено товаров: " + uri.getQueryParameter("count");
                }
                else
                {
                    response = "Произошла ошибка или чек не существует";
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
            infoR.setText(response);
        }
    }


    @Nullable
    @Override
    public View onCreateView (@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.scan, container, false);
        textViewResult = rootView.findViewById(R.id.resultScan);
        textViewAnswer = rootView.findViewById(R.id.resultAnswer);
        infoR = rootView.findViewById(R.id.infoRe);

        Button scanCode = rootView.findViewById(R.id.btnScan);
        scanCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasPermissions())
                {
                    startActivity(new Intent(getContext(), scanQR.class));
                }
                else
                {
                    requestPermissionWithRationale();
                }
            }
        });

        Button scanCode2 = rootView.findViewById(R.id.button3);
        scanCode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QueryTask().execute(textViewResult.getText().toString() +
                        "&login=" + MainActivity.forAllPhone +
                        "&password=" + MainActivity.forAllPassword);
            }
        });
        Button getData = rootView.findViewById(R.id.btnGetData);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new QueryTask2().execute(textViewResult.getText().toString() +
                        "&login=" + MainActivity.forAllPhone +
                        "&password=" + MainActivity.forAllPassword +
                        "&deviceId=" + MainActivity.forAllAndroidId +
                        "&deviceOs=" + Build.VERSION.RELEASE +
                        "&idRec=" + idRec);
            }
        });


        return rootView;
    }

    private boolean hasPermissions()
    {
        int res = 0;
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET};

        for ( String perms : permissions)
        {
            res = getContext().checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED))
            {
                return false;
            }
        }
        return true;
    }

    private void requestPerms()
    {
        String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.INTERNET};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        boolean allowed = true;

        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
                for (int res : grantResults)
                {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;
            default:
                allowed = false;
                break;
        }
        if (allowed)
        {
            startActivity(new Intent(getContext(), scanQR.class));
        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) && shouldShowRequestPermissionRationale(Manifest.permission.INTERNET))
                {
                    Toast.makeText(getContext(), "Вы дали разрешение, спасибо.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getContext(), "Вы не дали разрешение. Возможно, дать его теперь можно только через настройки.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    protected void requestPermissionWithRationale()
    {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) && shouldShowRequestPermissionRationale(Manifest.permission.INTERNET))
        {
            Toast.makeText(getContext(), "Blabla", Toast.LENGTH_SHORT).show();
            requestPerms();
        }
        else
        {
            requestPerms();
        }
    }
}
