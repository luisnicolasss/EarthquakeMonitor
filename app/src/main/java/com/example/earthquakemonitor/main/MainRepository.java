package com.example.earthquakemonitor.main;

import androidx.lifecycle.LiveData;

import com.example.earthquakemonitor.Earthquake;
import com.example.earthquakemonitor.api.EarthquakeJSONResponse;
import com.example.earthquakemonitor.api.EqApiClient;
import com.example.earthquakemonitor.api.Feature;
import com.example.earthquakemonitor.database.EqDatabase;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainRepository {

    /*public interface DownloadEqsListener {
        void onEqsDowloaded(List<Earthquake> earthquakeList);
    }*/

    private final EqDatabase database;

    public interface DownloadStatusListener {
        void downloadSuccess();
        void downloadError(String message);
    }

    public MainRepository(EqDatabase database) {
        this.database = database;
    }

    public LiveData<List<Earthquake>> getEqList() {
        return database.eqDao().getEarthquakes();
    }

    public void downloadAndSaveEarthquakes(DownloadStatusListener downloadStatusListener) {
        /*ArrayList<Earthquake> eqList = new ArrayList<>();
        eqList.add(new Earthquake("sdfsfdv323", "Buenos Aires", 5.0, 4234324L, 105.23, 98.127));
        eqList.add(new Earthquake("sadsdsadf", "Ciudad de Mexico", 4.0, 4234324L, 105.23, 98.127));
        eqList.add(new Earthquake("vcbcnbc234", "Lima", 1.6, 4234324L, 105.23, 98.127));
        eqList.add(new Earthquake("uyiiyr3445", "Madrid", 2.7, 4234324L, 105.23, 98.127));
        eqList.add(new Earthquake("kljjklj8965", "Caracas", 3.2, 4234324L, 105.23, 98.127));*/
        EqApiClient.EqService service = EqApiClient.getInstance().getService();

        /*service.getEarthquakes().enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                List<Earthquake> earthquakeList = parseEarthquakes(response.body());

                eqList.setValue(earthquakeList);
            } //Si fue exitoso el request
              //Response trae todos los datos para los terremotos

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            } //Si hubo un error
        }); //enqueue agrega el request a una cola que es la que va a traer los datos que precisamos

        //this.eqList.setValue(eqList);
    }*/

        service.getEarthquakes().enqueue(new Callback<EarthquakeJSONResponse>() {
            @Override
            public void onResponse(Call<EarthquakeJSONResponse> call, Response<EarthquakeJSONResponse> response) {

                //List<Earthquake> earthquakeList = parseEarthquakes(response.body());
                List<Earthquake> earthquakeList = getEarthquakesWithMoshi(response.body());
                EqDatabase.databaseWriteExecutor.execute(() -> {
                    database.eqDao().insertAll(earthquakeList);
                });
                 //downloadEqsListener.onEqsDowloaded(earthquakeList);
                //eqList.setValue(earthquakeList);
                downloadStatusListener.downloadSuccess();
            } //Si fue exitoso el request
            //Response trae todos los datos para los terremotos

            @Override
            public void onFailure(Call<EarthquakeJSONResponse> call, Throwable t) {
                downloadStatusListener.downloadError("There was an error in downloading earthquakes, check internet connection.");
            } //Si hubo un error
        }); //enqueue agrega el request a una cola que es la que va a traer los datos que precisamos

        //this.eqList.setValue(eqList);
    }

    private List<Earthquake> getEarthquakesWithMoshi(EarthquakeJSONResponse body) {
        ArrayList<Earthquake> eqList = new ArrayList<>();

        List<Feature> features = body.getFeatures();
        for(Feature feature: features) {
            String id = feature.getId();
            double magnitude = feature.getProperties().getMag();
            String place = feature.getProperties().getPlace();
            long time = feature.getProperties().getTime();

            double longitude = feature.getGeometry().getLongitude();
            double latitude = feature.getGeometry().getLatitude();
            Earthquake earthquake = new Earthquake(id, place, magnitude, time, longitude, latitude);
            eqList.add(earthquake);
        }

        return eqList;
    }
}
