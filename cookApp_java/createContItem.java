package com.example.cookapp1;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public class createContItem {
    private String ingre;
    private String ingre_we;
    private String season;
    private String season_we;
    private String sequence_num;
    private String sequence_cont;
    private BitmapDrawable sequence_img;

    public String getIngre() {
        return ingre;
    }

    public void setIngre(String ingre) {
        this.ingre = ingre;
    }

    public String getIngre_we() {
        return ingre_we;
    }

    public void setIngre_we(String ingre_we) {
        this.ingre_we = ingre_we;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getSeason_we() {
        return season_we;
    }

    public void setSeason_we(String season_we) {
        this.season_we = season_we;
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

    public BitmapDrawable getSequence_img() {
        return sequence_img;
    }

    public void setSequence_img(BitmapDrawable sequence_img) {
        this.sequence_img = sequence_img;
    }
}
