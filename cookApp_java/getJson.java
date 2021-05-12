package com.example.cookapp1;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class getJson extends AppCompatActivity {

    TextView textView1, textView2;
    Button button;
    ListView listView1;

    String result = null;

    String NAME, ID = "";

    HashMap<Integer, String> idMap = new HashMap<>();
    HashMap<Integer, String> nameMap = new HashMap<>();
    HashMap<Integer, String> pwdMap = new HashMap<>();
//    HashMap<Integer, String> emailMap = new HashMap<>();

    String strUrl = "http://15.165.241.115/db/db.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getjson);

        textView1 = findViewById(R.id.text1);
        textView2 = findViewById(R.id.text2);
        button = findViewById(R.id.bt1);
//        listView1.findViewById(R.id.list1);


        gogoUrl gogoUrl = new gogoUrl();

        gogoUrl.execute(strUrl);

    }

    public class gogoUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                String txt = (String) downloadUrl((String) strings[0]);
                return txt;
            } catch (Exception e) {
                return "다운로드 실패";
            }
        }

        protected void onPostExecute(String result) {
            textView1.setText(result);
        }

        private String downloadUrl(String api) throws IOException {
            HttpURLConnection conn = null;

            try {
                URL url = new URL(api);
                conn = (HttpURLConnection) url.openConnection();
                BufferedInputStream buf = new BufferedInputStream(conn.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(buf, "utf-8"));

                String line = null;
                String page = "";

                while ((line = bufreader.readLine()) != null) {
                    page += line;
                }

                return page;
            } catch (Exception e) {
                return null;
            } finally {
                conn.disconnect();
            }
        }
    }

    public void vv(View view) {
        Toast.makeText(getApplicationContext(), "시작시작", Toast.LENGTH_LONG).show();

        goJSON goJSON = new goJSON();
        goJSON.execute(strUrl);

    }

    public class goJSON extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings){
            return getS(strings[0]);

        }

        protected void onPostExecute(String result) {
            getJ(result);
//            for (int i = 0; i < idMap.size(); i++) {
//                if (i == 0) {
//                    textView2.setText(idMap.get(i)+" ");
//                }else{
//                    textView2.append(idMap.get(i)+" ");
//                }
//            }
        }

        private String getS(String str) {
            try {
                URL url = new URL(str);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                BufferedInputStream bbff = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufreader = new BufferedReader(new InputStreamReader(bbff, "UTF-8"));

                String line = null;
                String page = "";

                while ((line = bufreader.readLine()) != null) {
                    page += line;
                }

                return page;

            } catch (Exception e) {
                return null;
            }
        }

        private void getJ(String page) {

            try {
                JSONObject jsonObject = new JSONObject(page);

                JSONArray jArr = jsonObject.getJSONArray("result");
                for (int i = 0; i < jArr.length(); i++) {

                    jsonObject = jArr.getJSONObject(i);

                    if (i == 0) {
                        textView2.setText(jsonObject.getString("id")+"\n");
                    }else{
                        textView2.append(jsonObject.getString("id")+"\n");
                    }

//                    for (int j = 0; j < jArr.length(); j++) {
//                        if (j == 0) {
//                            textView2.setText(jsonObject.getString("id")+" ");
//                        }else{
//                            textView2.append(jsonObject.getString("id")+" ");
//                        }
//                    }

//                    nameMap.put(i, jsonObject.getString("name"));
//                    idMap.put(i, jsonObject.getString("id"));
//                    pwdMap.put(i, jsonObject.getString("pwd"));
////                    emailMap.put(i, jsonObject.getString("email"));

                }
            } catch (Exception e) {

            }


        }

    }

}
