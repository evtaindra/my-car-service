package pl.rzeszow.wsiz.carservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;

/**
 * @author prima
 *
 */
public class Splash extends Activity {
    ProgressBar bar;
    public int progress = 0;

    // это будет именем файла настроек
    public static final String login = "myLogin";
    public static final String login_username = "lUsername";
    public static final String login_password = "lPassword";

    SharedPreferences mLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        //getSharedPreferences
        mLogin = getSharedPreferences(login, Context.MODE_PRIVATE);

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
                    if (mLogin.contains(login_username)&&mLogin.contains(login_password)) {
                        Intent intent = new Intent(Splash.this, MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        startActivity(new Intent("pl.rzeszow.wsiz.CLEARSCREEN"));
                    }
                }
                catch (InterruptedException e) {
                    // TODO Auto-generated catch block
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