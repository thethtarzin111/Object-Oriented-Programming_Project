package com.example.oop_project;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DiscoverFragment extends Fragment implements View.OnClickListener {


    private TextView txtPopulation;
    private TextView txtWeather;
    private TextView txtWork;
    private TextView txtEmployment;
    private ImageView weatherIcon;

    private EditText editMunicipalityName;
    Button searchBtn;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_discover, container, false);


        weatherIcon = view.findViewById(R.id.weatherIcon);
        txtPopulation = view.findViewById(R.id.txtPopulation);
        txtWeather = view.findViewById(R.id.txtWeather);
        txtWork = view.findViewById(R.id.txtWorkSelfSufficiency);
        txtEmployment = view.findViewById(R.id.txtEmployment);
        editMunicipalityName = view.findViewById(R.id.search_bar);
        searchBtn = view.findViewById(R.id.search_button);

        searchBtn.setOnClickListener(this);

        return view;
    }


    public void onClick(View v) {
        Button clickedButton = (Button) v;
        if (clickedButton.getId() == R.id.search_button) {

            DiscoverFragment context = this;
            MunicipalityDataRetriever municipalityDataRetriever = new MunicipalityDataRetriever();
            WeatherDataRetriever weatherDataRetriever = new WeatherDataRetriever();

            // Here we fetch the municipality data in a background service, so that we do not disturb the UI
            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new Runnable() {
                                @Override
                                public void run() {
                                    MunicipalityDataRetriever.getMunicipalityCodesMap();
                                    ArrayList<PopulationData> municipalityDataArrayList = municipalityDataRetriever.getPopulationData(context, editMunicipalityName.getText().toString());

                                    if (municipalityDataArrayList == null) {
                                        return;
                                    }

                                    ArrayList<WorkplaceSelfSufficiencyData> workplaceSelfSufficiencyDataArrayList = municipalityDataRetriever.getWorkplaceAndEmploymentData(context, editMunicipalityName.getText().toString());

                                    if (workplaceSelfSufficiencyDataArrayList == null) {
                                        return;
                                    }

                                    ArrayList<EmploymentData> employmentDataArrayList = municipalityDataRetriever.getEmploymentData(context, editMunicipalityName.getText().toString());

                                    if (employmentDataArrayList == null) {
                                        return;
                                    }

                                    WeatherData weatherData = weatherDataRetriever.getData(editMunicipalityName.getText().toString());

                                    // When we want to update values we got from the API to the UI, we must do it inside runOnUiThread -method
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            String populationString = "Population Changes During the Past Three Years\n";

                                            for (PopulationData data : municipalityDataArrayList) {
                                                populationString = populationString + data.getYear() + ": " + data.getPopulation() + "\n";
                                            }
                                            txtPopulation.setText(populationString);

                                            String workString = "Workplace self-sufficiency\n";

                                            for (WorkplaceSelfSufficiencyData workdata : workplaceSelfSufficiencyDataArrayList) {
                                                workString = workString + workdata.getYear() + ": " + workdata.getWorkplaceSelfSufficiency() + "\n";
                                            }

                                            txtWork.setText(workString);
                                            String employmentString = "Employment Rate\n";

                                            for (EmploymentData employmentData : employmentDataArrayList) {
                                                employmentString = employmentString + employmentData.getYear() + ": " + employmentData.getEmploymentRate() + "\n";
                                            }

                                            txtEmployment.setText(employmentString);


                                            String weatherDataAsString = weatherData.getName() + "\n" +
                                                    "Weather now: " + weatherData.getMain() + "(" + weatherData.getDescription() + ")\n" +
                                                    "Temperature: " + weatherData.getTemperature() + " °C\n" +
                                                    "Wind speed: " + weatherData.getWindSpeed() + "\n";

                                            txtWeather.setText(weatherDataAsString);

                                            String weatherCondition = weatherData.getMain();
                                            int iconResource;
                                            switch (weatherCondition) {
                                                case "Sun":
                                                    iconResource = R.drawable.sunny;
                                                    break;
                                                case "Rain":
                                                    iconResource = R.drawable.rain;
                                                    break;
                                                case "Clouds":
                                                    iconResource = R.drawable.cloud;
                                                    break;
                                                case "Snow":
                                                    iconResource = R.drawable.snow;
                                                    break;
                                                // Add more cases for other weather conditions
                                                default:
                                                    iconResource = R.drawable.sunny;
                                                    break;
                                            }

                                            weatherIcon.setImageResource(iconResource);
                                        }
                                    });
                                }

                                private void updateFragmentDarkMode(boolean isDarkMode) {
                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    List<Fragment> fragments = fragmentManager.getFragments();
                                    if (fragments != null) {
                                        for (Fragment fragment : fragments) {
                                            if (fragment instanceof DiscoverFragment) {
                                                ((DiscoverFragment) fragment).updateDarkMode(isDarkMode);
                                            }
                                        }
                                    }
                                }


                            }
            );
        }
    }

    private void updateDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            // Dark mode is enabled
            getView().setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue));
        } else {
            getView().setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.LightBlue)); // Set background color to white
        }
    }
}

