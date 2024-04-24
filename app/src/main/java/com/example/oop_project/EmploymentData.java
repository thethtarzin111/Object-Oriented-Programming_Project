package com.example.oop_project;
//This is the employment data class where we initialize values related to employment data.
public class EmploymentData {
    double employmentRate;
    int year;
    public EmploymentData(int year, double employmentRate) {
        this.employmentRate = employmentRate;
        this.year = year;
    }
    public double getEmploymentRate() {
        return employmentRate;
    }
    public void setEmploymentRate(double employmentRate) {
        this.employmentRate = employmentRate;
    }
    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }
}
