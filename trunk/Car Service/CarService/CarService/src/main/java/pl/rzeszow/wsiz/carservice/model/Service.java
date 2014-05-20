package pl.rzeszow.wsiz.carservice.model;

import android.graphics.Bitmap;

/**
 * Created by rsavk_000 on 5/2/2014.
 */
public class Service {
    private int id;
    private String name;
    private String city;
    private String address;
    private double rating;
    private String description;
    private Bitmap image;
    private int us_id;
    private String phone;
    private String email;

    public Service(int id, String name, String city, String address, double rating, Bitmap image, int us_id) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.rating = rating;
        this.image = image;
        this.us_id = us_id;
    }

    public Service(int id, String name, String city, String address, double rating, Bitmap image, int us_id,
                   String description, String phone, String email) {
        this(id, name, city, address, rating, image, us_id);
        this.description = description;
        this.email = email;
        this.phone = phone;
    }

    public Service() {
    }

    public void setRating(double rating){
        this.rating = rating;
    }

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
}