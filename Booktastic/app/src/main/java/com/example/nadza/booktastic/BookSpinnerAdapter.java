package com.example.nadza.booktastic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

public class BookSpinnerAdapter extends ArrayAdapter<Knjiga> {

    public BookSpinnerAdapter(Context context, ArrayList<Knjiga> bookList) {
        super(context, 0, bookList);
    }

    private static class ViewHolder{
        private TextView title;
        private TextView author;
        private TextView description;
        private TextView publishedDate;
        private ImageView bookImage;
        private TextView pageCount;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        BookSpinnerAdapter.ViewHolder vh;
        View lv=convertView;
        if(lv==null){
            lv = LayoutInflater.from(getContext()).inflate(R.layout.spinner_drop_down, parent, false);
            vh = new BookSpinnerAdapter.ViewHolder();
            vh.title = (TextView) lv.findViewById(R.id.neki_title);
            lv.setTag(vh);
        } else {
            vh = (BookSpinnerAdapter.ViewHolder) lv.getTag();
        }
        Knjiga currentBookDetail = getItem(position);

        assert currentBookDetail != null;
        vh.title.setText(currentBookDetail.getNaziv());
        return lv;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        BookSpinnerAdapter.ViewHolder holder;
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_spinner_item, parent, false);
            holder = new BookSpinnerAdapter.ViewHolder();
            holder.title = (TextView) listItemView.findViewById(R.id.book_title);
            holder.author = (TextView) listItemView.findViewById(R.id.book_author);
            holder.bookImage = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
            holder.description = (TextView) listItemView.findViewById(R.id.book_descr);

            listItemView.setTag(holder);
        } else {

            holder = (BookSpinnerAdapter.ViewHolder) listItemView.getTag();
        }

        Knjiga currentBookDetail = getItem(position);

        assert currentBookDetail != null;
        holder.title.setText(currentBookDetail.getNaziv());

        ArrayList<Autor> mAuthors= currentBookDetail.getAutori();
        if (mAuthors == null) {
            //holder.author.setVisibility(View.GONE);
            holder.author.setText("Authors unknown");
        } else {
            StringBuilder autoriString= new StringBuilder();
            for(Autor a: mAuthors)
            {
                autoriString.append(a.getImeiPrezime());
                autoriString.append(", ");
            }
            holder.author.setVisibility(View.VISIBLE);
            holder.author.setText(autoriString);
        }
        if(currentBookDetail.getOpis()==null)
        {
            //holder.description.setVisibility(View.GONE);
            holder.description.setText("No description available");
        } else {
            holder.description.setText(currentBookDetail.getOpis());
        }

        if (currentBookDetail.getSlika() == null) {
            holder.bookImage.setImageResource(R.mipmap.no_image);
        } else {
            URL url = currentBookDetail.getSlika();
            Picasso.get().load(String.valueOf(url)).into(holder.bookImage);
            //holder.bookImage.setImageResource(R.mipmap.img3);
        }
        return listItemView;
    }
}