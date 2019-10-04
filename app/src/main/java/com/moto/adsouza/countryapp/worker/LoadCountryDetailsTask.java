package com.moto.adsouza.countryapp.worker;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import com.moto.adsouza.countryapp.ItemDetailFragment;
import com.moto.adsouza.countryapp.content.ContentManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadCountryDetailsTask extends AsyncTask<Void, Void, String> {
    private static final String COUNTRY_DETAILS_GET_URL = "https://restcountries.eu/rest/v1/name/%s";
    private static String TAG = LoadCountryDetailsTask.class.getSimpleName();
    private final ContentManager.Country mCounty;
    private final Handler mHandler;


    public LoadCountryDetailsTask(ContentManager.Country countryName, Handler callBack) {
        mCounty = countryName;
        mHandler = callBack;
    }

    @Override
    protected String doInBackground(Void... voids) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Will contain the raw JSON response as a string.
        String countryString = null;

        try {
            String getUrl = String.format(COUNTRY_DETAILS_GET_URL, mCounty.getName());
            Log.d(TAG, " Archi Getting data for: " + getUrl);
            URL url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int lengthOfFile = urlConnection.getContentLength();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            countryString = buffer.toString();
            Log.d(TAG, "Archi stting found :" + countryString);

            return countryString;
        } catch (IOException e) {
            Log.e(TAG, "Archi Error ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }

        return countryString;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mCounty == null || mCounty.getName() == null) {
            mHandler.sendMessage(mHandler.obtainMessage(ItemDetailFragment.UPDATE_COUNTRY_DETAILS,
                    new ItemDetailFragment.CountryInfoTask(null,
                            ItemDetailFragment.Status.FAIL)));
        }
    }

    @Override
    protected void onPostExecute(String json) {
        super.onPostExecute(json);
        ContentManager.CountryDetails countryDetails = parseCountryDetails(json);
        if (countryDetails == null) {
            countryDetails = createEmptyContry(mCounty);
        }

        mHandler.sendMessage(mHandler.obtainMessage(ItemDetailFragment.UPDATE_COUNTRY_DETAILS,
                new ItemDetailFragment.CountryInfoTask(countryDetails,
                        ItemDetailFragment.Status.SUCCESS)));
        Log.d(TAG, "Archi : Result ready : " + json);
    }

    private ContentManager.CountryDetails parseCountryDetails(String jsonString) {
        try {
            JSONArray arry = new JSONArray(jsonString);
            JSONObject obj = arry.getJSONObject(0);
            String name = obj.optString(CountryInfo.NAME_TAG, CountryInfo.INFO_UNAVAILABLE);
            String captial = obj.optString(CountryInfo.CAPITAL_TAG, CountryInfo.INFO_UNAVAILABLE);
            String pop = obj.optString(CountryInfo.POPULATION_TAG, CountryInfo.INFO_UNAVAILABLE);
            String area = obj.optString(CountryInfo.AREA_TAG, CountryInfo.INFO_UNAVAILABLE);
            String region = obj.optString(CountryInfo.REGION_TAG, CountryInfo.INFO_UNAVAILABLE);
            String subRegion = obj.optString(CountryInfo.SUBREGION_TAG, CountryInfo.INFO_UNAVAILABLE);
            Log.d(TAG, "Archi Json Name:" + name + ", capital:" + captial + ", pop:" + pop +
                    ", area:" +area + ", region:" + region + ", subregion:" + subRegion);
            return new ContentManager.CountryDetails(mCounty, captial, pop, area, region, subRegion);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private  ContentManager.CountryDetails createEmptyContry(ContentManager.Country country) {
        return new ContentManager.CountryDetails(country, CountryInfo.INFO_UNAVAILABLE,
                CountryInfo.INFO_UNAVAILABLE, CountryInfo.INFO_UNAVAILABLE,
                CountryInfo.INFO_UNAVAILABLE, CountryInfo.INFO_UNAVAILABLE);
    }
    interface CountryInfo {
        String NAME_TAG = "name";
        String CAPITAL_TAG = "capital";
        String AREA_TAG = "area";
        String REGION_TAG = "region";
        String SUBREGION_TAG = "subregion";
        String POPULATION_TAG = "population";
        String INFO_UNAVAILABLE = "NOT AVAILABLE";
    }
}
