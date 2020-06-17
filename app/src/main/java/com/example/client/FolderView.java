package com.example.client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.view.MenuItem;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Objects;
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
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;


public class FolderView extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerAdapter recadapter;
    SqlService sql;
    static boolean isActive1=true;
    static boolean isActive2=false;
    FloatingActionButton fab;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_view);
        sql = new SqlService(this);



        final ActionBar actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.navigbar)));

        Window window=this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ExternalFunc.setStatusBarGradiant(this,R.color.navigbar);

        Random rng=new Random();
        final ArrayList<FileData> files= new ArrayList<>();
        files.add(new FileData(0,"music.mp3",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(0,"mudassic.mp3",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(0,"musdsadic.mp3",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(0,"music.mp3",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(1,"musasdic",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(1,"muszxic",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(0,"music.jpg",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(0,"music.doc",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(0,"music.doc",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(0,"muaaaaasic.png",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        files.add(new FileData(0,"muaaaaasic.png",System.currentTimeMillis(),"parentIsNigga",1,123456,"aloGdeDengi?"));
        sql.insertData(files);






        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_bar);
        final View all = findViewById(R.id.home);
        final View fav = findViewById(R.id.important);
        bottomNavigationView.setSelectedItemId(R.id.home);
        all.setAlpha(1);
        fav.setAlpha(0.8f);
        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.navigbar));

        recyclerView=findViewById(R.id.review);
        int resId1 = R.anim.anim1_layout;
        int resId2 = R.anim.anim2_lauout;
        final LayoutAnimationController animation1 = AnimationUtils.loadLayoutAnimation(this,resId1);
        final LayoutAnimationController animation2 = AnimationUtils.loadLayoutAnimation(this,resId2);


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:{
                        if(!isActive1){
                            all.setAlpha(1);
                            fav.setAlpha(0.8f);
                            recadapter.swithcData(files);
                            recyclerView.setLayoutAnimation(animation2);
                            recyclerView.scheduleLayoutAnimation();
                            actionBar.setTitle("All of your data");
                            isActive2=false;
                            isActive1=true;
                            return true;
                        }
                        return false;
                    }
                    case R.id.important:{
                        if(!isActive2){
                            all.setAlpha(0.8f);
                            fav.setAlpha(1);
                            recadapter.swithcData(files);
                            recyclerView.setLayoutAnimation(animation1);
                            recyclerView.scheduleLayoutAnimation();
                            actionBar.setTitle("All of important your data");
                            isActive1=false;
                            isActive2=true;
                            return true;
                        }
                       return false;
                    }
                }
                return false;
            }
        });





        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recadapter=new RecyclerAdapter(files,this);
        recyclerView.setAdapter(recadapter);
        fab= findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("*/*");
                    i.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(Intent.createChooser(i,"Choose directory"), 9999);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fab.show();
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy>0 || dx<0 && fab.isShown()){
                    fab.hide();
                }
            }

        });



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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 9999:
                Uri uri = data.getData();


                assert uri != null;
                Toast.makeText(this, uri.getPath(),Toast.LENGTH_SHORT).show();
                break;
        }
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
