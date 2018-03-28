package com.developer.workoutpro.itruns.workoutpro;

public class MeineUebungen {

    private String name;
    private String muskelgruppe;
    private String beschreibung;

    public MeineUebungen() {
        name = "";
        muskelgruppe = "";
        beschreibung = "";
    } // Konstruktor MeineUebungen

    public void neueUebung(String pName, String pMuskelgruppe, String pBeschreibung) {
        name = pName;
        muskelgruppe =pMuskelgruppe;
        beschreibung = pBeschreibung;
    } // Methode neueUebung

    public String gibName() {
        return name;
    } // Methode gibName

    public String gibMuskelgruppe() {
        return muskelgruppe;
    } // Methode gibMuskelgruppe

    public String gibBeschreibung() {
        return beschreibung;
    } // Methode gibBeschreibung

} // Klasse MeineUebungen
