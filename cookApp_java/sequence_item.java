package com.example.cookapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

import java.net.URI;

public class sequence_item {
    private String sequence_num;
    private String sequence_cont;
    //    private BitmapDrawable sequence_img;
    private Bitmap sequence_img;
    private Uri sequence_img_uri;
    private String sequence_img_absolute_uri;

    public String getSequence_img_absolute_uri() {
        return sequence_img_absolute_uri;
    }

    public void setSequence_img_absolute_uri(String sequence_img_absolute_uri) {
        this.sequence_img_absolute_uri = sequence_img_absolute_uri;
    }

    public Uri getSequence_img_uri() {
        return sequence_img_uri;
    }

    public void setSequence_img_uri(Uri sequence_img_uri) {
        this.sequence_img_uri = sequence_img_uri;
    }

    public String getSequence_num() {
        return sequence_num;
    }

    public void setSequence_num(String sequence_num) {
        this.sequence_num = sequence_num;
    }

    public String getSequence_cont() {
        return sequence_cont;
    }

    public void setSequence_cont(String sequence_cont) {
        this.sequence_cont = sequence_cont;
    }

    public Bitmap getSequence_img() {
        return sequence_img;
    }

    public void setSequence_img(Bitmap sequence_img) {
        this.sequence_img = sequence_img;
    }

    //    public BitmapDrawable getSequence_img() {
//        return sequence_img;
//    }
//    public void setSequence_img(BitmapDrawable sequence_img) {
//        this.sequence_img = sequence_img;
//    }
}
