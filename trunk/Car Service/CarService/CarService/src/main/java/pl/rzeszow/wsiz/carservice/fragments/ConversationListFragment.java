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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.rzeszow.wsiz.carservice.R;
import pl.rzeszow.wsiz.carservice.activity.Conversation;
import pl.rzeszow.wsiz.carservice.adapters.ConversationListAdapter;
import pl.rzeszow.wsiz.carservice.model.BaseListItem;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.model.User;
import pl.rzeszow.wsiz.carservice.utils.ClientListener;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.json.JSONInterpreter;

/**
 * Służy do zarządzania wiadomościami
 */
public class ConversationListFragment extends Fragment implements
        ClientListener,
        ExpandableListView.OnChildClickListener,
        SwipeRefreshLayout.OnRefreshListener {

    private String TAG = "ConversationListFragment";

    private Context mContext;//!< służy do przytrzymania activity
    private ProgressDialog pDialog;//!< dialog z wskaźnikiem postępu wyświetlenia serwisu

    private ArrayList<BaseListItem> conversations;//!< lista zawierająca rozmowy.
    private ExpandableListView conversationsListView;//!<widok, który pokazuje elementy w pionowej dwupoziomowej liście .
    private static ConversationListAdapter conversationsListAdapter;//!< statyczny objekt ConversationListAdapter
    private SwipeRefreshLayout swipeRefreshLayout;//!< służy do odświeżania zawartości widoku poprzez pionowe machnięcie

    private boolean isListLoaded = false;//!<czy lista jest załadowana
    /**
     * Wywoływane, gdy fragment po raz pierwszy jest dołączony do jego activity.
     * @param activity activity fragmenta
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }
    /**
     *  Wywoływane, gdy aktywność zaczyna.
     * <p>
     *   tworzenie adaptera dla wysyłania wiadomości
     * </p>
     * @param savedInstanceState Po zamknięciu jeśli działalność jest ponownie inicjowana, Bundle
     *                           zawiera ostatnio dostarczone dane. W przeciwnym razie jest null
     *
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conversationsListAdapter = new ConversationListAdapter(mContext);
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
        View rootView = inflater.inflate(R.layout.fragment_converslist, container, false);
        Log.d(TAG, "onCreateView");

        conversationsListView = (ExpandableListView) rootView.findViewById(R.id.conversationList);
        conversationsListView.setAdapter(conversationsListAdapter);
        conversationsListView.setOnChildClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(R.color.color1, R.color.color2, R.color.color1, R.color.color2);

        return rootView;
    }

    /**
     * Ustawienie wskazówki na temat tego, czy fragment UI jest obecnie widoczny dla użytkownika.
     * <p>
     *     Załadowanie listy rozmów i jeżeli Singleton jest online pobieranie
     *     rozmów użytkownika po id
     * </p>
     * @param isVisibleToUser true, jeśli fragment jest aktualnie widoczny dla użytkownika (domyślnie), false jeśli nie jest.
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isListLoaded) {
            Log.d(TAG, "Load conversation list");

            if (Singleton.isOnline(mContext)) {
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
                Singleton.getSingletonInstance().setClientListener(this);
                Singleton.getSingletonInstance().getUserConversations(params);
            } else
                Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
        }
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
     * Wywoływana, gdy dziecko w tym rozszerzalnej lisście zostało kliknięte.
     * <p>
     *     W przypadku użytkownika lub usera ustawiamy odpowiednie id i sender
     *     i zapisujemy to w abstrakcyjny opis operacji, które mają być wykonywane
     * </p>
     * @param parent ExpandableListView gdzie zostało kliknięto
     * @param v widok w rozszerzalnej liście / ListView, który został kliknięty
     * @param groupPosition Pozycja grupy, która zawiera dziecka, który został kliknięty
     * @param childPosition Pozycja dziecka w grupie
     * @param id Id dziecka, który został kliknięty
     * @return True, jeśli kliknięcie było obsługiwane
     */
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        BaseListItem item = conversationsListAdapter.getChild(groupPosition, childPosition);
        int userID = 0, serviceID = 0, sender = 0;
        if (item instanceof User) {
            sender = 2;
            userID = (int) id;
            serviceID = (int) conversationsListAdapter.getGroupId(groupPosition);

        } else if (item instanceof Service) {
            sender = 1;
            userID = (int) conversationsListAdapter.getGroupId(groupPosition);
            serviceID = (int) id;
        }
//        Log.d(TAG, "Sender id:" + sender);
//        Log.d(TAG, "User id:" + userID);
//        Log.d(TAG, "Service id:" + serviceID);
        Intent i = new Intent(mContext , Conversation.class);
        i.putExtra("userID", userID);
        i.putExtra("serviceID", serviceID);
        i.putExtra("sender", sender);
        startActivity(i);

        return false;
    }
    /**
     * Wywoływane, gdy widok wcześniej stworzony przez onCreateView został odłączony od fragmentu.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     *  Wywoływane gdy żądanie zostało wyslane
     * <p>
     *     Tworzymy i pokazujemy dialog z wskaźnikiem postępu
     *     łądowania rozmów, Wyłączamy tryb nieokreślony dla tego okna i możliwośc
     *     anulowania okna klawiszem BACK.
     * </p>
     */
    @Override
    public void onRequestSent() {
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage(mContext.getString(R.string.loading_conversation_list));
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
    /**
     * Wywołane kiedy dane są przeanalizowane
     * <p>
     *  Usuwamy okno z ekranu i anulujemy wszelkie wizualne wskazanie odświeżenia.
     *  Parsujemy ten rezult za pomocą JSONInterpretera.
     *  Jeżeli rezult == null to znaczy że są problemy z połaczeniem do internetu,
     *  Jeżeli rezult jest pusty to znaczy że nie ma rozmów.
     *  W przeciwnym wypadku usuwamy serwisy i dodajemy nasz result do tego obiektu
     * </p>
     * @param resualt odpowiedż strony internetowej u postaci JSONobiektu
     */
    @Override
    public void onDataReady(JSONObject resualt) {
        pDialog.dismiss();
        swipeRefreshLayout.setRefreshing(false);

        conversations = JSONInterpreter.parseConversationList(resualt);
        ;

        if (conversations == null) {
            Toast.makeText(mContext, getString(R.string.alert_connection_problem), Toast.LENGTH_LONG).show();
        } else if (conversations.isEmpty()) {
            Toast.makeText(mContext, mContext.getString(R.string.alert_no_conversations), Toast.LENGTH_LONG).show();
        } else {
            conversationsListAdapter.clearData();
            conversationsListAdapter.addConversations(conversations);
            isListLoaded = true;
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
     * Odswieżanie zawartości widoku
     * <p>
     *  Jeżeli Singletone jest online pobieramy id użytkownika i wykonijemy akcję, w
     *  przeciwnym wypadku pokazujemy wiadomość sprawdź połączenie z internetem i
     *  anulujemy wszelkie wizualne wskazania odświeżenia.
     * </p>
     */
    @Override
    public void onRefresh() {
        if (Singleton.isOnline(mContext)) {
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("us_id", Integer.toString(Singleton.getSingletonInstance().userID)));
            Singleton.getSingletonInstance().setClientListener(this);
            Singleton.getSingletonInstance().getUserConversations(params);
        } else {
            Toast.makeText(mContext, R.string.alert_check_connection, Toast.LENGTH_LONG).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }
}
