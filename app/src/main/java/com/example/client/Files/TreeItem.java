package com.example.client.Files;

import java.util.ArrayList;

public class TreeItem {
    private FileData value;
    private TreeItem parent;
    private ArrayList<TreeItem> children;

    public TreeItem(FileData value, TreeItem parent) {
        this.value = value;
        this.parent = parent;
        children = new ArrayList<>();
    }

    public ArrayList<TreeItem> getChildren() {
        return children;
    }

    public FileData getValue() {
        return value;
    }

    public void setValue(FileData value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public TreeItem getParent() {
        return parent;
    }
}
