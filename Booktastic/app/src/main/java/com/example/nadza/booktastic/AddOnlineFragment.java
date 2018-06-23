package com.example.nadza.booktastic;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AddOnlineFragment extends Fragment implements DohvatiKnjige.IDohvatiKnjigeDone, DohvatiNajnovije.IDohvatiNajnovijeDone {

    Spinner category;
    Spinner book;
    EditText search;
    Button runSearch;
    Button addBook;
    Button goBack;
    ArrayList<Knjiga> bookList = new ArrayList<>();
    BookSpinnerAdapter mBookAdapter;
    String changedText = new String();

    public AddOnlineFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_online, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        category = (Spinner) getActivity().findViewById(R.id.sKategorija);
        book = (Spinner) getActivity().findViewById(R.id.sRezultat);
        search = (EditText) getActivity().findViewById(R.id.tekstUpit);
        addBook = (Button) getActivity().findViewById(R.id.dAdd);
        runSearch = (Button) getActivity().findViewById(R.id.dRun);

        ArrayAdapter adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, DataHolder.getGanrelist());
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);


        category.setAdapter(adapter);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                changedText = search.getText().toString();
                Log.i("chanedtxt2", changedText);
            }

            @Override
            public void afterTextChanged(Editable s) {
                //String changedText=search.getText().toString();
                //Log.i("chanedtxt3", changedText);
            }
        });

        runSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                /*try {
                    bookList=new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone)FragmentOnline.this).execute(changedText).get();
                } catch (InterruptedException e) {
                    Log.i("exp", e+"");
                } catch (ExecutionException e) {
                    Log.i("exp", e+"");
                }*/
                chooseSearchMethod(changedText);
                if (bookList == null) {
                    Toast.makeText(getContext(), "Sorry, the search didn't find any results!", Toast.LENGTH_LONG).show();
                } else {
                    mBookAdapter = new BookSpinnerAdapter(getContext(), new ArrayList<Knjiga>(bookList));
                    //mBookAdapter.setDropDownViewResource(R.layout.spinner_drop_down);
                    book.setAdapter(mBookAdapter);
                }

                Log.i("chanedtxt", changedText);
            }
        });
        addBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Knjiga knjiga = null;
                String kategorija;
                kategorija = category.getSelectedItem().toString();
                Object obj = book.getSelectedItem();
                if (obj == null) {
                    Toast.makeText(getContext(), "Please select a book!", Toast.LENGTH_LONG).show();
                } else {
                    if (obj instanceof Knjiga)
                        knjiga = (Knjiga) obj;
                    else
                        Toast.makeText(getContext(), "Please select a book!", Toast.LENGTH_LONG).show();

                    knjiga.setIdKtegorije(DataHolder.databaseHelper.getKategorijaIDbyName(kategorija));
                    if (knjiga.getAutori() != null) {
                        for (Autor a : knjiga.getAutori()) {
                            DataHolder.getAuthorList().add(a.getImeiPrezime());
                        }
                    }
                    DataHolder.getLK().add(knjiga);
                    long indeks = DataHolder.databaseHelper.dodajKnjigu(knjiga);
                    if (indeks != -1) {
                        Toast.makeText(getContext(), "Book added to database! Bok index: " + indeks, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Error adding book", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }

    @Override
    public void onDohvatiDone(List<Knjiga> knjige) {
        bookList = new ArrayList<Knjiga>(knjige);
    }

    @Override
    public void onNajnovijeDone(List<Knjiga> knjige) {
        bookList = new ArrayList<Knjiga>(knjige);
    }

    public static int countWords(String str) {
        char[] sentence = str.toCharArray();
        boolean inWord = false;
        int wordCt = 0;
        for (char c : sentence) {
            if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                if (!inWord) {
                    wordCt++;
                    inWord = true;
                }
            } else {
                inWord = false;
            }
        }
        return wordCt;
    }

    public Date StringToDate(String s) {

        Date result = new Date();
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            result = dateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void chooseSearchMethod(String search) {
        if (search == null) {
            Toast.makeText(getContext(), "Please enter a search term!", Toast.LENGTH_LONG).show();
            Log.i("chooseMethod", "searchNull");
        } else {
            Log.i("chooseMethod", "not null");
            if (countWords(search) > 1) {
                Log.i("chooseMethod", "more than one word");
                if (search.split(":").length > 1) {
                    Log.i("chooseMethod", "split");

                    String query = search.substring(search.lastIndexOf(":") + 1);
                    String condition = search.substring(0, search.lastIndexOf(':') + 1);
                    if (condition.equals("autor:")) {
                        Log.i("chooseMethod", "autor");
                        try {

                            bookList = new DohvatiNajnovije((DohvatiNajnovije.IDohvatiNajnovijeDone) AddOnlineFragment.this).execute(query).get();
                            //sort books by date from earliest
                            Collections.sort(bookList, new Comparator<Knjiga>() {
                                public int compare(Knjiga h1, Knjiga h2) {
                                    return StringToDate(h1.getDatumObjavljivanja()).compareTo(StringToDate(h2.getDatumObjavljivanja()));
                                }
                            });
                            //ArrayList<Knjiga> result= new ArrayList<>();
                            List<Knjiga> newList = new ArrayList<>(bookList.subList(0, 5));
                            bookList = new ArrayList<Knjiga>(newList);


                        } catch (InterruptedException e) {
                            Log.i("exp", e + "");
                        } catch (ExecutionException e) {
                            Log.i("exp", e + "");
                        }
                    } else {
                        Toast.makeText(getContext(), "Please add a valid search term", Toast.LENGTH_LONG).show();
                    }
                } else {
                    String[] strArray = search.split(";");
                    try {
                        Log.i("chooseMethod", "names");
                        bookList = new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) AddOnlineFragment.this).execute(strArray).get();
                    } catch (InterruptedException e) {
                        Log.i("exp", e + "");
                    } catch (ExecutionException e) {
                        Log.i("exp", e + "");
                    }
                }
            } else {
                try {
                    Log.i("chooseMethod", "one word");
                    bookList = new DohvatiKnjige((DohvatiKnjige.IDohvatiKnjigeDone) AddOnlineFragment.this).execute(changedText).get();
                } catch (InterruptedException e) {
                    Log.i("exp", e + "");
                } catch (ExecutionException e) {
                    Log.i("exp", e + "");
                }
            }
        }
    }
}
