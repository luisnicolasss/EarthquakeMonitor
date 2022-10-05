package com.example.earthquakemonitor.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.earthquakemonitor.Earthquake;

import java.util.List;

@Dao //Data Access Object
public interface EqDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) //OnConflict por si hay un terremoto con el mismo nombre
    void insertAll(List<Earthquake> eqList);

    @Query("SELECT * FROM earthquakes") //Selecciona los datos de el Entity earthquake
    LiveData<List<Earthquake>> getEarthquakes();

    @Delete
    void deleteEarthquake(Earthquake earthquake); //Para borrar un terremoto

    @Update
    void updateEarthquake(Earthquake earthquake); //Para actualizar un terremoto

    //@Query("SELECT * FROM earthquakes WHERE magnitude > :myMagnitude") //Devuelve los terremotos cuya magnitud sean mayores a la que le pasamos abajo
    //List<Earthquake> getEarthquakesWithMagnitudeAbove( double myMagnitude);
}
