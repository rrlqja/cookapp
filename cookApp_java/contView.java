package com.example.cookapp1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public static Activity _contview;

    String oriURL = "http://15.165.241.115";
    String contviewURL = "http://15.165.241.115/db/contview.php";
    String contlikeURL = "http://15.165.241.115/db/cont_like.php";

    TextView title_textview, cont_textview, writer_textview, delete_textview;
    TextView contlike_textview;
    ImageView cont_img_imgview;
    ImageView likeheart_imgview;

    contview_ingre_season ingre, season;
    contview_sequence sequence;

    contview_ingre_season_recyclerview_Adapter ingre_adapter, season_adapter;
    contview_sequence_recyclerview_Adapter sequence_adapter;

    ArrayList<contview_ingre_season> ingre_list = new ArrayList<>();
    ArrayList<contview_ingre_season> season_list = new ArrayList<>();
    ArrayList<contview_sequence> sequence_list = new ArrayList<>();
    ArrayList<Bitmap> sequence_img_list = new ArrayList<>();

    RecyclerView ingre_recyclerview, season_recyclerview, sequence_recyclerview;

    getCont getCont;

    String con_num;
    String user_id;
    String contlike;
    String writer;
    ArrayList<String> contlike_user = new ArrayList<>();

    insertLike insertLike;

    String del_result = "";
    String delUrl = "http://15.165.241.115/db/delcont.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contview);

        title_textview = findViewById(R.id.contview_title);
        cont_textview = findViewById(R.id.contview_cont);
        writer_textview = findViewById(R.id.contview_writer);
        cont_img_imgview = findViewById(R.id.contview_cont_img);
        contlike_textview = findViewById(R.id.contlike_tv);
        delete_textview = findViewById(R.id.txdelete);
        delete_textview.setVisibility(View.GONE);
        likeheart_imgview = findViewById(R.id.likeheart);

        _contview = contView.this;

        ingre_list.clear();
        season_list.clear();
        sequence_list.clear();

        Intent getintent = getIntent();
        con_num = getintent.getStringExtra("pos");
        user_id = getintent.getStringExtra("userid");

        ingre_recyclerview = findViewById(R.id.ingre_recycleview);
        ingre_recyclerview.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        season_recyclerview = findViewById(R.id.season_recycleview);
        season_recyclerview.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        sequence_recyclerview = findViewById(R.id.sequence_recycleview);
        sequence_recyclerview.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        getCont = new getCont();
        getCont.execute(contviewURL, con_num);

        CountDownTimer CDT = new CountDownTimer(15 * 1000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                sequenceInitadapt();
            }

            @Override
            public void onFinish() {
                sequenceInitadapt();
            }
        };

        CDT.start();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CDT.cancel();
            }
        }, 15000);

    }

    public void del_cont(View view) {
        Intent delintent = new Intent(contView.this, ask_del_cont.class);
        delintent.putExtra("con_num", con_num);
        delintent.putExtra("user_id", user_id);

        startActivity(delintent);
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
        sequence_adapter.notifyItemChanged(sequence_list.size());
    }

    public void insert_contlike(View view) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                insertLike = new insertLike();
                insertLike.execute(contlikeURL, con_num);
            }
        }, 500);

    }

    public void contlike_color() {
        for (int i = 0; i < contlike_user.size(); i++) {
            if (contlike_user.get(i) != null) {
                if (contlike_user.get(i).equals(user_id)) {
                    likeheart_imgview.setImageResource(R.drawable.yesheart);
                }else{
                    likeheart_imgview.setImageResource(R.drawable.noheart);
                }
            }
        }
    }

    public void setcontlike_color(String string) {
        if (string.equals("delete")) {
            likeheart_imgview.setImageResource(R.drawable.noheart);
        } else if (string.equals("insert")) {
            likeheart_imgview.setImageResource(R.drawable.yesheart);
        }
    }

    public class getCont extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return getString(strings);
        }

        protected void onPostExecute(String result) {
            getJson(result);

            contlike_color();

            ingreInitadapt();
            seasonInitadapt();

            sequenceInitadapt();

            if (user_id != null && writer != null) {
                if (user_id.equals(writer)) {
                    delete_textview.setVisibility(View.VISIBLE);
                }
            }

        }

        public String getString(String[] strings) {
            String strurl = strings[0];
            String position = strings[1];

            HttpURLConnection conn = null;
            String param = "con_num=" + position + "&user_id=" + user_id;

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
                    writer = result_json.getString("writer");

                    String imgurl = oriURL + result_json.getString("img_src").substring(2);

                    bitchange bitchange = new bitchange();
                    bitchange.execute(imgurl);

                }

                JSONObject contlike_result_json;
                JSONArray contlikejArr = json.getJSONArray("contlike_result");
                for (int i = 0; i < contlikejArr.length(); i++) {
                    contlike_result_json = contlikejArr.getJSONObject(i);
                    contlike = contlike_result_json.getString("contlike");

                    contlike_textview.append(contlike);

                }

                JSONObject contlike_user_result_json;
                JSONArray contlike_userjArr = json.getJSONArray("contlike_user_result");
                for (int i = 0; i < contlike_userjArr.length(); i++) {
                    contlike_user_result_json = contlike_userjArr.getJSONObject(i);
                    contlike_user.add(contlike_user_result_json.getString("contlike_user"));


                }

                JSONObject ingre_result_json;
                JSONArray ingrejArr = json.getJSONArray("ingre_result");
                for (int i = 0; i < ingrejArr.length(); i++) {
                    ingre = new contview_ingre_season();
                    ingre_result_json = ingrejArr.getJSONObject(i);
                    ingre.setName(ingre_result_json.getString("ingre"));
                    ingre.setWeight(ingre_result_json.getString("ingre_we"));
                    ingre_list.add(ingre);

                }

                JSONObject season_result_json;
                JSONArray seasonjArr = json.getJSONArray("season_result");
                for (int i = 0; i < seasonjArr.length(); i++) {
                    season = new contview_ingre_season();
                    season_result_json = seasonjArr.getJSONObject(i);
                    season.setName(season_result_json.getString("season"));
                    season.setWeight(season_result_json.getString("season_we"));
                    season_list.add(season);
                }

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

    public class insertLike extends AsyncTask<String, Void, String> {
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
            String param = "con_num=" + position + "&user_id=" + user_id;

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

                JSONObject like_result_json;
                JSONArray likejArr = json.getJSONArray("like_result");
                for (int i = 0; i < likejArr.length(); i++) {
                    like_result_json = likejArr.getJSONObject(i);
                    String res = like_result_json.getString("like_result");
                    setcontlike_color(res);
                }

                JSONObject likenum_result_json;
                JSONArray likenumjArr = json.getJSONArray("likenum_result");
                for (int i = 0; i < likenumjArr.length(); i++) {
                    likenum_result_json = likenumjArr.getJSONObject(i);
                    String likenum = likenum_result_json.getString("likenum_result");
                    contlike_textview.setText(likenum);
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
