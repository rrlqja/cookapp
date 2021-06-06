package com.example.cookapp1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class createCont extends AppCompatActivity {

    public static Activity _createcont;

    saveContItem saveContItem;

    EditText editText1, editText2;
    ImageView imageView1;

    String user_id;

    String title = "";
    Bitmap cont_img;
    Uri cont_img_Uri;
    String cont_img_absol_uri;
    String cont = "";

    static boolean title_pass = false;
    static boolean cont_img_pass = false;
    static boolean cont_pass = false;

    static boolean imgpass = false;

    ListView listView1, listView2;

    RecyclerView recyclerView1;

    ArrayList<Integer> ingre_num = new ArrayList<>();
    ArrayList<String> ingre_list = new ArrayList<>();
    ArrayList<String> ingre_we_list = new ArrayList<>();
    static boolean ingre_pass = false;

    ArrayList<Integer> season_num = new ArrayList<>();
    ArrayList<String> season_list = new ArrayList<>();
    ArrayList<String> season_we_list = new ArrayList<>();
    static boolean season_pass = false;

    ArrayList<Integer> sequence_num = new ArrayList<>();
    ArrayList<String> sequence_cont = new ArrayList<>();
    ArrayList<Bitmap> sequence_img = new ArrayList<>();
    ArrayList<Uri> sequence_img_uri = new ArrayList<>();
    static boolean sequence_pass = false;

    ingrelistAdapter ingre_listAdapter;
    seasonlistAdapter season_listAdapter;

    sequencerecycleradapter sequence_recyclerAdapter;
    ArrayList<sequence_item> sequence_items = new ArrayList<>();
    sequence_item sequence_item;

    static int Pos = 0;

    String create_url = "http://15.165.241.115/db/createcont.php";
    String excreate_url = "http://15.165.241.115/db/excreatecont.php";

    static String[] ingre_num_strings = new String[20];
    static String[] ingre_strings = new String[20];
    static String[] ingre_we_strings = new String[20];
    static String[] season_num_strings = new String[20];
    static String[] season_strings = new String[20];
    static String[] season_we_strings = new String[20];
    static String[] sequence_nums_strings = new String[20];
    static String[] sequence_conts_strings = new String[20];
    static Bitmap[] sequence_imgs_bitmap = new Bitmap[20];
    static Uri[] sequence_imgs_urls = new Uri[20];
    static String[] sequence_imgs_absolute_urls = new String[20];

    saveCont saveCont;
    exsvcont exsvcont;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcont);

        editText1 = findViewById(R.id.title);
        imageView1 = findViewById(R.id.ibimg);
        editText2 = findViewById(R.id.cont);

        _createcont = createCont.this;

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                title = s.toString();
                title_pass = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                title = s.toString();
                title_pass = true;
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cont = s.toString();
                cont_pass = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                cont = s.toString();
                cont_pass = true;
            }
        });
        imageView1 = findViewById(R.id.ibimg);

        ingre_listAdapter = new ingrelistAdapter();
        season_listAdapter = new seasonlistAdapter();

        listView1 = findViewById(R.id.ingre);
        listView2 = findViewById(R.id.season);

        listView1.setAdapter(ingre_listAdapter);
        listView2.setAdapter(season_listAdapter);

        ingre_listAdapter.addItem();
        ingre_listAdapter.notifyDataSetChanged();
        ingre_listheight();

        season_listAdapter.addItem();
        season_listAdapter.notifyDataSetChanged();
        season_listheight();

        Matrix matrix1 = new Matrix();
        matrix1.postScale(1.0f, 1.0f);
        imageView1.setImageMatrix(matrix1);

        Intent getintent = getIntent();
        user_id = getintent.getStringExtra("userid");

        recyclerView1 = findViewById(R.id.sequence);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        sequence_item = new sequence_item();
        sequence_items.add(sequence_item);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initadp();
            }
        }, 1000);

    }

    public void initadp() {
        sequence_recyclerAdapter = new sequencerecycleradapter(sequence_items);
        recyclerView1.setAdapter(sequence_recyclerAdapter);
        sequence_recyclerAdapter.setOnItemClickListener(new sequencerecycleradapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent intent = new Intent();
                Pos = position;
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 2);

            }
        });
    }

    private void ingre_listheight() {

        int totalheight = 0;
        for (int i = 0; i < ingre_listAdapter.getCount(); i++) {
            View listitem = ingre_listAdapter.getView(i, null, listView1);
            listitem.measure(0, 0);
            totalheight += listitem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params1 = listView1.getLayoutParams();
        params1.height = totalheight + (listView1.getDividerHeight() * (ingre_listAdapter.getCount() - 1));
    }

    private void season_listheight() {
        int totalheight = 0;
        for (int i = 0; i < season_listAdapter.getCount(); i++) {
            View listitem = season_listAdapter.getView(i, null, listView2);
            listitem.measure(0, 0);
            totalheight += listitem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params2 = listView2.getLayoutParams();
        params2.height = totalheight + (listView2.getDividerHeight() * (season_listAdapter.getCount() - 1));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Matrix matrix1 = new Matrix();
        matrix1.postScale(1.0f, 1.0f);
        imageView1.setImageMatrix(matrix1);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap bitmap = null;


            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));

                cont_img_Uri = uri;


            } catch (Exception e) {
            }

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            imageView1.setBackground(bitmapDrawable);

            cont_img_absol_uri = getpath(uri);

            cont_img = bitmap;
            cont_img_pass = true;
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgpass = true;

            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
                sequence_items.get(Pos).setSequence_img_uri(uri);
                sequence_items.get(Pos).setSequence_img_absolute_uri(getpath(uri));
            } catch (Exception e) {
            }

            sequence_items.get(Pos).setSequence_img(bitmap);
            sequence_recyclerAdapter.notifyDataSetChanged();

        }

    }

    public String getpath(Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            res = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return res;
    }

    public void addingreList(View view) {
        ingre_pass = false;

        if (ingre_listAdapter.getCount() < 20) {
            ingre_listAdapter.addItem();

            ingre_listAdapter.notifyDataSetChanged();

            ingre_listheight();
        }else {
            Toast.makeText(createCont.this, "더 이상 생성할 수 없습니다.", Toast.LENGTH_LONG).show();
        }
    }
    public void addseasonList(View view) {
        season_pass = false;

        if (season_listAdapter.getCount() < 20) {
            season_listAdapter.addItem();

            season_listAdapter.notifyDataSetChanged();

            season_listheight();
        } else {
            Toast.makeText(createCont.this, "더 이상 생성할 수 없습니다.", Toast.LENGTH_LONG).show();
        }
    }
    public void addsequenceList(View view) {
        imgpass = false;
        season_pass = false;

        if (sequence_items.size() >= 20) {
            Toast.makeText(createCont.this, "더 이상 생성할 수 없습니다.", Toast.LENGTH_LONG).show();
        } else {
            sequence_item = new sequence_item();

            sequence_items.add(sequence_item);

            sequence_recyclerAdapter.notifyItemChanged(sequence_items.size());
        }

    }

    public void getimg(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        startActivityForResult(intent, 1);
    }

    public void getIngre() {
        ingre_pass = false;
        ingre_num.clear();
        ingre_list.clear();
        ingre_we_list.clear();
        int ingre_count = ingre_listAdapter.getCount();

        for (int i = 0; i < ingre_count; i++) {
            if (ingre_listAdapter.geted1(i) == null) {
            } else {
                if ((ingre_listAdapter.geted1(i).getName() == null) && (ingre_listAdapter.geted1(i).getWeight() == null)) {
                    ingre_pass = false;
                    ingre_num.clear();
                    ingre_list.clear();
                    ingre_we_list.clear();
                    Toast.makeText(createCont.this, "재료를 입력하세요1", Toast.LENGTH_LONG).show();
                } else if ((ingre_listAdapter.geted1(i).getName() == null) && (ingre_listAdapter.geted1(i).getWeight()) != null) {
                    ingre_pass = false;
                    ingre_num.clear();
                    ingre_list.clear();
                    ingre_we_list.clear();
                    Toast.makeText(createCont.this, "재료를 입력하세요2", Toast.LENGTH_LONG).show();
                } else if ((ingre_listAdapter.geted1(i).getName() != null) && (ingre_listAdapter.geted1(i).getWeight() == null)) {
                    ingre_pass = false;
                    ingre_num.clear();
                    ingre_list.clear();
                    ingre_we_list.clear();
                    Toast.makeText(createCont.this, "용량을 입력하세요3", Toast.LENGTH_LONG).show();
                } else {
                    ingre_num.add(i+1);
                    ingre_list.add(ingre_listAdapter.geted1(i).getName());
                    ingre_we_list.add(ingre_listAdapter.geted1(i).getWeight());

                    ingre_num_strings[i] = String.valueOf(i + 1);
                    ingre_strings[i] = ingre_listAdapter.geted1(i).getName();
                    ingre_we_strings[i] = ingre_listAdapter.geted1(i).getWeight();
                    ingre_pass = true;
                }
            }
        }
    }
    public void getSeason() {
        season_pass = false;
        season_num.clear();
        season_list.clear();
        season_we_list.clear();

        int season_count = season_listAdapter.getCount();

        for (int i = 0; i < season_count; i++) {
            if (season_listAdapter.geted1(i) == null) {
            } else {
                if ((season_listAdapter.geted1(i).getName() == null) && (season_listAdapter.geted1(i).getWeight() == null)) {
                    season_pass = false;
                    season_num.clear();
                    season_list.clear();
                    season_we_list.clear();
                    Toast.makeText(createCont.this, "양념을 입력하세요4", Toast.LENGTH_LONG).show();
                } else if ((season_listAdapter.geted1(i).getName() == null) && (season_listAdapter.geted1(i).getWeight()) != null) {
                    season_pass = false;
                    season_num.clear();
                    season_list.clear();
                    season_we_list.clear();
                    Toast.makeText(createCont.this, "양념을 입력하세요5", Toast.LENGTH_LONG).show();
                } else if ((season_listAdapter.geted1(i).getName() != null) && (season_listAdapter.geted1(i).getWeight() == null)) {
                    season_pass = false;
                    season_num.clear();
                    season_list.clear();
                    season_we_list.clear();
                    Toast.makeText(createCont.this, "용량을 입력하세요6", Toast.LENGTH_LONG).show();
                } else {
                    season_num.add(i+1);
                    season_list.add(season_listAdapter.geted1(i).getName());
                    season_we_list.add(season_listAdapter.geted1(i).getWeight());

                    season_num_strings[i] = String.valueOf(i + 1);
                    season_strings[i] = season_listAdapter.geted1(i).getName();
                    season_we_strings[i] = season_listAdapter.geted1(i).getWeight();
                    season_pass = true;
                }
            }
        }
    }
    public void getSequence() {
        sequence_pass = false;
        sequence_num.clear();
        sequence_cont.clear();
        sequence_img.clear();

        int count = sequence_recyclerAdapter.getItemCount();

        for (int i = 0; i < count; i++) {
            if (sequence_recyclerAdapter.geted1(i) == null) {
                Toast.makeText(createCont.this, "값이 없다", Toast.LENGTH_LONG).show();
            }else{
                if ((sequence_recyclerAdapter.geted1(i).getSequence_cont() == null) && (sequence_recyclerAdapter.geted1(i).getSequence_img() == null)) {
                    sequence_pass = false;
                    sequence_num.clear();
                    sequence_cont.clear();
                    sequence_img.clear();
                    Toast.makeText(createCont.this, "조리법을 입력하세요7", Toast.LENGTH_LONG).show();
                } else if ((sequence_recyclerAdapter.geted1(i).getSequence_cont() == null) && (sequence_recyclerAdapter.geted1(i).getSequence_img() != null)) {
                    sequence_pass = false;
                    sequence_num.clear();
                    sequence_cont.clear();
                    sequence_img.clear();
                    Toast.makeText(createCont.this, "조리법을 입력하세요8", Toast.LENGTH_LONG).show();
                } else if ((sequence_recyclerAdapter.geted1(i).getSequence_cont() != null) && (sequence_recyclerAdapter.geted1(i).getSequence_img() == null)) {
                    sequence_pass = false;
                    sequence_num.clear();
                    sequence_cont.clear();
                    sequence_img.clear();
                    Toast.makeText(createCont.this, "사진을 등록하세요9", Toast.LENGTH_LONG).show();
                } else {
                    if (imgpass == true) {
                        sequence_num.add(Integer.parseInt(sequence_recyclerAdapter.geted1(i).getSequence_num()));
                        sequence_cont.add(sequence_recyclerAdapter.geted1(i).getSequence_cont());
                        sequence_img.add(sequence_recyclerAdapter.geted1(i).getSequence_img());
                        sequence_img_uri.add(sequence_recyclerAdapter.geted1(i).getSequence_img_uri());

                        sequence_nums_strings[i] = String.valueOf(i + 1);
                        sequence_conts_strings[i] = sequence_recyclerAdapter.geted1(i).getSequence_cont();
                        sequence_imgs_bitmap[i] = sequence_recyclerAdapter.geted1(i).getSequence_img();
                        sequence_imgs_urls[i] = sequence_recyclerAdapter.geted1(i).getSequence_img_uri();
                        sequence_imgs_absolute_urls[i] = sequence_recyclerAdapter.geted1(i).getSequence_img_absolute_uri();
                        sequence_pass = true;
                    }else{
                        Toast.makeText(createCont.this, "사진을 등록하세요", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    public void saveRecipe(View view) {
        ingre_pass = false;
        ingre_num.clear();
        ingre_list.clear();
        ingre_we_list.clear();
        season_pass = false;
        season_num.clear();
        season_list.clear();
        season_we_list.clear();
        sequence_pass = false;
        sequence_num.clear();
        sequence_cont.clear();
        sequence_img.clear();

        cont = editText2.getText().toString();

        getIngre();

        getSeason();

        getSequence();

        saveContItem = new saveContItem();
        saveContItem.setTitle(title);
        saveContItem.setCont(cont);
        saveContItem.setCont_img(cont_img);
        saveContItem.setCont_img_uri(cont_img_Uri);
        saveContItem.setCont_img_absolute_uri(cont_img_absol_uri);
        saveContItem.setIngre_num(ingre_num_strings);
        saveContItem.setIngre(ingre_strings);
        saveContItem.setIngre_we(ingre_we_strings);
        saveContItem.setSeason_num(season_num_strings);
        saveContItem.setSeason(season_strings);
        saveContItem.setSeason_we(season_we_strings);
        saveContItem.setSequence_num(sequence_nums_strings);
        saveContItem.setSequence(sequence_conts_strings);
        saveContItem.setSequence_img(sequence_imgs_bitmap);
        saveContItem.setSequence_img_uri(sequence_imgs_urls);
        saveContItem.setSequence_img_absolute_uri(sequence_imgs_absolute_urls);

        if ((ingre_pass == true) && (season_pass == true) && (sequence_pass == true) && (title_pass == true) && (cont_pass == true) && (cont_img_pass == true)) {

            saveCont = new saveCont();
            saveCont.execute(saveContItem);
            Toast.makeText(createCont.this, "등록중입니다. 잠시만 기다려주세요.", Toast.LENGTH_LONG).show();

        } else {
        }


    }

    public class saveCont extends AsyncTask<saveContItem, Void, String> {
        @Override
        protected String doInBackground(saveContItem... saveContItems) {

            return getS(saveContItems[0]);
        }
        protected void onPostExecute(String result) {

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(createCont.this, home.class);
                    intent.putExtra("userid", user_id);

                    home home = (com.example.cookapp1.home) com.example.cookapp1.home._home;
                    home.finish();
                    startActivity(intent);

                    finish();
                }
            }, 1000);
        }

        private String getS(saveContItem saveContItem) {
            String boundary = "***^^^^***";
            exsvcont = new exsvcont();

            String title = saveContItem.getTitle();
            String cont = saveContItem.getCont();
            Uri cont_img_uri = saveContItem.getCont_img_uri();
            Bitmap cont_img = saveContItem.getCont_img();
            String cont_img_absolute_uri = saveContItem.getCont_img_absolute_uri();

            String[] ingre_num = saveContItem.getIngre_num();
            String[] ingre = saveContItem.getIngre();
            String[] ingre_we = saveContItem.getIngre_we();

            String[] season_num = saveContItem.getSeason_num();
            String[] season = saveContItem.getSeason();
            String[] season_we = saveContItem.getSeason_we();

            String[] sequence_num = saveContItem.getSequence_num();
            String[] sequence = saveContItem.getSequence();
            Uri[] sequence_img_uri = saveContItem.getSequence_img_uri();
            Bitmap[] sequence_img = saveContItem.getSequence_img();
            String[] sequence_img_absolute_uri = saveContItem.getSequence_img_absolute_uri();

            int ingre_count = 0;
            for (int i = 0; i < ingre.length; i++) {
                if (ingre[i] != null) {
                    ingre_count++;
                }
            }
            int season_count = 0;
            for (int i = 0; i < season.length; i++) {
                if (season[i] != null) {
                    season_count++;
                }
            }
            int sequence_count = 0;
            for (int i = 0; i < sequence.length; i++) {
                if (sequence[i] != null) {
                    sequence_count++;
                }
            }

            HttpURLConnection conn = null;
            try {
                URL url = new URL(create_url);
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(10000);
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                conn.setDoOutput(true);
                conn.connect();

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());

                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"ingre_count\" \r\n\r\n" + ingre_count);
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"season_count\" \r\n\r\n" + season_count);
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"sequence_count\" \r\n\r\n" + sequence_count);

                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"title\" \r\n\r\n" + URLEncoder.encode(title, "utf-8"));
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"cont\" \r\n\r\n" + URLEncoder.encode(cont, "utf-8"));
                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"user_id\" \r\n\r\n" + user_id);

                wr.writeBytes("\r\n--" + boundary + "\r\n");
                wr.writeBytes("Content-Disposition: form-data; name=\"cont_img\"; filename=\"cont_img.jpg\" \r\n ");
                wr.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
                ParcelFileDescriptor r = createCont.this.getContentResolver().openFileDescriptor(cont_img_uri, "r", null);
                FileInputStream fileInputStream = new FileInputStream(r.getFileDescriptor());

                int bytesAvailable = fileInputStream.available();
                int maxBufferSize = 5 * 1024 * 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                while (bytesRead > 0) {
                    DataOutputStream dataWrite = new DataOutputStream(conn.getOutputStream());
                    dataWrite.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                fileInputStream.close();

                for (int i = 0; i < ingre_count; i++) {
                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"ingre_num[]\" \r\n\r\n" + URLEncoder.encode(ingre_num[i], "utf-8"));
                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"ingre[]\" \r\n\r\n" + URLEncoder.encode(ingre[i], "utf-8"));
                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"ingre_we[]\" \r\n\r\n" + URLEncoder.encode(ingre_we[i], "utf-8"));
                }

                for (int i = 0; i < season_count; i++) {
                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"season_num[]\" \r\n\r\n" + URLEncoder.encode(season_num[i], "utf-8"));
                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"season[]\" \r\n\r\n" + URLEncoder.encode(season[i], "utf-8"));
                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"season_we[]\" \r\n\r\n" + URLEncoder.encode(season_we[i], "utf-8"));
                }

                for (int i = 0; i < sequence_count; i++) {
                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"sequence_num[]\" \r\n\r\n" + URLEncoder.encode(sequence_num[i], "utf-8"));
                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"sequence[]\" \r\n\r\n" + URLEncoder.encode(sequence[i], "utf-8"));

                    wr.writeBytes("\r\n--" + boundary + "\r\n");
                    wr.writeBytes("Content-Disposition: form-data; name=\"sequence_img[]\"; filename=\"sequence_img.jpg\" \r\n ");
                    wr.writeBytes("Content-Type: application/octet-stream\r\n\r\n");
                    ParcelFileDescriptor sq = createCont.this.getContentResolver().openFileDescriptor(sequence_img_uri[i], "r", null);
                    FileInputStream sqfileInputStream = new FileInputStream(sq.getFileDescriptor());
                    int sqbytesAvailable = sqfileInputStream.available();
                    int sqmaxBufferSize = 5 * 1024 * 1024;
                    int sqbufferSize = Math.min(sqbytesAvailable, sqmaxBufferSize);
                    byte[] sqbuffer = new byte[sqbufferSize];
                    int sqbytesRead = sqfileInputStream.read(sqbuffer, 0, sqbufferSize);
                    while (sqbytesRead > 0) {
                        DataOutputStream sqdataWrite = new DataOutputStream(conn.getOutputStream());
                        sqdataWrite.write(sqbuffer, 0, sqbufferSize);
                        sqbytesAvailable = sqfileInputStream.available();
                        sqbufferSize = Math.min(sqbytesAvailable, sqmaxBufferSize);
                        sqbytesRead = sqfileInputStream.read(sqbuffer, 0, sqbufferSize);
                    }
                    sqfileInputStream.close();
                }

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
