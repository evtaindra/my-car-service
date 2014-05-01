package pl.rzeszow.wsiz.carservice.utils;

import android.util.Pair;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class JSONInterpreter {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    public static Pair<Integer, String> parseMessage(JSONObject json)
    {
        Pair<Integer,String> res = null;
        try{
            res = new Pair<Integer, String>(json.getInt(TAG_SUCCESS),json.getString(TAG_MESSAGE));
        } catch (JSONException e){
            e.printStackTrace();
        }
        return res;
    }
}
