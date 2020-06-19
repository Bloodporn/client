package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.connection.NetworkServiceMessage;
import com.example.client.connection.Request;
import com.example.client.connection.Response;
import com.example.client.dataclient.DataClient;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MainActivity extends AppCompatActivity {

    Switch myswitch;
    Button buttonLog, buttonReg;
    TextView password,login;

    AnimatorSet front,back;
    boolean isFront =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        ExternalFunc.setStatusBarGradiant(this);

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        findViewById(R.id.BOLT1).startAnimation(rotateAnimation);
        Window window=this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }


        buttonLog=findViewById(R.id.buttonlog);
        buttonReg=findViewById(R.id.textbuttonreg);

        password=findViewById(R.id.password);
        login=findViewById(R.id.login);

        buttonLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,FolderView.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
                /*String log,pass;
                log=login.getText().toString();
                pass=password.getText().toString();
                if (pass.equals("") || log.equals("")) {
                    Toast.makeText(MainActivity.this,"Заполните все поля",Toast.LENGTH_SHORT).show();
                } else {
                    DataClient.login = log;
                    DataClient.password = pass;
                    Toast.makeText(MainActivity.this, "Nice dick", Toast.LENGTH_SHORT).show();
                    Request request = new Request("AUTHORIZATION",101);
                    Authorization authorization = new Authorization(request);
                    authorization.execute();


                */
                }


        });

        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Registration.class);
                startActivity(i);
            }
        });
    }


    class Authorization extends NetworkServiceMessage {

        public Authorization(Request request) {
            super(request);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @SuppressLint("ShowToast")
        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            if (response.isValidCode()) {
                DataClient.parseTreeFromResponse(response.getText());
                Intent i = new Intent(MainActivity.this,FolderView.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(MainActivity.this,"Неправильный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
