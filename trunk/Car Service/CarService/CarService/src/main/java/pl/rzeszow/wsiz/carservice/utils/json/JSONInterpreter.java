package pl.rzeszow.wsiz.carservice.utils.json;

import android.graphics.Bitmap;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.rzeszow.wsiz.carservice.model.BaseListItem;
import pl.rzeszow.wsiz.carservice.model.Car;
import pl.rzeszow.wsiz.carservice.model.Message;
import pl.rzeszow.wsiz.carservice.model.Service;
import pl.rzeszow.wsiz.carservice.model.User;
import pl.rzeszow.wsiz.carservice.utils.Singleton;
import pl.rzeszow.wsiz.carservice.utils.image.BitmapEnDecode;

/**
 * Created by rsavk_000 on 5/1/2014.
 */
public class JSONInterpreter {

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_SERVICE_ARRAY = "services";
    private static final String TAG_CAR_ARRAY = "cars";

    public static Pair<Integer, String> parseMessage(JSONObject json) {
        Pair<Integer, String> res = null;
        try {
            res = new Pair<Integer, String>(json.getInt(TAG_SUCCESS), json.getString(TAG_MESSAGE));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static ArrayList<Car> parseCarList(JSONObject json) {
        ArrayList<Car> cars = null;
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                cars = new ArrayList<Car>();

                JSONArray jsonArray = json.getJSONArray(TAG_CAR_ARRAY);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Car c = parseCar(obj, false);
                    cars.add(c);
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return cars;
    }


    public static ArrayList<Service> parseServiceList(JSONObject json) {
        ArrayList<Service> services = null;
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                services = new ArrayList<Service>();

                JSONArray jsonArray = json.getJSONArray(TAG_SERVICE_ARRAY);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Service s = parseService(obj, false);
                    services.add(s);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return services;
    }

    public static Car parseCar(JSONObject json, boolean isDetailed) {
        Car car = null;
        try {
            int id = json.getInt("nr_id");
            String marka = json.getString("marka");
            String model = json.getString("model");
            String rej = json.getString("nr_rej");
            int rok = json.getInt("rok");
            if (isDetailed) {
                double silnik = json.getDouble("silnik");
                int przebieg = json.getInt("przebieg");
                String kolor = json.getString("kolor");
                String paliwo = json.getString("paliwo");
                int uid = json.getInt("us_id");

                car = new Car(id, uid, marka, model, rej, silnik, przebieg, kolor, paliwo, rok);
            } else {
                car = new Car(id, marka, model, rej, rok);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return car;
    }

    public static User parseUser(JSONObject json) {
        User user = null;
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
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

                user = new User(username, password, name, surname, sex, birth, nr_tel, email, city, adress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public static Service parseService(JSONObject json, boolean isDetailed) {
        Service service = null;
        try {
            int id = json.getInt("sid");
            String name = json.getString("sname");
            String city = json.getString("scity");
            String address = json.getString("saddress");
            double rating = json.getDouble("srating");
            Bitmap image = BitmapEnDecode.StringToBitmap(json.getString("simage"));
            int us_id = json.getInt("sus_id");
            if (isDetailed) {
                String description = json.getString("sdescription");
                String phone = json.getString("snr_tel");
                String email = json.getString("semail");
                service = new Service(id, name, city, address, rating, image, us_id, description, phone, email);
            } else
                service = new Service(id, name, city, address, rating, image, us_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return service;
    }

    public static ArrayList<BaseListItem> parseConversationList(JSONObject json) {
        ArrayList<BaseListItem> conversation = null;
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                conversation = new ArrayList<BaseListItem>();

                JSONArray jAYourConversations = json.getJSONArray("user_conversations");
                if (jAYourConversations.length() != 0) {
                    conversation.add(new User(Singleton.getSingletonInstance().userID, "Conversation that you started", ""));
                    for (int i = 0; i < jAYourConversations.length(); i++) {
                        JSONObject obj = jAYourConversations.getJSONObject(i);
                        ((User) conversation.get(0)).addContactedService(
                                new Service(obj.getInt("sr_id"), obj.getString("sr_name"))
                        );
                    }
                }
                JSONArray jAServicesConversations = json.getJSONArray("user_services_conversations");
                for (int i = 0; i < jAServicesConversations.length(); i++) {
                    JSONObject obj = jAServicesConversations.getJSONObject(i);
                    JSONArray jAServicesConversation = obj.getJSONArray("service_conversation");
                    if (jAServicesConversation.length() != 0) {
                        conversation.add(new Service(obj.getInt("sr_id"), obj.getString("sr_name")));
                        for (int j = 0; j < jAServicesConversation.length(); j++) {
                            JSONObject obj2 = jAServicesConversation.getJSONObject(j);
                            ((Service) conversation.get(i + 1)).addContactedUser(
                                    new User(obj2.getInt("us_id"), obj2.getString("fname"), obj2.getString("lname"))
                            );
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return conversation;
    }

    public static ArrayList<Message> parseConversation(JSONObject json) {
        ArrayList<Message> messages = null;
        try {
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                messages = new ArrayList<Message>();
                JSONArray jsonArray = json.getJSONArray("messages");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    obj.getInt("sender");
                    obj.getString("date");
                    obj.getString("content");
                    BitmapEnDecode.StringToBitmap(obj.getString("attach"));
                    obj.getBoolean("isread");

                    Message m = new Message(
                            obj.getInt("sender"),
                            obj.getString("date"),
                            obj.getString("content"),
                            BitmapEnDecode.StringToBitmap(obj.getString("attach")),
                            obj.getBoolean("isread")
                    );
                    messages.add(m);
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return messages;
    }

}
