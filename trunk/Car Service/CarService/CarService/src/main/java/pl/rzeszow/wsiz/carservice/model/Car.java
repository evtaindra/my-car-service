package pl.rzeszow.wsiz.carservice.model;

/**
 * Created by opryima on 2014-05-16.
 */
public class Car {

    private int nr_id;
    private int us_id;
    private String marka;
    private String model;
    private String nr_rej;
    private double silnik;
    private int przebieg;
    private String kolor;
    private String paliwo;
    private int rok;

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

    public Car(int nr_id, String marka, String model, String nr_rej, int rok){
        this.nr_id = nr_id;
        this.marka = marka;
        this.model = model;
        this.nr_rej = nr_rej;
        this.rok = rok;
    }

    public int getNr_id() {
        return nr_id;
    }

    public int getUs_id() {
        return us_id;
    }

    public String getMarka() {
        return marka;
    }

    public String getModel() {
        return model;
    }

    public String getNr_rej() {
        return nr_rej;
    }

    public double getSilnik() {
        return silnik;
    }

    public int getPrzebieg() {
        return przebieg;
    }

    public String getKolor() {
        return kolor;
    }

    public String getPaliwo() {
        return paliwo;
    }

    public int getRok() {
        return rok;
    }
}
