package com.example.blog.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.blog.R;
import com.example.blog.http.Blog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainAdapter extends ListAdapter<Blog, MainAdapter.MainViewHolder> { //1

    private List<Blog> originalList = new ArrayList<>();

    public void setData(@Nullable List<Blog> list) {
        originalList = list;
        super.submitList(list);
    }

    public void filter(String query) {
        List<Blog> filteredList = new ArrayList<>();
        for (Blog blog : originalList) { // 1
            if (blog.getTitle().toLowerCase().contains(query.toLowerCase())) { // 2
                filteredList.add(blog);
            }
        }
        submitList(filteredList); // 3
    }

    public interface OnItemClickListener { // 1
        void onItemClicked(Blog blog);
    }

    private OnItemClickListener clickListener;

    public MainAdapter(OnItemClickListener clickListener) { // 2
        super(DIFF_CALLBACK);
        this.clickListener = clickListener;
    }

    public MainAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Blog> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Blog>() {
                @Override
                public boolean areItemsTheSame(@NonNull Blog oldData,
                                               @NonNull Blog newData) {
                    return oldData.getId() == newData.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull Blog oldData, @NonNull Blog newData) {
                    return oldData.equals(newData);
                }
            };

    public void sortByTitle() {
        List<Blog> currentList = new ArrayList<>(originalList); // 1 why not getCurrentList ?
        Collections.sort(currentList,
                (o1, o2) -> o1.getTitle().compareTo(o2.getTitle())); // 2
        submitList(currentList); // 3
    }

    public void sortByDate() {
        List<Blog> currentList = new ArrayList<>(originalList);
        Collections.sort(currentList,
                (o1, o2) -> o2.getDateMillis().compareTo(o1.getDateMillis()));
        submitList(currentList);
    }

    /**
     * this method is called only a certain amount of times when recycler view needs a new view holder object
     */
    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        // 2
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_main, parent, false);
        return new MainViewHolder(view, clickListener); // 3
    }

    /**
     * this method is called every time when we need to render a list item, or in other words bind the model to the view holder
     */
    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        // 3
        holder.bindTo(getItem(position));
    }


    static class MainViewHolder extends RecyclerView.ViewHolder {
        // 4
        private TextView textTitle;
        private TextView textDate;
        private ImageView imageAvatar;
        private Blog blog;

        MainViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            itemView.setOnClickListener(v -> listener.onItemClicked(blog));
            textTitle = itemView.findViewById(R.id.textTitle);
            textDate = itemView.findViewById(R.id.textDate);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
        }

        void bindTo(Blog blog) {
            this.blog = blog;
            textTitle.setText(blog.getTitle());
            textDate.setText(blog.getDate());

            Glide.with(itemView)
                    .load(blog.getAuthor().getAvatarURL())
                    .transform(new CircleCrop())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageAvatar);
        }
    }
}