package com.example.nadza.booktastic;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class BazaOpenHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "app_database.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KATEGORIJA = "Kategorija"; //ime tabele kateogrija
    private static final String KNJIGA = "Knjiga"; //ime tabele knjiga
    private static final String AUTOR= "Autor"; //ime tabele autora
    private static final String AUTORSTVA= "Autorstva"; //ime tabele autorstva
    private static final String ID = "_id"; //ime kolone sa id-evim, tip integer primarni kljuc
    private static final String NAZIV = "naziv"; //ime kolone sa nazivom, tip text
    private static final String OPIS = "opis"; //ime kolone sa opisom, tip text
    private static final String DATUM_OBJAVLJIVANJA = "datumObjavljivanja"; //ime kolone sa datumom, tip text
    private static final String BR_STRANICA = "brojStranica"; //ime kolone sa brojem stranica, tip integer
    private static final String WEB_SERVICE_ID = "idWebServis"; //ime kolone sa id-em web servisa tip text
    private static final String KATEGORIJE_ID = "idkategorije"; //id kategorije u tabeli knjiga, tipa integer
    private static final String IME = "ime";//  ime kolone za ime autora, tip text
    private static final String AUTOR_ID = "idautora"; // id autora u tabeli autorstva, tip integer
    private static final String KNJIGA_ID = "idknjige"; //id knjige u tabeli autorstva, tip intger
    private static final String SLIKA = "slika"; //ime kolone sa src slike tipa string al je url slike
    private static final String PREGLEDANA = "pregledana"; // tip integer, 1 za kliknuto, 0 inace
    private static int KategorijaNextID=1;
    private static int AutorNextID=1;
    private static int AutorstvoNextID=1;

    public BazaOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String CREATE_TABLE_KATEGORIJE = "CREATE TABLE "
            + KATEGORIJA + "(" + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT," + NAZIV + " TEXT );";

    private static final String CREATE_TABLE_KNJIGE = "CREATE TABLE "
            + KNJIGA + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ NAZIV + " TEXT, "
            + OPIS +" TEXT, " + DATUM_OBJAVLJIVANJA + " TEXT, " + BR_STRANICA +" INTEGER,"
            + WEB_SERVICE_ID+ " TEXT, " + KATEGORIJE_ID + " INTEGER, " + SLIKA + " TEXT, "
            + PREGLEDANA + " INTEGER);";

    private static final String CREATE_TABLE_AUTOR = "CREATE TABLE "
            + AUTOR + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ IME + " TEXT );";

    private static final String CREATE_TABLE_AUTORSTVA = "CREATE TABLE "
            + AUTORSTVA + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+ AUTOR_ID + " INTEGER, "
            + KNJIGA_ID + " INTEGER );";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KATEGORIJE);
        db.execSQL(CREATE_TABLE_KNJIGE);
        db.execSQL(CREATE_TABLE_AUTOR);
        db.execSQL(CREATE_TABLE_AUTORSTVA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + KATEGORIJA + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + KNJIGA + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + AUTOR + "'");
        db.execSQL("DROP TABLE IF EXISTS '" + AUTORSTVA + "'");
        onCreate(db);
    }

    public long dodajKategoriju(String naziv) {
        //addCategory, provjeri postoji li kategorija ako ne dodaj i nadji sljedeciId
        if(firstKat()) {
            Log.i("kat", 1+" "+ naziv);
            int id=getNextIDKategorije();
            id++;
            SQLiteDatabase db = this.getWritableDatabase();
            String insQuery="INSERT INTO Kategorija VALUES (" + id + ", \""
                    + naziv+"\")"; //pregledana
            db.execSQL(insQuery);
            return id;
        }
        if((containsCategory(naziv))) {
            Log.i("kat", 2+" "+ naziv);
            int id=getNextIDKategorije();
            id++;
            SQLiteDatabase db = this.getWritableDatabase();
            String insQuery="INSERT INTO Kategorija VALUES (" + id + ", \""
                    + naziv+"\")"; //pregledana
            db.execSQL(insQuery);
            return id;
        } else{
            Log.i("kat", 3+" "+ naziv);
            return -1;//ako je doslo do greske
        }
    }
    public Boolean firstKat() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Kategorija",null);
        cursor.moveToFirst();
        return cursor.getCount()==0;
    }
    public Boolean containsCategory(String naziv) {
        Kategorija kat= null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT naziv FROM Kategorija WHERE naziv=\"" + naziv+ "\"",null);
        cursor.moveToFirst();
        return cursor.getCount()==0;
    }
    public int getNextIDKategorije() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Max(_id) FROM Kategorija ",null);
        cursor.moveToFirst();
        int check=cursor.getInt(0);
        if(check!=0) {
            Log.i("msgKat", cursor.getInt(0) +"");
            return cursor.getInt(0);
            //return KategorijaNextID;
        }
        else {
            Log.i("msgKat", "nula");
            return 0;
        }
    }
    public long dodajAutora(String autorIme) {

        if(firstAut()) {
            Log.i("msg", "doesn't contain");
            int id=getNextIDAutor();
            id++;
            SQLiteDatabase db = this.getWritableDatabase();
            String insQuery="INSERT INTO Autor VALUES (" + id + ", \""
                    + autorIme+"\")";
            db.execSQL(insQuery);
            return id;
        }
        if((containsAutor(autorIme))) {
            Log.i("msg", "doesn't contain");
            int id=getNextIDAutor();
            id++;
            SQLiteDatabase db = this.getWritableDatabase();
            String insQuery="INSERT INTO Autor VALUES (" + id + ", \""
                    + autorIme+"\")";
            db.execSQL(insQuery);
            return id;
        } else{
            Log.i("msg", "contains");
            return -1;//ako je doslo do greske
        }
    }
    public Boolean firstAut() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Autor",null);
        cursor.moveToFirst();
        return cursor.getCount()==0;
    }
    public Boolean containsAutor(String ime) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT ime FROM Autor WHERE ime=\"" + ime+ "\"",null);
        cursor.moveToFirst();
        return cursor.getCount()==0;
    }
    public int getNextIDAutor() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Max(_id) FROM Autor ", null);
        cursor.moveToFirst();
        int check=cursor.getInt(0);
        if(check!=0) {
            Log.i("msgAut", cursor.getInt(0) +"");
            return cursor.getInt(0);
            //return AutorNextID;
        }
        else {
            Log.i("msgAut", "nula");
            return 0;
        }
    }
    public long dodajAutorstvo(int idknjige, int idautora) {
        //bookid+autorid+nextid
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            int id=getNextIDAutortvo();
            id++;
            String insQuery="INSERT INTO Autorstva VALUES (" + id + ", "
                    + idautora+  ", "+ idknjige+ ")"; //pregledana
            db.execSQL(insQuery);
            return id;
        } catch(Error e)
        {
            return -1;//ako je doslo do greske
        }
    }
    public int getNextIDAutortvo() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Max(_id) FROM Autorstva ",null);
        cursor.moveToFirst();
        int check=cursor.getInt(0);
        if(check!=0) {
            Log.i("msgAutstvo", cursor.getInt(0) +"");
            return cursor.getInt(0);
            //return AutorNextID;
        }
        else {
            Log.i("msgAutstvo", "nula");
            return 0;
        }
    }
    public long dodajKnjigu(Knjiga knjiga)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int id=getNextIDKnjiga();
        String imgSrc=null;
        if(knjiga.getSlika()!=null)
        {
            imgSrc=knjiga.getSlika().toString();
        }
        id++;
        try {
            //ovdje se doda null ako nesto nije dodjeljeno,
            // sta su ovi webservisiID?? gdje se to trazi i gdje se inicijalizira ??
            String insQuery="INSERT INTO Knjiga VALUES (" + id + ", \""
                    + knjiga.getNaziv() +"\", \""
                    + knjiga.getOpis()+"\", \""
                    + knjiga.getDatumObjavljivanja()+"\", \""
                    + knjiga.getBrojStranica()+"\", \""
                    + knjiga.getIdWebServisa()+"\", \""
                    + knjiga.getIdKtegorije()+"\", \""
                    + imgSrc+"\", \""
                    + knjiga.getPregledana()+"\")"; //pregledana
            db.execSQL(insQuery);

            //dodaj knjigu u tabelu knjiga, ako nema autora u tabeli autora dodaj i njega tamo
            //dodaj id knjige i id autora u tabelu autorstva

            //dakle prvo chekira listu autora
            if(!(knjiga.getAutori().isEmpty()))
            {
                for(Autor a: knjiga.getAutori())
                {
                    //ako nadje autora u tabeli autora vrati njegov id
                    //ako nadje ne nadje autora u tabeli autora doda ga
                    //i onda svakako ima njegov id i id knjige i doda ih u tabelu autorsrva
                    long idAutora=dodajAutora(a.getImeiPrezime());
                    if(idAutora!=-1) {
                        dodajAutorstvo(id,(int)idAutora);
                    } else {
                        dodajAutorstvo(id, getAutorIDbyName(a.getImeiPrezime()));
                    }
                }
            } else {
                long idAutora=dodajAutora("Unknown");
                if(idAutora!=-1) {
                    dodajAutorstvo(id,(int)idAutora);
                } else {
                    dodajAutorstvo(id, getAutorIDbyName("Unknown"));
                }
            }
            return id;
        } catch (Error e) {
            return -1; //ako je doslo do greske
        }
    }
    public int getNextIDKnjiga() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT Max(_id) FROM Knjiga ",null);
        cursor.moveToFirst();
        int check=cursor.getInt(0);
        if(check!=0) {
            Log.i("msgKnjg", cursor.getInt(0) +"");
            return cursor.getInt(0);
            //return AutorNextID;
        }
        else {
            Log.i("msgKnjg", "nula");
            return 0;
        }
    }
    public ArrayList<Knjiga> knjigeKategorije(long idKategorije){
        ArrayList<Knjiga> bookList= new ArrayList<>();
        Knjiga book=null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Knjiga WHERE idkategorije=" + idKategorije,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            book = new Knjiga(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4),  cursor.getString(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8));
            bookList.add(book);
            cursor.moveToNext();
        }
        cursor.close();
        return bookList;
        //nadji sve knjige sa idkategorije= proslijedjeni id

    }
    public ArrayList<Knjiga> knjigaAutora(long idAutora) {
        ArrayList<Knjiga> knjigeSoritranePoAutoru= new ArrayList<>();
        //prvo nam treba lista id-eva knjiga iz tabele autorstva
        ArrayList<Integer> idknjigaList= new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        //nadji sve knjige sa idautora= proslijedjeni id
        Cursor cursor = db.rawQuery("SELECT idknjige FROM Autorstva WHERE idautora=" + idAutora, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Integer anInt = cursor.getInt(0);
            idknjigaList.add(anInt);
            cursor.moveToNext();
        }
        cursor.close();
        //nadji cijeli objekat Knjiga iz baze na osnovu id-eva iz liste id-eva i dodaj u listu knjigu
        for(Integer i: idknjigaList)
        {
            knjigeSoritranePoAutoru.add(getBookbyId(i));
        }
        return knjigeSoritranePoAutoru;
    }
    public ArrayList<Knjiga> getAllBooks(){
        Knjiga book = null;
        ArrayList<Knjiga> bookList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Knjiga", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            book = new Knjiga(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4),  cursor.getString(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8));
            bookList.add(book);
            cursor.moveToNext();
        }
        cursor.close();
        return bookList;
    }
    public ArrayList<Kategorija> getAllCategories() {
        ArrayList<Kategorija> categoryList= new ArrayList<>();
        Kategorija category = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Kategorija", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            category = new Kategorija(cursor.getInt(0), cursor.getString(1));
            categoryList.add(category);
            cursor.moveToNext();
        }
        cursor.close();
        return categoryList;
    }
    public ArrayList<Autor> getAllAuthors() {
        ArrayList<Autor> authorList= new ArrayList<>();
        Autor author = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Autor", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            author = new Autor(cursor.getInt(0), cursor.getString(1));
            authorList.add(author);
            cursor.moveToNext();
        }
        cursor.close();
        return authorList;
    }
    public Knjiga getBookbyId(int id) {
        Knjiga book= new Knjiga();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Knjiga WHERE _id=" + id,null);
        cursor.moveToFirst();
        book = new Knjiga(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4),  cursor.getString(5), cursor.getInt(6), cursor.getString(7), cursor.getInt(8));
        //Only 1 result
        cursor.close();
        return book;
    }
    public int getAutorIDbyName(String name) {
        int id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT _id FROM Autor WHERE ime=\"" + name+ "\"",null);
        cursor.moveToFirst();
        id = cursor.getInt(0);
        //Only 1 result
        cursor.close();
        return id;
    }
    public int getKategorijaIDbyName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id;
        Cursor cursor = db.rawQuery("SELECT _id FROM Kategorija WHERE naziv=\"" + name+ "\"",null);
        cursor.moveToFirst();
        id = cursor.getInt(0);
        //Only 1 result
        cursor.close();
        return id;
    }
    //delete by id?, update by id?
    public void updatePregledanaById(int id, int change) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("pregledana", change);
        String[] whereArgs = {Integer.toString(id)};
        SQLiteDatabase db = this.getWritableDatabase();
        long returnValue = db.update("Knjiga",contentValues, "_id=?", whereArgs);
    }
    //getAutore knjige
    public ArrayList<Autor> getAuthorListbyBookId(long id) {
        ArrayList<Autor> authorList= new ArrayList<>();
        Autor author= new Autor();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select a._id, a.ime from Autor a, Autorstva at where a._id=at.idautora and at.idknjige=" + id,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            author = new Autor(cursor.getInt(0), cursor.getString(1));
            authorList.add(author);
            cursor.moveToNext();
        }

        cursor.close();
        return authorList;
    }

}