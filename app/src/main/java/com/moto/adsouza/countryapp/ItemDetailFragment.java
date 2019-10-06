package com.moto.adsouza.countryapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.moto.adsouza.countryapp.content.ContentManager;
import com.moto.adsouza.countryapp.worker.LoadCountryDetailsTask;

public class ItemDetailFragment extends Fragment {

    private static final String TAG = ItemDetailFragment.class.getSimpleName();
    public static final String ARG_ITEM_ID = "item_id";
    private ContentManager.Country mItem;
    private Handler mHandler;
    private View mCountryDetailsView;
    public static int UPDATE_COUNTRY_DETAILS = 1;

    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startHandler();
        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mItem = ContentManager.getCountry(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.getName());
        }
        FloatingActionButton fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.isNetworkConnectionAvailable(activity)) {
                    Snackbar.make(view, "Updating info", Snackbar.LENGTH_SHORT).show();
                    loadCountryInfo();
                } else {
                    Toast.makeText(activity, R.string.network_unavailable,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void startHandler() {
        Log.d(TAG, "start detail activity handler");
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        Looper looper = handlerThread.getLooper();
        mHandler = new Handler(looper, message -> {
            if (UPDATE_COUNTRY_DETAILS == message.what) {
                CountryInfoTask countryInfo = (CountryInfoTask) message.obj;
                if (Status.SUCCESS == countryInfo.mStatus) {
                    updateCountyDetail(countryInfo.mCountryDetails);
                } else {
                    Log.d(TAG, " Failuer ");
                    CharSequence text = "Something gone wrong. Make sure you have internet " +
                            "connection.";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(getContext(), text, duration);
                    toast.show();
                }
            }
            return true;
        });
    }

    public static class CountryInfoTask {
        public ContentManager.CountryDetails mCountryDetails;
        public Status mStatus;

        public CountryInfoTask(ContentManager.CountryDetails contryDetails, Status status) {
            mCountryDetails = contryDetails;
            mStatus = status;
        }
    }

    private void updateCountyDetail(ContentManager.CountryDetails countryDetails) {
        if (getActivity() == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isRemoving()) {
                    setText(mCountryDetailsView.findViewById(R.id.contry_name_txt),
                            countryDetails.getCountry().getName());
                    setText(mCountryDetailsView.findViewById(R.id.cap_txt_val),
                            countryDetails.getCapital());
                    setText(mCountryDetailsView.findViewById(R.id.area_txt_val),
                            countryDetails.getArea());
                    setText(mCountryDetailsView.findViewById(R.id.pop_txt_val),
                            countryDetails.getPopulation());
                    setText(mCountryDetailsView.findViewById(R.id.reg_txt_val),
                            countryDetails.getRegion());
                    setText(mCountryDetailsView.findViewById(R.id.subreg_txt_val),
                            countryDetails.getSubRegion());
                }
            }
        });
    }

    private void setText(TextView textView, String text) {
        textView.setText(text);
        if (LoadCountryDetailsTask.CountryInfo.INFO_UNAVAILABLE.equals(text)) {
            textView.setTextColor(Color.RED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mCountryDetailsView = inflater.inflate(R.layout.country_details, container, false);
        loadCountryInfo();

        return mCountryDetailsView;
    }

    private void loadCountryInfo() {
        LoadCountryDetailsTask task = new LoadCountryDetailsTask(mItem, mHandler);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public enum Status {SUCCESS, FAIL}
}
