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

    public Service(int id, String name, String city, String address, int rating, String description, Bitmap image, int us_id) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.rating = rating;
        this.description = description;
        this.image = image;
        this.us_id = us_id;
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
