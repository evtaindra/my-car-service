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
    private double rating;      //!< Srednia ocena Serwisu.
    private String description; //!< Opis uslug nadawanych Serwisem.
    private Bitmap image;       //!< Miniatura/logo Serwisu.
    private int us_id;          //!< ID Użytkownika, ktory jest właścicielem.
    private String phone;       //!< Numer kontaktowy serwisu.
    private String email;       //!< E-mail Serwisu.
    private ArrayList<User> contactedUsers;//!< Lista użytkownikow którzy nawiązali kontakt z Serwisem

    /**
     * Service Konstruktor
     * <p>
     *     Inicjalizuje prywatne zmienne.
     * </p>
     * @param id ID Serwisu
     * @param name Nazwa Serwisu
     * @param city  Miasto, gdzie znajduję się Serwis
     * @param address Adres Serwisu
     * @param rating Średnia ocena Serwisu
     * @param image Logo Serwisu
     * @param us_id ID Użytkownika, który jest właścicielem
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
     * @param id ID Serwisu
     * @param name Nazwa Serwisu
     * @param city   Miasto, gdzie znajduję się Serwis
     * @param address Adres Serwisu
     * @param rating Średnia ocena Serwisu
     * @param image Logo Serwisu
     * @param us_id ID Użytkownika, który jest właścicielem
     * @param description Opis uslug nadawanych Serwisem
     * @param phone  Numer kontaktowy Serwisu
     * @param email  E-mail Serwisu
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
     *     Inicjalizuje prywatne zmienne oraz tworzy nową pustą listę użytkownikow, którzy nawiązali kontakt z Serwisem.
     * </p>
     * @param id ID Serwisu
     * @param name Nazwa Serwisu
     */
    public Service(int id, String name){
        this.id = id;
        this.name = name;
        contactedUsers = new ArrayList<User>();
    }

    /**
     *
     * @param rating Średnia ocena Serwisu
     */
    public void setRating(double rating){
        this.rating = rating;
    }

    @Override
    /**
     * Nadaje dostęp do zmiennej id Serwisu
     * @return ID Serwisu.
     */
    public int getId() {
        return id;
    }
    /**
     * Nadaje dostęp do zmiennej nazwa Serwisu
     * @return nazwa Serwisu.
     */
    public String getName() {
        return name;
    }
    /**
     * Nadaje dostęp do zmiennej miasto Serwisu
     * @return miasto Serwisu.
     */
    public String getCity() {
        return city;
    }
    /**
     * Nadaje dostęp do zmiennej adres Serwisu
     * @return adres Serwisu.
     */
    public String getAddress() {
        return address;
    }
    /**
     * Nadaje dostęp do zmiennej średnia ocena Serwisu
     * @return średnia ocena Serwisu.
     */
    public double getRating() {
        return rating;
    }
    /**
     * Nadaje dostęp do zmiennej opis usług Serwisu
     * @return opis usług Serwisu.
     */
    public String getDescription() {
        return description;
    }
    /**
     * Nadaje dostęp do zmiennej logo Serwisu
     * @return logo Serwisu.
     */
    public Bitmap getImage() {
        return image;
    }
    /**
     * Nadaje dostęp do zmiennej id Użytkownika
     * @return ID Użytkownika
     */
    public int getUs_id() {
        return us_id;
    }
    /**
     * Nadaje dostęp do zmiennej numer kontaktowy Serwisu
     * @return numer kontaktowy Serwisu.
     */
    public String getPhone() {
        return phone;
    }
    /**
     * Nadaje dostęp do zmiennej email Serwisu
     * @return email Serwisu.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Dodaje do listy użytkownika z którym Serwis się kontaktował.
     * @param u Użytkownik
     */
    public void addContactedUser(User u){
        contactedUsers.add(u);
    }

    /**
     * Zwraca ilość nawiązanych kontaktów.
     * @return Ilość nawiązanych kontaktów.
     */
    @Override
    public int getContactCount() {
        return contactedUsers.size();
    }

    /**
     * Zwraca Użytkownika z którym kontaktował się Serwis.
     * @param position pozycja uzytkownika w liście kontaktów
     * @return BaseListItem jako użytkownik
     */
    @Override
    public BaseListItem getContact(int position) {
        return contactedUsers.get(position);
    }

}
