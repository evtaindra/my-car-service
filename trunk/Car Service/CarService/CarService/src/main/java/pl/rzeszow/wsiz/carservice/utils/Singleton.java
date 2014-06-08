package pl.rzeszow.wsiz.carservice.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;
import pl.rzeszow.wsiz.carservice.utils.async.AsyncPerformer;
import pl.rzeszow.wsiz.carservice.utils.async.RequestMethod;
import pl.rzeszow.wsiz.carservice.utils.async.TaskCallback;

/**
 * Objekt pozwalający na utrzymanie polączenia między żądaniem a widokiem.
 * <p>
 * Objekt pozwalający na utrzymanie polączenia między żądaniem a
 * widokiem nie zależnie od zmiany orientacji widoku.
 * </p>
 */
public class Singleton implements TaskCallback {
    private static String TAG = "Singleton";
    private AsyncPerformer mTask;               //!< Objekt wykonawcy żądań
    private ClientListener clientListener;      //!< Objekt do wykonania callback`ow

    private static Singleton singletonInstance; //!< Objekt utrzymujący instancję Singleton jedną na calą aplikację
    public int userID;                          //!< ID użytkownuka po udanym zalogowaniu

    /**
     * Konstruktor klasy
     * <p>
     * Prywatny żeby zabiezpieczyc od wielokrotnego tworzenia innymi klasami
     * </p>
     */
    private Singleton() {
    }

    /**
     * Otrzymanie instancji Singleton`u
     * @return object Singleton
     */
    public static Singleton getSingletonInstance() {
        if (null == singletonInstance) {
            singletonInstance = new Singleton();
            Log.d(TAG, "Creating new instance");
        }
        return singletonInstance;
    }

    /**
     * Ustalenie widoku do ktorego będą zwracane dane
     * @param l widok implementujacy ClientListener
     */
    public void setClientListener(ClientListener l) {
        if (!(l instanceof ClientListener)) {
            throw new IllegalStateException("Must implement the ActivityListener interface.");
        }
        clientListener = l;
    }

    /**
     * Wyslanie żadanie do stworzenia nowego użytkownika
     * @param params lista parametrow żądania
     */
    public void createNewUser(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.REGISTER_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);

    }
    /**
     * Wyslanie żadanie do aktualizacji danych użytkownika
     * @param params lista parametrow żądania
     */
    public void updatePersonalData(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.UPDATE_PERSONAL_DATA_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);

    }

    /**
     * Wyslanie żadanie do pobierania listy samochodow użytkownika
     * @param params lista parametrow żądania
     */
    public void getUserCars(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SELECT_USER_CAR_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do logowania użytkownika
     * @param params lista parametrow żądania
     */
    public void attemptLogin(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.LOGIN_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);

    }

    /**
     * Wyslanie żadanie do stworzenia nowego serwisu
     * @param params lista parametrow żądania
     */
    public void createNewService(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SERVICE_REGISTER_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do aktualizacji danych o serwisie
     * @param params lista parametrow żądania
     */
    public void updateServiceInfo(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.UPDATE_SERVICE_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do stworzenia nowego samochodu
     * @param params lista parametrow żądania
     */
    public void addNewCar(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.ADD_NEW_CAR_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do aktualizacji danych o samochodzie
     * @param params lista parametrow żądania
     */
    public void updateCar(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.UPDATE_CAR_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do usunięcia samochodu
     * @param params lista parametrow żądania
     */
    public void deleteCar(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.DELETE_CAR_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do usunięcia serwisu
     * @param params lista parametrow żądania
     */
    public void deleteService(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.DELETE_SERVICE_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do pobrania wszystkich serwisow zarejestrownych w systemie
     * @param params lista parametrow żądania
     */
    public void getAllServices(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SERVICES_URL, RequestMethod.GET_FROM_URL);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do pobrania danych użytkownika
     * @param params lista parametrow żądania
     */
    public void getPersonalData(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SELECT_PERSONAL_DATA_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do pobrania listy serwisow uzytkownika
     * @param params lista parametrow żądania
     */
    public void getUserServices(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SELECT_USER_SERVICE_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do pobrania szczegolowych informacji o samochodzie
     * @param params lista parametrow żądania
     */
    public void getCarInfo(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SELECT_CAR_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do pobrania szczegolowych informacji o serwisie
     * @param params lista parametrow żądania
     */
    public void getServiceDetails(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SELECT_SERVICE, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do wystawienia oceny serwisu
     * @param params lista parametrow żądania
     */
    public void rateService(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.RATE_SERVICE, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do wyslania wiadomosci
     * @param params lista parametrow żądania
     */
    public void sendMessage(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SEND_MESSAGE, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do pobrania listy rozmow użytkownika
     * @param params lista parametrow żądania
     */
    public void getUserConversations(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SELECT_USER_CONVERSATIONS, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Wyslanie żadanie do pobrania listy wiadomosci rozmowy
     * @param params lista parametrow żądania
     */
    public void getConversation(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.SELECT_CONVERSATION, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    /**
     * Sprawdzenie czy nie smartfon jest polączony z internetem
     * @param context
     * @return stan polączenia
     */
    public static Boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

    /**
     * Przerwanie wykonania bierzącego żądania
     */
    public void cancelCurrentTask() {
        mTask.cancel(true);
    }

    @Override
    public void onPreExecute() {
        clientListener.onRequestSent();
    }

    @Override
    public void onPostExecute(JSONObject result) {
        clientListener.onDataReady(result);
    }

    @Override
    public void onCancelled() {
        clientListener.onRequestCancelled();
    }

}
