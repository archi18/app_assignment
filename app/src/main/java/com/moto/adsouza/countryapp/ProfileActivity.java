package com.moto.adsouza.countryapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.moto.adsouza.countryapp.content.ContentManager;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ContentManager mContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = new ContentManager(this);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edu_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("My Profile");
        View recyclerView = findViewById(R.id.edu_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new EducationRecyclerViewAdapter(this, mContent.getEducationList()));
    }

    private void setupRecyclerView2(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ItemListActivity.SimpleItemRecyclerViewAdapter(this, mContent.getCountryList(), false));
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static class EducationRecyclerViewAdapter
            extends RecyclerView.Adapter<EducationRecyclerViewAdapter.ViewHolder> {

        private final AppCompatActivity mParentActivity;
        private final List<ContentManager.Education> mValues;

        EducationRecyclerViewAdapter(AppCompatActivity parent,
                                      List<ContentManager.Education> items) {
            mValues = items;
            mParentActivity = parent;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.education_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final EducationRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mColNameView.setText(mValues.get(position).getCollName());
            holder.mDurView.setText(mValues.get(position).getGradYear());
            holder.mDegreeView.setText(mValues.get(position).getLevel());
            holder.mMajorView.setText(mValues.get(position).getMajor());

            holder.itemView.setTag(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mColNameView;
            final TextView mDurView;
            final TextView mDegreeView;
            final TextView mMajorView;

            ViewHolder(View view) {
                super(view);
                mColNameView = (TextView) view.findViewById(R.id.col_title_txt);
                mDurView = (TextView) view.findViewById(R.id.dur_txt);
                mDegreeView = (TextView) view.findViewById(R.id.degree_txt);
                mMajorView = (TextView) view.findViewById(R.id.major_txt);
            }
        }
    }
}
