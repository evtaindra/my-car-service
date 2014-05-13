package pl.rzeszow.wsiz.carservice.utils.json;

import android.graphics.Bitmap;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.model.User;
import pl.rzeszow.wsiz.carservice.utils.image.BitmapEnDecode;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class JSONInterpreter {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SERVICE_ARRAY = "services";

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

    public static ArrayList<Service> parseServiceList(JSONObject json)
    {
        ArrayList<Service> services = null;
        try{
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1){
                services = new ArrayList<Service>();

                JSONArray jsonArray = json.getJSONArray(TAG_SERVICE_ARRAY);

                for(int i = 0; i<jsonArray.length();i++){
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Service s = parseServiceSimple(obj);
                    services.add(s);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return services;
    }

    public static User parseUserSimple(JSONObject json)
    {
        User user = null;
        try{
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                int id = json.getInt("us_id");
                String username = json.getString("username");
                String password = json.getString("password");
                String name = json.getString("name");
                String surname = json.getString("surname");
                int sex = json.getInt("sex");
                String birth = json.getString("birth");
                String nr_tel = json.getString("nr_tel");
                String email = json.getString("email");
                String city = json.getString("city");
                String adress = json.getString("adress");

                user = new User(id, username, password, name, surname, sex, birth, nr_tel, email, city, adress);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    private static Service parseServiceSimple (JSONObject json)
    {
        Service service = null;
        try {
            int id = json.getInt("sid");
            String name = json.getString("sname");
            String city = json.getString("scity");
            String address = json.getString("saddress");
            int rating = json.getInt("srating");
            String description = json.getString("sdescription");
            Bitmap image = BitmapEnDecode.StringToBitmap(json.getString("simage"));
            int us_id = json.getInt("sus_id");

            service = new Service(id,name,city,address,rating,description,image,us_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return service;
    }

}
