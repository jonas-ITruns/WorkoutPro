package com.developer.workoutpro.itruns.workoutpro;

public class ObjMeineUebungen {

    private int uebungNummer;
    private String name;
    private String muskelgruppe;
    private String beschreibung;
    private int minuten;
    private int sekunden;

    public ObjMeineUebungen() {
        uebungNummer = 0;
        name = "";
        muskelgruppe = "";
        beschreibung = "";
        minuten = 0;
        sekunden = 0;
    } // Konstruktor ObjMeineUebungen

    public void neueUebung(int pUebungNummer, String pName, String pMuskelgruppe, String pBeschreibung, int pMinuten, int pSekunden) {
        uebungNummer = pUebungNummer;
        name = pName;
        muskelgruppe =pMuskelgruppe;
        beschreibung = pBeschreibung;
        minuten = pMinuten;
        sekunden = pSekunden;
    } // Methode neueUebung

    public int gibUebungNummer() {
        return uebungNummer;
    } // Methode gibUebungNummer

    public void setzeNummer(int pUebungNummer) {
        uebungNummer = pUebungNummer;
    } // Methode setzeNummer

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

    public int gibMinuten() {
        return minuten;
    } // Methode gibMinuten

    public void setzeMinuten(int pMinuten) {
        minuten = pMinuten;
    } // Methode setzeMinuten

    public int gibSekunden() {
        return sekunden;
    } // Methode gibSekunden

    public void setzeSekunden(int pSekunden) {
        sekunden = pSekunden;
    } // Methode setzeSekunden


} // Klasse ObjMeineUebungen
