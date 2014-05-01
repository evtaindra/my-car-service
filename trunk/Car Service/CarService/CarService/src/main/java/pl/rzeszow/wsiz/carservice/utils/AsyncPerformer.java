package pl.rzeszow.wsiz.carservice.utils;

import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by rsavk_000 on 4/13/2014.
 */
public class AsyncPerformer extends AsyncTask<List<NameValuePair>, Void,JSONObject>{

    private TaskCallback mTaskCallBack;

    private boolean isRuning = false;
    private boolean isExecuted = false;

    private String url;
    private RequestMethod method;

    private static String TAG ="AsyncPerformer";

    public AsyncPerformer(Singleton s, String url,RequestMethod method){
        if (!(s instanceof TaskCallback)) {
            throw new IllegalStateException("Must implement the TaskCallbacks interface.");
        }
        this.mTaskCallBack = (TaskCallback) s;
        this.url = url;
        this.method = method;
    }

    @Override
    protected void onPreExecute() {
        isRuning = true;
        mTaskCallBack.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(List<NameValuePair>... params) {
        JSONObject json = JSONRequester.makeHttpRequest(url,method,params[0]);
        return json;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        isRuning = false;
        isExecuted = true;
        mTaskCallBack.onCancelled();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        isRuning = false;
        isExecuted = true;
        mTaskCallBack.onPostExecute(jsonObject);
    }

    public boolean isRunning(){
        return isRuning;
    }
    public boolean isExecuted() {return  isExecuted;}
}
