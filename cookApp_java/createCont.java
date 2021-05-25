package com.example.cookapp1;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class createCont extends AppCompatActivity {

    saveContItem saveContItem;

    EditText editText1, editText2;
    ImageView imageView1;

    String title = "";
    Bitmap cont_img;
    Uri cont_img_Uri;
    String cont_img_absol_uri;
    String cont = "";

    static boolean title_pass = false;
    static boolean cont_img_pass = false;
    static boolean cont_pass = false;

    static boolean imgpass = false;

    TextView textView1;
//    ImageView imageView2;
    ListView listView1, listView2;

    RecyclerView recyclerView1;

//    TextView textView2;

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

//    TextView txttv;
//    ImageView txtiv;

    static int Pos = 0;

    String create_url = "http://15.165.241.115/db/createcont.php";

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


    TextView svtx1, svtx2, svtx3, svtx4, svtx5, svtx6, svtx7, svtx8;
    ImageView sviv1;

    saveCont saveCont;
    exsvcont exsvcont;


    static String[] result_ingre = new String[10];
    TextView resultingre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcont);

//        saveContItem = new saveContItem();

        editText1 = findViewById(R.id.title);
        imageView1 = findViewById(R.id.ibimg);
        editText2 = findViewById(R.id.cont);

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

        svtx1 = findViewById(R.id.sv_ingre_num);
        svtx2 = findViewById(R.id.sv_ingre);
        svtx3 = findViewById(R.id.sv_ingre_we);
        svtx4 = findViewById(R.id.sv_season_num);
        svtx5 = findViewById(R.id.sv_season);
        svtx6 = findViewById(R.id.sv_season_we);
        svtx7 = findViewById(R.id.sv_sequence_num);
        svtx8 = findViewById(R.id.sv_sequence);
        sviv1 = findViewById(R.id.sv_sequence_img);

        resultingre = findViewById(R.id.resultingre);

        textView1 = findViewById(R.id.tv1);
//        textView2 = findViewById(R.id.exedtx);
        imageView1 = findViewById(R.id.ibimg);
//        imageView2 = findViewById(R.id.testimg);

//        txttv = findViewById(R.id.txtimg_tx);
//        txtiv = findViewById(R.id.txtimg_iv);

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
        String userid = getintent.getStringExtra("userid");

        textView1.setText(userid);

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
                Toast.makeText(createCont.this, "아이템 터치", Toast.LENGTH_LONG).show();
//                txttv.setText(String.valueOf(position));

                Intent intent = new Intent();
                Pos = position;
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 2);

            }
        });
    }

    public void show_ingre_testlist(View view) {
        ingre_list.clear();
        ingre_we_list.clear();

        int count = ingre_listAdapter.getCount();

        for (int i = 0; i < count; i++) {
            if (ingre_listAdapter.geted1(i) == null) {
            } else {
                if ((ingre_listAdapter.geted1(i).getName() == null) && (ingre_listAdapter.geted1(i).getWeight() == null)) {
                    ingre_list.clear();
                    ingre_we_list.clear();
                    Toast.makeText(createCont.this, "재료를 입력하세요1", Toast.LENGTH_LONG).show();
                } else if ((ingre_listAdapter.geted1(i).getName() == null) && (ingre_listAdapter.geted1(i).getWeight()) != null) {
                    ingre_list.clear();
                    ingre_we_list.clear();
                    Toast.makeText(createCont.this, "재료를 입력하세요2", Toast.LENGTH_LONG).show();
                } else if ((ingre_listAdapter.geted1(i).getName() != null) && (ingre_listAdapter.geted1(i).getWeight() == null)) {
                    ingre_list.clear();
                    ingre_we_list.clear();
                    Toast.makeText(createCont.this, "용량을 입력하세요3", Toast.LENGTH_LONG).show();
                } else {
                    ingre_list.add(ingre_listAdapter.geted1(i).getName());
                    ingre_we_list.add(ingre_listAdapter.geted1(i).getWeight());
                }
            }
        }

//        for (int i = 0; i < ingre_list.size(); i++) {
//            textView2.append(ingre_list.get(i));
//        }


    }

    public void show_season_testlist(View view) {
        season_list.clear();
        season_we_list.clear();

        int count = season_listAdapter.getCount();

        for (int i = 0; i < count; i++) {
            if (season_listAdapter.geted1(i) == null) {
            } else {
                if ((season_listAdapter.geted1(i).getName() == null) && (season_listAdapter.geted1(i).getWeight() == null)) {
                    season_list.clear();
                    season_we_list.clear();
                    Toast.makeText(createCont.this, "재료를 입력하세요1", Toast.LENGTH_LONG).show();
                } else if ((season_listAdapter.geted1(i).getName() == null) && (season_listAdapter.geted1(i).getWeight()) != null) {
                    season_list.clear();
                    season_we_list.clear();
                    Toast.makeText(createCont.this, "재료를 입력하세요2", Toast.LENGTH_LONG).show();
                } else if ((season_listAdapter.geted1(i).getName() != null) && (season_listAdapter.geted1(i).getWeight() == null)) {
                    season_list.clear();
                    season_we_list.clear();
                    Toast.makeText(createCont.this, "용량을 입력하세요3", Toast.LENGTH_LONG).show();
                } else {
                    season_list.add(season_listAdapter.geted1(i).getName());
                    season_we_list.add(season_listAdapter.geted1(i).getWeight());


                }
            }
        }

//        for (int i = 0; i < season_list.size(); i++) {
//            textView2.append(season_list.get(i));
//        }


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
//                Toast.makeText(createCont.this, uri.toString(), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
            }

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            imageView1.setBackground(bitmapDrawable);

            cont_img_absol_uri = getpath(uri);
//            cont_img_absol_uri = createCopyAndReturnRealPath(createCont.this, uri);
            Toast.makeText(createCont.this, String.valueOf(cont_img_absol_uri), Toast.LENGTH_LONG).show();

            cont_img = bitmap;
            cont_img_pass = true;

//            imageView2.setImageBitmap(bitmap);
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imgpass = true;

            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
                sequence_items.get(Pos).setSequence_img_uri(uri);
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
        }
    }

    public void addseasonList(View view) {
        season_pass = false;

        if (season_listAdapter.getCount() < 20) {
            season_listAdapter.addItem();

            season_listAdapter.notifyDataSetChanged();

            season_listheight();
        }
    }

    public void addsequenceList(View view) {
        imgpass = false;
        season_pass = false;

        if (sequence_items.size() >= 20) {
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

//    public void kkzz(View view) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//
//        startActivityForResult(intent, 2);
//    }
//
//    public void testtxtimg(View view) {
//        String[] strings = sequence_recyclerAdapter.getEdit_Arr();
//        String result = null;
//        if (strings == null) {
//        } else {
//            for (int i = 0; i < strings.length; i++) {
//                if (strings[i] != null) {
//                    if (i == 0) {
//                        result = strings[i];
//                    } else {
//                        result += (strings[i]);
//                    }
//                }
//            }
//        }
//        if (result != null) {
////            txttv.setText(result);
//        }
//    }

    public void getIngre() {
        ingre_pass = false;
        ingre_num.clear();
        ingre_list.clear();
        ingre_we_list.clear();
        int ingre_count = ingre_listAdapter.getCount();

        for (int i = 0; i < ingre_count; i++) {
            if (ingre_listAdapter.geted1(i) == null) {
//                textView2.setText("값이 없다");
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
//                textView2.setText("값이 없다");
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

//        title = editText1.getText().toString();
        cont = editText2.getText().toString();

        getIngre();

        getSeason();

        getSequence();

        saveContItem = new saveContItem();
        saveContItem.setTitle(title);
        saveContItem.setCont(cont);
        saveContItem.setCont_img(cont_img);
        saveContItem.setCont_img_uri(cont_img_Uri);
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

        if ((ingre_pass == true) && (season_pass == true) && (sequence_pass == true) && (title_pass == true) && (cont_pass == true) && (cont_img_pass == true)) {
            svtx1.setText(ingre_num_strings[ingre_num.size() - 1]);
            svtx2.setText(ingre_strings[ingre_list.size() - 1]);
            svtx3.setText(ingre_we_strings[ingre_we_list.size() - 1]);

            svtx4.setText(season_num_strings[season_num.size() - 1]);
            svtx5.setText(season_strings[season_list.size() - 1]);
            svtx6.setText(season_we_strings[season_we_list.size() - 1]);

            svtx7.setText(sequence_nums_strings[sequence_num.size() - 1]);
            svtx8.setText(sequence_conts_strings[sequence_cont.size() - 1]);
//            sviv1.setImageBitmap(sequence_imgs_bitmap[sequence_img.size() - 1]);
            Bitmap[] bb = saveContItem.getSequence_img();
            sviv1.setImageBitmap(bb[0]);
        } else {
        }

        saveCont = new saveCont();
        saveCont.execute(saveContItem);

//        Toast.makeText(createCont.this, saveContItem.getTitle(), Toast.LENGTH_LONG).show();
//
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                saveCont = new saveCont();
//                saveCont.execute(ingre_num_strings, ingre_strings, ingre_we_strings, season_num_strings, season_strings, season_we_strings);
//            }
//        }, 1500);


    }

    public class saveCont extends AsyncTask<saveContItem, Void, exsvcont> {
        @Override
        protected exsvcont doInBackground(saveContItem... saveContItems) {
            return getS(saveContItems[0]);
        }
        protected void onPostExecute(String result) {
            Toast.makeText(createCont.this, result, Toast.LENGTH_LONG).show();
        }

        private exsvcont getS(saveContItem saveContItem) {
            exsvcont = new exsvcont();

            String title = saveContItem.getTitle();
            String cont = saveContItem.getCont();
            Bitmap cont_img = saveContItem.getCont_img();
            Uri cont_img_uri = saveContItem.getCont_img_uri();

            String[] ingre_num = saveContItem.getIngre_num();
            String[] ingre = saveContItem.getIngre();
            String[] ingre_we = saveContItem.getIngre_we();

            String[] season_num = saveContItem.getSeason_num();
            String[] season = saveContItem.getSeason();
            String[] season_we = saveContItem.getSeason_we();

            String[] sequence_num = saveContItem.getSequence_num();
            String[] sequence = saveContItem.getSequence();
            Bitmap[] sequence_img = saveContItem.getSequence_img();
            Uri[] sequence_img_uri = saveContItem.getSequence_img_uri();

            String param = "";

//            param += "title=" + title + " &cont=" + cont;
            param += "title=" + title + " &cont=" + cont + " &cont_img=" + cont_img;

            HttpURLConnection conn = null;
            try {
                URL url = new URL(create_url);
                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data; doundary="+"****");
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

//                return sb.toString();
                return exsvcont;

            } catch (Exception e) {
                return null;
            }

        }
    }



//    public class saveCont extends AsyncTask<String[], Void, String> {
//
//        @Override
//        protected String doInBackground(String[]... strings) {
//            String[] ingre_num = strings[0];
//            String[] ingre = strings[1];
//            String[] ingre_we = strings[2];
//
//            String[] season_num = strings[3];
//            String[] season = strings[4];
//            String[] season_we = strings[5];
//
//            return getS(ingre_num, ingre, ingre_we, season_num, season, season_we);
//        }
//
//        protected void onPostExecute(String result) {
//            getJ(result);
//        }
//
//        private String getS(String[]... strings){
//            String[] ingre_num = strings[0];
//            String[] ingre = strings[1];
//            String[] ingre_we = strings[2];
//
//            String[] season_num = strings[3];
//            String[] season = strings[4];
//            String[] season_we = strings[5];
//
//            HttpURLConnection conn = null;
//
//            String param = "";
//
//            for (int i = 0; i < ingre_num.length; i++) {
//                if (ingre_num[i] != null) {
//                    param += "&ingre_num[]=" + ingre_num[i];
//                }
//            }
//            for (int i = 0; i < ingre.length; i++) {
//                if (ingre[i] != null) {
//                    param += "&ingre[]=" + ingre[i];
//                }
//            }
//
//            try {
//                URL url = new URL(create_url);
//                conn = (HttpURLConnection) url.openConnection();
//
//                conn.setReadTimeout(5000);
//                conn.setConnectTimeout(5000);
//                conn.setRequestMethod("POST");
//                conn.connect();
//
//                OutputStream outputStream = conn.getOutputStream();
//                outputStream.write(param.getBytes("UTF-8"));
//                outputStream.flush();
//                outputStream.close();
//
//                int response = conn.getResponseCode();
//
//                InputStream iStream;
//                if (response == HttpURLConnection.HTTP_OK) {
//                    iStream = conn.getInputStream();
//                } else {
//                    iStream = conn.getErrorStream();
//                }
//
//                InputStreamReader inputStreamReader = new InputStreamReader(iStream, "UTF-8");
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                StringBuilder sb = new StringBuilder();
//                String line = "";
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    sb.append(line);
//                }
//
//                bufferedReader.close();
//
//                return sb.toString();
//
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        private void getJ(String page) {
//
//            try {
//                JSONObject json = new JSONObject(page);
//
//                JSONArray jArr = json.getJSONArray("result");
//
//                JSONObject result_json;
//
//                for (int i = 0; i < jArr.length(); i++) {
//
//                    result_json = jArr.getJSONObject(i);
//
//                    result_ingre[i] = result_json.getString("ingre");
//
//                    resultingre.append(result_json.getString("ingre_num"));
//                    resultingre.append(result_json.getString("ingre"));
//
//                }
//
//            } catch (Exception e) {
//
//            }
//        }
//    }

}
