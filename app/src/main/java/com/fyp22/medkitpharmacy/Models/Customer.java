package com.fyp22.medkitpharmacy.Models;

import java.io.Serializable;
import java.util.List;

public class Customer extends Person implements Serializable {

    public Customer() {
    }

    public Customer(String bloodGroup, List<String> currentMedication) {
        this.bloodGroup = bloodGroup;
        this.currentMedication = currentMedication;
    }

    String bloodGroup;
    List<String> currentMedication;

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public List<String> getCurrentMedication() {
        return currentMedication;
    }

    public void setCurrentMedication(List<String> currentMedication) {
        this.currentMedication = currentMedication;
    }
}
