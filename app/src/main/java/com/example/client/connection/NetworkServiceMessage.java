package com.example.client.connection;

import android.nfc.Tag;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.client.dataclient.DataClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkServiceMessage extends GetData {

    private final Request request;

    public NetworkServiceMessage(Request request) {
        this.request = request;
    }

    @Override
    protected final Response doInBackground(Void... voids) {
        Response response = new Response(request.getCode());
        Log.i("ASYNCtSK","in back");
        try (Socket socket = new Socket(DataClient.SERVER, DataClient.PORT);
             DataOutputStream oos = new DataOutputStream(socket.getOutputStream());
             DataInputStream ois = new DataInputStream(socket.getInputStream()))
        {
            Log.i("ASYNCtSK","in back123");
            //Если скокет подсоединен, то отправляем сообщение и принимаем
            if (!socket.isOutputShutdown()) {
                outMessage(oos,request);
                response = inMessage(ois, request.getCode());
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            response.setCode(999);
        }
        return response;
    }
}
