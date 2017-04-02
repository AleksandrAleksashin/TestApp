package com.example.alex.testapp;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alex on 26.03.2017.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<Repo> list;

    public MyAdapter(List<Repo> list) {
        this.list = list;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView description;
        TextView createdAt;
        TextView updatedAt;
        TextView stargazersCount;
        TextView language;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameOrFullName);
            description = (TextView) itemView.findViewById(R.id.description);
            createdAt = (TextView) itemView.findViewById(R.id.createdAt);
            updatedAt = (TextView) itemView.findViewById(R.id.updatedAt);
            stargazersCount = (TextView) itemView.findViewById(R.id.stargazersCount);
            language = (TextView) itemView.findViewById(R.id.language);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Repo repos = list.get(position);

        if (repos.isFork())
            holder.name.setText(repos.getFullName());
        else
            holder.name.setText(repos.getName());
        holder.description.setText(repos.getDescription());
        holder.createdAt.setText(repos.getCreatedAt());
        holder.updatedAt.setText(repos.getUpdatedAt());
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////
        // по неизвестной мне причине,
        // stargazersCount.setText(repos.getStargazersCount()); приводит к остановке приложения
        Integer count = repos.getStargazersCount();
        holder.stargazersCount.setText(count.toString());
        /////////////////////////////////////////////////////////////////////////////
        /////////////////////////////////////////////////////////////////////////////
        holder.language.setText((String) repos.getLanguage());

    }

    @Override
    public int getItemCount() {
        if (list == null)
            return 0;
        return list.size();
    }
}