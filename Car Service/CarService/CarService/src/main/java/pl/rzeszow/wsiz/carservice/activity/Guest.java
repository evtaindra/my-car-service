package pl.rzeszow.wsiz.carservice.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.fragments.ServiceListFragment;

/**
 * Klasa Guest
 * <p>
 * Klasa do obsługi logowania jako gośc
 * </p>
 */
public class Guest extends ActionBarActivity {
    /**
     * Wywoływane, gdy aktywność zaczyna.
     * <p>
     *     Ustawienie treści do widoku. Zamieniamy to co jest w widoku container z naszym fragmentem
     *       i zatwierdzamy transakcje.
     * </p>
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guest);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,new ServiceListFragment()).commit();
    }

    /**
     * Inicjalizacja zawartości menu.
     * <p>
     * Ttworzymy wystąpienia XML plików w menu objektach i
     * i  hierarchię menu z określonego XML zasobu.
     * </p>
     * @param menu menu, w którym można umieścić Opcje.
     * @return true dla wyświetlenia menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.global, menu);
        return true;
    }

    /**
     *  Jest wywoływana, kiedy został wybrany element z menu
     * <p>
     *     Pobieramy id klikniętego elementu, jeżeli to jest wylogowanie, usuwamy zawartość loginu
     *     i rozpoczynamy nowe activity
     * </p>
     * @param item element menu, który został wybrany.
     * @return false aby umożliwić normalne menu dla kontynuacji przetwarzania,
     * true aby je konsumować.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_logout){
            getSharedPreferences(Constants.LOGIN, Context.MODE_PRIVATE).edit().clear().commit();
            finish();
            startActivity(new Intent(Guest.this, Login.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
