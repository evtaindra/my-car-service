package pl.rzeszow.wsiz.carservice;

public class Constants {

    public static final String LOGIN = "myLogin";
    public static final String LOGIN_USER = "lUsername";
    public static final String LOGIN_PASSWORD = "lPassword";
    public static final String LOGIN_ID = "lID";

    public static final String USER_ID = "userID";
    public static final String SERVICE_ID = "serviceID";
    public static final String CAR_ID = "carID";

    private final static String PAGE_URL = "http://carservice.esy.es/carserv/";
    public final static String REGISTER_URL = PAGE_URL +"register.php";
    public final static String LOGIN_URL = PAGE_URL +"login.php";

    public final static String SERVICE_REGISTER_URL = PAGE_URL +"service_register.php";
    public final static String SERVICES_URL = PAGE_URL+"services.php";

    public final static String SELECT_PERSONAL_DATA_URL = PAGE_URL +"select_user.php";
    public final static String UPDATE_PERSONAL_DATA_URL = PAGE_URL +"update_user.php";

    public final static String SELECT_USERS_CAR_URL = PAGE_URL +"select_usercars.php";
    public final static String ADD_NEW_CAR_URL = PAGE_URL +"car_register.php";

    public final static String SELECT_SERVICE = PAGE_URL+"select_service.php";
    public final static String RATE_SERVICE = PAGE_URL+"rate_service.php";
}
