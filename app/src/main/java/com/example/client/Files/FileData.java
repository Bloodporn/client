package com.example.client.Files;

import androidx.annotation.NonNull;

public class FileData {

    public boolean isPapka;
    public String name;
    public String time;
    public long size;


    public FileData(boolean isPapka, String name, String time, long size) {
        this.isPapka = isPapka;
        this.name = name;
        this.time = time;
        this.size = size;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
