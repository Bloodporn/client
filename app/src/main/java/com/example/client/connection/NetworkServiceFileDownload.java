package com.example.client.connection;

import android.util.Log;

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

    public File getFile() {
        return file;
    }

    public boolean isFile() {
        return isFile;
    }

    public Request getRequest() {
        return request;
    }

    public String getNameFile() {
        return nameFile;
    }

    private File file;
    private final boolean isFile;
    private final Request request;
    private final String nameFile;

    public NetworkServiceFileDownload(File file, boolean isFile, Request request, String nameFile) {
        //конструктор, file - папка, nameFile - название нвоого файла
        this.file = file;
        this.isFile = isFile;
        this.request = request;
        this.nameFile = nameFile;
    }

    public NetworkServiceFileDownload(File file, boolean isFile, Request request)
    {
        //конструктор, file - уже созданный файл, кда закачивать
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
                //Отправляем запрос
                outMessage(oos,request);
                //Получаем ответ
                response = inMessage(ois, request.getCode());
                if (response.isValidCode()) { //Если ответ успешный, то передаем получаем файл
                    if (!isFile) publishProgress((long) 0, (long) 0 );
                    //Спим 500 мс для того, чтобы сервер успел начать отдавтаь данные
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //Если передалась папка и название нвоого файла, то создаем новый файл и туда загружаем
                    if (!nameFile.equals("null"))
                    {
                        file = new File(file.getAbsolutePath() + File.separator + nameFile);
                        Log.i("Create file:", Boolean.toString(file.createNewFile()));
                    }
                    //Открываем стримы
                    FileOutputStream fos=new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(ois);
                    //Считываем размер файла
                    byte[] buffer = new byte[8192];
                    byte[] sizeByte = new byte[8];
                    bis.read(sizeByte);
                    long size=GetData.bytesToLong(sizeByte);
                    long allSize = size;
                    System.out.println(allSize);
                    publishProgress(allSize - size, allSize);
                    //Получаем файл
                    while (size > 0) {
                        int i = bis.read(buffer);
                        fos.write(buffer, 0, i);
                        size-= i;
                        publishProgress(allSize - size,allSize);
                    }
                    //Закрываем стримы и сокеты
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
