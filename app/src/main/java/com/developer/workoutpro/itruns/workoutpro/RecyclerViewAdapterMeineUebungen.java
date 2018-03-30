package com.developer.workoutpro.itruns.workoutpro;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

public class RecyclerViewAdapterMeineUebungen extends RecyclerView.Adapter<RecyclerViewAdapterMeineUebungen.ViewHolder> {

    private ArrayList<String> mName = new ArrayList<>();
    private boolean nameScrollt = false;
    private ArrayList<String> mMuskelgruppe = new ArrayList<>();
    private boolean muskelgruppeScrollt = false;
    private ArrayList<String> mBeschreibung = new ArrayList<>();
    private boolean beschreibungScrollt = false;
    private Context mContext;

    public RecyclerViewAdapterMeineUebungen(Context context, ArrayList<String> name, ArrayList<String> muskelgruppe, ArrayList<String> beschreibung) {
        mName = name;
        mMuskelgruppe = muskelgruppe;
        mBeschreibung = beschreibung;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fr_meine_uebungen_row, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.btnName.setText(mName.get(position));
        holder.btnMuskelgruppe.setText(mMuskelgruppe.get(position));
        holder.btnBeschreibung.setText(mBeschreibung.get(position));

        final ViewHolder pHolder = holder;
        pHolder.btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameScrollt) {
                    pHolder.btnName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    pHolder.btnName.setSelected(true);
                    pHolder.btnName.setSingleLine(true);
                    nameScrollt = true;
                } // then
                else {
                    pHolder.btnName.setEllipsize(TextUtils.TruncateAt.END);
                    pHolder.btnName.setSelected(true);
                    pHolder.btnName.setSingleLine(true);
                    nameScrollt = false;
                } // else
            }
        });

        pHolder.btnMuskelgruppe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!muskelgruppeScrollt) {
                    pHolder.btnMuskelgruppe.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    pHolder.btnMuskelgruppe.setSelected(true);
                    pHolder.btnMuskelgruppe.setSingleLine(true);
                    muskelgruppeScrollt = true;
                } // then
                else {
                    pHolder.btnMuskelgruppe.setEllipsize(TextUtils.TruncateAt.END);
                    pHolder.btnMuskelgruppe.setSelected(true);
                    pHolder.btnMuskelgruppe.setSingleLine(true);
                    muskelgruppeScrollt = false;
                } // else
            }
        });

        pHolder.btnBeschreibung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beschreibungScrollt) {
                    pHolder.btnBeschreibung.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    pHolder.btnBeschreibung.setSelected(true);
                    pHolder.btnBeschreibung.setSingleLine(true);
                    beschreibungScrollt = true;
                } // then
                else {
                    pHolder.btnBeschreibung.setEllipsize(TextUtils.TruncateAt.END);
                    pHolder.btnBeschreibung.setSelected(true);
                    pHolder.btnBeschreibung.setSingleLine(true);
                    beschreibungScrollt = false;
                } // else
            }
        });
    }

    @Override
    public int getItemCount() {
        return mName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Button btnName;
        Button btnMuskelgruppe;
        Button btnBeschreibung;
        ConstraintLayout clRow;

        public ViewHolder(View itemView) {
            super(itemView);
            btnName = itemView.findViewById(R.id.btnName);
            btnMuskelgruppe = itemView.findViewById(R.id.btnMuskelgruppe);
            btnBeschreibung = itemView.findViewById(R.id.btnBeschreibung);
            clRow = itemView.findViewById(R.id.clRow);
        }
    }
}
