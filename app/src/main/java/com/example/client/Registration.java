package com.example.client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    TextView login,password,email;
    Button registration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration2);



        getSupportActionBar().hide();
        ExternalFunc.setStatusBarGradiant(this);

        RotateAnimation rotateAnimation = new RotateAnimation(360f, 0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        findViewById(R.id.BOLT2).startAnimation(rotateAnimation);


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

                if(log.equals("")){

                    Toast.makeText(Registration.this,"Bad login",Toast.LENGTH_SHORT).show();
                } else{
                    if(pass.equals("")){
                        Toast.makeText(Registration.this,"Bad pass",Toast.LENGTH_SHORT).show();
                    }else{
                        if(!isValidEmail(mail)){
                            Toast.makeText(Registration.this,"Bad mail",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(Registration.this,"Nice dick",Toast.LENGTH_SHORT).show();
                            /*
                             *<-- Registration -->
                             */

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

}
