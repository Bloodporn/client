package com.example.client.dataclient;

import java.util.regex.Pattern;

public class DataClient {
    public static String login;
    public static String password;
    public static boolean isAutoEnter;
    public static boolean isSavedPassword;
    public static String email;
    public static String tree;
    public static long storageAll;
    public static long storageFill;

    public static final String SERVER = "192.168.1.115";
    public static final int PORT = 24571;

    public static void parseTreeFromResponse(String response)
    {
        Pattern pattern = Pattern.compile("//");
        String[] strings = pattern.split(response);
        if (strings.length > 2) return;
        pattern = Pattern.compile("/");
        String[] storage = pattern.split(strings[0]);
        DataClient.storageAll = Long.parseLong(storage[0]);
        DataClient.storageFill = Long.parseLong(storage[1]);
        if (strings.length == 2)
            DataClient.tree = strings[1];
    }
}
