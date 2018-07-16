package com.amirely.elite.popularmovies.data;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import models.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase{

    public abstract MovieDao movieDao();

}
