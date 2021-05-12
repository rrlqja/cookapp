package com.example.cookapp1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private ArrayList<content> ContentList = new ArrayList<content>();

    public ListViewAdapter() {

    }

    @Override
    public int getCount() {
        return ContentList.size();
    }

    @Override
    public Object getItem(int position) {
        return ContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView title = convertView.findViewById(R.id.title);
        TextView cont = convertView.findViewById(R.id.cont);
        TextView writer = convertView.findViewById(R.id.writer);

        content content = ContentList.get(position);

        title.setText(content.getTitle());
        cont.setText(content.getCont());
        writer.setText(content.getWriter());

        return convertView;
    }

    public void addItem(String title, String cont, String writer) {
        content content = new content();

        content.setTitle(title);
        content.setCont(cont);
        content.setWriter(writer);

        ContentList.add(content);
    }
}
