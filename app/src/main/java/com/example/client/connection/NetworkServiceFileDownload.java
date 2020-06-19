package com.example.client.connection;

import com.example.client.dataclient.DataClient;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkServiceFileDownload extends GetData {

    private File file;
    private final boolean isFile;
    private final Request request;
    private final String nameFile;

    public NetworkServiceFileDownload(File file, boolean isFile, Request request, String nameFile) {
        this.file = file;
        this.isFile = isFile;
        this.request = request;
        this.nameFile = nameFile;
    }

    public NetworkServiceFileDownload(File file, boolean isFile, Request request)
    {
        super();
        this.file = file;
        this.isFile = isFile;
        this.request = request;
        this.nameFile = "null";
    }

    @Override
    protected final Response doInBackground(Void... voids) {
        Response response = new Response(request.getCode());
        try (Socket socket = new Socket(DataClient.SERVER, DataClient.PORT);
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream());)
        {
            if (!socket.isOutputShutdown()) {
                outMessage(oos,request);
                response = inMessage(ois, request.getCode());
                if (response.isValidCode()) {
                    if (!isFile) publishProgress((long) 0, (long) 0 );
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!nameFile.equals("null"))
                    {
                        file = new File(file.getAbsolutePath() + "\\" + nameFile);
                        file.createNewFile();
                    }
                    FileOutputStream fos=new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(ois);
                    byte[] buffer = new byte[8192];
                    byte[] sizeByte = new byte[8];
                    bis.read(sizeByte);
                    long size=GetData.bytesToLong(sizeByte);
                    long allSize = size;
                    System.out.println(allSize);
                    publishProgress(allSize - size, allSize);
                    while (size > 0) {
                        int i = bis.read(buffer);
                        fos.write(buffer, 0, i);
                        size-= i;
                        publishProgress(allSize - size,allSize);
                    }
                    publishProgress(allSize,allSize);
                    fos.close();
                    ois.close();
                    oos.close();
                    bis.close();
                    socket.close();
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
