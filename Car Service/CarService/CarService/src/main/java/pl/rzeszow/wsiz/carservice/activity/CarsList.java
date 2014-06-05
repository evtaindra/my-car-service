package pl.rzeszow.wsiz.carservice.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.adapters.CarListAdapter;
import pl.rzeszow.wsiz.carservice.model.Car;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Klasa CarList
 * <p>
 *   Służy do wyświetlenia samochodów na liście
 * </p>
 */
public class CarsList extends ActionBarActivity implements ClientListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener{

    private String TAG = "CarListActivity"; //!< zmienna przyjmująca wartość string

    private CarListAdapter carListAdapter; //!< obiekt klasy CarListAdapter do wyświetlania danych o samochodzie na liście
    private ListView carListView; //!< widok, który pokazuje samochody na liście
    private SwipeRefreshLayout swipeRefreshLayout; //!< służy do odświeżania zawartości widoku poprzez pionowe machnięcie

    private ArrayList<Car> cars; //!< lista zawierająca samochody.

    private ProgressDialog pDialog; //!< dialog z wskaźnikiem postępu wyświetlenia auta

    private int userID; //!< id użytkownika

    /**
     * Wywoływane, gdy aktywność zaczyna.
     * <p>
     *    Wysyłanie wiadomości o debugowaniu, ustawienie treści do widoku,
     *    tworzenie adaptera.  Jeżeli intencję, która rozpoczęła tę działalność
     *  nie jest pusta pobieramy id uzytkownika. Ustawiamy  widok, który pokazuje samochody na liście,
     *  adapter, kliknięcie na samochód, i odświeżanie zawartości widoku i listener do niego
     * </p>
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");

        setContentView(R.layout.cralist);

        carListAdapter = new CarListAdapter(this);

        if (getIntent() != null)
            userID = getIntent().getExtras().getInt(Constants.USER_ID);

        carListView = (ListView) findViewById(R.id.carList);
        carListView.setAdapter(carListAdapter);
        carListView.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeRefreshLayoutCar);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color1, R.color.color2);

    }

    /**
     * Pobieranie samochodów użytkownika
     * <p>
     *     Tworzymy nową listę z kluczem i wartością i dodajemy  do niej patametr id użytkownika.
     *     Jeżeli Singleton jest online, wykonujemy pobiranie samochodów u zytkownika,
     *     w przeciwnym razie pokazujemy wiadomość sprawdź połączenie z internetem.
     * </p>
     */
    private void getUsersCars(){
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("us_id", Integer.toString(userID)));

        if (Singleton.isOnline(this)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getUserCars(params);
        } else
            Toast.makeText(this, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
    }

    /**
     * Inicjalizacja zawartości menu.
     * <p>
     * Tworzymy wystąpienia XML plików w menu objektach i
     * i tworzymy hierarchię menu z określonego XML zasobu.
     * </p>
     * @param menu menu, w którym można umieścić Opcje.
     * @return true dla wyświetlenia menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_servicelist, menu);
        return true;
    }

    /**
     * Jest wywoływana, kiedy został wybrany element z menu
     * <p>
     *     Pobieramy id tego elementa, jeżeli to jest dodanie nowego auta
     *     zaczynamy nowe activity dla dodania auta
     * </p>
     * @param item element menu, który został wybrany.
     * @return false aby umożliwić normalne menu dla kontynuacji przetwarzania,
     * true aby je konsumować.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add_new_service){
            this.startActivity(new Intent(this, AddNewCar.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Wywoływane dla rozpoczęcia interakcji z użytkownikiem.
     * <p>
     *     pobieramy auta użytkownika
     * </p>
     */
    @Override
    protected void onResume() {
        super.onResume();
        getUsersCars();
    }

    /**
     * Odswieżanie zawartości widoku
     * <p>
     *     pobieramy auta użytkownika i
     *     anulujemy wszelkie wizualne wskazanie odświeżenia.
     * </p>
     */
    @Override
    public void onRefresh() {
        getUsersCars();
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * Kliknięcie na element widoku
     * <p>
     * tworzenie intentu dla komunikowania sie z obsługą w tle
     * i dodanie rozszerzonych danych czyli id auta do intencji
     * i rozpoczęcie nowego activity
     * </p>
     * @param parent adapter gdzie było kliknięcie
     * @param view widok adaptera, który został kliknięty
     * @param position pozycja widoku
     * @param id Id elementu, który został kliknięty.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(this, CarDetails.class);
        i.putExtra(Constants.CAR_ID, id);
        this.startActivity(i);
    }

    /**
     * Wywoływane gdy żądanie zostało wyslane
     * <p>
     *     Jezeli w zbiorze danych nie ma samochodów,tworzymy i pokazujemy dialog z wskaźnikiem postępu
     *     łądowania aut, Wyłączamy tryb nieokreślony dla tego okna i możliwośc
     *     anulowania okna klawiszem BACK.
     * </p>
     */
    @Override
    public void onRequestSent() {
//      swipeRefreshLayout.setRefreshing(true);
        if (carListAdapter.getCount() == 0) {
            pDialog = new ProgressDialog(this);
            pDialog.setMessage(getString(R.string.loading_cars));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                /**
                 * Metoda onCancel
                 * <p>
                 *    Anulowanie wykonania żądania
                 * </p>
                 *
                 * @param dialog interfejs dialogu
                 */
                @Override
                public void onCancel(DialogInterface dialog) {
                    Singleton.getSingletonInstance().cancelCurrentTask();
                }
            });
            pDialog.show();
        }
    }

    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno z ekranu i anulujemy wszelkie wizualne wskazanie odświeżenia.
     *  Parsujemy ten rezult za pomocą JSONInterpretera.
     *  Jeżeli rezult == null to znaczy że są problemy z połaczeniem do internetu,
     *  Jeżeli rezult jest pusty to znaczy że nie ma u danego użytkownika samochodów.
     *  W przeciwnym wypadku usuwamy auta i dodajemy nasz result do tego obiektu
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);

        cars = JSONInterpreter.parseCarList(resualt);

        if (cars == null) {
            Toast.makeText(this, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        } else if (cars.isEmpty()) {
            Toast.makeText(this, getString(R.string.alert_no_cars), Toast.LENGTH_LONG).show();
        } else {
            carListAdapter.clearData();
            carListAdapter.addCars(cars);
        }
    }

    /**
     * Usunięcie okna z ekranu i i
     *     anulowanie wszelkich wizualnych wskazań odświeżenia.
     */
    @Override
    public void onRequestCancelled() {
        if (pDialog != null)
            pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }
}
