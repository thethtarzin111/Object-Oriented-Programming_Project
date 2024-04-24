package com.example.oop_project;

//This is the population data class where we initialize values related to population data.
public class PopulationData {

    private int year;
    private int population;



    public PopulationData(int year, int population) {
        this.year = year;
        this.population = population;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public int getPopulation() {
        return population;
    }
    public void setPopulation(int population) {
        this.population = population;
    }

}
