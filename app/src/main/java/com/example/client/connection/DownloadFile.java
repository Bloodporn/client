package com.example.client.connection;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

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
            try {
                openFile(context, new File(Environment.getDataDirectory() + File.separator + "BOLT" + File.separator + getFile()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }
    }

    public static void openFile(Context context, File url) throws IOException {
        // Create URI
        File file=url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if(url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if(url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if(url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if(url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // WAV audio file
            intent.setDataAndType(uri, "application/x-wav");
        } else if(url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if(url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if(url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if(url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if(url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if(url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
