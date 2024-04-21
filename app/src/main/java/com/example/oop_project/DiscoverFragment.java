package com.example.oop_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscoverFragment extends Fragment {


    private TextView txtPopulation;
    private TextView txtWeather;

    private EditText editMunicipalityName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);


        txtPopulation = view.findViewById(R.id.txtPopulation);
        txtWeather = view.findViewById(R.id.txtWeather);
        editMunicipalityName = view.findViewById(R.id.search_bar);

        return view;

    }

    public void onSearchButtonClick(View view) {
        DiscoverFragment discoverFragment = this;
        MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();
        WeatherDataRetriever weatherDataRetriever = new WeatherDataRetriever();

        // Here we fetch the municipality data in a background service, so that we do not disturb the UI
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(new Runnable() {
                            @Override
                            public void run() {
                                MunicipalityDataRetriever.getMunicipalityCodesMap();
                                ArrayList<PopulationData> populationDataArrayList = municipalityDataRetriever.getPopulationData(getContext(), editMunicipalityName.getText().toString());

                                if (populationDataArrayList == null) {
                                    return;
                                }

                                WeatherData weatherData = weatherDataRetriever.getData(editMunicipalityName.getText().toString());

                                // When we want to update values we got from the API to the UI, we must do it inside runOnUiThread -method
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String dataString = "";
                                        for (PopulationData data: populationDataArrayList) {
                                            dataString = dataString + data.getYear() + ": " + data.getPopulation() + "\n";
                                        }
                                        txtPopulation.setText(dataString);


                                        String weatherDataAsString = weatherData.getName() + "\n" +
                                                "Weather now: " + weatherData.getMain() + "(" + weatherData.getDescription() + ")\n" +
                                                "Temperature: " + weatherData.getTemperature() + "\n" +
                                                "Wind speed: " + weatherData.getWindSpeed() + "\n";

                                        txtWeather.setText(weatherDataAsString);
                                    }
                                });                            }
                        }
        );
    }




}
