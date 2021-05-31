package com.example.cookapp1;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class ask_del_cont extends Activity {

    TextView textView;
    Button ans_yes, ans_no;

    String con_num;
    String user_id;
    String delUrl = "http://15.165.241.115/db/delcont.php";

    String del_result = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_del_cont);

        ans_yes = findViewById(R.id.ans_yes);
        ans_no = findViewById(R.id.ans_no);

        Intent intent = getIntent();
        con_num = intent.getStringExtra("con_num");
        user_id = intent.getStringExtra("user_id");

    }

    public void del_yes(View view) {

//        Toast.makeText(ask_del_cont.this, con_num, Toast.LENGTH_LONG).show();

        delCont delCont = new delCont();
        delCont.execute(con_num);

        Toast.makeText(ask_del_cont.this, "삭제중 입니다. 잠시만 기다려주세요.", Toast.LENGTH_LONG).show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ask_del_cont.this, home.class);
//                intent.putExtra("con_num", con_num);
                intent.putExtra("userid", user_id);

//                contView contView = (contView) com.example.cookapp1.contView._contview;
//                contView.finish();

                home home = (home) com.example.cookapp1.home._home;
                home.finish();

//                startActivityForResult(intent, 1);
                startActivity(intent);
                finish();
            }
        }, 500);

    }
    public void del_no(View view) {
        finish();
    }

    public class delCont extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return getS(strings);
        }

        protected void onPostExecute(String result) {
            if (result == "success") {
                del_result = "삭제하였습니다.";
            }else{
                del_result = "삭제에 실패하였습니다";
            }
        }

        public String getS(String[] strings) {
            String con_num = strings[0];

            HttpURLConnection conn = null;
            String param = "con_num=" + con_num;

            try {
                URL url = new URL(delUrl);
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
    }

}
