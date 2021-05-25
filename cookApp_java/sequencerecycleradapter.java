package com.example.cookapp1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class sequencerecycleradapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<sequence_item> edit_Arr=new ArrayList<>();
    sequence_item sequence_item;

    private final int VIEW_TYPE_ITEM=0;
    private final int VIEW_TYPE_LOADING = 1;

    static String[] edHint = {" 예) 고기를 먹기 좋은 크기로 자릅니다.", " 예) 채소를 깨끗히 세척합니다.", " 예) 고기와 채소를 볶아줍니다."};

    String[] strings = new String[20];

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public ArrayList<sequence_item> mItemList;
    public sequencerecycleradapter(ArrayList<sequence_item> mItemList) {
        this.mItemList = mItemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_recipe_sequence, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        populateItemRows((itemViewHolder) holder, position);

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class itemViewHolder extends RecyclerView.ViewHolder {

        EditText editText1;
        ImageView imageView1;
        TextView textView1;

        public itemViewHolder(@NonNull View itemView) {
            super(itemView);

            this.editText1 = itemView.findViewById(R.id.recipe_sequence_cont);
            imageView1 = itemView.findViewById(R.id.recipe_sequence_img);
            textView1 = itemView.findViewById(R.id.recipe_sequence_num);

            Matrix matrix1 = new Matrix();
            matrix1.postScale(1.0f, 1.0f);
            imageView1.setImageMatrix(matrix1);

            imageView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                            imageView1.setBackground(null);

                        }
                    }
                }
            });
        }
    }

    private void populateItemRows(itemViewHolder viewHolder, int position) {

        mItemList.get(position).setSequence_num(String.valueOf(position+1));
        mItemList.get(position).setSequence_img(mItemList.get(position).getSequence_img());

        String num = mItemList.get(position).getSequence_num();
        String cont = mItemList.get(position).getSequence_cont();
        Bitmap img = mItemList.get(position).getSequence_img();

        viewHolder.editText1.setHint(edHint[position % 3]);

        viewHolder.editText1.setText(cont);
        sequence_item = new sequence_item();
        viewHolder.editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mItemList.get(position).setSequence_cont(s.toString());
                strings[position] = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {
                mItemList.get(position).setSequence_cont(s.toString());
                strings[position] = s.toString();
            }
        });

        viewHolder.textView1.setText(num);

        Matrix matrix1 = new Matrix();
        matrix1.postScale(1.0f, 1.0f);
        viewHolder.imageView1.setImageMatrix(matrix1);

        viewHolder.imageView1.setImageBitmap(img);

    }

    public String[] getEdit_Arr() {
        for (int i = 0; i < edit_Arr.size(); i++) {
            if (strings[i] != null) {
                strings[i] = edit_Arr.get(i).getSequence_cont();
            }
        }
        return strings;
    }

    public sequence_item geted1(int i) {
        sequence_item sequence_item = mItemList.get(i);

        return sequence_item;
    }

}
