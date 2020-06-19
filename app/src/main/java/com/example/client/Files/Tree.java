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

    public String getPath() {
        StringBuilder sb = new StringBuilder("\\");
        TreeItem item = cur;
        while (item.getParent() != null) {
            sb.insert(0, item.getValue().name);
            sb.insert(0,"\\");
            item = item.getParent();
        }
        return sb.toString();
    }
}
