package com.example.blog;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class BlogDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_details);

        // Setting data from code
        ImageView imageMain = findViewById(R.id.imageMain);
        imageMain.setImageResource(R.drawable.sydney_image);

        ImageView imageAvatar = findViewById(R.id.imageAvatar);
        imageAvatar.setImageResource(R.drawable.avatar);

        TextView textTitle = findViewById(R.id.textTitle);
        textTitle.setText("G'day from Sydney");

        TextView textDate = findViewById(R.id.textDate);
        textDate.setText("August 2, 2019");

        TextView textAuthor = findViewById(R.id.textAuthor);
        textAuthor.setText("Grayson Wells");

        TextView textRating = findViewById(R.id.textRating);
        textRating.setText("4.4");

        TextView textViews = findViewById(R.id.textViews);
        textViews.setText("(2687 views)");

        TextView textDescription = findViewById(R.id.textDescription);
        textDescription.setText("Australia is one of the most popular travel destinations in the world.");

        RatingBar ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(4.4f);

        // Image with the back icon
        ImageView imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> finish());
    }
}