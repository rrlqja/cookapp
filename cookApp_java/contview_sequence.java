package com.example.cookapp1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class contview_sequence {
    private String sequence_num;
    private String sequence;
    private Bitmap sequence_img;

    public String getSequence_num() {
        return sequence_num;
    }

    public void setSequence_num(String sequence_num) {
        this.sequence_num = sequence_num;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public Bitmap getSequence_img() {
        return sequence_img;
    }

    public void setSequence_img(Bitmap sequence_img) {
        this.sequence_img = sequence_img;
    }

    public void setSequence_img(String string) {
        bitchange bitchange = new bitchange();
        bitchange.execute(string);
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
            sequence_img = result;
        }
    }
}
