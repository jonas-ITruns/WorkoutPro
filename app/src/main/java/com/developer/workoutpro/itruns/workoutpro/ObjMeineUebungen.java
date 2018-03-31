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

    public void setzeName(String pName) {
        name = pName;
    } // Methode setzeName

    public String gibMuskelgruppe() {
        return muskelgruppe;
    } // Methode gibMuskelgruppe

    public void setzeMuskelgruppe(String pMuskelgruppe) {
        muskelgruppe = pMuskelgruppe;
    } // Methode setzeMuskelgruppe

    public String gibBeschreibung() {
        return beschreibung;
    } // Methode gibBeschreibung

    public void setzeBeschreibung(String pBeschreibung) {
        beschreibung = pBeschreibung;
    } // Methode setzeBeschreibung

} // Klasse ObjMeineUebungen
