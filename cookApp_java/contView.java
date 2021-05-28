package com.example.cookapp1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class contView extends AppCompatActivity {

    String oriURL = "http://15.165.241.115";
    String contviewURL = "http://15.165.241.115/db/contview.php";

    TextView title_textview, cont_textview, writer_textview;
    ImageView cont_img_imgview;

    contview_ingre_season ingre, season;
    contview_sequence sequence;

    contview_ingre_season_recyclerview_Adapter ingre_adapter, season_adapter;
    contview_sequence_recyclerview_Adapter sequence_adapter;

    ArrayList<contview_ingre_season> ingre_list = new ArrayList<>();
    ArrayList<contview_ingre_season> season_list = new ArrayList<>();
    ArrayList<contview_sequence> sequence_list = new ArrayList<>();

    RecyclerView ingre_recyclerview, season_recyclerview, sequence_recyclerview;

    getCont getCont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contview);

        title_textview = findViewById(R.id.contview_title);
        cont_textview = findViewById(R.id.contview_cont);
        writer_textview = findViewById(R.id.contview_writer);
        cont_img_imgview = findViewById(R.id.contview_cont_img);

        ingre_list.clear();
        season_list.clear();
        sequence_list.clear();

        Intent getintent = getIntent();
        String position = getintent.getStringExtra("pos");
        //        int position = getintent.getIntExtra("position", 0);

        ingre_recyclerview = findViewById(R.id.ingre_recycleview);
        ingre_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        season_recyclerview = findViewById(R.id.season_recycleview);
        season_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        sequence_recyclerview = findViewById(R.id.sequence_recycleview);
        sequence_recyclerview.setLayoutManager(new LinearLayoutManager(this));

        getCont = new getCont();
        getCont.execute(contviewURL, position);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ingreInitadapt();
                seasonInitadapt();
                sequenceInitadapt();
            }
        }, 1000);

    }

    public void ingreInitadapt() {
        ingre_adapter = new contview_ingre_season_recyclerview_Adapter(ingre_list);
        ingre_recyclerview.setAdapter(ingre_adapter);
    }
    public void seasonInitadapt() {
        season_adapter = new contview_ingre_season_recyclerview_Adapter(season_list);
        season_recyclerview.setAdapter(season_adapter);
    }
    public void sequenceInitadapt() {
        sequence_adapter = new contview_sequence_recyclerview_Adapter(sequence_list);
        sequence_recyclerview.setAdapter(sequence_adapter);
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

                JSONObject result_json;
                JSONArray jArr = json.getJSONArray("result");
                for (int i = 0; i < jArr.length(); i++) {
                    result_json = jArr.getJSONObject(i);
                    title_textview.setText(result_json.getString("title"));
                    cont_textview.setText(result_json.getString("cont"));
                    writer_textview.setText(result_json.getString("writer"));

                    String imgurl = oriURL + result_json.getString("img_src").substring(2);

                    bitchange bitchange = new bitchange();
                    bitchange.execute(imgurl);

                }

                JSONObject ingre_result_json;
                JSONArray ingrejArr = json.getJSONArray("ingre_result");
                for (int i = 0; i < ingrejArr.length(); i++) {
                    ingre = new contview_ingre_season();
                    ingre_result_json = ingrejArr.getJSONObject(i);
                    ingre.setName(ingre_result_json.getString("ingre"));
                    ingre.setWeight(ingre_result_json.getString("ingre_we"));
                    ingre_list.add(ingre);

                    title_textview.append(ingre_result_json.getString("ingre"));
                    title_textview.append(ingre_result_json.getString("ingre_we"));

                }

                JSONObject season_result_json;
                JSONArray seasonjArr = json.getJSONArray("season_result");
                for (int i = 0; i < seasonjArr.length(); i++) {
                    season = new contview_ingre_season();
                    season_result_json = seasonjArr.getJSONObject(i);
                    season.setName(season_result_json.getString("season"));
                    season.setWeight(season_result_json.getString("season_we"));
                    season_list.add(season);

                    title_textview.append(season_result_json.getString("season"));
                    title_textview.append(season_result_json.getString("season_we"));
                }

                title_textview.append("tlqkf");
                JSONObject sequence_result_json;
                JSONArray sequencejArr = json.getJSONArray("sequence_result");
                for (int i = 0; i < sequencejArr.length(); i++) {
                    sequence = new contview_sequence();
                    sequence_result_json = sequencejArr.getJSONObject(i);
                    sequence.setSequence_num(sequence_result_json.getString("sequence_num"));
                    sequence.setSequence(sequence_result_json.getString("sequence"));
                    sequence.setSequence_img(oriURL + sequence_result_json.getString("sequence_img_src").substring(2));
                    sequence_list.add(sequence);
                }

            } catch (Exception e) {

            }
        }
    }

    public class bitchange extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urli = strings[0];
            Bitmap bitmap = null;

            try {
                InputStream in = new URL(urli).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            cont_img_imgview.setImageBitmap(result);
        }
    }

}
