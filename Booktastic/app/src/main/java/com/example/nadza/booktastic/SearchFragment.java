package com.example.nadza.booktastic;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchFragment extends Fragment {

    EditText search;
    Button ganres;
    Button authors;
    ListView listView;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ganres=(Button)getActivity().findViewById(R.id.ganreBtn);
        authors=(Button)getActivity().findViewById(R.id.authorsBtn);
        search=(EditText)getActivity().findViewById(R.id.search);
        listView=(ListView)getActivity().findViewById(R.id.list);

        search.setVisibility(View.GONE);

        final ArrayAdapter<String> adapter2;
        adapter2 = new ArrayAdapter <String>(getContext(),android.R.layout.simple_list_item_1, DataHolder.getGanrelist());
        final ArrayAdapter<String> adapter3;
        adapter3 = new ArrayAdapter <String>(getContext(),android.R.layout.simple_list_item_1, DataHolder.getAuthorList());

        ganres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {

                listView.setAdapter(adapter2);
                search.setVisibility(View.VISIBLE);
                search.setHint("Search by ganre ...");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        String item = ((TextView)view).getText().toString();
                        BookFragment fragment= new BookFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("key",item);
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.content1, fragment);
                        ft.addToBackStack(null);
                        ft.commit();
                    }
                });
            }
        });
        authors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                listView.setAdapter(adapter3);
                search.setVisibility(View.VISIBLE);
                search.setHint("Search by author ...");
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                        String item = ((TextView)view).getText().toString();
                        BookFragment fragment= new BookFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("key",item);
                        fragment.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.content1, fragment).addToBackStack("dodavanje").commit();
                    }
                });
            }
        });
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
