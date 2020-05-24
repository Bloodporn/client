package com.example.client;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Random;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.SearchView;

import androidx.recyclerview.widget.DividerItemDecoration;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.recyclerview.widget.RecyclerView;



import android.os.Bundle;

import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;


public class FolderView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter recadapter;

    static boolean isActive=false;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_view);




        ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navigbar)));

        Window window=this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ExternalFunc.setStatusBarGradiant(this,R.color.navigbar);

        Random rng=new Random();
        ArrayList<FileData> files= new ArrayList<>();
        files.add(new FileData("music.mp3",false,123));
        files.add(new FileData("papka",false,123));
        files.add(new FileData("papka2",false,123));
        files.add(new FileData("shakal.jpg",false,123));
        files.add(new FileData("shakal.png",false,123));
        files.add(new FileData("kyrsak.docx",false,123));
        files.add(new FileData("kyrsak.pdf",false,123));
        files.add(new FileData("shakal.flac",false,123));
        files.add(new FileData("shakal.png",false,123));
        files.add(new FileData("kyrsak.docx",false,123));
        files.add(new FileData("kyrsak.pdf",false,123));
        files.add(new FileData("shakal.flac",false,123));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.navigbar));

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:{
                        return true;
                    }
                    case R.id.important:{
                        Intent i = new Intent(getApplicationContext(), FolderViewFav.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                        startActivity(i);
                        //overridePendingTransition(R.anim.left_out,R.anim.left_out);

                        //{Хз нужена ли еще одна активность под избранное или просто ребилднем рекуклер как вариант

                        break;
                    }
                }
                return false;
            }
        });



        recyclerView=findViewById(R.id.review);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recadapter=new RecyclerAdapter(files,this);
        recyclerView.setAdapter(recadapter);
    }
    SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recadapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }








/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                recadapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }
    */

}
