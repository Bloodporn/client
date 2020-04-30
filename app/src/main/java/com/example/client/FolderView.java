package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FolderView extends AppCompatActivity {

    RecyclerView recyclerView;
    private RecyclerAdapter recadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_view);
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
        recyclerView=findViewById(R.id.review);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridLayoutManager);
        recadapter=new RecyclerAdapter(files,this);
        recyclerView.setAdapter(recadapter);
    }
}
