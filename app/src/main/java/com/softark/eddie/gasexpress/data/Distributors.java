package com.softark.eddie.gasexpress.data;

import com.softark.eddie.gasexpress.models.Distributor;

import java.util.ArrayList;

/**
 * Created by Eddie on 4/16/2017.
 */

public class Distributors {

    ArrayList<Distributor> distributors = new ArrayList<>();

    public Distributors() {
        Distributor[] distributors1 = new Distributor[4];
        String[] distributorNames = {"K-Gas", "National Oil", "Total", "Hashi"};

        for (int i = 0; i < distributors1.length; i++) {
            distributors1[i] = new Distributor();
            distributors1[i].setName(distributorNames[i]);
            distributors.add(distributors1[i]);
        }
    }

    public ArrayList<Distributor> getDistributors() {
        return distributors;
    }

    public void setDistributors(ArrayList<Distributor> distributors) {
        this.distributors = distributors;
    }
}
