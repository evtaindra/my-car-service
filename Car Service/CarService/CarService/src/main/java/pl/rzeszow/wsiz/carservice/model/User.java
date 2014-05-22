package pl.rzeszow.wsiz.carservice.model;

import java.util.ArrayList;

/**
 * Created by opryima on 2014-05-12.
 */
public class User extends BaseListItem {

    private int id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private int sex;
    private String birth;
    private String nr_tel;
    private String email;
    private String city;
    private String adress;
    private ArrayList<Service> contactedServices;

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
    public User(){
    }

    public User(int id,String fname,String lname){
        this.id = id;
        this.name = fname;
        this.surname = lname;
        contactedServices = new ArrayList<Service>();
    }

    @Override
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public int getSex() {
        return sex;
    }

    public String getBirth() {
        return birth;
    }

    public String getNr_tel() {
        return nr_tel;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getAdress() {
        return adress;
    }

    public void addContactedService(Service s){
        contactedServices.add(s);
    }

    @Override
    public int getContactCount() {
        return contactedServices.size();
    }

    @Override
    public BaseListItem getContact(int position) {
        return contactedServices.get(position);
    }
}
