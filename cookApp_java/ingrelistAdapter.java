package com.example.cookapp1;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import java.util.ArrayList;

public class ingrelistAdapter extends BaseAdapter {
    public ArrayList<ingre_item> listViewItemList = new ArrayList<>();

    public EditText editText1, editText2;

    public String[] ed1_hint = {" 예) 돼지고기", " 예) 파", " 예) 마늘"};
    public String[] ed2_hint = {" 예) 500g", " 예) 1개", " 예) 400ml"};

    public ingrelistAdapter() {}

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

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ingre_list, parent, false);
        }

        editText1 = convertView.findViewById(R.id.ingre);
        editText2 = convertView.findViewById(R.id.ingre_we);

        editText1.setHint(ed1_hint[position % 3]);
        editText2.setHint(ed2_hint[position % 3]);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listViewItemList.get(position).setName(s.toString());
            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listViewItemList.get(position).setWeight(s.toString());
            }
        });

        return convertView;
    }

    public ingre_item geted1(int i) {
        ingre_item ingre_item = listViewItemList.get(i);

        return ingre_item;
    }

    public void addItem() {
        ingre_item ingre_item = new ingre_item();

        listViewItemList.add(ingre_item);
    }

}
