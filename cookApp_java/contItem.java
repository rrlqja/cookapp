package com.example.cookapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class contItem {
    private String con_num;
    private String title;
    private String writer;
    private String cont;
    private String date;
    private Bitmap img;
    bitchange bitchange;

    public String getCon_num() {
        return con_num;
    }

    public void setCon_num(String con_num) {
        this.con_num = con_num;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public void setImg(String img) {
        bitchange = new bitchange();
        bitchange.execute(img);
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
            img = result;
        }
    }
}
