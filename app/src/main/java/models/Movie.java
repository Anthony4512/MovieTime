package models;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;


@Entity
public class Movie implements Serializable{

    @PrimaryKey
    private final @NonNull String id;
    private final String posterString;
    private final double rating;
    private final String plot;
    private final String title;
    private final String releaseDate;
    private String trailerId;


    public Movie(String id, String posterString, String title, double rating, String plot, String releaseDate) {
        this.posterString = posterString;
        this.title = title;
        this.rating = rating;
        this.plot = plot;
        this.releaseDate = releaseDate;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public double getRating() {
        return rating;
    }

    public String getPlot() {
        return plot;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterString() {
        return posterString;
    }

    public String getTrailerId() {
        return trailerId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public String getId() {
        return id;
    }
}
