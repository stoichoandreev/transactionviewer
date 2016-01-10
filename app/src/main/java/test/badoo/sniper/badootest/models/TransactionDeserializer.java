package test.badoo.sniper.badootest.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sniper on 1/9/16.
 */
public class TransactionDeserializer implements JsonDeserializer<TransactionResponse> {

    @Override
    public TransactionResponse deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
            throws JsonParseException {
        Transaction[] transactions = null;
        try {
            transactions = context.deserialize(json, Transaction[].class);
//        List<Transaction> transactionList = new ArrayList<>(Arrays.asList(transactions));
        }catch (JsonParseException ex){
            transactions = new Transaction[0];
        }

        final TransactionResponse allData = new TransactionResponse();

        allData.setAllTransactions(transactions);

        return allData;
    }
}

