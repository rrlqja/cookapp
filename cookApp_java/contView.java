package com.example.cookapp1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class contView extends AppCompatActivity {
    TextView textView1,textView2,textView3, textView4;

    String contviewURL = "http://15.165.241.115/db/contview.php";

    contItem contItem;

    getCont getCont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contview);

        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.tv2);
        textView3 = findViewById(R.id.tv3);
        textView4 = findViewById(R.id.tv4);

        textView1.setText("title");
        textView2.setText("writer");
        textView3.setText("cont");
        textView4.setText("con_num");
        
        Intent getintent = getIntent();
        String position = getintent.getStringExtra("pos");
//        int position = getintent.getIntExtra("position", 0);

        getCont = new getCont();
        getCont.execute(contviewURL, position);

    }

    public class getCont extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return getString(strings);
        }

        protected void onPostExecute(String result) {
            getJson(result);
        }

        public String getString(String[] strings) {
            String strurl = strings[0];
            String position = strings[1];

            HttpURLConnection conn = null;
            String param = "con_num=" + position;

            try {
                URL url = new URL(strurl);
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

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                return null;
            }
        }

        public void getJson(String page) {
            try {
                JSONObject json = new JSONObject(page);

                JSONArray jArr = json.getJSONArray("result");

                JSONObject result_json;

                for (int i = 0; i < jArr.length(); i++) {
                    result_json = jArr.getJSONObject(i);

                    textView1.setText(result_json.getString("title"));
                    textView2.setText(result_json.getString("writer"));
                    textView3.setText(result_json.getString("cont"));
                    textView4.setText(result_json.getString("con_num"));
                }
            } catch (Exception e) {

            }
        }
    }

}
