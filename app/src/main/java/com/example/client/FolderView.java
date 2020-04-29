package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.Random;

public class FolderView extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerAdapter recadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_view);
        Random rng=new Random();
        FileData[] files = new FileData[40];
        for(int i = 0; i< files.length; i++){
            files[i]=new FileData("222222",false,rng.nextLong());
        }

        recyclerView=findViewById(R.id.review);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recadapter=new RecyclerAdapter(files);
        recyclerView.setAdapter(recadapter);
    }
}
