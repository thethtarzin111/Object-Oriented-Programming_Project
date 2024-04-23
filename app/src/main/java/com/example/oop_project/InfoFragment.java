package com.example.oop_project;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;



import net.bytebuddy.implementation.Implementation;

import java.util.ArrayList;

public class InfoFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Integer> integerArrayList;
    private RecyclerViewAdapter recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        
        recyclerView = view.findViewById(R.id.recyclerView);
        
        integerArrayList = new ArrayList<>();
        integerArrayList.add(R.drawable.helsinki);
        integerArrayList.add(R.drawable.rovaniemi);
        integerArrayList.add(R.drawable.turku);
        integerArrayList.add(R.drawable.tampere);
        integerArrayList.add(R.drawable.oulu);
        integerArrayList.add(R.drawable.savonlinna);

        recyclerViewAdapter = new RecyclerViewAdapter(getContext(), integerArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(recyclerViewAdapter);
        return view;
    }

    }
