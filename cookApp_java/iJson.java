package com.example.cookapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class iJson extends AppCompatActivity {

    TextView textView1, textView2;
    ImageView imageView1, imageView2;
    Button button1;

    HashMap<Integer, String> img = new HashMap<>();
    HashMap<Integer, String> iname = new HashMap<>();

    String oriUrl = "http://15.165.241.115";
    String url = "http://15.165.241.115/db/showJimg.php";
//    String url = "http://15.165.241.115/db/showimg.php";
    String imgurl = "http://15.165.241.115/upload_files/22.jpg";

    InputStream inputStream;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ijson);

        imageView1 = findViewById(R.id.iv1);
        textView1 = findViewById(R.id.tx1);
        textView2 = findViewById(R.id.tx2);
        imageView2 = findViewById(R.id.iv2);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView2.setImageBitmap(bitmap);
            }
        }, 3000);

    }

    public void show_img(View view) {
        downImg downImg = new downImg();
        downImg.execute(url);

//        imageView1.setImageURI(Uri.parse("http://15.165.241.115/db/showimg.php"));

//        downBit downBit = new downBit();
//        downBit.execute(oriUrl+img.get(0));

    }

    public class downImg extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

//            Bitmap bitmap = null;
//            try {
//                String img_url = strings[0];
//                URL url = new URL(img_url);
//
//                InputStream in = new java.net.URL(img_url).openStream();
//                bitmap = BitmapFactory.decodeStream(in);
////                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch (Exception e) {
//                return null;
//            }
//            return bitmap;

            try {
                String txt = downloadurl(strings[0]);
                return txt;
            } catch (Exception e) {
                return "다운로드 실패";
            }

        }

        public void onPostExecute(String result) {
            getJ(result);
            String i = oriUrl + img.get(0).substring(2);
            textView1.setText(img.get(0).substring(2));
//            textView1.setText(oriUrl + img.get(0).substring(2));
//            textView1.setText(i);
//            textView2.setText(imgurl);

            downBit downBit = new downBit();
            downBit.execute(i);

//            imageView1.setImageBitmap(img.get(0));

//            imageView1.setImageBitmap(result);

//
//            for (int i = 0; i < iname.size(); i++) {
//                imageView1.setImageBitmap(img.get(i));
//                textView1.setText(iname.get(i));
//            }


        }

        private String downloadurl(String stringurl) {
            try {
                URL url = new URL(stringurl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(connection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream, "UTF-8"));

                String line = null;
                String page = "";

                while ((line = bufferedReader.readLine()) != null) {
                    page += line;
                }

                return page;

            } catch (Exception e) {
                return null;
            }
        }

        private void getI(String Url) {

        }

        private void getJ(String page) {

            try {
                JSONObject json = new JSONObject(page);

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(json);

                JSONArray jArr = json.getJSONArray("result");

                for (int i = 0; i < jArr.length(); i++) {
                    json = jArr.getJSONObject(i);

                    iname.put(i, json.getString("iName"));
                    img.put(i, json.getString("iMg"));

//                    img.put(i, (Bitmap) json.get("im"));
                }
            } catch (Exception e) {

            }
        }
    }

    public class downBit extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            String urli = strings[0];
            Bitmap bitmap2 = null;

            try {
                InputStream in = new URL(urli).openStream();
                bitmap2 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                return null;
            }
            textView2.setText(urli);
            return bitmap2;

        }

        protected void onPostExecute(Bitmap result) {
            imageView1.setImageBitmap(result);
            bitmap = result;
        }
    }
}
