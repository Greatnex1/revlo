package com.nouah.revlo.ai.model;


import org.springframework.context.annotation.Configuration;
import smile.regression.OLS;

import java.util.List;

@Configuration
public class DemandPredictor {
    private OLS model;

    public DemandPredictor(List<Integer> sales) {
        // Convert List to Smile-compatible double arrays
        double[][] X = new double[sales.size()][1]; // one feature: week index
        double[] y = new double[sales.size()]; // target: quantity sold

        for (int i = 0; i < sales.size(); i++) {
            X[i][0] = i;           // week index
            y[i] = sales.get(i);   // sales value
        }

        // Train a simple linear regression model
        model = new OLS();
    }

    public int predictNextWeek() {
        double prediction = Double.parseDouble(model.toString()); // next week index
        return (int) Math.round(prediction * 1.2); // add buffer
    }
}
