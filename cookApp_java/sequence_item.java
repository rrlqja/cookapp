package com.example.cookapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;

public class sequence_item {
    private String sequence_num;
    private String sequence_cont;
    private BitmapDrawable sequence_img;

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

    public BitmapDrawable getSequence_img() {
        return sequence_img;
    }

    public void setSequence_img(BitmapDrawable sequence_img) {
        this.sequence_img = sequence_img;
    }
}
