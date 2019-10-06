package com.moto.adsouza.countryapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        View eduRecyclerView = findViewById(R.id.edu_list);
        assert eduRecyclerView != null;
        setupEducationRecyclerView((RecyclerView) eduRecyclerView);

        View expRecyclerView = findViewById(R.id.exp_list);
        assert expRecyclerView != null;
        setupExperienceRecyclerView((RecyclerView) expRecyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, ItemListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupEducationRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new EducationRecyclerViewAdapter(this, mContent.getEducationList()));
    }

    private void setupExperienceRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new ExperienceRecyclerViewAdapter(this, mContent.getExperienceList()));
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
                mColNameView = (TextView) view.findViewById(R.id.comp_name_txt);
                mDurView = (TextView) view.findViewById(R.id.work_dur_txt);
                mDegreeView = (TextView) view.findViewById(R.id.pos_title_txt);
                mMajorView = (TextView) view.findViewById(R.id.loc_txt);
            }
        }
    }

    public static class ExperienceRecyclerViewAdapter
            extends RecyclerView.Adapter<ExperienceRecyclerViewAdapter.ViewHolder> {

        private final AppCompatActivity mParentActivity;
        private final List<ContentManager.Experience> mValues;

        ExperienceRecyclerViewAdapter(AppCompatActivity parent,
                                      List<ContentManager.Experience> items) {
            mValues = items;
            mParentActivity = parent;
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.experience_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ExperienceRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mCompNameView.setText(mValues.get(position).getCompName());
            holder.mDurView.setText(mValues.get(position).getDur());
            holder.mPositionView.setText(mValues.get(position).getTitle());
            holder.mLocView.setText(mValues.get(position).getLocation());

            holder.itemView.setTag(mValues.get(position));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mCompNameView;
            final TextView mDurView;
            final TextView mPositionView;
            final TextView mLocView;

            ViewHolder(View view) {
                super(view);
                mCompNameView = (TextView) view.findViewById(R.id.comp_name_txt);
                mDurView = (TextView) view.findViewById(R.id.work_dur_txt);
                mPositionView = (TextView) view.findViewById(R.id.pos_title_txt);
                mLocView = (TextView) view.findViewById(R.id.loc_txt);
            }
        }
    }
}
