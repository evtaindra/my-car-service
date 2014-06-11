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

/**
 *Służy do pokazywania logo aplikacji przy starcie
 */
public class Splash extends Activity {
     private ProgressBar bar;//!< Wizualny wskaźnik postępu otwatcia aplikacji
    public int progress = 0;

    private SharedPreferences mLogin; //!<dla dostępu i modyfikowania loginu

    /**
     * Wywoływane, gdy aktywność zaczyna.
     * <p>
     *     Ustawienie treści do widoku. Tworzenie nowej instancji Sigletone i
     *     ustawienie progressbara. W nowym wątku tworzymy się regulator czasowy
     *     i probujemy ustawić ten regulator na wskażniku postępu.
     *     Keżeli ten login zawiera imię użytkownika i hasło tworzymy nową instancję
     *     i przechodzimy na główną stronę aplikacji w innum przypadku pokazujemy pusty ekran.
     * </p>
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
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
                    while (logoTimer < 1000) {
                        sleep(100);
                        logoTimer = logoTimer + 100;
                        bar.setProgress(progress);
                        progress = progress + 10;
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