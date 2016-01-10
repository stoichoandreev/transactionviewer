package test.badoo.sniper.badootest.models;


/**
 * Created by sniper on 1/9/16.
 */
public class TransactionResponse {
    private Transaction[] allTransactions;

    public Transaction[] getAllTransactions() {
        return allTransactions;
    }

    public void setAllTransactions(Transaction[] allTransactions) {
        this.allTransactions = allTransactions;
    }
}
