package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FrMeineUebungen extends Fragment {

    View frView;
    LayoutInflater pInflater;
    ViewGroup pContainer;
    private ArrayList<String> mName = new ArrayList<>();
    private ArrayList<String> mMuskelgruppe = new ArrayList<>();
    private ArrayList<String> mBeschreibung = new ArrayList<>();
    private SwipeRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private AlertDialog alert;
    private AlertDialog.Builder builder;
    private EditText etName;
    private EditText etBeschreibung;
    private ImageButton imgbtnGanzkoerper;
    private ImageButton imgbtnArme;
    private ImageButton imgbtnBeine;
    private ImageButton imgbtnBauch;
    private ImageButton imgbtnBrust;
    private ImageButton imgbtnRuecken;
    private boolean ganzkoerper;
    private boolean arme;
    private boolean beine;
    private boolean bauch;
    private boolean brust;
    private boolean ruecken;
    private int edit_position;
    private Paint p = new Paint();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        pInflater = inflater;
        pContainer = container;
        frView = inflater.inflate(R.layout.fr_meine_uebungen, container, false);

        uebungenHolen();

        return frView;
    }

    private void uebungenHolen() {
        // Anzahl, Name, Muskelgruppe und Beschreibung der Übungen aus der MainClass holen
        int anzahlMeineUebungen = MainClass.gibAnzahlMeineUebungen();

        for (int index = 0; index < anzahlMeineUebungen; index++) {
            mName.add(MainClass.gibMeineUebungenName(index));
            mMuskelgruppe.add(MainClass.gibMeineUebungenMuskelgruppe(index));
            mBeschreibung.add(MainClass.gibMeineUebungenBeschreibung(index));
        } // for

        initViews();
    }

    private void initViews(){
        recyclerView = frView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new SwipeRecyclerViewAdapter(getActivity(), mName, mMuskelgruppe, mBeschreibung);
        recyclerView.setAdapter(adapter);
        initSwipe();


    }
    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                // Nach links swipen; für nach rechts swipen else Teil
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.removeItem(position);
                } // then
                else {
                    edit_position = position;

                    builder = new AlertDialog.Builder(getActivity());
                    builder.setView(R.layout.fr_uebung_hinzufuegen);
                    builder.setCancelable(true);
                    alert = builder.create();
                    alert.show();

                    // Deklarieren der Textfelder
                    TextView tvAlertUeberschrift = alert.findViewById(R.id.tvAlertUeberschrift);
                    tvAlertUeberschrift.setText("Übung bearbeiten");

                    etName = alert.findViewById(R.id.etUebungName);
                    etBeschreibung = alert.findViewById(R.id.etUebungBeschreibung);
                    imgbtnGanzkoerper = alert.findViewById(R.id.imgbtnGanzkoerper);
                    imgbtnArme = alert.findViewById(R.id.imgbtnArme);
                    imgbtnBeine = alert.findViewById(R.id.imgbtnBeine);
                    imgbtnBauch = alert.findViewById(R.id.imgbtnBauch);
                    imgbtnBrust = alert.findViewById(R.id.imgbtnBrust);
                    imgbtnRuecken = alert.findViewById(R.id.imgbtnRuecken);

                    // Name setzen
                    etName.setText(mName.get(edit_position));

                    // Beschreibung setzen
                    etBeschreibung.setText(mBeschreibung.get(edit_position));

                    // Muskelgruppe setzen
                    ganzkoerper = false;
                    arme = false;
                    beine = false;
                    bauch = false;
                    brust = false;
                    ruecken = false;

                    if(mMuskelgruppe.get(edit_position).equals("ganzkoerper")) {
                        ganzkoerper = true;
                        imgbtnGanzkoerper.setBackgroundResource(R.color.blauTransparent);
                    } else if (mMuskelgruppe.get(edit_position).equals("arme")) {
                        arme = true;
                        imgbtnArme.setBackgroundResource(R.color.blauTransparent);
                    } else if (mMuskelgruppe.get(edit_position).equals("beine")) {
                        beine = true;
                        imgbtnBeine.setBackgroundResource(R.color.blauTransparent);
                    } else if (mMuskelgruppe.get(edit_position).equals("bauch")) {
                        bauch = true;
                        imgbtnBauch.setBackgroundResource(R.color.blauTransparent);
                    } else if (mMuskelgruppe.get(edit_position).equals("brust")) {
                        brust = true;
                        imgbtnBrust.setBackgroundResource(R.color.blauTransparent);
                    } else if (mMuskelgruppe.get(edit_position).equals("ruecken")) {
                        ruecken = true;
                        imgbtnRuecken.setBackgroundResource(R.color.blauTransparent);
                    } // if

                    etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            etName.post(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.showSoftInput(etName, InputMethodManager.SHOW_IMPLICIT);
                                }
                            });
                        }
                    });
                    etName.requestFocus();

                    muskelgruppeInitialisieren();

                    Button btnUebungSpeichern = alert.findViewById(R.id.btnUebungSpeichern);
                    btnUebungSpeichern.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Muss in diese Methode, sonst Null.... Fehler
                            MainClass mainClass = (MainClass) getActivity();

                            if (etName.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), "Bitte Übungsnamen eintragen", Toast.LENGTH_SHORT).show();
                                return;
                            } else if (etBeschreibung.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), "Bitte Beschreibung eintragen", Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                // Name bestimmen
                                mName.set(edit_position, etName.getText().toString());

                                // Muskelgruppe bestimmen
                                if (ganzkoerper) {
                                    mMuskelgruppe.set(edit_position, "ganzkoerper");
                                } else if (arme) {
                                    mMuskelgruppe.set(edit_position, "arme");
                                } else if (beine) {
                                    mMuskelgruppe.set(edit_position, "beine");
                                } else if (bauch) {
                                    mMuskelgruppe.set(edit_position, "bauch");
                                } else if (brust) {
                                    mMuskelgruppe.set(edit_position, "brust");
                                } else if (ruecken) {
                                    mMuskelgruppe.set(edit_position, "ruecken");
                                } else {
                                    mMuskelgruppe.set(edit_position, "");
                                } // if

                                // Beschreibung bestimmen
                                mBeschreibung.set(edit_position, etBeschreibung.getText().toString());

                                mainClass.setzeMeineUebungName(mName.get(edit_position), edit_position);

                                mainClass.setzeMeineUebungMuskelgruppe(mMuskelgruppe.get(edit_position), edit_position);

                                mainClass.setzeMeineUebungBeschreibung(mBeschreibung.get(edit_position), edit_position);

                                adapter.notifyDataSetChanged();

                                alert.cancel();
                            } // else
                        }
                    });
                    Button btnUebungAbbrechen = alert.findViewById(R.id.btnUebungAbbrechen);
                    btnUebungAbbrechen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Muss in diese Methode, sonst Null.... Fehler
                            MainClass mainClass = (MainClass) getActivity();

                            mainClass.meineUebungenOeffnen();

                            alert.cancel();
                        }
                    });
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#4041577d"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                    } else {
                        p.setColor(Color.parseColor("#40a50000"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void muskelgruppeInitialisieren() {
        imgbtnGanzkoerper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = true; imgbtnGanzkoerper.setBackgroundResource(R.color.blauTransparent);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);

            }
        });

        imgbtnArme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = true; imgbtnArme.setBackgroundResource(R.color.blauTransparent);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
            }
        });

        imgbtnBeine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = true; imgbtnBeine.setBackgroundResource(R.color.blauTransparent);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
            }
        });

        imgbtnBauch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = true; imgbtnBauch.setBackgroundResource(R.color.blauTransparent);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
            }
        });

        imgbtnBrust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = true; imgbtnBrust.setBackgroundResource(R.color.blauTransparent);
                ruecken = false; imgbtnRuecken.setBackgroundColor(0x0041577d);
            }
        });

        imgbtnRuecken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ganzkoerper = false; imgbtnGanzkoerper.setBackgroundColor(0x0041577d);
                arme = false; imgbtnArme.setBackgroundColor(0x0041577d);
                beine = false; imgbtnBeine.setBackgroundColor(0x0041577d);
                bauch = false; imgbtnBauch.setBackgroundColor(0x0041577d);
                brust = false; imgbtnBrust.setBackgroundColor(0x0041577d);
                ruecken = true; imgbtnRuecken.setBackgroundResource(R.color.blauTransparent);
            }
        });

    }

}
