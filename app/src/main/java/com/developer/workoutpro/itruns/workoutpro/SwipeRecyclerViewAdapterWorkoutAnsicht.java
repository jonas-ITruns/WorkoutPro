package com.developer.workoutpro.itruns.workoutpro;

import android.content.Context;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class SwipeRecyclerViewAdapterWorkoutAnsicht extends RecyclerView.Adapter<SwipeRecyclerViewAdapterWorkoutAnsicht.ViewHolder> {

    private ArrayList<String> uebungName;
    private ArrayList<String> uebungMuskelgruppe;
    private ArrayList<String> uebungBeschreibung;
    private ArrayList<String> uebungDauer;
    MainClass mainClass;
    private boolean uebungDauerErhoehen;

    public SwipeRecyclerViewAdapterWorkoutAnsicht(Context context, ArrayList<String> uebungName, ArrayList<String> uebungMuskelgruppe, ArrayList<String> uebungBeschreibung, ArrayList<String> uebungDauer) {
        mainClass = (MainClass) context;
        this.uebungName = uebungName;
        this.uebungMuskelgruppe = uebungMuskelgruppe;
        this.uebungBeschreibung = uebungBeschreibung;
        this.uebungDauer = uebungDauer;
    }

    @Override
    public SwipeRecyclerViewAdapterWorkoutAnsicht.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fr_uebungen_row_zeit, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
            viewHolder.tvUebungName.setText(uebungName.get(i));
            viewHolder.tvUebungBeschreibung.setText(uebungBeschreibung.get(i));
            if (uebungMuskelgruppe.get(i).equals("besonderes")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_besondere_uebungen_32);
            } else if (uebungMuskelgruppe.get(i).equals("ganzkoerper")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_muskelgruppe_ganzkoerper_32);
            } else if (uebungMuskelgruppe.get(i).equals("arme")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_muskelgruppe_arme_32);
            } else if (uebungMuskelgruppe.get(i).equals("beine")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_muskelgruppe_beine_32);
            } else if (uebungMuskelgruppe.get(i).equals("bauch")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_muskelgruppe_bauch_32);
            } else if (uebungMuskelgruppe.get(i).equals("brust")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_muskelgruppe_brust_32);
            } else if (uebungMuskelgruppe.get(i).equals("ruecken")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_muskelgruppe_ruecken_32);
            } else {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_besondere_uebungen_32);
            } // if
            viewHolder.tvUebungDauer.setText(uebungDauer.get(i));
            viewHolder.btnUebungDauerErhoehen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainClass.workoutUebungDauerErhoehen(i);
                    uebungDauer.set(i, Integer.toString(Integer.parseInt(uebungDauer.get(i)) + 1));
                    notifyDataSetChanged();
                }
            });
            viewHolder.btnUebungDauerVerringern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainClass.workoutUebungDauerVerringern(i);
                    uebungDauer.set(i, Integer.toString(Integer.parseInt(uebungDauer.get(i)) - 1));
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemCount() {
        return uebungName.size();
    }

    public void removeItem(int position) {
        uebungName.remove(position);
        uebungMuskelgruppe.remove(position);
        uebungBeschreibung.remove(position);
        uebungDauer.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, uebungName.size());
        mainClass.workoutUebungLoeschen(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        TextView tvUebungName;
        TextView tvUebungBeschreibung;
        ImageView imgvMuskelgruppe;
        TextView tvUebungDauer;
        Button btnUebungDauerErhoehen;
        Button btnUebungDauerVerringern;

        public ViewHolder(View view) {
            super(view);
                constraintLayout = view.findViewById(R.id.constraint_layout);
                tvUebungName = view.findViewById(R.id.tvUebungName);
                tvUebungBeschreibung = view.findViewById(R.id.tvUebungBeschreibung);
                imgvMuskelgruppe = view.findViewById(R.id.imgvMuskelgruppe);
                tvUebungDauer = view.findViewById(R.id.tvUebungDauer);
                btnUebungDauerErhoehen = view.findViewById(R.id.btnUebungDauerErhoehen);
                btnUebungDauerVerringern = view.findViewById(R.id.btnUebungDauerVerringern);
        }
    }
}