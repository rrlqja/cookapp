package com.example.cookapp1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import androidx.appcompat.app.AppCompatActivity;

public class Li extends AppCompatActivity {

    TextView textView1;
    EditText editText1;
    String Surl = "http://15.165.241.115/db/extb.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_li);

        textView1 = findViewById(R.id.extx1);
        editText1 = findViewById(R.id.exed1);

    }

    public void show_list(View view) {

        String str = editText1.getText().toString();

        extb extb = new extb();
        extb.execute(str);

    }

    public class extb extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return getS(strings);
        }

        protected void onPostExecute(String result) {
            textView1.setText(result);
        }

        public String getS(String[] strings) {

            String str = strings[0];

            HttpURLConnection conn = null;
            try {
                URL url = new URL(Surl);
                conn = (HttpURLConnection) url.openConnection();

                String boundary = "***^^^^***";

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
//                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.setDoOutput(true);
                conn.connect();

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"str\" \r\n\r\n" + URLEncoder.encode(str, "UTF-8"));

                wr.writeBytes("\r\n--" + boundary + "--\r\n");
                wr.flush();

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
    }

}
