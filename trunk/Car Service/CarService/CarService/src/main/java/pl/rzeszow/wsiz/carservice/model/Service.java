package pl.rzeszow.wsiz.carservice.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Klasa reprezentująca Serwis.
 * <p>
 *    Przechowywuje wszystkie informacje o serwisie.
 * </p>
 */
public class Service extends BaseListItem {
    private int id;             //!< ID Serwisu.
    private String name;        //!< Nazwa Serwisu.
    private String city;        //!< Misto, gdzie znajduje się Serwis.
    private String address;     //!< Adres Serwisu.
    private double rating;      //!< Srednia ocena serwisu.
    private String description; //!< Opis uslug nadawanych serwisem.
    private Bitmap image;       //!< Miniatura/logo Serwisu.
    private int us_id;          //!< ID Użytkownika ktory jest wlascicielem.
    private String phone;       //!< Numer kontaktowy serwisu.
    private String email;       //!< E-mail serwisu.
    private ArrayList<User> contactedUsers;//!< Lista użytkownikow którzy nawiązali kontakt z serwisem

    /**
     * Service Konstruktor
     * <p>
     *     Inicjalizuje prywatne zmienne.
     * </p>
     * @param id ID Serwisu
     * @param name
     * @param city
     * @param address
     * @param rating
     * @param image
     * @param us_id
     */
    public Service(int id, String name, String city, String address, double rating, Bitmap image, int us_id) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.rating = rating;
        this.image = image;
        this.us_id = us_id;
    }

    /**
     * Service Konstruktor
     * <p>
     *     Inicjalizuje prywatne zmienne.
     * </p>
     * @param id
     * @param name
     * @param city
     * @param address
     * @param rating
     * @param image
     * @param us_id
     * @param description
     * @param phone
     * @param email
     */
    public Service(int id, String name, String city, String address, double rating, Bitmap image, int us_id,
                   String description, String phone, String email) {
        this(id, name, city, address, rating, image, us_id);
        this.description = description;
        this.email = email;
        this.phone = phone;
    }

    /**
     * Service Konstruktor
     * <p>
     *     Konstruktor domyślny czyli bez parametrów.
     * </p>
     */
    public Service() {
    }

    /**
     * Service Konstruktor
     * <p>
     *     Inicjalizuje prywatne zmienne oraz tworzy nową pustą listę użytkownikow którzy nawiązali kontakt z serwisem.
     * </p>
     * @param id
     * @param name
     */
    public Service(int id, String name){
        this.id = id;
        this.name = name;
        contactedUsers = new ArrayList<User>();
    }

    public void setRating(double rating){
        this.rating = rating;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
        return image;
    }

    public int getUs_id() {
        return us_id;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public void addContactedUser(User u){
        contactedUsers.add(u);
    }

    @Override
    public int getContactCount() {
        return contactedUsers.size();
    }

    @Override
    public BaseListItem getContact(int position) {
        return contactedUsers.get(position);
    }

}
