package pl.rzeszow.wsiz.carservice;

/**
 * Zmienne dla połaczenia z php
 */
public class Constants {

    public static final String LOGIN = "myLogin";//!< login
    public static final String LOGIN_USER = "lUsername";//!< imię użytkownika
    public static final String LOGIN_PASSWORD = "lPassword";//!< hasło
    public static final String LOGIN_ID = "lID";//!< id logina

    public static final String USER_ID = "userID";//!< id użytkownika
    public static final String SERVICE_ID = "serviceID";//!< id serwisu
    public static final String CAR_ID = "carID";//!< id auta

    private final static String PAGE_URL = "http://carservice.esy.es/carserv/";//!< url serwisu
    //private final static String PAGE_URL = "http://10.0.2.2:1234/webservice/";

    public final static String REGISTER_URL = PAGE_URL +"register.php";//!< link do rejestacji użytkownika
    public final static String LOGIN_URL = PAGE_URL +"login.php"; //!< link do zalogowania

    public final static String SERVICE_REGISTER_URL = PAGE_URL +"service_register.php";//!< link do tejestracji serwisu
    public final static String SERVICES_URL = PAGE_URL+"services.php"; //!< link do wyświetlania wszystkich serwisów

    public final static String SELECT_PERSONAL_DATA_URL = PAGE_URL +"select_user.php";//!< link do wybrania użytkownika
    public final static String UPDATE_PERSONAL_DATA_URL = PAGE_URL +"update_user.php";//!< link do aktualizacji użytkownika

    public final static String SELECT_USER_CAR_URL = PAGE_URL +"select_usercars.php";//!< link do wybrania aut użytkownika
    public final static String ADD_NEW_CAR_URL = PAGE_URL +"car_register.php";//!< link do rejestracji auta
    public final static String SELECT_CAR_URL = PAGE_URL +"select_car.php";//!< link do wybrania aut
    public final static String UPDATE_CAR_URL = PAGE_URL +"car_update.php";//!< link do aktualizacji aut
    public final static String DELETE_CAR_URL = PAGE_URL +"delete_car.php";//!< link do usuwania aut

    public final static String SELECT_USER_SERVICE_URL = PAGE_URL +"select_servicesuser.php";//!< link do wybrania serwisu użytkownika
    public final static String SELECT_SERVICE = PAGE_URL+"select_service.php";//!< link do wybrania serwisów
    public final static String RATE_SERVICE = PAGE_URL+"rate_service.php";//!< link do oceny serwisu

    public final static String UPDATE_SERVICE_URL = PAGE_URL+"service_update.php";//!< link do aktualizacji serwisu
    public final static String DELETE_SERVICE_URL = PAGE_URL+"delete_service.php";//!< link do usuwania serwisu

    public final static String SEND_MESSAGE = PAGE_URL+"send_message.php";//!< link do wysłania wiadomości
    public final static String SELECT_USER_CONVERSATIONS = PAGE_URL+"select_user_conversations.php";//!< link do wybrania rozmów użytkownika
    public final static String SELECT_CONVERSATION = PAGE_URL+"select_conversation.php";//!< link do wybrania rozmów
}
