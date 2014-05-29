package pl.rzeszow.wsiz.carservice.model;

import java.util.ArrayList;

    //! Klasa reprezentująca Użytkownika.
    /*!
      Przechowywuje wszystkie informacje o użytkowniku.
    */
public class User extends BaseListItem {

    private int id;             //!< ID Użytkownika.
    private String username;    //!< Imię dla logowania Użytkownika.
    private String password;    //!< Hasło Użytkownika.
    private String name;        //!< Imię Użytkownika.
    private String surname;     //!< Nazwisko Użytkownika.
    private int sex;            //!< Płeć Użytkownika.
    private String birth;       //!< Data urodzenia Użytkownika.
    private String nr_tel;      //!< Numer telefonu Użytkownika.
    private String email;       //!< E-mail Użytkownika.
    private String city;        //!< Miasto, w którym Użytkownik mieszka.
    private String adress;      //!< Adres zamiaszkania Użytkownika.
    private ArrayList<Service> contactedServices; //!< Lista serwisów z którymi użytkownik nawiązał kontakt.

    //!  User konstruktor
    /*!
     Inicjalizuje prywatne zmienne.
    \param username Imie do logowania.
    \param password Hasło.
    \param name Imie.
    \param surname Nazwisko.
    \param sex Płec.
    \param birth Data urodzenia.
    \param nr_tel Numer telefonu.
    \param email Adres mailowy.
    \param city Miasto.
    \param adress Adres.
    */
        public User(String username, String password, String name, String surname, int sex, String birth, String nr_tel, String email, String city, String adress)
    {
        this.username = username;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.birth = birth;
        this.nr_tel = nr_tel;
        this.email = email;
        this.city = city;
        this.adress = adress;
    }
    
    //!  User konstruktor.
    /*!
    Inicjalizuje prywatne zmienne oraz tworzy nową pustą listę serwisów z którymi użytkownik się kontaktował.
    \param id ID użytkownika.
    \param fname Imie.
    \param lname Nazwisko.
    */
    public User(int id,String fname,String lname){
        this.id = id;
        this.name = fname;
        this.surname = lname;
        contactedServices = new ArrayList<Service>();
    }

    //!  User konstruktor.
    /*!
    Konstruktor domyślny czyli bez parametrów.
    */
    public User(){}

    @Override
    //! Metoda, króra nadaje dostęp do zmiennej id.
    /*!
    \return ID użytkownika.
    */
    public int getId() {
        return id;
    }

    //! Metoda, króra nadaje dostęp do zmiennej imie do logowania.
    /*!
    \return Imie do logowania.
    */
    public String getUsername() {
        return username;
    }
    //! Metoda, króra nadaje dostęp do zmiennej hasło.
    /*!
    \return Hasło użytkownika.
    */
    public String getPassword() {
        return password;
    }

    //! Metoda, króra nadaje dostęp do zmiennej imię.
    /*!
    \return Imię.
    */
    public String getName() {
        return name;
    }

    //! Metoda, króra nadaje dostęp do zmiennej nazwisko.
    /*!
    \return Nazwisko.
    */
    public String getSurname() {
        return surname;
    }

    //! Metoda, króra nadaje dostęp do zmiennej płeć.
    /*!
    \return Płeć.
    */
    public int getSex() {
        return sex;
    }

    //! Metoda, króra nadaje dostęp do zmiennej datę urodzenia.
    /*!
    \return Data urodzenia.
    */
    public String getBirth() {
        return birth;
    }

    //! Metoda, króra nadaje dostęp do zmiennej numer telefonu.
    /*!
    \return Numer telefonu.
    */
    public String getNr_tel() {
        return nr_tel;
    }

    //! Metoda, króra nadaje dostęp do zmiennej email.
    /*!
    \return E-mail.
    */
    public String getEmail() {
        return email;
    }

    //! Metoda, króra nadaje dostęp do zmiennej miasto.
    /*!
    \return Miasto.
    */
    public String getCity() {
        return city;
    }

    //! Metoda, króra nadaje dostęp do zmiennej adres.
    /*!
    \return Adres.
    */
    public String getAdress() {
        return adress;
    }

    //! Dodaje do listy serwis z którym Użytkownik się kontaktował.
    /*!
    \param s Service Serwis.
    */
    public void addContactedService(Service s){
        contactedServices.add(s);
    }

    @Override
    //! Metoda, króra zwraca ilość nawiązanych kontactów.
    /*!
    \return Ilość nawiązanych kontaktów.
    */
    public int getContactCount() {
        return contactedServices.size();
    }

    @Override
    //! Metoda, która zwraca serwis z którym kontaktował się użytkownik.
    /*!
      \param position pozycja serwisu w liście kontaktów.
      \return BaseListItem jako serwis
    */
    public BaseListItem getContact(int position) {
        return contactedServices.get(position);
    }
}
