package com.example.cookapp1;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class contview_sequence_recyclerview_Adapter extends RecyclerView.Adapter<contview_sequence_recyclerview_Adapter.itemViewholder> {

    ArrayList<contview_sequence> contview_sequences;

    public contview_sequence_recyclerview_Adapter(ArrayList<contview_sequence> contview_sequences) {
        this.contview_sequences = contview_sequences;
    }

    @NonNull
    @Override
    public itemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sequence_recyclerview_item, parent, false);
        return new itemViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewholder holder, int position) {
        populateItemRows(holder, position);
    }

    @Override
    public int getItemCount() {
        return contview_sequences == null ? 0 : contview_sequences.size();
    }

    public class itemViewholder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;
        ImageView imageView1;

        public itemViewholder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.contview_sequence_num);
            textView2 = itemView.findViewById(R.id.contview_sequence_cont);
            imageView1 = itemView.findViewById(R.id.contview_sequence_img);
        }
    }

    private void populateItemRows(itemViewholder viewHolder, int position) {

        String sequence_num = contview_sequences.get(position).getSequence_num();
        String sequence = contview_sequences.get(position).getSequence();
        Bitmap sequence_img = contview_sequences.get(position).getSequence_img();

        viewHolder.textView1.setText(sequence_num);
        viewHolder.textView2.setText(sequence);
        viewHolder.imageView1.setImageBitmap(sequence_img);
    }
}
