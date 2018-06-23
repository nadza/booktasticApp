package com.example.nadza.booktastic;

import java.util.ArrayList;
import java.util.Arrays;

public class Autor {
    private int _id;
    private String id;
    private String imeiPrezime;
    private ArrayList<String> knjige = new ArrayList<>();

    public Autor() {
    }

    public Autor(String imeiPrezime, ArrayList<String> knjige) {
        this.imeiPrezime = imeiPrezime;
        this.knjige = knjige;
    }

    public Autor(int id, String imeiPrezime) {
        this._id = id;
        this.imeiPrezime = imeiPrezime;
    }

    public Autor(String imeiPrezime, String id) {
        this.id = id;
        this.imeiPrezime = imeiPrezime;
    }

    public Autor(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public void dodajAutora(String imeiPrezime, String idKnjige) {
        if (getKnjige() == null) {
            knjige = new ArrayList<String>(Arrays.asList(idKnjige));
        }
        this.imeiPrezime = imeiPrezime;
        this.knjige.add(idKnjige);
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        this._id = id;
    }

    public String getImeiPrezime() {
        return imeiPrezime;
    }

    public void setImeiPrezime(String imeiPrezime) {
        this.imeiPrezime = imeiPrezime;
    }

    public ArrayList<String> getKnjige() {
        return knjige;
    }

    public void setKnjige(ArrayList<String> knjige) {
        this.knjige = knjige;
    }

    public void dodajKnjigu(String idKnjige) {
        this.knjige.add(idKnjige);
    }
}