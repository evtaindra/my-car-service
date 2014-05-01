package pl.rzeszow.wsiz.carservice.utils;

import org.json.JSONObject;

public interface TaskCallback {
    public void onPreExecute();
    public void onPostExecute(JSONObject result);
    public void onCancelled();
}
