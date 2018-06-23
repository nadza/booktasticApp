package com.example.nadza.booktastic;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GetNearbyLibrary extends AsyncTask<Object, String, String> {

    private String libraryData;
    private GoogleMap mMap;
    String url;

    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        try {
            libraryData = readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return libraryData;
    }

    @Override
    protected void onPostExecute(String s){

        List<HashMap<String, String>>libraryList;
        libraryList = parse(s);

        showNearbyPlaces(libraryList);
    }

    private void showNearbyPlaces(List<HashMap<String, String>> libraryList)
    {
        for(int i = 0; i < libraryList.size(); i++)
        {
            MarkerOptions mOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = libraryList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble( googlePlace.get("lat"));
            double lng = Double.parseDouble( googlePlace.get("lng"));

            LatLng latLng = new LatLng( lat, lng);
            mOptions.position(latLng);
            mOptions.title(placeName + " : "+ vicinity);
            mOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(mOptions);

        }
    }

    public String readUrl(String myUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(myUrl);
            urlConnection=(HttpURLConnection) url.openConnection();
            urlConnection.connect();

            iStream = urlConnection.getInputStream();
            BufferedReader buffRead = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sbuff = new StringBuffer();

            String line = "";
            while((line = buffRead.readLine()) != null)
            {
                sbuff.append(line);
            }

            data = sbuff.toString();
            buffRead.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private HashMap<String, String> getLibrary(JSONObject googlePlaceJson)
    {
        HashMap<String, String> libraryMap = new HashMap<>();
        String libraryName = "";
        String libraryVicinity= "";
        String libraryLatitude= "";
        String libraryLongitude="";
        String reference="";

        try {
            if (!googlePlaceJson.isNull("name")) {
                libraryName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                libraryVicinity = googlePlaceJson.getString("vicinity");
            }

            libraryLatitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            libraryLongitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

            reference = googlePlaceJson.getString("reference");

            libraryMap.put("place_name", libraryName);
            libraryMap.put("vicinity",libraryVicinity);
            libraryMap.put("lat", libraryLatitude);
            libraryMap.put("lng", libraryLongitude);
            libraryMap.put("reference", reference);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return libraryMap;

    }
    private List<HashMap<String, String>>getlibrarys(JSONArray jsonArray)
    {
        int count = jsonArray.length();
        List<HashMap<String, String>> librarylist = new ArrayList<>();
        HashMap<String, String> libraryMap = null;

        for(int i = 0; i<count;i++)
        {
            try {
                libraryMap = getLibrary((JSONObject) jsonArray.get(i));
                librarylist.add(libraryMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return librarylist;
    }

    public List<HashMap<String, String>> parse(String jsonData)
    {
        JSONArray jArray = null;
        JSONObject jObject;
        try {
            jObject = new JSONObject(jsonData);
            jArray = jObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getlibrarys(jArray);
    }
} 