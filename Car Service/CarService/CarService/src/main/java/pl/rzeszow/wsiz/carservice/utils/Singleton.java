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
 * Created by rsavk_000 on 4/13/2014.
 */
public class Singleton implements TaskCallback {
    private static String TAG = "Singleton";
    //hold url for async task
    private AsyncPerformer mTask;
    private ClientListener clientListener;
    // Static member holds only one instance of the
    // SingletonExample class
    private static Singleton singletonInstance;
    public int userID;

    // SingletonExample prevents any other class from instantiating
    private Singleton() {
    }
    // Providing Global point of access
    public static Singleton getSingletonInstance() {
        if (null == singletonInstance) {
            singletonInstance = new Singleton();
            Log.d(TAG, "Creating new instance");
        }
        return singletonInstance;
    }

    public void setClientListener(ClientListener l) {
        if (!(l instanceof ClientListener)) {
            throw new IllegalStateException("Must implement the ActivityListener interface.");
        }
        clientListener = l;
    }

    public void createNewUser(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.REGISTER_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);

    }

    public void updatePersonalData(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.UPDATE_PERSONAL_DATA_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);

    }

    public void getUserCars(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.SELECT_USERS_CAR_URL, RequestMethod.POST);

        if(!mTask.isRunning())
            mTask.execute(params);
    }

    public void attemptLogin(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.LOGIN_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);

    }

    public void createNewService(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.SERVICE_REGISTER_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void addNewCar(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.ADD_NEW_CAR_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void updateCar(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.UPDATE_CAR_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void deleteCar(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.DELETE_CAR_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void getAllServices(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.SERVICES_URL , RequestMethod.GET_FROM_URL);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void getPersonalData(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.SELECT_PERSONAL_DATA_URL , RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void getUserServices(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.SELECT_USER_SERVICE_URL , RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void getCarInfo(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.SELECT_CAR_URL , RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void getServiceDetails(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.SELECT_SERVICE, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public void rateService(List<NameValuePair> params){
        mTask = new AsyncPerformer(this, Constants.RATE_SERVICE, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);
    }

    public static Boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnected())
            return true;

        return false;
    }

    public void cancelCurrentTask(){
        mTask.cancel(true);
    }
    /**
     * ***************************
     * AsyncTask CallBack
     * ***************************
     */
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
