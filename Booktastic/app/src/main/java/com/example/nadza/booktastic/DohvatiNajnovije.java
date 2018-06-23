package com.example.nadza.booktastic;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DohvatiNajnovije extends AsyncTask<String, Integer, ArrayList<Knjiga>> {


    private IDohvatiNajnovijeDone iDohvati;
    private ArrayList<Knjiga> knjige;
    String query1;

    private final static String LOG_TAG = "webservice";
    public DohvatiNajnovije(DohvatiNajnovije.IDohvatiNajnovijeDone iDohvati) {
        this.iDohvati=iDohvati;
    }


    @Override
    protected ArrayList<Knjiga> doInBackground(String... strings) {
        String query = null;
        URL url=null;
        String JSONResponse = null;
        String name = strings[0];
        String lastName = "";
        String firstName= "";
        if(name.split("\\w+").length>1){

            lastName = name.substring(name.lastIndexOf(" ")+1);
            firstName = name.substring(0, name.lastIndexOf(' '));
        }
        else{
            firstName = name;
        }
        query1=firstName+" "+lastName;
        try {
            query = URLEncoder.encode("\""+query1+"\"", "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        String url1 = "https://www.googleapis.com/books/v1/volumes?q=inauthor:" + query;
        try {
            url = new URL(url1);

        } catch (MalformedURLException h)
        {
            Log.i("url", h+"");
        }


        Log.i(LOG_TAG, "URL object created");
        try {
            JSONResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem closing input stream : fetchBookInformation() block gets from makeHttpRequest() block try closing inputStream.close()");
        }

        // If JSON response is not equal to null then extract the features of response
        // in order to prevent app from crashing if received response is null
        if (JSONResponse != null) {
            ArrayList<Knjiga> booksList = extractFeaturesOfJSON(JSONResponse);
            Log.i("JSON FULL", JSONResponse);
            knjige=booksList;
            return booksList;
        } else {
            Log.i("JSON FULL", "nothing there");
            // Otherwise return null object
            return null;
        }
    }

   /* @Override
    protected void onPreExecute() {
        super.onPreExecute();
        searchListener.onSearching();
    }
*/


    private static String makeHttpRequest(URL url) throws IOException {
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        String JSONResponse = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);

            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            if (urlConnection.getResponseCode() ==200) {
                Log.i(LOG_TAG, "Success Response Code : 200");
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            } else {
                Log.i(LOG_TAG, "Response code : " + urlConnection.getResponseCode());
                // If received any other response(i.e 400) code return null JSON response
                JSONResponse = null;
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error solving JSON response : makeHttpRequest() block");
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing an inputStream can throw IOException, which why makeHttpRequest
                // method signature specifies, throws IOException
                inputStream.close();
            }
        }

        return JSONResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        StringBuilder outputString = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    outputString.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error creating JSON response: readFromStream() block");
            }
        }
        return outputString.toString();
    }
    public static ArrayList<Knjiga> extractFeaturesOfJSON(String JSONResponse) {

        ArrayList<Knjiga> bookList = new ArrayList<Knjiga>();

        try {
            JSONObject baseJsonObject = new JSONObject(JSONResponse);

            // If JSON response has error object, return null List<Book> object
            if (baseJsonObject.has(DohvatiKnjige.PublicKeys.JSON_ERROR_KEY)) {
                //Log.e(LOG_TAG, "Json object has error");
                bookList = null;
            }
            if (baseJsonObject.has(DohvatiKnjige.PublicKeys.JSON_ITEMS_KEY)) {
                //Log.i(LOG_TAG, "has JSON object ; items");
                JSONArray bookArray = baseJsonObject.getJSONArray(DohvatiKnjige.PublicKeys.JSON_ITEMS_KEY);
                for (int i = 0; i < bookArray.length(); i++) {

                    JSONObject bookObject = bookArray.getJSONObject(i);

                    JSONObject volumeInfo = bookObject.getJSONObject(DohvatiKnjige.PublicKeys.JSON_VOLUME_INFO_KEY);
                    String id=bookObject.getString(DohvatiKnjige.PublicKeys.JSON_BOOK_ID);

                    String title = volumeInfo.getString(DohvatiKnjige.PublicKeys.JSON_BOOK_TITLE_KEY);

                    ArrayList<Autor> authors= new ArrayList<>();
                    if (volumeInfo.has(DohvatiKnjige.PublicKeys.JSON_AUTHOR_INFO_KEY)) {
                        JSONArray authorsArray = volumeInfo.getJSONArray(DohvatiKnjige.PublicKeys.JSON_AUTHOR_INFO_KEY);
                        String author = new String();
                        for (int j = 0; j < authorsArray.length(); j++) {

                            author=(authorsArray.getString(j));
                            Log.i("autor", author);
                            authors.add(new Autor(author, id));
                        }
                    } else {
                        authors = null;
                    }

                    String description;
                    if (volumeInfo.has(DohvatiKnjige.PublicKeys.JSON_BOOK_DESCRIPTION)) {
                        description = volumeInfo.getString(DohvatiKnjige.PublicKeys.JSON_BOOK_DESCRIPTION);
                        Log.i("opis", description);
                    } else {
                        description = null;
                    }

                    String publishedDate;
                    if (volumeInfo.has(DohvatiKnjige.PublicKeys.JSON_PUBLISHED_DATE_KEY)) {
                        publishedDate = volumeInfo.getString(DohvatiKnjige.PublicKeys.JSON_PUBLISHED_DATE_KEY);
                        Log.i("datum", publishedDate);
                    } else {
                        publishedDate = null;
                    }

                    int pageNumber;
                    if(volumeInfo.has(DohvatiKnjige.PublicKeys.JSON_BOOK_PAGE_COUNT)) {
                        pageNumber=volumeInfo.getInt(DohvatiKnjige.PublicKeys.JSON_BOOK_PAGE_COUNT);
                        Log.i("brStr", ""+pageNumber);
                    } else {
                        pageNumber= 0;
                    }

                    URL thumbnailUrlString = null;
                    if (volumeInfo.has(DohvatiKnjige.PublicKeys.JSON_IMAGE_LINK_KEY)) {
                        JSONObject imageLinks = volumeInfo.getJSONObject(DohvatiKnjige.PublicKeys.JSON_IMAGE_LINK_KEY);
                        try{
                            thumbnailUrlString = new URL(imageLinks.getString(DohvatiKnjige.PublicKeys.JSON_SMALL_THUMBNAIL_KEY));
                        } catch (MalformedURLException h)
                        {
                            Log.i("imageSourceUrl", h+"");
                        }
                    }


                    assert bookList != null;
                    Knjiga mBook=new Knjiga();
                    mBook.setIdWebServisa(id);
                    mBook.setNaziv(title);
                    mBook.setAutori(authors);
                    mBook.setDatumObjavljivanja(publishedDate);
                    mBook.setOpis(description);
                    mBook.setSlika(thumbnailUrlString);
                    bookList.add(mBook);
                    //Log.i("mBook", mBook.getId());
                    //Log.i("mBook", mBook.getNaziv());
                    //Log.i("mBook", mBook.getOpis());
                    //Log.i("mBook", mBook.getDatumObjavljivanja());
                    //DataHolder.LK.add(new Knjiga(id, title, authors, description, publishedDate, thumbnailUrlString, pageNumber));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem generating JSON object : extractFeaturesOfJSON() block", e);
        }
        return bookList;
    }

    @Override
    protected void onPostExecute(ArrayList<Knjiga> knjige) {
        super.onPostExecute(knjige);
        iDohvati.onNajnovijeDone(knjige);
    }
    public interface IDohvatiNajnovijeDone {
        void onNajnovijeDone(List<Knjiga> knjige);
    }
    public class PublicKeys {

        public static final String JSON_ERROR_KEY = "error";
        public static final String JSON_ITEMS_KEY = "items";
        public static final String JSON_VOLUME_INFO_KEY = "volumeInfo";
        public static final String JSON_BOOK_ID = "id";
        public static final String JSON_BOOK_TITLE_KEY = "title";
        public static final String JSON_AUTHOR_INFO_KEY = "authors";
        public static final String JSON_PUBLISHED_DATE_KEY = "publishedDate";
        public static final String JSON_BOOK_DESCRIPTION="description";
        public static final String JSON_BOOK_PAGE_COUNT="pageCount";

        public static final String JSON_IMAGE_LINK_KEY = "imageLinks";
        public static final String JSON_SMALL_THUMBNAIL_KEY = "smallThumbnail";

        /*public static final String SEARCH_TYPE_KEY = "search_type";
        public static final String SEARCH_API_INTITLE_KEY = "intitle";
        public static final String SEARCH_API_INAUTOR_KEY = "inauthor";*/


        private PublicKeys() {

        }
    }

}