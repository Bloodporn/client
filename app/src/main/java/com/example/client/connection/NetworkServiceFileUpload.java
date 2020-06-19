package com.example.client.connection;

import com.example.client.dataclient.DataClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class NetworkServiceFileUpload extends GetData {
    private File file;
    protected final boolean isFile;
    private final Request request;

    public NetworkServiceFileUpload(File file, boolean isFile, Request request) {
        this.file = file;
        this.isFile = isFile;
        this.request = request;
    }

    @Override
    protected final Response doInBackground(Void... voids) {

        Response response = new Response(request.getCode());
        try (Socket socket = new Socket(DataClient.SERVER, DataClient.PORT);
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream()))
        {
            if (!socket.isOutputShutdown()) {
                if (isFile)
                {
                    outMessage(oos,request);
                    response = inMessage(ois, request.getCode());
                    if (response.isValidCode())
                    {
                        Thread.sleep(500);
                        BufferedInputStream oif = new BufferedInputStream(new FileInputStream(file));
                        outData(socket, oos, ois, oif);
                    }
                }
                else
                {
                    outMessage(oos,request);
                    response = inMessage(ois, request.getCode());
                    if (response.isValidCode())
                    {
                        publishProgress((long) 0, (long)0);
                        file = Zip(file, File.createTempFile("archive",".zip"));
                        BufferedInputStream oif = new BufferedInputStream(new FileInputStream(file));
                        Thread.sleep(500);
                        outData(socket, oos, ois, oif);
                        file.delete();
                    }
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void outData(Socket socket, DataOutputStream oos, DataInputStream ois, BufferedInputStream oif) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(oos);
        long fileSize = file.length();
        byte[] sizeas = GetData.longToBytes(fileSize);
        bos.write(sizeas);
        byte[] buffer = new byte[8192];
        int i;
        int size = 0;
        publishProgress((long) size, fileSize);
        while ( (i = oif.read(buffer)) != -1)
        {
            bos.write(buffer,0,i);
            size += i;
            publishProgress((long) size, fileSize);
        }
        bos.flush();
        publishProgress((long) size, fileSize);
        bos.close();
        ois.close();
        oos.close();
        oif.close();
        socket.close();
    }

    public File Zip(File sourceDir, File tmpFile) throws IOException {
        FileOutputStream fileOut=new FileOutputStream(tmpFile);
        ZipOutputStream zipOut = new ZipOutputStream(fileOut);
        zipOut.setMethod(ZipOutputStream.STORED);


        zipOut.setMethod(ZipOutputStream.DEFLATED);
        zipOut.setLevel(0);

        addDirectory(zipOut,sourceDir, "");

        zipOut.close();
        fileOut.close();
        return tmpFile;
    }

    private void addDirectory(ZipOutputStream zipOut, File sourceDir, String entry) throws IOException {
        for (File value : sourceDir.listFiles()) {
            if (value.isDirectory()) {
                addDirectory(zipOut, value, !entry.equals("") ? entry + "\\" + value.getName() : value.getName());
                continue;
            }

            FileInputStream fis = new FileInputStream(value);

            zipOut.putNextEntry(new ZipEntry(!entry.equals("") ? entry + "\\" + value.getName() : value.getName()));

            byte[] buffer = new byte[4048];
            int lenght = 0;
            while ((lenght = fis.read(buffer)) > 0)
                zipOut.write(buffer, 0, lenght);
            zipOut.closeEntry();
            fis.close();
        }
    }
}
