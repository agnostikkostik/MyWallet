package space.krya.newkurs;

import android.net.Uri;
import android.util.Base64;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.GregorianCalendar;
import java.util.Scanner;

public class FNS
{
    public static String addNull(int string)
    {
        String answer = String.valueOf(string);
        if (answer.length() == 1)
        {
            return "0" + answer;
        }
        else
        {
            return answer;
        }
    }

    public static String addNullFP(String string)
    {
        String answer = String.valueOf(string);
        String temp = "";
        while ((temp.length() + answer.length()) != 10)
        {
            temp += "0";
        }
        return temp + answer;
    }

    public static String Registration(String email, String name, String phone) throws IOException
    {
        URL url = new URL("https://proverkacheka.nalog.ru:9999/v1/mobile/users/signup");

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = "{" +
                    "\"phone\":\"" + phone + "\"," +
                    "\"email\":\"" + email + "\"," +
                    "\"name\":\"" + name + "\"" +
                    "}";

            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            return String.valueOf(responseCode);
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String Login(String phone, String password) throws IOException
    {
        URL url = new URL("https://proverkacheka.nalog.ru:9999/v1/mobile/users/login");

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            String auth = phone + ":" + password;

            byte[] data1 = auth.getBytes("utf8");
            String base64 = Base64.encodeToString(data1, Base64.NO_WRAP);
            String authHeaderValue = "Basic " + base64;
            urlConnection.setRequestProperty("Authorization", authHeaderValue);
            int responseCode = urlConnection.getResponseCode();
            return String.valueOf(responseCode);
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String RestorePassword(String phone) throws IOException
    {
        URL url = new URL("https://proverkacheka.nalog.ru:9999/v1/mobile/users/restore");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = "{" +
                    "\"phone\":\"" + phone + "\"" +
                    "}";

            try(OutputStream os = urlConnection.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            urlConnection.connect();

            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();

            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String Check(String params) throws IOException
    {
        Uri uri = Uri.parse("http://mywallet.fun/parse?" + params);
        String t = uri.getQueryParameter("t");
        String s = uri.getQueryParameter("s");
        String fn = uri.getQueryParameter("fn");
        String fd = uri.getQueryParameter("i");
        String fp = addNullFP(uri.getQueryParameter("fp"));
        String n = uri.getQueryParameter("n");
        String login = uri.getQueryParameter("login");
        String password = uri.getQueryParameter("password");

        s = String.valueOf(Double.valueOf(Double.valueOf(uri.getQueryParameter("s"))*100).intValue());

        SimpleDateFormat dateFormatDate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat dateFormatTime1 = new SimpleDateFormat("HHmm");
        SimpleDateFormat dateFormatTime2 = new SimpleDateFormat("HHmmss");
        String temp = "";

        Date tempDate = new Date();
        Date tempDate2 = new Date();
        Calendar calendar = new GregorianCalendar();

        try {
            tempDate = dateFormatDate.parse(t.split("T")[0]);
            calendar.setTime(tempDate);
            temp += addNull(calendar.get(Calendar.YEAR)) + "-" + addNull(calendar.get(Calendar.MONTH) + 1) + "-" + addNull(calendar.get(Calendar.DATE)) + "T";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (t.split("T")[1].length() == 4)
        {
            try {
                tempDate2 = dateFormatTime1.parse(t.split("T")[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        else
        {
            try {
                tempDate2 = dateFormatTime2.parse(t.split("T")[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        calendar.setTime(tempDate2);
        temp += addNull(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + addNull(calendar.get(Calendar.MINUTE)) + ":" + addNull(calendar.get(Calendar.SECOND));
        t = temp;

        URL url = new URL(
                "https://proverkacheka.nalog.ru:9999/v1/ofds/*/inns/*/fss/" + fn +
                "/operations/" + n + "/tickets/" + fd + "?fiscalSign=" + fp + "&date=" + t + "&sum=" + s);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            String auth = login + ":" + password;

            byte[] data1 = auth.getBytes("utf8");
            String base64 = Base64.encodeToString(data1, Base64.NO_WRAP);
            String authHeaderValue = "Basic " + base64;
            urlConnection.setRequestProperty("Authorization", authHeaderValue);

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 204)
            {
                Formatter f = new Formatter();

                f.format("http://mywallet.fun/API/addReceipt.php?t=%s&s=%s&fn=%s&i=%s&fp=%s&w=1", uri.getQueryParameter("t"), s, fn, fd, fp);

                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = httpClient.execute(new HttpGet(f.toString()));
                InputStream in = httpResponse.getEntity().getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                scan.idRec = finalJson;
            }


            urlConnection.disconnect();
            return String.valueOf(responseCode);
        } finally {
            urlConnection.disconnect();
        }
    }

    public static String Receive(String params) throws IOException
    {
        Uri uri = Uri.parse("http://mywallet.fun/parse?" + params);
        String fn = uri.getQueryParameter("fn");
        String fd = uri.getQueryParameter("i");
        String fp = addNullFP(uri.getQueryParameter("fp"));
        String login = uri.getQueryParameter("login");

        login = login.trim();
        login = "+" + login;

        String password = uri.getQueryParameter("password");
        String deviceId = uri.getQueryParameter("deviceId");
        String deviceOs = uri.getQueryParameter("deviceOs");

        URL url = new URL(
                "https://proverkacheka.nalog.ru:9999/v1/inns/*/kkts/*/fss/" + fn +
                        "/tickets/" + fd + "?fiscalSign=" + fp + "&sendToEmail=no");
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        try {
            String auth = login + ":" + password;

            byte[] data1 = auth.getBytes("utf8");
            String base64 = Base64.encodeToString(data1, Base64.NO_WRAP);
            String authHeaderValue = "Basic " + base64;
            urlConnection.setRequestProperty("Authorization", authHeaderValue);

            urlConnection.setRequestProperty("Device-Id", deviceId);
            urlConnection.setRequestProperty("Device-OS", deviceOs);

            urlConnection.connect();

            if (urlConnection.getResponseCode() == 406)
            {
                return "";
            }

            InputStream in = urlConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null)
            {
                buffer.append(line);
            }

            String finalJson = buffer.toString();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == 200)
            {
                JSONObject jsonObject = new JSONObject(finalJson).getJSONObject("document");
                JSONObject receipt = jsonObject.getJSONObject("receipt");
                JSONArray items = receipt.getJSONArray("items");

                for (int i=0; i < items.length(); i++)
                {
                    JSONObject tempObj = items.getJSONObject(i);

                    Formatter f = new Formatter();

                    f.format("http://www.mywallet.fun/API/addProduct.php?r=%s&n=%s&p=%s&c=%s&s=%s",
                            uri.getQueryParameter("idRec"),
                            URLEncoder.encode(tempObj.getString("name"),"UTF-8"),
                            tempObj.getString("price"),
                            tempObj.getString("quantity"),
                            tempObj.getString("sum"));

                    HttpClient httpClient = new DefaultHttpClient();
                    HttpResponse httpResponse = httpClient.execute(new HttpGet(f.toString()));
                    InputStream in2 = httpResponse.getEntity().getContent();
                    in2.close();
                }


                return "?count=" + items.length() + "&place=" + receipt.getString("user");
            }
            else
            {
                return "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        } finally {
            urlConnection.disconnect();
        }
    }

}
