package com.example.nadza.booktastic;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;


public class BookFragment extends Fragment {

    public BookFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_book, container, false);
    }
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        EditText editText;
        ListView listView;
        BookAdapter adapter;
        ArrayList<Knjiga> nova= new ArrayList<>();


        Bundle bundle = this.getArguments();
        String msg = bundle.getString("key");
        Log.i("poruka", msg);

        if(DataHolder.getAuthorList().contains(msg))
        {
            nova=DataHolder.databaseHelper.knjigaAutora(DataHolder.databaseHelper.getAutorIDbyName(msg));
        }else if(DataHolder.getGanrelist().contains(msg))
        {
            nova=DataHolder.databaseHelper.knjigeKategorije(DataHolder.databaseHelper.getKategorijaIDbyName(msg));
        }

        listView = (ListView)getActivity().findViewById(R.id.listaKnjiga );
        adapter=new BookAdapter(getActivity(),nova);
        listView.setAdapter(adapter);

    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
