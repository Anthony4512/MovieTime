package models;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.io.Serializable;


@Entity
public class Movie implements Serializable{

    @PrimaryKey
    private final @NonNull String id;

    @ColumnInfo
    private final String posterString;
    @ColumnInfo
    private final double rating;
    @ColumnInfo
    private final String plot;
    @ColumnInfo
    private final String title;
    @ColumnInfo
    private final String releaseDate;
    @ColumnInfo
    private String trailerId;


    public Movie(@NonNull String id, String posterString, String title, double rating, String plot, String releaseDate) {
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
