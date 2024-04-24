package com.example.oop_project;

//Here, for comparing the cities, a fragment is implemented.A lot of data about two cities can be compared
//but since i'm using many text views instead of recycler view, only population, work, and employment rate
//are compared because if there are more text views, the app loads longer and lags.
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CompareFragment extends Fragment  implements View.OnClickListener {

    private EditText editTextCity1; //initialize variable for search box which the user type the first city
    private EditText editTextCity2; //initialize variable for search box which the user type the first city
    Button buttonCompare; //variable of button for comparing

    //Below, we have separate text views like population, work self-sufficiency and employment rate for each city
    private TextView city1Population;
    private TextView city2Population;
    private TextView city1Work;
    private TextView city2Work;
    private TextView city1EmploymentRate;
    private TextView city2EmploymentRate;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_compare, container, false);
        //Here, we have to match the id of the components we have in fragment_compare.xml file
        editTextCity1 = view.findViewById(R.id.search_city1);
        editTextCity2 = view.findViewById(R.id.search_city2);
        buttonCompare = view.findViewById(R.id.compareBtn);
        city1Population = view.findViewById(R.id.c1Population);
        city2Population = view.findViewById(R.id.c2Population);
        city1Work = view.findViewById(R.id.c1Work);
        city2Work = view.findViewById(R.id.c2Work);
        city1EmploymentRate = view.findViewById(R.id.c1Employ);
        city2EmploymentRate = view.findViewById(R.id.c2Employ);

        buttonCompare.setOnClickListener(this);
        return view;
    }


    //This is to make sure the functions work when the user click on the compare button.
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        if (clickedButton.getId() == R.id.compareBtn) {
            CompareFragment context = this;
            CompareDataRetriever compareDataRetriever = new CompareDataRetriever();

            ExecutorService service = Executors.newSingleThreadExecutor();
            service.execute(new Runnable() {
                @Override
                public void run() {


                    CompareDataRetriever.obtainMunicipalityCodeMap();
                    //Population
                    // Fetch data for city 1
                    String cityName1 = editTextCity1.getText().toString();
                    String cityName2 = editTextCity2.getText().toString();
                    ArrayList<PopulationData> populationDataCity1 = compareDataRetriever.obtainPopulationData(context, editTextCity1.getText().toString());
                    // Fetch data for city 2
                    ArrayList<PopulationData> populationDataCity2 = compareDataRetriever.obtainPopulationData(context, editTextCity2.getText().toString());
                    if (populationDataCity1 == null || populationDataCity2 == null) {
                        return;
                    }
                    //WorkplaceSelfSufficiencyData. Here WPSS stands for workplace self-sufficiency
                    ArrayList<WorkplaceSelfSufficiencyData> city1WPSS = compareDataRetriever.obtainWorkplaceAndEmploymentData(context, editTextCity1.getText().toString());
                    ArrayList<WorkplaceSelfSufficiencyData> city2WPSS = compareDataRetriever.obtainWorkplaceAndEmploymentData(context, editTextCity2.getText().toString());

                    //If either of the city data is absent, we will not get any value.
                    if (city1WPSS == null || city2WPSS == null) {
                        return;
                    }

                    //Employment
                    ArrayList<EmploymentData> city1Employment = compareDataRetriever.obtainEmploymentData(context, editTextCity1.getText().toString());
                    ArrayList<EmploymentData> city2Employment = compareDataRetriever.obtainEmploymentData(context, editTextCity2.getText().toString());




                    // Update UI with comparison results
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {


                            //Here, we code the texts that we want to see when we hit compare button.
                            String M1Population = cityName1+"\n"+"Population\n";
                            for (PopulationData data : populationDataCity1) {
                                M1Population = M1Population + data.getYear() + ": " + data.getPopulation() + "\n";
                            }
                            city1Population.setText(M1Population);

                            String M2Population = cityName2 +"\n"+ "Population\n";
                            for (PopulationData data : populationDataCity2) {
                                M2Population = M2Population + data.getYear() + ": " + data.getPopulation() + "\n";
                            }
                            city2Population.setText(M2Population);


                            //Work self-sufficiency of city 1
                            String M1WorkString = "Workplace self-sufficiency\n";

                            for (WorkplaceSelfSufficiencyData workdata : city1WPSS) {
                                M1WorkString = M1WorkString + workdata.getYear() + ": " + workdata.getWorkplaceSelfSufficiency() + "\n";
                            }
                            city1Work.setText(M1WorkString);



                            //Work self-sufficiency of city 2
                            String M2WorkString = "Workplace self-sufficiency\n";

                            for (WorkplaceSelfSufficiencyData workdata : city2WPSS) {
                                M2WorkString = M2WorkString + workdata.getYear() + ": " + workdata.getWorkplaceSelfSufficiency() + "\n";
                            }
                            city2Work.setText(M2WorkString);


                            //Employment rate of city 1
                            String M1Emp = "Employment Rate\n";

                            for (EmploymentData employmentData : city1Employment) {
                                M1Emp = M1Emp + employmentData.getYear() + ": " + employmentData.getEmploymentRate() + "\n";
                            }
                            city1EmploymentRate.setText(M1Emp);

                            String M2Emp = "Employment Rate\n";

                            //Employment rate of city 2
                            for (EmploymentData employmentData : city2Employment) {
                                M2Emp = M2Emp + employmentData.getYear() + ": " + employmentData.getEmploymentRate() + "\n";
                            }
                            city2EmploymentRate.setText(M2Emp);


                        }
                    });
                }
            });
        }
    }

    private void updateFragmentDarkMode(boolean isDarkMode) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof CompareFragment) {
                    ((CompareFragment) fragment).updateDarkMode(isDarkMode);
                }
            }
        }
    }

    //This is to make sure the layout, themes and colour changes correctly when the dark mode is on.
    private void updateDarkMode(boolean isDarkMode) {
        if (isDarkMode) {
            // Dark mode is enabled
            getView().setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.darkBlue)); // Set background color to black
            // Adjust text colors to be visible on dark background
            // For example:
            editTextCity1.setTextColor(Color.BLACK);
            editTextCity2.setTextColor(Color.BLACK);
            city1Population.setTextColor(Color.WHITE);
            city2Population.setTextColor(Color.WHITE);
            city1Work.setTextColor(Color.WHITE);
            city2Work.setTextColor(Color.WHITE);
            city1EmploymentRate.setTextColor(Color.WHITE);
            city2EmploymentRate.setTextColor(Color.WHITE);

        } else {
            // Dark mode is disabled
            getView().setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.LightBlue)); // Set background color to white
            editTextCity1.setTextColor(Color.BLACK);
            editTextCity2.setTextColor(Color.BLACK);
            city1Population.setTextColor(Color.WHITE);
            city2Population.setTextColor(Color.WHITE);
            city1Work.setTextColor(Color.WHITE);
            city2Work.setTextColor(Color.WHITE);
            city1EmploymentRate.setTextColor(Color.WHITE);
            city2EmploymentRate.setTextColor(Color.WHITE);
        }
    }
}

