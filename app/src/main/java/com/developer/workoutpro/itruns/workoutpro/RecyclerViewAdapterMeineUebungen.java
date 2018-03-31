package com.developer.workoutpro.itruns.workoutpro;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewAdapterMeineUebungen extends RecyclerView.Adapter<RecyclerViewAdapterMeineUebungen.ViewHolder> {

    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mMuskelgruppe = new ArrayList<>();
    private ArrayList<String> mBeschreibung = new ArrayList<>();
    private Context mContext;

    public RecyclerViewAdapterMeineUebungen(Context context, ArrayList<String> name, ArrayList<String> muskelgruppe, ArrayList<String> beschreibung) {
        mName = name;
        mMuskelgruppe = muskelgruppe;
        mBeschreibung = beschreibung;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fr_uebungen_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvUebungName.setText(mName.get(position));
        holder.tvUebungBeschreibung.setText(mBeschreibung.get(position));
        //holder.imgvMuskelgruppe.setText(mMuskelgruppe.get(position));
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUebungName;
        TextView tvUebungBeschreibung;
        ImageView imgvMuskelgruppe;
        ConstraintLayout clItemHolder;

        public ViewHolder(View itemView) {
            super(itemView);
            tvUebungName = itemView.findViewById(R.id.tvUebungName);
            tvUebungBeschreibung = itemView.findViewById(R.id.tvUebungBeschreibung);
            imgvMuskelgruppe = itemView.findViewById(R.id.imgvMuskelgruppe);
            clItemHolder = itemView.findViewById(R.id.clItemHolder);
        }
    }
}
