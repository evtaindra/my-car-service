package pl.rzeszow.wsiz.carservice.utils.async;

import org.json.JSONObject;

public interface TaskCallback {
    public void onPreExecute();
    public void onPostExecute(JSONObject result);
    public void onCancelled();
}
