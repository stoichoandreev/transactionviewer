package test.badoo.sniper.badootest.interfaces;


import java.util.HashMap;
import java.util.List;

import test.badoo.sniper.badootest.models.Product;
import test.badoo.sniper.badootest.models.Transaction;

/**
 * Created by sniper on 01/09/16.
 */
public interface JSONFileLoadListener {
    void onLoadSuccess(HashMap<String,List<Transaction>> productsTransactions,List <Product> allProducts);
    void onLoadFailure(String error);
}
