package com.example.nadza.booktastic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.net.URL;
import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Knjiga> {
    Context context;

    public BookAdapter(Context context, ArrayList<Knjiga> bookList) {
        super(context, 0, bookList);
        this.context=context;
    }

    private static class ViewHolder{
        private TextView title;
        private TextView author;
        private TextView description;
        private TextView publishedDate;
        private ImageView bookImage;
        private ImageView readStatusIcon;
        private TextView readStatusTxt;
        private TextView pageCount;
        private Button recommend;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        View lv=convertView;
        if(lv==null){
            lv = LayoutInflater.from(getContext()).inflate(R.layout.spinner_drop_down, parent, false);
            vh = new ViewHolder();
            vh.title = (TextView) lv.findViewById(R.id.neki_title);
            lv.setTag(vh);
        } else {
            vh = (ViewHolder) lv.getTag();
        }
        Knjiga currentBookDetail = getItem(position);

        assert currentBookDetail != null;
        vh.title.setText(currentBookDetail.getNaziv());
        return lv;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;
        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_list_item, parent, false);
            holder = new ViewHolder();
            holder.title = (TextView) listItemView.findViewById(R.id.book_title);
            holder.author = (TextView) listItemView.findViewById(R.id.book_author);
            holder.publishedDate = (TextView) listItemView.findViewById(R.id.publishing_date);
            holder.bookImage = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
            holder.description = (TextView) listItemView.findViewById(R.id.book_descr);
            holder.pageCount = (TextView) listItemView.findViewById(R.id.book_page_count);
            holder.readStatusIcon=(ImageView)listItemView.findViewById(R.id.read);
            holder.readStatusTxt=(TextView) listItemView.findViewById(R.id.read_txt);
            holder.recommend=(Button) listItemView.findViewById(R.id.dPreporuci);
            listItemView.setTag(holder);
        } else {

            holder = (ViewHolder) listItemView.getTag();
        }

        final Knjiga currentBookDetail = getItem(position);

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

        if (currentBookDetail.getDatumObjavljivanja() == null) {
            //holder.publishedDate.setVisibility(View.GONE);
            holder.publishedDate.setText("Date of Publishing: unknown");
        } else {
            holder.publishedDate.setVisibility(View.VISIBLE);
            // Change the format of date and then set it to textView
            holder.publishedDate.setText("Date of Publishing: "+(currentBookDetail.getDatumObjavljivanja()));
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
        if (currentBookDetail.getBrojStranica() == 0) {
            //holder.pageCount.setVisibility(View.GONE);
            holder.pageCount.setText("Page count: unknown");
        } else {
            holder.pageCount.setVisibility(View.VISIBLE);
            // Change the format of date and then set it to textView
            holder.pageCount.setText("Page count:"+ currentBookDetail.getBrojStranica()+"");
        }
        if(currentBookDetail.getPregledana()==0) {
            holder.readStatusIcon.setImageResource(R.drawable.ic_seen_red);
            holder.readStatusTxt.setText("TO READ");
            holder.readStatusTxt.setTextColor(Color.parseColor("#f44336"));
            //#f44336
        } else {
            holder.readStatusIcon.setImageResource(R.drawable.ic_seen_blue);
            holder.readStatusTxt.setText("READ");
            holder.readStatusTxt.setTextColor(Color.parseColor("#368f8b"));
        }

        holder.readStatusIcon.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Knjiga bookNew=currentBookDetail;
                if(bookNew.getPregledana()==0)
                {

                    Log.i("tag1", "0");

                    DataHolder.databaseHelper.updatePregledanaById(currentBookDetail.get_id(),1);

                    bookNew.setPregledana(1);
                    holder.readStatusIcon.setImageResource(R.drawable.ic_seen_blue);
                    holder.readStatusTxt.setText("READ");
                    holder.readStatusTxt.setTextColor(Color.parseColor("#368f8b"));

                }
                else
                {
                    Log.i("tag2", "1");

                    DataHolder.databaseHelper.updatePregledanaById(currentBookDetail.get_id(),0);

                    bookNew.setPregledana(0);
                    holder.readStatusIcon.setImageResource(R.drawable.ic_seen_red);
                    holder.readStatusTxt.setText("TO READ");
                    holder.readStatusTxt.setTextColor(Color.parseColor("#f44336"));

                }
            }
        });
        holder.recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                sendMail(position);
            }
        });

        return listItemView;
    }
    public void sendMail(int position) {
        Knjiga currentBookDetail = getItem(position);
        StringBuilder bookInfo= new StringBuilder();

        assert currentBookDetail != null;
        bookInfo.append("Name of the book: " + currentBookDetail.getNaziv());

        ArrayList<Autor> mAuthors= currentBookDetail.getAutori();
        if (mAuthors == null) {
            //holder.author.setVisibility(View.GONE);
            bookInfo.append("\nAuthors unknown");
        } else {
            bookInfo.append("\nAuthors: ");
            for(Autor a: mAuthors)
            {
                bookInfo.append(a.getImeiPrezime());
                bookInfo.append(", ");
            }
        }
        if(currentBookDetail.getOpis()==null)
        {
            //holder.description.setVisibility(View.GONE);
            bookInfo.append("\nNo description available");
        } else {
            bookInfo.append("\nDescription: "+ currentBookDetail.getOpis());
        }

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_SUBJECT, "I would like to recommend you a book");
        i.putExtra(Intent.EXTRA_TEXT   , new String(bookInfo));
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
            //Toast.makeText(getContext(), "Mail is being sent...", Toast.LENGTH_SHORT).show();
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}