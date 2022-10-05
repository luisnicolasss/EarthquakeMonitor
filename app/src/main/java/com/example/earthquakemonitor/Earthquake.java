package com.example.earthquakemonitor;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "earthquakes") //Entity nos permite usar la clase a la base de datos
public class Earthquake {

    @PrimaryKey //PrimaryKey es con lo que va a identificar los terremotos con el id
    @NonNull
    private final String id;
    private final String place;
    private final double magnitude;
    private final long time;
    private final double latitude;
    private final double longitude;

    public Earthquake(String id, String place, double magnitude, long time, double latitude, double longitude) {
        this.id = id;
        this.place = place;
        this.magnitude = magnitude;
        this.time = time;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public String getPlace() {
        return place;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public long getTime() {
        return time;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Earthquake that = (Earthquake) o;
        return Double.compare(that.magnitude, magnitude) == 0 && time == that.time && Double.compare(that.latitude, latitude) == 0 && Double.compare(that.longitude, longitude) == 0 && id.equals(that.id) && place.equals(that.place);
    } //El equals compara que todos los campos que tenemos aqui sean iguales
      //Para el terremoto viejo y para el terremoto nuevo

    @Override
    public int hashCode() {
        return Objects.hash(id, place, magnitude, time, latitude, longitude);
    }
}
