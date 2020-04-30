package com.example.client;

public class FileData {
    private String diskPath;
    private String name;
    private boolean isPapka;
    private long time;

    private FileData[] downFolder;

    /**
     *
     * @param name
     * @param isPapka
     * @param time
     */
    public FileData(String name, boolean isPapka, long time) {
        this.diskPath = name;
        this.name=diskPath.substring(diskPath.lastIndexOf('\\')+1);
        this.isPapka = isPapka;
        this.time = time;
    }
    public FileData(String name, boolean isPapka, long time,FileData[] df) {
        this.diskPath = name;
        this.isPapka = isPapka;
        this.time = time;
        downFolder=df;
    }


    public String getName() {
        return name;
    }

    public void setDiskPath(String diskPath) {
        this.diskPath = diskPath;
    }

    public void setPapka(boolean papka) {
        isPapka = papka;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDiskPath() {
        return diskPath;
    }

    public boolean isPapka() {
        return isPapka;
    }

    public long getTime() {
        return time;
    }
}
