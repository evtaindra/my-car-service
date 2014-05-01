package pl.rzeszow.wsiz.carservice.utils;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

import pl.rzeszow.wsiz.carservice.Constants;

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

    public void setUserID(int id){
        userID = id;
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

    public void attemptLogin(List<NameValuePair> params) {
        mTask = new AsyncPerformer(this, Constants.LOGIN_URL, RequestMethod.POST);

        if (!mTask.isRunning())
            mTask.execute(params);

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
