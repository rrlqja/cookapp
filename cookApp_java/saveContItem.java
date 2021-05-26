package com.example.cookapp1;

import android.graphics.Bitmap;
import android.net.Uri;

public class saveContItem {
    private String title;
    private String cont;
    private Bitmap cont_img;
    private Uri cont_img_uri;
    private String cont_img_absolute_uri;

    private String[] ingre_num;
    private String[] ingre;
    private String[] ingre_we;

    private String[] season_num;
    private String[] season;
    private String[] season_we;

    private String[] sequence_num;
    private String[] sequence;
    private Bitmap[] sequence_img;
    private Uri[] sequence_img_uri;
    private String[] sequence_img_absolute_uri;

    public String getCont_img_absolute_uri() {
        return cont_img_absolute_uri;
    }

    public void setCont_img_absolute_uri(String cont_img_absolute_uri) {
        this.cont_img_absolute_uri = cont_img_absolute_uri;
    }

    public String[] getSequence_img_absolute_uri() {
        return sequence_img_absolute_uri;
    }

    public void setSequence_img_absolute_uri(String[] sequence_img_absolute_uri) {
        this.sequence_img_absolute_uri = sequence_img_absolute_uri;
    }

    public Uri getCont_img_uri() {
        return cont_img_uri;
    }

    public void setCont_img_uri(Uri cont_img_uri) {
        this.cont_img_uri = cont_img_uri;
    }

    public Uri[] getSequence_img_uri() {
        return sequence_img_uri;
    }

    public void setSequence_img_uri(Uri[] sequence_img_uri) {
        this.sequence_img_uri = sequence_img_uri;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public Bitmap getCont_img() {
        return cont_img;
    }

    public void setCont_img(Bitmap cont_img) {
        this.cont_img = cont_img;
    }

    public String[] getIngre_num() {
        return ingre_num;
    }

    public void setIngre_num(String[] ingre_num) {
        this.ingre_num = ingre_num;
    }

    public String[] getIngre() {
        return ingre;
    }

    public void setIngre(String[] ingre) {
        this.ingre = ingre;
    }

    public String[] getIngre_we() {
        return ingre_we;
    }

    public void setIngre_we(String[] ingre_we) {
        this.ingre_we = ingre_we;
    }

    public String[] getSeason_num() {
        return season_num;
    }

    public void setSeason_num(String[] season_num) {
        this.season_num = season_num;
    }

    public String[] getSeason() {
        return season;
    }

    public void setSeason(String[] season) {
        this.season = season;
    }

    public String[] getSeason_we() {
        return season_we;
    }

    public void setSeason_we(String[] season_we) {
        this.season_we = season_we;
    }

    public String[] getSequence_num() {
        return sequence_num;
    }

    public void setSequence_num(String[] sequence_num) {
        this.sequence_num = sequence_num;
    }

    public String[] getSequence() {
        return sequence;
    }

    public void setSequence(String[] sequence) {
        this.sequence = sequence;
    }

    public Bitmap[] getSequence_img() {
        return sequence_img;
    }

    public void setSequence_img(Bitmap[] sequence_img) {
        this.sequence_img = sequence_img;
    }
}
