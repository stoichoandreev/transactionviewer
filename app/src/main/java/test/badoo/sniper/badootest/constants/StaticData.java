package test.badoo.sniper.badootest.constants;

import java.util.List;

import test.badoo.sniper.badootest.models.Rate;

/**
 * Created by sniper on 1/9/16.
 */
public class StaticData {
    private static StaticData mInstance;
    private List<Rate> rates;

    private StaticData(){}

    public static StaticData getInstance(){
        if(mInstance == null){
            mInstance = new StaticData();
        }
        return mInstance;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}
