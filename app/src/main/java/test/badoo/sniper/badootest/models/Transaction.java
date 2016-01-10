package test.badoo.sniper.badootest.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import test.badoo.sniper.badootest.constants.Preferences;
import test.badoo.sniper.badootest.graph.Algorithm;
import test.badoo.sniper.badootest.graph.Edge;
import test.badoo.sniper.badootest.graph.Graph;
import test.badoo.sniper.badootest.graph.Vertex;

/**
 * Created by sniper on 1/9/16.
 */
public class Transaction implements Serializable{
    @SerializedName(Preferences.AMOUNT)
    private String amount;
    @SerializedName(Preferences.SKU)
    private String sku;
    @SerializedName(Preferences.CURRENCY)
    private String currency;
    private float convertedAmountToGBP;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float getConvertedAmount(){
        return convertedAmountToGBP;
    }

    public void convertToGBP(){
        try {
            if (currency.equals(Preferences.GBP)) {
                convertedAmountToGBP = Float.parseFloat(amount);
            }else {
                Algorithm algorithm = new Algorithm();
                try {
                    algorithm.execute(Graph.getInstance().getVertexes().get(Graph.getNodeIndex(currency)));
                }catch (ArrayIndexOutOfBoundsException ex){
                    convertedAmountToGBP = 0.0f;
                    return;
                }
                LinkedList<Vertex> path = algorithm.getPath(Graph.getInstance().getVertexes().get(Graph.MY_CURRENCY_INDEX));
                float floatAmount = 0.0f;
                try {
                    floatAmount = Float.parseFloat(amount);
                }catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
                for(Vertex ver : path){
                    float edgeRate = algorithm.getRate().get(ver).floatValue();
                    if(edgeRate == 0.0f) continue;//do nothing
                    if(convertedAmountToGBP == 0.0f){
                        convertedAmountToGBP += (floatAmount * algorithm.getRate().get(ver).floatValue());
                    }else {
                        convertedAmountToGBP = (convertedAmountToGBP * algorithm.getRate().get(ver).floatValue());
                    }
                }
            }
        }catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }
}
