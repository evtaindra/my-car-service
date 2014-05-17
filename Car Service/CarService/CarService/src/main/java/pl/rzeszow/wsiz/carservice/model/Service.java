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
    private int rating;
    private String description;
    private Bitmap image;
    private int us_id;
    private String phone;
    private String email;

    public Service(int id, String name, String city, String address, int rating, Bitmap image, int us_id) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.rating = rating;
        this.image = image;
        this.us_id = us_id;
    }

    public Service(int id, String name, String city, String address, int rating, String description, Bitmap image, int us_id,
                   String phone, String email) {
        this(id,name,city,address,rating,image,us_id);
        this.description = description;
        this.email = email;
        this.phone = phone;
    }

    public Service(){
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

    public int getRating() {
        return rating;
    }

    public void updateRating(int rating){
        this.rating = (this.rating + rating)/2;
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
}
