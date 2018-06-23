package com.example.nadza.booktastic;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Knjiga {
    private String idWebServisa;
    private int _id;
    private String naziv;
    private ArrayList<Autor> autori;
    private int autor_id;
    private String opis;
    private String datumObjavljivanja;
    private URL slika;
    private int brojStranica;
    private int idKtegorije;
    private int pregledana;

    public Knjiga() {}
    //konstruktor za web servise
    public Knjiga(String id, String naziv, ArrayList<Autor> autori, String opis, String datumObjavljivanja, URL slika, int brojStranica) {
        this.idWebServisa = id;
        this.naziv = naziv;
        this.autori = autori;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        this.slika = slika;
        this.brojStranica = brojStranica;
        this.pregledana=0;
    }
    //konstruktor za bazu
    public Knjiga(int _id, String naziv, String opis, String datumObjavljivanja, int brojStranica, String id, int idKtegorije, String slika, int pregledana) {
        this._id = _id;
        this.naziv = naziv;
        this.opis = opis;
        this.datumObjavljivanja = datumObjavljivanja;
        try {
            this.slika = new URL(slika);
        } catch (MalformedURLException e) {
            this.slika= null;
        }
        this.autori=DataHolder.databaseHelper.getAuthorListbyBookId(_id);
        this.brojStranica = brojStranica;
        this.idKtegorije = idKtegorije;
        this.pregledana=pregledana;
    }

    public Knjiga(String naziv, String autor, int idKtegorije)
    {
        this.naziv=naziv;
        this.idKtegorije=idKtegorije;
        ArrayList<Autor> newA= null;
        newA.add(new Autor(autor));
        this.autori= newA;
        this.pregledana=0;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public ArrayList<Autor> getAutori() {
        return autori;
    }

    public void setAutori(ArrayList<Autor> autori) {
        this.autori = autori;
    }

    public void setAutori(String autor) {if(this.autori==null){this.autori=new ArrayList<>(); this.autori.add(new Autor(autor));} else this.autori.add(new Autor(autor));}

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getDatumObjavljivanja() {
        return datumObjavljivanja;
    }

    public void setDatumObjavljivanja(String datumObjavljivanja) { this.datumObjavljivanja = datumObjavljivanja; }

    public URL getSlika() {
        return slika;
    }

    public void setSlika(URL slika) {
        this.slika = slika;
    }

    public int getBrojStranica() { return brojStranica; }

    public void setBrojStranica(int brojStranica) {
        this.brojStranica = brojStranica;
    }

    public int getIdKtegorije() { return idKtegorije; }

    public void setIdKtegorije(int idKtegorije) { this.idKtegorije = idKtegorije; }

    public String getIdWebServisa() {
        return idWebServisa;
    }

    public void setIdWebServisa(String idWebServisa) {
        this.idWebServisa = idWebServisa;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getAutor_id() {
        return autor_id;
    }

    public void setAutor_id(int autor_id) {
        this.autor_id = autor_id;
    }

    public int getPregledana() {
        return pregledana;
    }

    public void setPregledana(int pregledana) {
        this.pregledana = pregledana;
    }
}