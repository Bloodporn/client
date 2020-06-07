package com.example.client;

public class FileData {

    public int isPapka;
    public String name;
    public long time;
    public String parent;
    public int fav;
    public long size;
    public String truePath;


    public FileData(int isPapka, String name, long time, String parent, int fav, long size, String truePath) {
        this.isPapka = isPapka;
        this.name = name;
        this.time = time;
        this.parent = parent;
        this.fav = fav;
        this.size = size;
        this.truePath = truePath;
    }
}
