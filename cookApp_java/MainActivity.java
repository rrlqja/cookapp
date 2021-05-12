package com.example.cookapp1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editId, editPwd;
    Button button1, button2;

    String login_chk_url = "http://15.165.241.115/db/findlog.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void login(View view) {
        editId = findViewById(R.id.id);
        editPwd = findViewById(R.id.pwd);
        String gId = editId.getText().toString();
        String gPwd = editPwd.getText().toString();

        login_chk login_chk = new login_chk();
        login_chk.execute(login_chk_url, gId, gPwd);

    }

    public class login_chk extends AsyncTask<String, Void, String> {

        String ID;

        @Override
        protected String doInBackground(String... strings) {
            try {
                this.ID = strings[1];
                return get_chk(strings);
            } catch (Exception e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
//            editId = findViewById(R.id.id);
//            editPwd = findViewById(R.id.pwd);
            String gId = editId.getText().toString();
//            String gPwd = editPwd.getText().toString();

            if (result.equals("success")) {
                Intent intent = new Intent(MainActivity.this, home.class);
//                intent.putExtra("usid", gId);
                intent.putExtra("userid", editId.getText().toString());
//                intent.putExtra("pwd", gPwd);
                startActivity(intent);
                finish();

//                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }else{

                Toast.makeText(getApplicationContext(), "로그인 정보를 다시 입력하세요.", Toast.LENGTH_LONG).show();
            }
        }

        private String get_chk(String[] strings) {
            String myurl = strings[0];
            String myId = strings[1];
            String myPwd = strings[2];

            HttpURLConnection httpURLConnection = null;

            String postp = "id=" + myId + "&pwd=" + myPwd;

            try {
                URL url = new URL(myurl);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postp.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int response = httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (response == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                return "다운로드 실패";
            }finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
    }

    public void signUp(View view) {
        Intent intent = new Intent(this, go_singup.class);
        startActivity(intent);
    }

    public void go(View view) {
        Intent intent = new Intent(this, getJson.class);
        startActivity(intent);
    }

    public void gg(View view) {
        Intent intent = new Intent(this, hihi.class);
        startActivity(intent);

    }

    public void go_List(View view) {
        Intent intent = new Intent(this, Li.class);
        startActivity(intent);
    }

    public void imgJson(View view) {
        Intent intent = new Intent(this, iJson.class);
        startActivity(intent);
    }
}