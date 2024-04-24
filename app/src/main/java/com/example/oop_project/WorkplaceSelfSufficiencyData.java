package com.example.oop_project;
//This is where we initialize workplace self-sufficiency attributes,
public class WorkplaceSelfSufficiencyData {
    double workplaceSelfSufficiency;

    private int year;

    public WorkplaceSelfSufficiencyData(int year, double workplaceSelfSufficiency) {
        this.year = year;
        this.workplaceSelfSufficiency = workplaceSelfSufficiency;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public double getWorkplaceSelfSufficiency() {
        return workplaceSelfSufficiency;
    }
    public void setWorkplaceSelfSufficiency(float workplaceSelfSufficiency) {
        this.workplaceSelfSufficiency = workplaceSelfSufficiency;
    }
}
