package com.example.oop_project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class CompareFragment extends Fragment  implements View.OnClickListener {

    private EditText editTextCity1;
    private EditText editTextCity2;
    Button buttonCompare;
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


                            String M1Population = cityName1+"\n"+"Population\n";
                            for (PopulationData data : populationDataCity1) {
                                M1Population = M1Population + data.getYear() + ": " + data.getPopulation() + "\n";
                            }
                            city1Population.setText(M1Population);
                            // Before updating the UI, print the contents of comparisonResults


                            String M2Population = cityName2 +"\n"+ "Population\n";
                            for (PopulationData data : populationDataCity2) {
                                M2Population = M2Population + data.getYear() + ": " + data.getPopulation() + "\n";
                            }
                            city2Population.setText(M2Population);

                            // Before updating the UI, print the contents of comparisonResults

                            //WorkSelfsuficiency of city 1
                            String M1WorkString = "Workplace self-sufficiency\n";

                            for (WorkplaceSelfSufficiencyData workdata : city1WPSS) {
                                M1WorkString = M1WorkString + workdata.getYear() + ": " + workdata.getWorkplaceSelfSufficiency() + "\n";
                            }
                            city1Work.setText(M1WorkString);

                            // Before updating the UI, print the contents of comparisonResults

                            //WorkSelfsuficiency of city 2
                            String M2WorkString = "Workplace self-sufficiency\n";

                            for (WorkplaceSelfSufficiencyData workdata : city2WPSS) {
                                M2WorkString = M2WorkString + workdata.getYear() + ": " + workdata.getWorkplaceSelfSufficiency() + "\n";
                            }
                            city2Work.setText(M2WorkString);


                            //Employment of city 1
                            String M1Emp = "Employment Rate\n";

                            for (EmploymentData employmentData : city1Employment) {
                                M1Emp = M1Emp + employmentData.getYear() + ": " + employmentData.getEmploymentRate() + "\n";
                            }
                            city1EmploymentRate.setText(M1Emp);

                            String M2Emp = "Employment Rate\n";

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
}

