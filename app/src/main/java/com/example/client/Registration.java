package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.client.connection.NetworkServiceMessage;
import com.example.client.connection.Request;
import com.example.client.connection.Response;
import com.example.client.dataclient.DataClient;

public class Registration extends AppCompatActivity {

    TextView login,password,email;
    Button registration;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);
        progressBar = findViewById(R.id.progressBarReg);
        progressBar.setVisibility(View.INVISIBLE);

        getSupportActionBar().hide();
        ExternalFunc.setStatusBarGradiant(this);

        RotateAnimation rotateAnimation = new RotateAnimation(360f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        findViewById(R.id.BOLT2).startAnimation(rotateAnimation);

        Window window=this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        registration=findViewById(R.id.registrbutton);


        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String log,pass,mail;
                login=findViewById(R.id.loginreg);
                password=findViewById(R.id.passreg);
                email=findViewById(R.id.emailreg);
                log=login.getText().toString();
                pass=password.getText().toString();
                mail=email.getText().toString();


                if (!isValidEmail(mail)) {
                    email.setError("Неправильные данные");
                } else {
                    if(log.equals("")){
                        login.setError("Заполните поле");
                    } else {
                        if (pass.equals("")) {
                            password.setError("Заполните поле");
                        } else {
                            DataClient.login = log;
                            DataClient.password = pass;
                            DataClient.email = mail;
                            Request request = new Request("REGISTRATION", DataClient.email,100);
                            RegistrationNetwork registrationNetwork = new RegistrationNetwork(request);
                            registrationNetwork.execute();
                        }
                    }
                }


            }
        });
    }

    public boolean isValidEmail(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }




    class RegistrationNetwork extends NetworkServiceMessage {

        public RegistrationNetwork(Request request) {
            super(request);
        }


        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            progressBar.setVisibility(View.INVISIBLE);
            if (response.isValidCode()) {
                Toast.makeText(Registration.this,"Успешная регистрация", Toast.LENGTH_SHORT).show();
                finishActivity(10);
            } else {
                Toast.makeText(Registration.this, "Этот пользователь уже существует", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }
    }
}
