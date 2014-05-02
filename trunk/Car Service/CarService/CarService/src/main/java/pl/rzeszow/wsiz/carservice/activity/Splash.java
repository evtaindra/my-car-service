package pl.rzeszow.wsiz.carservice.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.utils.Singleton;

public class Splash extends Activity {
     private ProgressBar bar;
    public int progress = 0;

    private SharedPreferences mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //creating new Instance of singleton;
        Singleton.getSingletonInstance();

        mLogin = getSharedPreferences(Constants.LOGIN, Context.MODE_PRIVATE);

        bar = (ProgressBar) findViewById(R.id.progress);

        Thread logoTimer = new Thread() {
            public void run() {
                try {
                    int logoTimer = 0;
                    while (logoTimer < 5000) {
                        sleep(100);
                        logoTimer = logoTimer + 100;
                        bar.setProgress(progress);
                        progress = progress + 2;
                    }
                    ;
                    if (mLogin.contains(Constants.LOGIN_USER)&&mLogin.contains(Constants.LOGIN_PASSWORD)) {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        intent.putExtra(Constants.USER_ID, mLogin.getInt(Constants.LOGIN_ID,0));
                        startActivity(intent);
                    }
                    else{
                        startActivity(new Intent("pl.rzeszow.wsiz.CLEARSCREEN"));
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
}