package pl.rzeszow.wsiz.carservice.model;

import java.util.ArrayList;

    //!  Klasa User.
    /*!
      Klasa reprezentująca Użytkownika dziedziczy po abstraktnej klasie BaseListItem.
    */
public class User extends BaseListItem {

    private int id;             /// zmienna, która przechowuje id Użytkownika.
    private String username;    /// prywatna zmienna, która przechowuje imię dla logowania Użytkownika.
    private String password;    /// prywatna zmienna, która przechowuje hasło Użytkownika.
    private String name;        /// prywatna zmienna, która przechowuje imię Użytkownika.
    private String surname;     /// prywatna zmienna, która przechowuje nazwisko Użytkownika.
    private int sex;            /// prywatna zmienna, która przechowuje płeć Użytkownika.
    private String birth;       /// prywantna zmienna, która przechowuje datę urodzenia Użytkownika.
    private String nr_tel;      ///prywatna zmienna, która przechowuje numer telefonu Użytkownika.
    private String email;       /// prywatna zmienna, która przechowuje email Użytkownika.
    private String city;        /// prywatna zmienna, która przechowuje miasto, w którym Użytkownik mieszka.
    private String adress;      ///prywatna zmienna, która przechowuje adres zamiaszkania Użytkownika.
    private ArrayList<Service> contactedServices; /// implementacja listy w postaci tablicy, która przechowuje serwisy z którymi użytkownik nawiązał kontakt.

    //!  User konstruktor
    /*!
     Konstruktor, który przypisuje odpowiednie wartości wewnętrznym zmiennym Użytkownika.
    \param username String imie do logowania.
    \param password String hasło.
    \param name String imie.
    \param surname String nazwisko.
    \param sex int płec.
    \param birth String data urodzenia.
    \param nr_tel String numer telefonu.
    \param email String adres mailowy.
    \param city String miasto.
    \param adress String ulica.
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
     Konstruktor domyślny czyli bez parametrów.
    */
    public User(){
    }
    //!  User konstruktor.
        /*!
         Konstruktor przypisujący odpowiednie wartości wewnętrznym zmiennym Użytkownika.
         i tworzący nową ArrayList serwisów z którymi użytkownik się kontaktował.
    \param id int id użytkownika.
    \param fname String imie.
    \param lname String nazwisko.
        */
    public User(int id,String fname,String lname){
        this.id = id;
        this.name = fname;
        this.surname = lname;
        contactedServices = new ArrayList<Service>();
    }

    @Override
    //! Metoda, króra nadaje dostęp do zmiennej id.
    /*!
    \return id użytkownika.
    */
    public int getId() {
        return id;
    }

    //! Metoda, króra nadaje dostęp do zmiennej imie do logowania.
    /*!
    \return imie do logowania.
    */
    public String getUsername() {
        return username;
    }
    //! Metoda, króra nadaje dostęp do zmiennej hasło.
    /*!
    \return hasło użytkownika.
    */
    public String getPassword() {
        return password;
    }

   //! Metoda, króra nadaje dostęp do zmiennej imię.
    /*!
    \return imię.
    */
    public String getName() {
        return name;
    }

    //! Metoda, króra nadaje dostęp do zmiennej nazwisko.
    /*!
    \return nazwisko.
    */
    public String getSurname() {
        return surname;
    }

    //! Metoda, króra nadaje dostęp do zmiennej płeć.
    /*!
    \return płeć.
    */
    public int getSex() {
        return sex;
    }

    //! Metoda, króra nadaje dostęp do zmiennej datę urodzenia.
    /*!
    \return datę urodzenia.
    */
    public String getBirth() {
        return birth;
    }

    //! Metoda, króra nadaje dostęp do zmiennej numer telefonu.
    /*!
    \return numer telefonu.
    */
    public String getNr_tel() {
        return nr_tel;
    }

    //! Metoda, króra nadaje dostęp do zmiennej email.
    /*!
    \return email.
    */
    public String getEmail() {
        return email;
    }

    //! Metoda, króra nadaje dostęp do zmiennej miasto.
    /*!
    \return miasto.
    */
    public String getCity() {
        return city;
    }

        //! Metoda, króra nadaje dostęp do zmiennej adres.
    /*!
    \return adres.
    */
    public String getAdress() {
        return adress;
    }

    //! Metoda,która dodaje do ArrayList nowy serwis Uzytkownika z którym on sie skontaktował.
    /*!
    \param s Service serwis.
    */
    public void addContactedService(Service s){
        contactedServices.add(s);
    }

    @Override
    //! metoda, króra zwraca rozmiar ArrayList.
    public int getContactCount() {
        return contactedServices.size();
    }

    @Override
    //! Metoda, która zwraca pozycję objektu w ArrayList.
    /*!
      \param position int pozycja serwisu.
    */
    public BaseListItem getContact(int position) {
        return contactedServices.get(position);
    }
}
