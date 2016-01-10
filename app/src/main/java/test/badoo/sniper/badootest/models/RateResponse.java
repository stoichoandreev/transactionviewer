package test.badoo.sniper.badootest.models;

import java.util.List;

/**
 * Created by sniper on 1/9/16.
 */
public class RateResponse {
    private List<Rate> allRates;

    public List<Rate> getAllRates() {
        return allRates;
    }

    public void setAllRates(List<Rate> allRates) {
        this.allRates = allRates;
    }
}
