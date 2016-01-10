package test.badoo.sniper.badootest.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import test.badoo.sniper.badootest.BadooTestApp;
import test.badoo.sniper.badootest.activities.StartActivity;
import test.badoo.sniper.badootest.constants.Preferences;
import test.badoo.sniper.badootest.constants.StaticData;
import test.badoo.sniper.badootest.interfaces.JSONFileLoadListener;
import test.badoo.sniper.badootest.models.Product;
import test.badoo.sniper.badootest.models.RateDeserializer;
import test.badoo.sniper.badootest.models.RateResponse;
import test.badoo.sniper.badootest.models.Transaction;
import test.badoo.sniper.badootest.models.TransactionDeserializer;
import test.badoo.sniper.badootest.models.TransactionResponse;
import test.badoo.sniper.badootest.utils.AssetJsonUtil;

/**
 * Created by sniper on 01/09/16.
 */
public class LoadAllApplicationData extends AsyncTask<String, String, Boolean> {
    private static String TAG = LoadAllApplicationData.class.getCanonicalName();

    private JSONFileLoadListener listener;
    private Context context;
//    private List<Rate> rates;//this will hold all rates from json
//    private List<Transaction> transactions;//this will hold all transaction from json
    private List<Product> products;//this will hold only products for adapter lists
    private HashMap<String,List<Transaction>> allProductsTransactions;// this will hold all transactions in separate lists by key sku value

    public LoadAllApplicationData(JSONFileLoadListener listener){
        this.listener = listener;
        this.allProductsTransactions = new HashMap<>();
        this.products = new ArrayList<>();
        if(listener instanceof StartActivity) {
            this.context = ((StartActivity) listener).getApplicationContext();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected Boolean doInBackground(String... params) {
        try {
            return manageCurrency();
        }catch (IOException e) {
            Log.e(TAG, "Exception LoadAllApplicationData = "+e);
            return false;
        } catch (JSONException e) {
            Log.e(TAG, "Exception LoadAllApplicationData = "+e);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (listener != null) {
            if (result) {
                listener.onLoadSuccess(allProductsTransactions,products);
            } else{
                listener.onLoadFailure("Some error");
            }
        }
    }

    private boolean manageCurrency() throws IOException,JSONException{
        if(context == null) return false;

        Log.d(TAG, "manageCurrency start");//take around 200 - 300 mls
        final String ratesFileName = Preferences.isTestDataTwo ? Preferences.RATES_FILE_NAME_TWO : Preferences.RATES_FILE_NAME;
        final String transactionFileName = Preferences.isTestDataTwo ? Preferences.TRANSACTIONS_FILE_NAME_TWO : Preferences.TRANSACTIONS_FILE_NAME;

        String ratesJSON = AssetJsonUtil.AssetJSONFile(ratesFileName, context.getAssets());
        String transactionJSON = AssetJsonUtil.AssetJSONFile(transactionFileName, context.getAssets());

        //deserialize rates
        final GsonBuilder ratesGsonBuilder = new GsonBuilder();
        ratesGsonBuilder.registerTypeAdapter(RateResponse.class, new RateDeserializer());
        final Gson ratesGson = ratesGsonBuilder.create();

        StaticData.getInstance().setRates(ratesGson.fromJson(ratesJSON, RateResponse.class).getAllRates());

        //deserialize tran
        final GsonBuilder transactionGsonBuilder = new GsonBuilder();
        transactionGsonBuilder.registerTypeAdapter(TransactionResponse.class, new TransactionDeserializer());
        final Gson transactionGson = transactionGsonBuilder.create();

//        transactions = transactionGson.fromJson(transactionJSON, TransactionResponse.class).getAllTransactions();

        getAllProductsTransactions(transactionGson.fromJson(transactionJSON, TransactionResponse.class).getAllTransactions());
        getAllProducts();

        Log.d(TAG, "manageCurrency end two lists are ready");

        return true;
    }
    private void getAllProductsTransactions(Transaction [] transactions){
        if(transactions != null){
            for(Transaction trans : transactions){
                if(checkDoesProductExist(trans.getSku())){
                    trans.convertToGBP();
                    allProductsTransactions.get(trans.getSku()).add(trans);
                }else {
                    allProductsTransactions.put(trans.getSku(), new ArrayList<Transaction>());
                }
            }
        }
    }
    private boolean checkDoesProductExist(String skuForCheck){
        return allProductsTransactions.containsKey(skuForCheck);
    }

    public void getAllProducts() {
        for (Map.Entry<String, List<Transaction>> entry : allProductsTransactions.entrySet()) {
            Product newProduct = new Product(entry.getKey(),entry.getValue().size());
            products.add(newProduct);
        }
    }
}
