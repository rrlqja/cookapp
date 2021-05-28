package com.example.cookapp1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class contview_ingre_season_recyclerview_Adapter extends RecyclerView.Adapter<contview_ingre_season_recyclerview_Adapter.itemViewholder> {

    contview_ingre_season contview_ingre_season;
    ArrayList<contview_ingre_season> array_ingre_seasons;

    public contview_ingre_season_recyclerview_Adapter(ArrayList<contview_ingre_season> array_ingre_seasons) {
        this.array_ingre_seasons = array_ingre_seasons;
    }

    @NonNull
    @Override
    public itemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingre_season_recyclerview_item, parent, false);
        return new itemViewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull itemViewholder holder, int position) {
        populateItemRows(holder, position);
    }

    @Override
    public int getItemCount() {
        return array_ingre_seasons == null ? 0 : array_ingre_seasons.size();
    }

    public class itemViewholder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;

        public itemViewholder(@NonNull View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.ingre_season_name);
            textView2 = itemView.findViewById(R.id.ingre_season_we);
        }
    }

    private void populateItemRows(itemViewholder viewHolder, int position) {

        String ingre_season_name = array_ingre_seasons.get(position).getName();
        String ingre_season_weight = array_ingre_seasons.get(position).getWeight();

        viewHolder.textView1.setText(ingre_season_name);
        viewHolder.textView2.setText(ingre_season_weight);
    }

}
