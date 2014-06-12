package pl.rzeszow.wsiz.carservice.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.activity.RegisterNewService;
import pl.rzeszow.wsiz.carservice.activity.ServiceDetail;
import pl.rzeszow.wsiz.carservice.adapters.ServiceListAdapter;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 *  Służy do wyświetlenia serwisów na liście
 */
public class ServiceListFragment extends Fragment implements ClientListener,
        SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener {

    private String TAG = "ServiceListFragment";
    private Context mContext; //!< służy do przytrzymania activity
    private ProgressDialog pDialog; //!< dialog z wskaźnikiem postępu wyświetlenia serwisu

    private ArrayList<Service> services; //!< lista zawierająca serwisy.
    private ListView servicesListView; //!< widok, który pokazuje serwisy na liście
    private static ServiceListAdapter serviceListAdapter; //!< statyczny objekt ServiceListAdapter
    private SwipeRefreshLayout swipeRefreshLayout;//!< służy do odświeżania zawartości widoku poprzez pionowe machnięcie

    /**
     * Wywoływane, gdy fragment po raz pierwszy jest dołączony do jego activity.
     * @param activity activity fragmenta
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    /**
     *  Wywoływane, gdy aktywność zaczyna.
     * <p>
     *   Pobieranie activity, tworzenie adaptera z tym activity.
     *   Jeżeli Singleton z tym activity jest online, pobieramy wszystkie
     *   serwisy, w innym przypadku wyświetlamy komunikat sprawdż
     *   internet połączenie
     * </p>
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        Log.d(TAG, "onCreate");

        serviceListAdapter = new ServiceListAdapter(mContext);

        if (Singleton.isOnline(mContext)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getAllServices(null);
        } else
            Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
    }

    /**
     * Wywoływane żeby mieć instancję fragmentu w interfejsie użytkownika
     * @param inflater stosuje się do nadmuchania widoków w fragmencie
     * @param container Jeśli niezerowy, to jest widok z rodziców, do którego fragment UI powinien
     *                  być dołączony.
     * @param savedInstanceState Jeśli niezerowy, fragment ten jest zbudowany z poprzedniego nowo zapisanego stanu.
     * @return widok dla UI fragmentu albo null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_servicelist, container, false);
        Log.d(TAG, "onCreateView");

        servicesListView = (ListView) rootView.findViewById(R.id.servicesList);
        servicesListView.setAdapter(serviceListAdapter);
        servicesListView.setOnItemClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color1, R.color.color2);

        setHasOptionsMenu(true);
        return rootView;
    }

    /**
     * Wywoływane żeby zapytać fragment dla zapisania aktualnego stanu dynamicznego,
     * @param outState Pakiet, w którym stawimy zapisany stan.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * Zainicjować zawartość menu activity
     * @param menu menu, w którym można umieścić opcje.
     * @param inflater tworzenie instancji plików XML do obiektów menu.
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (Singleton.getSingletonInstance().userID != 0)
            inflater.inflate(R.menu.menu_servicelist, menu);
    }

    /**
     * Wywoływane, kiedy wybrany jest element w menu.
     * @param item Element menu, który został wybrany
     * @return false, aby umożliwić normalne przetwarzanie menu, true żeby go spożywać
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add_new_service) {
            mContext.startActivity(new Intent(mContext, RegisterNewService.class));
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Wywoływane, gdy widok wcześniej stworzony przez onCreateView został odłączony od fragmentu.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * Odswieżanie zawartości widoku
     * <p>
     *  Jeżeli Singletone jest online wykonijemy akcję, w
     *  przeciwnym wypadku pokazujemy wiadomość sprawdź połączenie z internetem i
     *  anulujemy wszelkie wizualne wskazania odświeżenia.
     * </p>
     */
    @Override
    public void onRefresh() {
        if (Singleton.isOnline(mContext)) {
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getAllServices(null);
        } else {
            Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    /**
     *
     *  Kliknięcie na element widoku
     * <p>
     * tworzenie intentu dla szczególnej informacji o serwisie
     * i dodanie rozszerzonych danych czyli id serwisu do intencji
     * i rozpoczęcie nowego activity
     * </p>
     * @param parent adapter gdzie było kliknięcie
     * @param view widok adaptera, który został kliknięty
     * @param position pozycja widoku
     * @param id Id elementu, który został kliknięty.
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(mContext, ServiceDetail.class);
        i.putExtra(Constants.SERVICE_ID, id);
        mContext.startActivity(i);
    }
    /**
     *  Wywoływane gdy żądanie zostało wyslane
     * <p>
     *     Jezeli w zbiorze danych nie ma serwisów,tworzymy i pokazujemy dialog z wskaźnikiem postępu
     *     łądowania serwisów, Wyłączamy tryb nieokreślony dla tego okna i możliwośc
     *     anulowania okna klawiszem BACK.
     * </p>
     */
    @Override
    public void onRequestSent() {
//      swipeRefreshLayout.setRefreshing(true);
        if (serviceListAdapter.getCount() == 0) {
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage(getString(R.string.loading_services));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                /**
                 * Za pomocą Singletona anulujemy bieżące zadanie
                 * @param dialog dialog interfejsu
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
     *  Jeżeli rezult jest pusty to znaczy że nie ma serwisów.
     *  W przeciwnym wypadku usuwamy serwisy i dodajemy nasz result do tego obiektu
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);

        if (resualt == null)
            Toast.makeText(mContext, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        else {
            services = JSONInterpreter.parseServiceList(resualt);
            if (services.isEmpty()) {
                Toast.makeText(mContext, getString(R.string.alert_no_services), Toast.LENGTH_LONG).show();
            } else {
                serviceListAdapter.clearData();
                serviceListAdapter.addServices(services);
            }
        }
    }
    /**
     * Usunięcie okna z ekranu i
     *     anulowanie wszelkich wizualnych wskazań odświeżenia.
     */
    @Override
    public void onRequestCancelled() {
        if (pDialog != null)
            pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);
    }
    /**
     * Aktualizacja rankingu po id serwisu
     * @param id id serwisu, ranking którego aktualizujemy
     * @param rating ocena serwisu
     */
    public static void updateServiceRating(int id, double rating) {
        serviceListAdapter.updateItemRating(id, rating);
    }
}
