package com.submission.moviecatalogsubmission4made.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.submission.moviecatalogsubmission4made.BuildConfig;
import com.submission.moviecatalogsubmission4made.R;
import com.submission.moviecatalogsubmission4made.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private OnItemClickCallback onItemClickCallback;

    public MovieAdapter() {}

    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setData(List<Movie> items) {
        if (items.size() > 0) {
            movies.clear();
        }
        movies.addAll(items);
        notifyDataSetChanged();
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_film, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final Movie movie = movies.get(position);

        Glide.with(holder.itemView.getContext())
                .load(BuildConfig.BASE_URL_IMG + movie.getPoster())
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .error(new ColorDrawable(Color.LTGRAY))
                .transform(new CenterCrop(), new RoundedCorners(16))
                .into(holder.imgPoster);
        holder.txtTitle.setText(movie.getTitle());
        holder.txtYear.setText(movie.getYear());
        holder.txtDescription.setText(movie.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickCallback.onItemClicked(movies.get(holder.getAdapterPosition()));
            }
        });

        holder.btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TITLE, movie.getTitle());
                intent.putExtra(Intent.EXTRA_SUBJECT, movie.getTitle());
                intent.putExtra(Intent.EXTRA_TEXT, movie.getTitle() + "\n\n" + movie.getDescription());
                holder.btnShare.getContext().startActivity(Intent.createChooser(intent,
                        holder.btnShare.getResources().getString(R.string.share)));
            }
        });
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgPoster;
        private TextView txtTitle, txtYear, txtDescription;
        private Button btnShare;

        ViewHolder(View view) {
            super(view);

            imgPoster = view.findViewById(R.id.img_poster);
            txtTitle = view.findViewById(R.id.txt_title);
            txtYear = view.findViewById(R.id.txt_year);
            txtDescription= view.findViewById(R.id.txt_description);

            btnShare = view.findViewById(R.id.btn_set_share);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(Movie data);
    }
}
