package com.example.nadza.booktastic;

public class Kategorija {
    public int id;
    public String naziv;

    public Kategorija() {}

    public Kategorija(int id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public int get_id() {
        return id;
    }

    public void set_id(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }
}