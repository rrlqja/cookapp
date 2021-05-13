package com.example.cookapp1;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM=0;
    private final int VIEW_TYPE_LOADING = 1;

    public interface OnItemClickListener{
        void onItemClick(View v, int position);
    }
    private OnItemClickListener mListener = null;
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    //    public List<String> mItemList;
    public List<RecyclerItem> mItemList;

    //    public RecyclerViewAdapter(List<String> itemList) {
//        mItemList = itemList;
//    }
    public RecyclerViewAdapter(List<RecyclerItem> itemList) {
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvWriter;
        TextView tvItem;
        TextView tvLike;
        TextView tvDate;
        TextView tvNum;
        ImageView tvImg;
//        TextView tvImg;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvWriter = itemView.findViewById(R.id.tvWriter);
            tvItem = itemView.findViewById(R.id.tvItem);
            tvLike = itemView.findViewById(R.id.tvLike);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvNum = itemView.findViewById(R.id.tvNum);
            tvImg = itemView.findViewById(R.id.tvImg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        if (mListener != null) {
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

        }
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //
    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        String title = mItemList.get(position).getTitle();
        String writer = mItemList.get(position).getWriter();
        String item = mItemList.get(position).getCont();
        String like = mItemList.get(position).getLike();
        String date = mItemList.get(position).getDate();
        String num = mItemList.get(position).getNum();
        Bitmap img = mItemList.get(position).getImg();
//        String img = mItemList.get(position).getImg();

        viewHolder.tvItem.setText(item);
        viewHolder.tvTitle.setText(title);
        viewHolder.tvWriter.setText(writer);
        viewHolder.tvLike.setText(like);
        viewHolder.tvDate.setText(date);
        viewHolder.tvNum.setText(String.valueOf(num));
        viewHolder.tvImg.setImageBitmap(img);
//        viewHolder.tvImg.setText(img);

    }
}
