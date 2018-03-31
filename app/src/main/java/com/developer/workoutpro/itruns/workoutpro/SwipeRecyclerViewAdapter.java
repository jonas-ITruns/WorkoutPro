package com.developer.workoutpro.itruns.workoutpro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SwipeRecyclerViewAdapter extends RecyclerView.Adapter<SwipeRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> uebungName;
    private ArrayList<String> uebungMuskelgruppe;
    private ArrayList<String> uebungBeschreibung;
    MainClass mainClass;

    public SwipeRecyclerViewAdapter(Context context, ArrayList<String> uebungName, ArrayList<String> uebungMuskelgruppe, ArrayList<String> uebungBeschreibung) {
        mainClass = (MainClass) context;
        this.uebungName = uebungName;
        this.uebungMuskelgruppe = uebungMuskelgruppe;
        this.uebungBeschreibung = uebungBeschreibung;
    }

    @Override
    public SwipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fr_uebungen_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.tvUebungName.setText(uebungName.get(i));
        viewHolder.tvUebungBeschreibung.setText(uebungBeschreibung.get(i));
        if (uebungMuskelgruppe.get(i).equals("ganzkoerper")) {
            viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_exercises);
        } else if (uebungMuskelgruppe.get(i).equals("arme")) {
            viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.muskelgruppe_arm);
        } else if (uebungMuskelgruppe.get(i).equals("beine")) {
            viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_add_black_24dp);
        } else if (uebungMuskelgruppe.get(i).equals("bauch")) {
            viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_delete_black_24dp);
        } else if (uebungMuskelgruppe.get(i).equals("brust")) {
            viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_edit);
        } else if (uebungMuskelgruppe.get(i).equals("ruecken")) {
            viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_menu);
        } else {
            viewHolder.imgvMuskelgruppe.setImageResource(R.color.transparent);
        } // if

    }

    @Override
    public int getItemCount() {
        return uebungName.size();
    }

    public void removeItem(int position) {
        uebungName.remove(position);
        uebungMuskelgruppe.remove(position);
        uebungBeschreibung.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, uebungName.size());
        mainClass.uebungLoeschen(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvUebungName;
        TextView tvUebungBeschreibung;
        ImageView imgvMuskelgruppe;
        public ViewHolder(View view) {
            super(view);

            tvUebungName = view.findViewById(R.id.tvUebungName);
            tvUebungBeschreibung = view.findViewById(R.id.tvUebungBeschreibung);
            imgvMuskelgruppe = view.findViewById(R.id.imgvMuskelgruppe);
        }
    }
}