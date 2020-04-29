package com.example.client;

public class FileData {
    private String name;
    private boolean isPapka;
    private long time;

    public FileData(String name, boolean isPapka, long time) {
        this.name = name;
        this.isPapka = isPapka;
        this.time = time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPapka(boolean papka) {
        isPapka = papka;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public boolean isPapka() {
        return isPapka;
    }

    public long getTime() {
        return time;
    }
}
