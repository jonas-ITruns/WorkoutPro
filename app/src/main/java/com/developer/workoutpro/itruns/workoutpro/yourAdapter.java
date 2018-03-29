package com.developer.workoutpro.itruns.workoutpro;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class yourAdapter extends BaseAdapter {

    Context context;
    String[] name;
    private boolean nameScrollt = false;
    String[] muskelgruppe;
    private boolean muskelgruppeScrollt = false;
    String[] beschreibung;
    private boolean beschreibungScrollt = false;

    private static LayoutInflater inflater = null;

    public yourAdapter(Context context, String[] pName, String[] pMuskelgruppe, String[] pBeschreibung) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.name = pName;
        this.muskelgruppe = pMuskelgruppe;
        this.beschreibung = pBeschreibung;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return name.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return name[position];
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.fragment_exercise_row_standard_uebungen, null);

        final Button btnName = vi.findViewById(R.id.btnName);
        btnName.setText(name[position]);
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nameScrollt) {
                    btnName.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    btnName.setSelected(true);
                    btnName.setSingleLine(true);
                    nameScrollt = true;
                } // then
                else {
                    btnName.setEllipsize(TextUtils.TruncateAt.END);
                    btnName.setSelected(true);
                    btnName.setSingleLine(true);
                    nameScrollt = false;
                } // else
            }
        });

        final Button btnMuskelgruppe = vi.findViewById(R.id.btnMuskelgruppe);
        btnMuskelgruppe.setText(muskelgruppe[position]);
        btnMuskelgruppe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!muskelgruppeScrollt) {
                    btnMuskelgruppe.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    btnMuskelgruppe.setSelected(true);
                    btnMuskelgruppe.setSingleLine(true);
                    muskelgruppeScrollt = true;
                } // then
                else {
                    btnMuskelgruppe.setEllipsize(TextUtils.TruncateAt.END);
                    btnMuskelgruppe.setSelected(true);
                    btnMuskelgruppe.setSingleLine(true);
                    muskelgruppeScrollt = false;
                } // else
            }
        });

        final Button btnBeschreibung = vi.findViewById(R.id.btnBeschreibung);
        btnBeschreibung.setText(beschreibung[position]);
        btnBeschreibung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!beschreibungScrollt) {
                    btnBeschreibung.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    btnBeschreibung.setSelected(true);
                    btnBeschreibung.setSingleLine(true);
                    beschreibungScrollt = true;
                } // then
                else {
                    btnBeschreibung.setEllipsize(TextUtils.TruncateAt.END);
                    btnBeschreibung.setSelected(true);
                    btnBeschreibung.setSingleLine(true);
                    beschreibungScrollt = false;
                } // else
            }
        });


        return vi;
    }
}
