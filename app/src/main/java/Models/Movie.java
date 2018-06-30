package Models;


import java.io.Serializable;

public class Movie implements Serializable{

    private final String posterString;
    private final double rating;
    private final String plot;
    private final String title;
    private final String releaseDate;

    public Movie(String posterString, String title, double rating, String plot, String releaseDate) {
        this.posterString = posterString;
        this.title = title;
        this.rating = rating;
        this.plot = plot;
        this.releaseDate = releaseDate;
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

}
