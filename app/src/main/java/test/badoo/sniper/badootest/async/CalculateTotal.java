package test.badoo.sniper.badootest.async;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import test.badoo.sniper.badootest.interfaces.CalculateListener;
import test.badoo.sniper.badootest.models.Transaction;

/**
 * Created by sniper on 01/09/16.
 */
public class CalculateTotal extends AsyncTask<String, String, Boolean> {
    private static String TAG = CalculateTotal.class.getCanonicalName();

    private CalculateListener listener;
    private List<Transaction> transactions;
    private float total;

    public CalculateTotal(CalculateListener listener,List<Transaction> trans){
        this.listener = listener;
        this.transactions = trans;
    }

    @Override
    protected void onPreExecute() {//block UI
        super.onPreExecute();
    }
    @Override
    protected Boolean doInBackground(String... params) {
        try {
            return calculate();
        }catch (Exception ex){
            Log.e(TAG, "Exception CalculateTotal = "+ex);
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (listener != null) {
            if (result) {
                listener.onCalculateSuccess(total);
            } else{
                listener.onCalculateFailure();
            }
        }
    }

    private boolean calculate() throws Exception{
        Log.d(TAG, "calculate total start");
        for(Transaction tr : transactions){
            total += tr.getConvertedAmount();
        }

        Log.d(TAG, "calculate total end");
        return true;
    }
}
