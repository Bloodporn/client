package com.example.client.Files;

public class Tree {
    private TreeItem root;
    private TreeItem cur;

    public Tree() {
        root = new TreeItem(new FileData(true,"root", "25.25.25",0), null);
        cur = root;
    }

    public TreeItem getCur() {
        return cur;
    }

    public TreeItem getRoot() {
        return root;
    }

    public void setCur(TreeItem cur) {
        this.cur = cur;
    }
}
