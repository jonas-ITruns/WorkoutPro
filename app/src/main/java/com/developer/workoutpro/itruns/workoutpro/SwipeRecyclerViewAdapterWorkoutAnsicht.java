package com.developer.workoutpro.itruns.workoutpro;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

public class SwipeRecyclerViewAdapterWorkoutAnsicht extends RecyclerView.Adapter<SwipeRecyclerViewAdapterWorkoutAnsicht.ViewHolder> implements ItemTouchHelperAdapter {

    private ArrayList<String> uebungName;
    private ArrayList<String> uebungMuskelgruppe;
    private ArrayList<String> uebungBeschreibung;
    private ArrayList<String> uebungMinuten;
    private ArrayList<String> uebungSekunden;
    MainClass mainClass;
    private boolean draggen = false;
    private boolean btnsDraggen;
    private final OnStartDragListener mDragStartListener;

    public SwipeRecyclerViewAdapterWorkoutAnsicht(Context context, ArrayList<String> uebungName, ArrayList<String> uebungMuskelgruppe, ArrayList<String> uebungBeschreibung, ArrayList<String> uebungMinuten, ArrayList<String> uebungSekunden, OnStartDragListener dragStartListener, boolean btnsDraggen) {
        mainClass = (MainClass) context;
        this.uebungName = uebungName;
        this.uebungMuskelgruppe = uebungMuskelgruppe;
        this.uebungBeschreibung = uebungBeschreibung;
        this.uebungMinuten = uebungMinuten;
        this.uebungSekunden = uebungSekunden;
        mDragStartListener = dragStartListener;
        this.btnsDraggen = btnsDraggen;
    }

    public interface OnStartDragListener {
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
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
        if (! btnsDraggen) {
            viewHolder.imgvMuskelgruppe.setVisibility(View.VISIBLE);
            viewHolder.imgvMuskelgruppe.setClickable(true);
            viewHolder.imgvDrag.setVisibility(View.INVISIBLE);
            viewHolder.imgvDrag.setClickable(false);
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
        } // then
        else {
            viewHolder.imgvMuskelgruppe.setVisibility(View.INVISIBLE);
            viewHolder.imgvMuskelgruppe.setClickable(false);
            viewHolder.imgvDrag.setVisibility(View.VISIBLE);
            viewHolder.imgvDrag.setClickable(true);
            viewHolder.imgvDrag.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                        mDragStartListener.onStartDrag(viewHolder);
                        return true;
                    } // if
                    return false;
                }
            });
        } // else

        // Null ergänzen, falls es unter 10 ist
        if (uebungMinuten.get(i).length() < 2) {
            uebungMinuten.set(i, "0" + uebungMinuten.get(i));
        } // if
        viewHolder.tvUebungDauerMinuten.setText(uebungMinuten.get(i));
        if (uebungSekunden.get(i).length() < 2) {
            uebungSekunden.set(i, "0" + uebungSekunden.get(i));
        } // if
        viewHolder.tvUebungDauerSekunden.setText(uebungSekunden.get(i));

        // Auf Hoch- oder Runterstellen warten

        viewHolder.imgbtnPlusMinuten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wenn nicht verschoben wurde
                if (! draggen) {
                    mainClass.workoutUebungDauerBearbeiten(1, i);
                    if (Integer.parseInt(uebungMinuten.get(i)) == 59) {
                        uebungMinuten.set(i, "0");
                    } else {
                        uebungMinuten.set(i, Integer.toString(Integer.parseInt(uebungMinuten.get(i)) + 1));
                    } // if
                } else {
                    draggen = false;
                } // if
                notifyDataSetChanged();
            }
        });
        viewHolder.imgbtnMinusMinuten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wenn nicht verschoben wurde
                if (! draggen) {
                    mainClass.workoutUebungDauerBearbeiten(2, i);
                    if (Integer.parseInt(uebungMinuten.get(i)) == 0) {
                        uebungMinuten.set(i, "59");
                    } else {
                        uebungMinuten.set(i, Integer.toString(Integer.parseInt(uebungMinuten.get(i)) - 1));
                    } // if
                } else {
                    draggen = false;
                } // if
                notifyDataSetChanged();
            }
        });
        viewHolder.imgbtnPlusSekunden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wenn nicht verschoben wurde
                if (! draggen) {
                    mainClass.workoutUebungDauerBearbeiten(3, i);
                    if (Integer.parseInt(uebungSekunden.get(i)) == 59) {
                        uebungSekunden.set(i, "0");
                    } else {
                        uebungSekunden.set(i, Integer.toString(Integer.parseInt(uebungSekunden.get(i)) + 1));
                    } // if
                } else {
                    draggen = false;
                } // if
                notifyDataSetChanged();
            }
        });
        viewHolder.imgbtnMinusSekunden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Wenn nicht verschoben wurde
                if (! draggen) {
                    mainClass.workoutUebungDauerBearbeiten(4, i);
                    if (Integer.parseInt(uebungSekunden.get(i)) == 0) {
                        uebungSekunden.set(i, "59");
                    } else {
                        uebungSekunden.set(i, Integer.toString(Integer.parseInt(uebungSekunden.get(i)) - 1));
                    } // if
                } else {
                    draggen = false;
                } // if
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return uebungName.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        draggen = true;
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                // Daten tauschen
                Collections.swap(uebungName, i, i + 1);
                Collections.swap(uebungMuskelgruppe, i, i + 1);
                Collections.swap(uebungBeschreibung, i, i + 1);
                Collections.swap(uebungMinuten, i, i + 1);
                Collections.swap(uebungSekunden, i, i + 1);
                mainClass.workoutUebungDrag(i, i + 1);
            } // for
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                // Daten tauschen
                Collections.swap(uebungName, i, i - 1);
                Collections.swap(uebungMuskelgruppe, i, i - 1);
                Collections.swap(uebungBeschreibung, i, i - 1);
                Collections.swap(uebungMinuten, i, i - 1);
                Collections.swap(uebungSekunden, i, i - 1);
                mainClass.workoutUebungDrag(i, i - 1);
            } // for
        } // if
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        uebungName.remove(position);
        uebungMuskelgruppe.remove(position);
        uebungBeschreibung.remove(position);
        uebungMinuten.remove(position);
        uebungSekunden.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, uebungName.size());
        mainClass.workoutUebungLoeschen(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvUebungName;
        TextView tvUebungBeschreibung;
        ImageView imgvMuskelgruppe;
        TextView tvUebungDauerMinuten;
        TextView tvUebungDauerSekunden;
        ImageButton imgbtnPlusMinuten;
        ImageButton imgbtnMinusMinuten;
        ImageButton imgbtnPlusSekunden;
        ImageButton imgbtnMinusSekunden;
        ImageView imgvDrag;


        public ViewHolder(View view) {
            super(view);
                tvUebungName = view.findViewById(R.id.tvUebungName);
                tvUebungBeschreibung = view.findViewById(R.id.tvUebungBeschreibung);
                imgvMuskelgruppe = view.findViewById(R.id.imgvMuskelgruppeAktuell);
                tvUebungDauerMinuten = view.findViewById(R.id.tvUebungDauerMinuten);
                tvUebungDauerSekunden = view.findViewById(R.id.tvUebungDauerSekunden);
                imgbtnPlusMinuten = view.findViewById(R.id.imgbtnPlusMinuten);
                imgbtnMinusMinuten = view.findViewById(R.id.imgbtnMinusMinuten);
                imgbtnPlusSekunden = view.findViewById(R.id.imgbtnPlusSekunden);
                imgbtnMinusSekunden = view.findViewById(R.id.imgbtnMinusSekunden);
                imgvDrag = view.findViewById(R.id.imgvDrag);
        }
    }

}