package com.example.cookapp1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class createCont extends AppCompatActivity {
    TextView textView1;
    ImageView imageView1, imageView2;
    ListView listView1, listView2;

    //    ListView listView3;
    RecyclerView recyclerView1;

    ImageButton imageButton1;

    TextView textView2;

    ArrayList<ingre_item> list_item = new ArrayList<>();

    ArrayList<String> ingre_list = new ArrayList<>();
    ArrayList<String> ingre_we_list = new ArrayList<>();

    ArrayList<String> season_list = new ArrayList<>();
    ArrayList<String> season_we_list = new ArrayList<>();

    ArrayList<String> sequence_cont = new ArrayList<>();
    ArrayList<BitmapDrawable> sequence_img = new ArrayList<>();
    static int sequence_img_point = 0;

    ingrelistAdapter ingre_ingrelistAdapter;
    seasonlistAdapter season_listAdapter;
    sequencelistadapter sequence_listAdapter;

    sequencerecycleradapter sequence_recyclerAdapter;
    ArrayList<sequence_item> sequence_items = new ArrayList<>();
    sequence_item sequence_item;

    TextView txttv;
    ImageView txtiv;
//    ImageView txtiv2;

    static int popo = 0;


    ImageView imageViewA, imageViewB, imageViewC, imageViewD;
    Button buttonA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcont);


        imageViewA = findViewById(R.id.aa1);
        imageViewB = findViewById(R.id.aa2);
        imageViewC = findViewById(R.id.aa3);
        imageViewD = findViewById(R.id.aa4);
        buttonA = findViewById(R.id.bb1);
        buttonA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable[] IImg = new BitmapDrawable[4];
                for (int i = 0; i < 4; i++) {
                    IImg[i] = sequence_items.get(i).getSequence_img();
                }
                imageViewA.setBackground(IImg[0]);
                imageViewB.setBackground(IImg[1]);
                imageViewC.setBackground(IImg[2]);
                imageViewD.setBackground(IImg[3]);
            }
        });



        textView1 = findViewById(R.id.tv1);
        textView2 = findViewById(R.id.exedtx);
        imageView1 = findViewById(R.id.ibimg);
//        imageButton1 = findViewById(R.id.ibimg);
        imageView2 = findViewById(R.id.testimg);

        txttv = findViewById(R.id.txtimg_tx);
        txtiv = findViewById(R.id.txtimg_iv);
//        txtiv2 = findViewById(R.id.txtimg_iv2);

        ingre_ingrelistAdapter = new ingrelistAdapter();
        season_listAdapter = new seasonlistAdapter();
//        sequence_listAdapter = new sequencelistadapter();

        listView1 = findViewById(R.id.ingre);
        listView2 = findViewById(R.id.season);
//        listView3 = findViewById(R.id.sequence);

        listView1.setAdapter(ingre_ingrelistAdapter);
        listView2.setAdapter(season_listAdapter);
//        listView3.setAdapter(sequence_listAdapter);

        ingre_ingrelistAdapter.addItem();
        ingre_ingrelistAdapter.notifyDataSetChanged();
        ingre_listheight();

        season_listAdapter.addItem();
        season_listAdapter.notifyDataSetChanged();
        season_listheight();

//        sequence_listAdapter.addItem();
//        sequence_listAdapter.notifyDataSetChanged();
//        sequence_listheight();

        Matrix matrix1 = new Matrix();
        matrix1.postScale(1.0f, 1.0f);
        imageView1.setImageMatrix(matrix1);
//        imageButton1.setImageMatrix(matrix1);

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
//        initadp();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initadp();
            }
        }, 1000);

    }

    public ArrayList<sequence_item> tt = new ArrayList<>();
    public void initadp() {
        sequence_recyclerAdapter = new sequencerecycleradapter(sequence_items, sequence_img);
        recyclerView1.setAdapter(sequence_recyclerAdapter);
        sequence_recyclerAdapter.setOnItemClickListener(new sequencerecycleradapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Toast.makeText(createCont.this, "아이템 터치", Toast.LENGTH_LONG).show();
                txttv.setText(String.valueOf(position));

//                sequence_recyclerAdapter.mItemList.get(position).setSequence_num("ㅇ");
//                sequence_recyclerAdapter.notifyItemChanged(sequence_items.size());

                Intent intent = new Intent();
//                intent.putExtra("position", String.valueOf(position));
                popo = position;
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 2);

            }
        });
    }

    public void show_testlist(View view) {
        ingre_list.clear();

        int count = ingre_ingrelistAdapter.getCount();

        for (int i = 0; i < count; i++) {
            if (ingre_ingrelistAdapter.geted1(i) == null) {
                textView2.setText("값이 없다");
            } else {
                if ((ingre_ingrelistAdapter.geted1(i).getName() == null) && (ingre_ingrelistAdapter.geted1(i).getWeight() == null)) {
                    ingre_list.clear();
                    Toast.makeText(createCont.this, "재료를 입력하세요1", Toast.LENGTH_LONG).show();
                } else if ((ingre_ingrelistAdapter.geted1(i).getName() == null) && (ingre_ingrelistAdapter.geted1(i).getWeight()) != null) {
                    ingre_list.clear();
                    Toast.makeText(createCont.this, "재료를 입력하세요2", Toast.LENGTH_LONG).show();
                } else if ((ingre_ingrelistAdapter.geted1(i).getName() != null) && (ingre_ingrelistAdapter.geted1(i).getWeight() == null)) {
                    ingre_list.clear();
                    Toast.makeText(createCont.this, "용량을 입력하세요3", Toast.LENGTH_LONG).show();
                } else {
                    ingre_list.add(ingre_ingrelistAdapter.geted1(i).getName());
                    ingre_we_list.add(ingre_ingrelistAdapter.geted1(i).getWeight());
                }
            }
        }

        for (int i = 0; i < ingre_list.size(); i++) {
            textView2.append(ingre_list.get(i));
        }


    }

    private void ingre_listheight() {

        int totalheight = 0;
        for (int i = 0; i < ingre_ingrelistAdapter.getCount(); i++) {
            View listitem = ingre_ingrelistAdapter.getView(i, null, listView1);
            listitem.measure(0, 0);
            totalheight += listitem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params1 = listView1.getLayoutParams();
        params1.height = totalheight + (listView1.getDividerHeight() * (ingre_ingrelistAdapter.getCount() - 1));
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
//        imageButton1.setImageMatrix(matrix1);
//        int positon = data.getIntExtra("position", 0);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
            } catch (Exception e) {
            }

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            imageView1.setBackground(bitmapDrawable);
//            imageButton1.setBackground(bitmapDrawable);

            imageView2.setImageBitmap(bitmap);
        }

        if (requestCode == 2 && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            int pos = data.getIntExtra("position", 0);
//            String pos = data.getStringExtra("position");

            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
            } catch (Exception e) {
            }

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);

//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    Toast.makeText(createCont.this, pos, Toast.LENGTH_LONG).show();
//                }
//            }, 2000);

//            sequence_listAdapter.sequence_img.add(bitmapDrawable);
//            sequence_img.add(bitmapDrawable);
//            sequence_recyclerAdapter.setBit(pos, bitmapDrawable);

//            sequence_item = new sequence_item();
//            sequence_item.setSequence_img(bitmapDrawable);
//            sequence_items.add(sequence_item);

            sequence_items.get(popo).setSequence_img(bitmapDrawable);
            sequence_recyclerAdapter.notifyItemChanged(sequence_items.size());
            initadp();

//            txtiv.setBackground(bitmapDrawable);

//            sequence_recyclerAdapter.setBit(pos, bitmapDrawable);
//            sequence_recyclerAdapter.notifyItemChanged(sequence_items.size());

//            sequence_listAdapter.listViewItemList.get(positon).setSequence_img(bitmapDrawable);
//            sequence_listAdapter.listViewItemList.get(positon).getSequence_img();
//            sequence_listAdapter.notifyDataSetChanged();

//            sequence_listAdapter.imageView1.setBackground(bitmapDrawable);

        }
    }

    public void addingreList(View view) {

        ingre_ingrelistAdapter.addItem();

        ingre_ingrelistAdapter.notifyDataSetChanged();

        ingre_listheight();

    }

    public void addseasonList(View view) {

        season_listAdapter.addItem();

        season_listAdapter.notifyDataSetChanged();

        season_listheight();
    }

    public void addsequenceList(View view) {

        if (sequence_items.size() >= 20) {
//            Toast.makeText(createCont.this, "그만", Toast.LENGTH_LONG).show();
        } else {
            sequence_item = new sequence_item();

            sequence_items.add(sequence_item);

            sequence_recyclerAdapter.notifyItemChanged(sequence_items.size());
        }

    }

    public void getimg(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 1);
    }

    public void kkzz(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent, 2);
    }

    public void testtxtimg(View view) {
        String[] strings = sequence_recyclerAdapter.getEdit_Arr();
        String result = null;
        if (strings == null) {
        } else {
            for (int i = 0; i < strings.length; i++) {
                if (strings[i] != null) {
                    if (i == 0) {
                        result = strings[i];
                    } else {
                        result += (strings[i]);
                    }
                }
            }
        }
        if (result != null) {
            txttv.setText(result);
        }
    }
}
