package com.example.earthquakemonitor.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.earthquakemonitor.api.EqApiClient;
import com.example.earthquakemonitor.api.RequestStatus;
import com.example.earthquakemonitor.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MainViewModel viewModel = new ViewModelProvider(this, new MainViewModelFactory(getApplication())).get(MainViewModel.class);



        binding.eqRecycler.setLayoutManager(new LinearLayoutManager(this));

        /*ArrayList<Earthquake> eqList = new ArrayList<>();
        eqList.add(new Earthquake("sdfsfdv323", "Buenos Aires", 5.0, 4234324L, 105.23, 98.127));
        eqList.add(new Earthquake("sadsdsadf", "Ciudad de Mexico", 4.0, 4234324L, 105.23, 98.127));
        eqList.add(new Earthquake("vcbcnbc234", "Lima", 1.6, 4234324L, 105.23, 98.127));
        eqList.add(new Earthquake("uyiiyr3445", "Madrid", 2.7, 4234324L, 105.23, 98.127));
        eqList.add(new Earthquake("kljjklj8965", "Caracas", 3.2, 4234324L, 105.23, 98.127));
        */

        EqAdapter adapter = new EqAdapter();
        //Si utilizamos this en un Override debemos poner explicitamente en que contexto estamos
        adapter.setOnItemClickLister(earthquake -> Toast.makeText(MainActivity.this, earthquake.getPlace(), Toast.LENGTH_SHORT).show());//Implementamos el setter del Onclicklistener creado en el adapter
        binding.eqRecycler.setAdapter(adapter);

        /*adapter.submitList(eqList);

        if(eqList.isEmpty()) {
            binding.emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyView.setVisibility(View.GONE);
        }*/

        viewModel.getEqList().observe(this, eqList -> {
            adapter.submitList(eqList);

            if(eqList.isEmpty()) {
                binding.emptyView.setVisibility(View.VISIBLE);
            } else {
                binding.emptyView.setVisibility(View.GONE);
            }

        });

        viewModel.getStatusMutableLiveData().observe(this, statusWithDescription -> {
            if(statusWithDescription.getStatus() == RequestStatus.LOADING) {
                binding.loadingWheel.setVisibility(View.VISIBLE);
            } else {
               binding.loadingWheel.setVisibility(View.GONE);
            }

            if(statusWithDescription.getStatus() == RequestStatus.ERROR) {
                Toast.makeText(this, statusWithDescription.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.downloadEarthquakes();

        EqApiClient.getInstance().getService().getEarthquakes(); //Esto se realiza en un ViewModel
    }
}