package pl.rzeszow.wsiz.carservice.utils.async;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

import pl.rzeszow.wsiz.carservice.utils.json.JSONRequester;
import pl.rzeszow.wsiz.carservice.utils.Singleton;

/**
 * Klasa wykonująca asynchronicznie ządania JSONRequester`a
 */
public class AsyncPerformer extends AsyncTask<List<NameValuePair>, Void,JSONObject>{

    private TaskCallback mTaskCallBack;     //!< Objekt do wykonania callback`ow

    private boolean isRuning = false;       //!< Czy żądanie jest w trakcie wykonania
    private boolean isExecuted = false;     //!< Czy żądanie jest już wykonane

    private String url;                     //!< Link do wykonania żądania
    private RequestMethod method;           //!< Metod wykonania żądania

    private static String TAG ="AsyncPerformer";

    /**
     * Konstruktor wykonawcy żądań
     * @param s Punkt zwracania wynukow
     * @param url Link żądania
     * @param method Metoda żądania
     */
    public AsyncPerformer(Singleton s, String url,RequestMethod method){
        if (!(s instanceof TaskCallback)) {
            throw new IllegalStateException("Must implement the TaskCallbacks interface.");
        }
        this.mTaskCallBack = (TaskCallback) s;
        this.url = url;
        this.method = method;
    }

    /**
     * Rozpoczęcie wykonnia żądania
     */
    @Override
    protected void onPreExecute() {
        isRuning = true;
        mTaskCallBack.onPreExecute();
    }

    /**
     * Wykonanie żadania w tle
     * @param params lista parametrow do żądania
     * @return odpowiedź na żądania
     */
    @Override
    protected JSONObject doInBackground(List<NameValuePair>... params) {
        JSONObject json = JSONRequester.makeHttpRequest(url, method, params[0]);
        return json;
    }

    /**
     * Przerwania wykonania żądania w trakcie
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        isRuning = false;
        isExecuted = true;
        mTaskCallBack.onCancelled();
    }

    /**
     * Zakończenie wykonania żadania
     * @param jsonObject uzyskana odpowiedź na żądanie
     */
    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        isRuning = false;
        isExecuted = true;
        mTaskCallBack.onPostExecute(jsonObject);
    }

    /**
     * Sprawdzenie czy żadanie w trakcie wykonania
      * @return stan żądanie
     */
    public boolean isRunning(){
        return isRuning;
    }

    /**
     * Sprawdzenie czy żądanie jest już wykonane
     * @return stan żądania
     */
    public boolean isExecuted() {return  isExecuted;}
}
