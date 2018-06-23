package com.example.nadza.booktastic;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    static public BazaOpenHelper databaseHelper;

    static public ArrayList<Knjiga> LK= new ArrayList<>();
    static public ArrayList<Kategorija> kategorije= new ArrayList<>();
    public static ArrayList<Autor> autori= new ArrayList<>();
    static public ArrayList<String> authorList= new ArrayList<String>();
    static public ArrayList<String> ganrelist = new ArrayList<String>();

    public static ArrayList<Knjiga> getLK() { return LK; }
    public static void setLK(ArrayList<Knjiga> LK) {
        DataHolder.LK = LK;
    }

    public static void setAuthorList(ArrayList<String> authorList) {
        DataHolder.authorList = authorList;
        popuniAuthor();
    }
    public static List<String> getAuthorList() {
        return authorList;
    }

    public static List<String> getGanrelist() {return ganrelist; }

    public static void kategorije() {
        databaseHelper.dodajKategoriju("Non-fiction"); //1
        databaseHelper.dodajKategoriju("Biography"); //2
        databaseHelper.dodajKategoriju("Comics"); //3
        databaseHelper.dodajKategoriju("Graphic Novel"); //4
        databaseHelper.dodajKategoriju("Fantasy"); //5
        databaseHelper.dodajKategoriju("Sci fi"); //6
        databaseHelper.dodajKategoriju("Romance"); //7
        databaseHelper.dodajKategoriju("Mystery"); //8
        databaseHelper.dodajKategoriju("Novel"); //9
        databaseHelper.dodajKategoriju("Historical fiction"); //10
        databaseHelper.dodajKategoriju("Horror"); //11
        databaseHelper.dodajKategoriju("Thriller"); //12
        databaseHelper.dodajKategoriju("Poetry"); //13
    }

    public static void popuniAuthor() {
        if(getAuthorList().isEmpty())
        {
            for(Knjiga k: getLK())
            {
                if(k.getAutori()!=null)
                {
                    for(Autor a: k.getAutori())
                    {
                        authorList.add(a.getImeiPrezime());
                    }
                }
            }
        }}


    //daj sve autore

    //daj sve zanrove

    //daj sve naslove knjiga

    //provjeri duplikate

    public static void updateData(){
        LK.clear();
        kategorije.clear();
        autori.clear();
        authorList.clear();
        ganrelist.clear();
        LK=databaseHelper.getAllBooks();
        kategorije();
        LK=databaseHelper.getAllBooks();
        kategorije=databaseHelper.getAllCategories();
        autori=databaseHelper.getAllAuthors();
        for(Kategorija k: kategorije) {
            ganrelist.add(k.getNaziv());
        }
        for(Autor a: autori) {
            authorList.add(a.getImeiPrezime());
        }
    }


} 