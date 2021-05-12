package com.example.cookapp1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.appcompat.app.AppCompatActivity;

public class go_signup extends AppCompatActivity {

    TextView textView;

    EditText editText1, editText2, editText3;
//    EditText editText4;

    Button button1;

    String strUrl = "http://15.165.241.115/db/signup.php";
    String abab = "http://15.165.241.115/db/findID.php";

    String id, name, pwd;
//    String email;

    boolean pass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_signup);

        textView = findViewById(R.id.inpu_text);
        button1 = findViewById(R.id.id_chk);
    }

    public void inpu(View view) {
        editText1 = findViewById(R.id.inpu_one);
        editText2 = findViewById(R.id.inpu_two);
        editText3 = findViewById(R.id.inpu_three);
//        editText4 = findViewById(R.id.inpu_four);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                pass = false;
            }
        });

        id = editText1.getText().toString();
        name = editText2.getText().toString();
        pwd = editText3.getText().toString();
//        email = editText4.getText().toString();

        inputUrl inputUrl = new inputUrl();

        if (pass == false) {
            Toast.makeText(getApplicationContext(), "아이디 중복체크를 하십시오.", Toast.LENGTH_LONG).show();
        } else if (id.equals("") || id.equals(null)) {
            Toast.makeText(getApplicationContext(), "아이디를 입력하십시오.", Toast.LENGTH_LONG).show();
        } else if (name.equals("") || name.equals(null)) {
            Toast.makeText(getApplicationContext(), "이름을 입력하십시오.", Toast.LENGTH_LONG).show();
        } else if (pwd.equals("") || pwd.equals(null)) {
            Toast.makeText(getApplicationContext(), "비밀번호를 입력하십시오.", Toast.LENGTH_LONG).show();
        } else {

            if (pass = false) {
                Toast.makeText(getApplicationContext(), "아이디 중복체크를 하십시오.", Toast.LENGTH_LONG).show();
            }else{
                inputUrl.execute(strUrl, id, name, pwd);
            }

            Intent intent = new Intent(this, MainActivity.class);
            Toast.makeText(getApplicationContext(), "가입 완료", Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }

    }

    public void go_idchk(View view) {
        editText1 = findViewById(R.id.inpu_one);

        String go_id = editText1.getText().toString();

        if (go_id.equals("") || go_id.equals(null)) {
            Toast.makeText(getApplicationContext(), "아이디를 입력해야 합니다.", Toast.LENGTH_LONG).show();
        }else{
            chk_id chk_id = new chk_id();
            chk_id.execute(abab, go_id);
        }
    }

    public class inputUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            return getgo(strings);
        }

        protected void onPostExecute(String result) {

        }

        private String getgo(String[] strings) {
            String id = strings[1];
            String name = strings[2];
            String pwd = strings[3];

            String mURL = strings[0];
            String postParam = "id=" + id + "&name=" + name + "&pwd=" + pwd;

            HttpURLConnection httpURLConnection = null;
            try {

                URL url = new URL(mURL);
                httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParam.getBytes("UTF-8"));
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
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
            }
        }
    }

    public class chk_id extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return getS(strings);
        }

        protected void onPostExecute(String result) {
            if (result.equals("ID_Pass")) {
                pass = true;
                Toast.makeText(getApplicationContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
            }else{
                pass = false;
                Toast.makeText(getApplicationContext(), "사용불가능한 아이디 입니다.", Toast.LENGTH_LONG).show();
            }
        }

        private String getS(String[] strings) {
            String go_url = strings[0];
            String find_id = strings[1];

            HttpURLConnection id_httpURLConnection = null;

            String postid = "id=" + find_id;

            try {
                URL id_url = new URL(go_url);
                id_httpURLConnection = (HttpURLConnection) id_url.openConnection();

                id_httpURLConnection.setReadTimeout(5000);
                id_httpURLConnection.setConnectTimeout(5000);
                id_httpURLConnection.setRequestMethod("POST");
                id_httpURLConnection.connect();

                OutputStream outputStream = id_httpURLConnection.getOutputStream();
                outputStream.write(postid.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int response = id_httpURLConnection.getResponseCode();

                InputStream inputStream;
                if (response == HttpURLConnection.HTTP_OK) {
                    inputStream = id_httpURLConnection.getInputStream();
                } else {
                    inputStream = id_httpURLConnection.getErrorStream();
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
            }
        }

    }

}
