package com.example.cookapp1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.util.ArrayList;

public class sequencelistadapter extends BaseAdapter {
    public ArrayList<sequence_item> listViewItemList = new ArrayList<>();
    ArrayList<BitmapDrawable> sequence_img = new ArrayList<>();

    public TextView textView1;
    public EditText editText1;
    public ImageView imageView1;

    public String[] ed1_hint = {" 예) 야채를 손질합니다. ", " 예) 고기를 볶습니다.", " 예) 밑간을 해줍니다."};

    int pos;

    public sequencelistadapter() {}

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        pos = position;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_recipe_sequence, parent, false);
        }
        listViewItemList.get(position).setSequence_num(String.valueOf(position));

        textView1 = convertView.findViewById(R.id.recipe_sequence_num);
        editText1 = convertView.findViewById(R.id.recipe_sequence_cont);
        imageView1 = convertView.findViewById(R.id.recipe_sequence_img);

        textView1.setText(listViewItemList.get(position).getSequence_num());
        editText1.setHint(ed1_hint[position % 3]);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listViewItemList.get(position).setSequence_cont(s.toString());
            }
        });

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("position", pos);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                ((createCont) context).startActivityForResult(intent, 2);
            }
        });

//        imageView1.setBackground(listViewItemList.get(position).getSequence_img());
        imageView1.setImageBitmap(listViewItemList.get(position).getSequence_img());

        return convertView;
    }

    public void getdraw(BitmapDrawable bitmap, int pos) {
        sequence_img.add(bitmap);

        imageView1.setImageDrawable(bitmap);

    }

    public sequence_item geted1(int i) {
        sequence_item sequence_item = listViewItemList.get(i);

        return sequence_item;
    }

    public void addItem() {
        sequence_item sequence_item = new sequence_item();

        listViewItemList.add(sequence_item);
    }

}
