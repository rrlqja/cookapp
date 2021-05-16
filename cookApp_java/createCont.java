package com.example.cookapp1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

public class createCont extends AppCompatActivity {
    TextView textView1;
    ImageView imageView1, imageView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createcont);

        textView1 = findViewById(R.id.tv1);
        imageView1 = findViewById(R.id.ibimg);
        imageView2 = findViewById(R.id.testimg);

        Matrix matrix1 = new Matrix();
        matrix1.postScale(1.0f, 1.0f);
        imageView1.setImageMatrix(matrix1);

        Intent getintent = getIntent();
        String userid = getintent.getStringExtra("userid");

        textView1.setText(userid);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        imageView1.setBackground(null);
        Matrix matrix1 = new Matrix();
        matrix1.postScale(1.0f, 1.0f);
        imageView1.setImageMatrix(matrix1);


        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(getContentResolver(), uri));
            } catch (Exception e) {}

            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
            imageView1.setBackground(bitmapDrawable);

            imageView2.setImageBitmap(bitmap);
        }

    }

    public void getimg(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }
}
