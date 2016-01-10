package test.badoo.sniper.badootest.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sniper on 1/9/16.
 */
public class RateDeserializer implements JsonDeserializer<RateResponse> {

    @Override
    public RateResponse deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
//        final JSONArray jsonObject = json.getAsJsonArray();
        List<Rate> ratesList = null;
        try {
            final Rate[] rates = context.deserialize(json, Rate[].class);
            ratesList = new ArrayList<>(Arrays.asList(rates));
        }catch (JsonParseException ex){
            ratesList = new ArrayList<>();//return empty to prevent crashes
        }

        final RateResponse allData = new RateResponse();

        allData.setAllRates(ratesList);

        return allData;
    }
}

