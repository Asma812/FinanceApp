package com.example.fmanagement;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class homePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Vous pouvez ajouter d'autres initialisations ici si nécessaire

        // Récupération des ImageView
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView1 = findViewById(R.id.imageView1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView2 = findViewById(R.id.imageView2);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView3 = findViewById(R.id.imageView3);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView imageView4 = findViewById(R.id.imageView4);

        // Définition des clics sur les ImageView
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homePage.this, MyWallet.class));
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homePage.this, Monitoring.class));
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(homePage.this, Advice.class));
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(homePage.this, FollowWallet.class));
            }
        });
    }
}

