package com.example.gitapp;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

    public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

        private List<Rep> repoList;

        public RepoAdapter(List<Rep> repoList) {
            this.repoList = repoList;
        }

        @NonNull
        @Override
        public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_2, parent, false);
            return new RepoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
            Rep repo = repoList.get(position);
            holder.repoName.setText(repo.getName());
            holder.repoDescription.setText("Stars: " + repo.getStargazersCount() + "\n" + repo.getDescription());
        }

        @Override
        public int getItemCount() {
            return repoList.size();
        }

        static class RepoViewHolder extends RecyclerView.ViewHolder {
            TextView repoName, repoDescription;

            public RepoViewHolder(@NonNull View itemView) {
                super(itemView);
                repoName = itemView.findViewById(android.R.id.text1);
                repoDescription = itemView.findViewById(android.R.id.text2);
            }
        }
    }

