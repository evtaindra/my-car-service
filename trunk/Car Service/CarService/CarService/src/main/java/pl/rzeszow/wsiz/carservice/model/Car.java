package pl.rzeszow.wsiz.carservice.model;

/**
 * Klasa reprezentująca samochód.
 * <p>
 *    Przechowywuje wszystkie informacje o samochodzie.
 * </p>
 */
public class Car {

    private int nr_id;      //!< ID samochodu.
    private int us_id;      //!< ID użytkownika, który jest właścicielem samochodu.
    private String marka;   //!< marka samochodu.
    private String model;   //!< model samochodu.
    private String nr_rej;  //!< numer rejestracyjny samochodu.
    private double silnik;  //!< rozmiar silnika samochodu.
    private int przebieg;   //!< przebieg samochodu.
    private String kolor;   //!< kolor samochodu.
    private String paliwo;  //!< paliwo samochodu.
    private int rok;        //!< rok samochodu.

    /**
     *  Car Konstruktor
     * <p>
     *     Inicjalizuje prywatne zmienne.
     * </p>
     *
     * @param nr_id ID samochodu.
     * @param us_id ID użytkownika, który jest właścicielem samochodu.
     * @param marka  marka samochodu.
     * @param model model samochodu.
     * @param nr_rej numer rejestracyjny samochodu.
     * @param silnik rozmiar silnika samochodu.
     * @param przebieg przebieg samochodu.
     * @param kolor kolor samochodu.
     * @param paliwo  paliwo samochodu.
     * @param rok rok samochodu.
     */
    public Car(int nr_id, int us_id, String marka, String model, String nr_rej, double silnik, int przebieg, String kolor, String paliwo, int rok)
    {
        this.nr_id = nr_id;
        this.us_id = us_id;
        this.marka = marka;
        this.model = model;
        this.nr_rej = nr_rej;
        this.silnik = silnik;
        this.przebieg = przebieg;
        this.kolor = kolor;
        this.paliwo = paliwo;
        this.rok = rok;
    }

    /**
     * Car Konstruktor
     * <p>
     *     Inicjalizuje prywatne zmienne.
     * </p>
     *
     * @param nr_id ID samochodu.
     * @param marka marka samochodu.
     * @param model model samochodu.
     * @param nr_rej numer rejestracyjny samochodu.
     * @param rok rok samochodu.
     */
    public Car(int nr_id, String marka, String model, String nr_rej, int rok){
        this.nr_id = nr_id;
        this.marka = marka;
        this.model = model;
        this.nr_rej = nr_rej;
        this.rok = rok;
    }

    /**
     * Nadaje dostęp do zmiennej ID samochodu
     * @return ID samochodu
     */
    public int getNr_id() {
        return nr_id;
    }
    /**
     * Nadaje dostęp do zmiennej ID użytkownika, który jest właścicielem samochodu.
     * @return ID użytkownika, który jest właścicielem samochodu.
     */
    public int getUs_id() {
        return us_id;
    }
    /**
     * Nadaje dostęp do zmiennej marka samochodu
     * @return marka samochodu
     */
    public String getMarka() {
        return marka;
    }
    /**
     * Nadaje dostęp do zmiennej model samochodu
     * @return model samochodu
     */
    public String getModel() {
        return model;
    }
    /**
     * Nadaje dostęp do zmiennej numer rejestracyjny samochodu
     * @return numer rejestracyjny samochodu
     */
    public String getNr_rej() {
        return nr_rej;
    }
    /**
     * Nadaje dostęp do zmiennej rozmiar silnika samochodu
     * @return rozmiar silnika samochodu
     */
    public double getSilnik() {
        return silnik;
    }
    /**
     * Nadaje dostęp do zmiennej przebieg samochodu
     * @return przebieg samochodu
     */
    public int getPrzebieg() {
        return przebieg;
    }
    /**
     * Nadaje dostęp do zmiennej kolor samochodu
     * @return kolor samochodu
     */
    public String getKolor() {
        return kolor;
    }
    /**
     * Nadaje dostęp do zmiennej paliwo samochodu
     * @return paliwo samochodu
     */
    public String getPaliwo() {
        return paliwo;
    }
    /**
     * Nadaje dostęp do zmiennej rok samochodu
     * @return rok samochodu
     */
    public int getRok() {
        return rok;
    }
}
