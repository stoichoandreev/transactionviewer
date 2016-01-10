package test.badoo.sniper.badootest.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import test.badoo.sniper.badootest.constants.Preferences;

/**
 * Created by sniper on 1/9/16.
 */
public class Rate implements Serializable{
    @SerializedName(Preferences.FROM)
    private String from;
    @SerializedName(Preferences.TO)
    private String to;
    @SerializedName(Preferences.RATE)
    private String rate;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getRate() {
        return rate;
    }
    public float getFloatRate(){
        try {
            return Float.parseFloat(rate);
        }catch (NumberFormatException ex){
            return 0.0f;
        }
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
