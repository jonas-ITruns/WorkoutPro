package com.developer.workoutpro.itruns.workoutpro;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SwipeRecyclerViewAdapter extends RecyclerView.Adapter<SwipeRecyclerViewAdapter.ViewHolder> {

    private ArrayList<String> uebungName;
    private ArrayList<String> uebungMuskelgruppe;
    private ArrayList<String> uebungBeschreibung;
    private boolean onClick;
    MainClass mainClass;

    public SwipeRecyclerViewAdapter(Context context, ArrayList<String> uebungName, ArrayList<String> uebungMuskelgruppe, ArrayList<String> uebungBeschreibung, boolean onClick) {
        mainClass = (MainClass) context;
        this.uebungName = uebungName;
        this.uebungMuskelgruppe = uebungMuskelgruppe;
        this.uebungBeschreibung = uebungBeschreibung;
        this.onClick = onClick;
    }

    @Override
    public SwipeRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fr_uebungen_row, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
            viewHolder.tvUebungName.setText(uebungName.get(i));
            viewHolder.tvUebungBeschreibung.setText(uebungBeschreibung.get(i));
            if (uebungMuskelgruppe.get(i).equals("ganzkoerper")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_exercises);
            } else if (uebungMuskelgruppe.get(i).equals("arme")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.muskelgruppe_arm);
            } else if (uebungMuskelgruppe.get(i).equals("beine")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_add_black_24dp);
            } else if (uebungMuskelgruppe.get(i).equals("bauch")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_muskelgruppe_bauch_32);
            } else if (uebungMuskelgruppe.get(i).equals("brust")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_muskelgruppe_brust_32);
            } else if (uebungMuskelgruppe.get(i).equals("ruecken")) {
                viewHolder.imgvMuskelgruppe.setImageResource(R.drawable.ic_menu);
            } else {
                viewHolder.imgvMuskelgruppe.setImageResource(R.color.transparent);
            } // if


        if (onClick) {
            viewHolder.constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainClass.workoutUebungHinzufuegen(i);
                    mainClass.workoutHinzufuegenOeffnen();
                    mainClass.alert.cancel();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return uebungName.size();
    }

    public void removeItem(int position, boolean workoutUebung) {
        uebungName.remove(position);
        uebungMuskelgruppe.remove(position);
        uebungBeschreibung.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, uebungName.size());
        if (workoutUebung) {
            mainClass.workoutUebungLoeschen(position);
        } // then
        else {
            mainClass.uebungLoeschen(position);
        } // else
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraintLayout;
        TextView tvUebungName;
        TextView tvUebungBeschreibung;
        ImageView imgvMuskelgruppe;

        public ViewHolder(View view) {
            super(view);
                constraintLayout = view.findViewById(R.id.constraint_layout);
                tvUebungName = view.findViewById(R.id.tvUebungName);
                tvUebungBeschreibung = view.findViewById(R.id.tvUebungBeschreibung);
                imgvMuskelgruppe = view.findViewById(R.id.imgvMuskelgruppe);
        }
    }
}