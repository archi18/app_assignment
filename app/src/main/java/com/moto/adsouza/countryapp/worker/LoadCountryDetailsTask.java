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
        String countryString;

        try {
            String getUrl = String.format(COUNTRY_DETAILS_GET_URL, mCounty.getName());
            Log.d(TAG, "Getting data for: " + getUrl);
            URL url = new URL(getUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int respCode = urlConnection.getResponseCode();
            Log.d(TAG, "Response code received: " + respCode);
            if (respCode >= 400) {
                Log.w(TAG, "Broken link "+ getUrl);
                return null;
            }
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            countryString = buffer.toString();
            Log.d(TAG, "return string found :" + countryString);

            return countryString;
        } catch (IOException e) {
            Log.e(TAG, "Error while getting data ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(TAG, "Error closing stream", e);
                }
            }
        }

        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mCounty == null) {
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
        Log.d(TAG, "onPostExecute Result ready : " + json);
    }

    private ContentManager.CountryDetails parseCountryDetails(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) return null;
        try {
            JSONArray arry = new JSONArray(jsonString);
            JSONObject obj = arry.getJSONObject(0);
            String name = obj.optString(CountryInfo.NAME_TAG, CountryInfo.INFO_UNAVAILABLE);
            String captial = obj.optString(CountryInfo.CAPITAL_TAG, CountryInfo.INFO_UNAVAILABLE);
            String pop = obj.optString(CountryInfo.POPULATION_TAG, CountryInfo.INFO_UNAVAILABLE);
            String area = obj.optString(CountryInfo.AREA_TAG, CountryInfo.INFO_UNAVAILABLE);
            String region = obj.optString(CountryInfo.REGION_TAG, CountryInfo.INFO_UNAVAILABLE);
            String subRegion = obj.optString(CountryInfo.SUBREGION_TAG, CountryInfo.INFO_UNAVAILABLE);
            if(captial.isEmpty()) captial = CountryInfo.INFO_UNAVAILABLE;
            if(pop.isEmpty()) pop = CountryInfo.INFO_UNAVAILABLE;
            if(area.isEmpty()) area = CountryInfo.INFO_UNAVAILABLE;
            if(region.isEmpty()) region = CountryInfo.INFO_UNAVAILABLE;
            if(subRegion.isEmpty()) subRegion = CountryInfo.INFO_UNAVAILABLE;
            Log.d(TAG, "Parse json Info { Name:" + name + ", capital:" + captial + ", pop:" + pop +
                    ", area:" +area + ", region:" + region + ", subregion:" + subRegion +"}");
            return new ContentManager.CountryDetails(mCounty,name, captial, pop, area, region, subRegion);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private  ContentManager.CountryDetails createEmptyContry(ContentManager.Country country) {
        return new ContentManager.CountryDetails(country, CountryInfo.INFO_UNAVAILABLE,
                CountryInfo.INFO_UNAVAILABLE, CountryInfo.INFO_UNAVAILABLE,
                CountryInfo.INFO_UNAVAILABLE, CountryInfo.INFO_UNAVAILABLE,
                CountryInfo.INFO_UNAVAILABLE);
    }
    public interface CountryInfo {
        String NAME_TAG = "name";
        String CAPITAL_TAG = "capital";
        String AREA_TAG = "area";
        String REGION_TAG = "region";
        String SUBREGION_TAG = "subregion";
        String POPULATION_TAG = "population";
        String INFO_UNAVAILABLE = "NOT AVAILABLE";
    }
}
