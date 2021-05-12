package com.example.cookapp1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class hihi extends AppCompatActivity {
    static int count = 0;

    //    String str_url = "http://15.165.241.115/db/db.php";
    String str_url = "http://15.165.241.115/db/countselect.php";

    TextView textView1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hihi);

        textView1 = findViewById(R.id.hi_textview);

    }

    public void abcabc(View view) {
        String c = String.valueOf(count);
        gegestring gegestring = new gegestring();
        gegestring.execute(str_url, c);
        count++;
    }

    public class gegestring extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                String txt = (String) downloadUrl((String) strings[0], (String) strings[1]);
                return txt;

            } catch (Exception e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            textView1.setText(result);
        }

        private String downloadUrl(String api, String Count) throws IOException {
            HttpURLConnection conn = null;
            String param = "count=" + Count;

            try{
                URL url = new URL(api);
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.connect();

                OutputStream outputStream = conn.getOutputStream();
                outputStream.write(param.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int response = conn.getResponseCode();

                InputStream iStream;
                if (response == HttpURLConnection.HTTP_OK) {
                    iStream = conn.getInputStream();
                } else {
                    iStream = conn.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(iStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = "";

//                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
//                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));
//
//                String line = null;
//                String page = "";

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            }finally {
                conn.disconnect();
            }
        }
    }


//    public String getstring() {
//        try {
//            URL url = new URL("http://15.165.241.115/db/db.php");
//            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, "UTF-8"));
//
//            String line = null;
//            String page = "";
//
//            while ((line = bufferedReader.readLine()) != null) {
//                page += line;
//            }
//
//            return page;
//
//        } catch (Exception e) {
//            return "다운로드 실패";
//        }
//    }
}
