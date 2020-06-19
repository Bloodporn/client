package com.example.client.connection;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.widget.Toast;

import java.io.File;

public class DownloadFile extends NetworkServiceFileDownload {

    //file - папка
    //NameFile - название файла
    Context context;
    Notification notification;
    Notification.Builder notificationBuilder;
    NotificationManager notificationManager;
    Integer notificationID = 100;
    public DownloadFile(File file, boolean isFile, Request request, String nameFile, Context c) {
        super(file, isFile, request, nameFile);
        context=c;
    }

    //file - сразу созданный файл
    public DownloadFile(File file, boolean isFile, Request request,Context c) {
        super(file, isFile, request);
        c=context;
    }

    //Получаем прогрес загрузки
    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
        long inMoment = values[0];
        long allSize = values[1];
        notificationBuilder.setProgress((int)allSize, (int)inMoment, false);
        notification = notificationBuilder.build();
        notificationManager.notify(notificationID, notification);
    }

    //Перед загрузкой
    @Override
    protected void onPreExecute() {
        super.onPreExecute();


        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Set notification information:
        notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setOngoing(true)
                .setContentTitle("Dowload")
                .setContentText(getFile().toString())
                .setProgress(100, 0, false);
        notification = notificationBuilder.build();
        notificationManager.notify(notificationID, notification);
    }

    //Окончание загрузки
    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        //TODO
        notificationManager.cancel(notificationID);
        if (response.isValidCode()) {
            Toast.makeText(context,"Downloaded at " + getFile().toString(),Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }
    }
}
