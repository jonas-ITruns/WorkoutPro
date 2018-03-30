package com.developer.workoutpro.itruns.workoutpro;

public class ObjMeineUebungen {

    private String name;
    private String muskelgruppe;
    private String beschreibung;

    public ObjMeineUebungen() {
        name = "";
        muskelgruppe = "";
        beschreibung = "";
    } // Konstruktor ObjMeineUebungen

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

} // Klasse ObjMeineUebungen
