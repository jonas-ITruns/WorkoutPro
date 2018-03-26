package com.developer.workoutpro.itruns.workoutpro;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentExerciseRow extends Fragment {

    private int aktUebung;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_row, container, false);
        TextView tvName;

        switch (aktUebung) {
            case 0: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 1");
                    tvName.setId(aktUebung);
                    break;
            case 1: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 2");
                    tvName.setId(aktUebung);
                    break;
            case 2: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 3");
                    tvName.setId(aktUebung);
                    break;
            case 3: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 4");
                    tvName.setId(aktUebung);
                    break;
            case 4: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 5");
                    tvName.setId(aktUebung);
                    break;
            case 5: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 6");
                    tvName.setId(aktUebung);
                    break;
            case 6: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 7");
                    tvName.setId(aktUebung);
                    break;
            case 7: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 8");
                    tvName.setId(aktUebung);
                    break;
            case 8: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 9");
                    tvName.setId(aktUebung);
                    break;
            case 9: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 10");
                    tvName.setId(aktUebung);
                    break;
            case 10: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 11");
                    tvName.setId(aktUebung);
                    break;
            case 11: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 12");
                    tvName.setId(aktUebung);
                    break;
            case 12: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 13");
                    tvName.setId(aktUebung);
                    break;
            case 13: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 14");
                    tvName.setId(aktUebung);
                    break;
            case 14: tvName = view.findViewById(R.id.tvNameTest);
                    tvName.setText("Nummer 15");
                    tvName.setId(aktUebung);
                    break;
        } // switch

        return view;
    }

    public void setzeAktUebung(int pAktUebung) {
        aktUebung = pAktUebung;
    } // Methode setzeAtkUebung

}
