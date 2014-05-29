package pl.rzeszow.wsiz.carservice.model;

import java.util.ArrayList;

/**
 * Created by opryima on 2014-05-12.
 */

    //!  Klasa User
    /*!
      Klasa reprezentująca Użytkownika dziedziczy po abstraktniej klasie BaseListItem
    */
public class User extends BaseListItem {

    private int id;             /// zmienna, która przechowuje id Użytkownika
    private String username;    /// prywatna zmienna, która przechowuje imię dla logowania Użytkownika
    private String password;    /// prywatna zmienna, która przechowuje hasło Użytkownika
    private String name;        /// prywatna zmienna, która przechowuje imię Użytkownika
    private String surname;     /// prywatna zmienna, która przechowuje nazwisko Użytkownika
    private int sex;            /// prywatna zmienna, która przechowuje płeć Użytkownika
    private String birth;       /// prywantna zmienna, która przechowuje datę urodzenia Użytkownika
    private String nr_tel;      ///prywatna zmienna, która przechowuje numer telefonu Użytkownika
    private String email;       /// prywatna zmienna, która przechowuje email Użytkownika
    private String city;        /// prywatna zmienna, która przechowuje miasto, w którym Użytkownik mieszka
    private String adress;      ///prywatna zmienna, która przechowuje adres zamiaszkania Użytkownika
    private ArrayList<Service> contactedServices; /// implementacja listy w postaci tablicy, która przechowuje serwisy

    //!  User konstruktor
    /*!
     Konstruktor, który przypisuje odpowiednie wartości wewnętrznym zmiennym Użytkownika
    */
        public User(String username, String password, String name, String surname, int sex, String birth, String nr_tel, String email, String city, String adress)
    {
        this.username = username; ///przypisanie imienia do logowania do zmiennej wewnętrznej
        this.password = password;   ///przypisanie hasła do zmiennej wewnętrznej
        this.name = name;   ///przypisanie imienia do zmiennej wewnętrznej
        this.surname = surname; ///przypisanie nazwiska do zmiennej wewnętrznej
        this.sex = sex; ///przypisanie płci do zmiennej wewnętrznej
        this.birth = birth; ///przypisanie daty urodzenia do zmiennej wewnętrznej
        this.nr_tel = nr_tel;   ///przypisanie numeru telefonu do logowania do zmiennej wewnętrznej
        this.email = email; ///przypisanie emaila do zmiennej wewnętrznej
        this.city = city;   ///przypisanie maiasta zamieszkania do zmiennej wewnętrznej
        this.adress = adress;   ///przypisanie adresu do zmiennej wewnętrznej
    }
    //!  User konstruktor
    /*!
     Konstruktor domyślny czyli bez parametrów
    */
    public User(){
    }
    //!  User konstruktor
        /*!
         Konstruktor przypisujący odpowiednie wartości wewnętrznym zmiennym Użytkownika
         i tworzący nową ArrayList serwisów
        */
    public User(int id,String fname,String lname){
        this.id = id; ///przypisanie imienia do logowania do zmiennej wewnętrznej
        this.name = fname;  ///przypisanie imienia do logowania do zmiennej wewnętrznej
        this.surname = lname;   ///przypisanie imienia do logowania do zmiennej wewnętrznej
        contactedServices = new ArrayList<Service>();
    }

    @Override ///nadużycie metody z klasy abstraktnej
    //! własciwośc, króra nadaje dostęp do zmiennej id
    public int getId() {
        return id;
    }/// zwraca id
    //! własciwośc, króra nadaje dostęp do zmiennej imię do logowania
    public String getUsername() {
        return username;/// zwraca imie do logowania
    }
    //! własciwośc, króra nadaje dostęp do zmiennej hasło
    public String getPassword() {
        return password;
    }/// zwraca hasło
    //! własciwośc, króra nadaje dostęp do zmiennej imię
    public String getName() {
        return name;
    }/// zwraca imię
    //! własciwośc, króra nadaje dostęp do zmiennej nazwisko
    public String getSurname() {
        return surname;
    }/// zwraca nazwisko
    //! własciwośc, króra nadaje dostęp do zmiennej płeć
    public int getSex() {
        return sex;
    }/// zwraca płeć
    //! własciwośc, króra nadaje dostęp do zmiennej data urodzenia
    public String getBirth() {
        return birth;
    }/// zwraca datę urodzenia
    //! własciwośc, króra nadaje dostęp do zmiennej numer telefonu
    public String getNr_tel() {
        return nr_tel;
    }/// zwraca numer telefonu
    //! własciwośc, króra nadaje dostęp do zmiennej email
    public String getEmail() {
        return email;
    }/// zwraca email
    //! własciwośc, króra nadaje dostęp do zmiennej miasto
    public String getCity() {
        return city;
    }/// zwraca miasto
    //! własciwośc, króra nadaje dostęp do zmiennej adres
    public String getAdress() {
        return adress;
    }/// zwraca adres

    //! Metoda,zawierająca w parametach objekt serwisu
    /*!
    dodaje do ArrayList nowy serwis Uzytkownika
    */
    public void addContactedService(Service s){
        contactedServices.add(s);
    }

    @Override ///nadużycie metody z klasy abstraktnej
    //! metoda, króra zwraca rozmiar ArrayList
    public int getContactCount() {
        return contactedServices.size();
    }

    @Override ///nadużycie metody z klasy abstraktnej
    //! Metoda typu abstraktnej klasy,zawierająca w parametach pozycję objektu
    /*!
     zwraca pozycję objektu w ArrayList
    */
    public BaseListItem getContact(int position) {
        return contactedServices.get(position);
    }
}
