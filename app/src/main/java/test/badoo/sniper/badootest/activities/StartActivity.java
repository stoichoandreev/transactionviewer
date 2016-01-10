package test.badoo.sniper.badootest.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.badoo.sniper.badootest.R;
import test.badoo.sniper.badootest.adapters.ProductsAdapter;
import test.badoo.sniper.badootest.async.LoadAllApplicationData;
import test.badoo.sniper.badootest.constants.Preferences;
import test.badoo.sniper.badootest.graph.Graph;
import test.badoo.sniper.badootest.interfaces.AdapterListener;
import test.badoo.sniper.badootest.interfaces.JSONFileLoadListener;
import test.badoo.sniper.badootest.models.Product;
import test.badoo.sniper.badootest.models.Rate;
import test.badoo.sniper.badootest.models.Transaction;

public class StartActivity extends MyBaseActivity implements View.OnClickListener,JSONFileLoadListener,AdapterListener {

    @Bind(R.id.action_button) FloatingActionButton actionButton;
    @Bind(R.id.toolbar) Toolbar toolbar ;
    @Bind(R.id.start_activity_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.no_content) TextView noContentText;
    private ProductsAdapter adapter;
    private HashMap<String, List<Transaction>> productsTransactions;
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productsTransactions = new HashMap<>();
        getRatesData();
        setContentView(R.layout.start_activity_layout);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        adapter = new ProductsAdapter(this,this,ProductsAdapter.ProductAdapterType.TYPE_PRODUCT_ITEM);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        actionButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_update :
                getRatesData();
                return true;
            case R.id.action_swich_test_data_source:
                Preferences.isTestDataTwo = (!Preferences.isTestDataTwo);
                final String toastMessage = "Switch to test data number "+ (Preferences.isTestDataTwo ? "2" : "1");
                Toast.makeText(this,toastMessage,Toast.LENGTH_SHORT).show();
                Graph.getInstance().reset();
                selectedPosition = 0;
                getRatesData();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_button:
                //if not items don't do anything
                if(adapter.getItemCount() == 0) return;

                Intent intent = new Intent(this,OtherActivity.class);
                if(productsTransactions.containsKey(adapter.getProductName(selectedPosition))) {
                    intent.putExtra(Preferences.SELECTED_PRODUCT, (ArrayList)productsTransactions.get(adapter.getProductName(selectedPosition)));
                    intent.putExtra(Preferences.SELECTED_PRODUCT_NAME, adapter.getProductName(selectedPosition));
                }else{
                    intent.putExtra(Preferences.SELECTED_PRODUCT, new ArrayList<>());
                    intent.putExtra(Preferences.SELECTED_PRODUCT_NAME, "");
                }
                String transitionName = getString(R.string.transition_action_button_name);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, actionButton, transitionName);
                ActivityCompat.startActivity(StartActivity.this, intent, options.toBundle());

                break;
        }
    }

    public void getRatesData() {
        getProgressDialog().show();
        new LoadAllApplicationData(this).execute();
    }


    @Override
    public void onLoadSuccess(HashMap<String, List<Transaction>> productsTransactions, List<Product> allProducts) {
        getProgressDialog().dismiss();
        this.productsTransactions.clear();
        this.productsTransactions.putAll(productsTransactions);

        noContentText.setVisibility(View.GONE);
        if(allProducts == null || allProducts.size() == 0){
            noContentText.setVisibility(View.VISIBLE);
        }
        adapter.setProducts(allProducts, true);
    }

    @Override
    public void onLoadFailure(String error) {
        getProgressDialog().dismiss();
        if(adapter.getItemCount() == 0){
            noContentText.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onAdapterItemSelected(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        actionButton.performClick();
    }
}
