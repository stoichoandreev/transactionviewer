package test.badoo.sniper.badootest.activities;

import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.badoo.sniper.badootest.adapters.ProductsAdapter;
import test.badoo.sniper.badootest.async.CalculateTotal;
import test.badoo.sniper.badootest.constants.Preferences;
import test.badoo.sniper.badootest.interfaces.AdapterListener;
import test.badoo.sniper.badootest.interfaces.CalculateListener;
import test.badoo.sniper.badootest.models.Transaction;
import test.badoo.sniper.badootest.utils.HeightAnimation;
import test.badoo.sniper.badootest.R;

/**
 * Created by sniper on 1/9/16.
 */
public class OtherActivity extends MyBaseActivity implements Animation.AnimationListener, CalculateListener, AdapterListener {

    private static final String TAG = OtherActivity.class.getCanonicalName();
    private static final int CONTAINER_ANIMATION_SPEED = 1500;

    @Bind(R.id.other_activity_coordinator_layout) CoordinatorLayout coordinatorLayout;
    @Bind(R.id.list_container) RelativeLayout container;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.other_activity_recycler_view) RecyclerView recyclerView;
    @Bind(R.id.no_content) TextView noContentText;
    @Bind(R.id.toolbar_title) TextView toolbarTitle;
    @Bind(R.id.total_text) TextView totalText;

    private ProductsAdapter adapter;
    private int containerMaxHeight;
    private List<Transaction> allTransactions;
    private String selectedProductName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_activity_layout);

        getAllIntents();
        startConvertOperations();

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        adapter = new ProductsAdapter(this,this,ProductsAdapter.ProductAdapterType.TYPE_TRANSACTION_ITEM);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        toolbarTitle.setText(String.format(getResources().getString(R.string.transaction_to),selectedProductName));

        containerMaxHeight = getAvailableHeight();

        ViewTreeObserver vto = container.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                startContainerAnimation();

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    container.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {}
    @Override
    public void onAnimationRepeat(Animation animation) {}

    @Override
    public void onAnimationEnd(Animation animation) {
        startBackgroundTransition();
        loadTransitionsList();
    }

    private void startConvertOperations() {
        getProgressDialog().show();
        new CalculateTotal(this,allTransactions).execute();
    }

    public int getAvailableHeight() {
        if(containerMaxHeight > 0) return containerMaxHeight;

        final float margin = getResources().getDimension(R.dimen.activity_vertical_margin);

        return (int)(getResources().getDisplayMetrics().heightPixels - (2 * margin));
    }
    private void loadTransitionsList() {
        noContentText.setVisibility(View.GONE);
        if(allTransactions == null || allTransactions.size() == 0){
            noContentText.setVisibility(View.VISIBLE);
        }
        adapter.setTransactions(allTransactions, true);
    }

    private void startContainerAnimation(){
        HeightAnimation animation = new HeightAnimation(container, containerMaxHeight, 0);
        animation.setDuration(CONTAINER_ANIMATION_SPEED);
        animation.setAnimationListener(this);
        container.startAnimation(animation);
    }
    private void startBackgroundTransition() {
        TransitionDrawable trans = (TransitionDrawable) ContextCompat.getDrawable(this,R.drawable.other_activity_background_transition);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            coordinatorLayout.setBackgroundDrawable(trans);
        } else {
            coordinatorLayout.setBackground(trans);
        }

        trans.reverseTransition(CONTAINER_ANIMATION_SPEED);
    }

    @Override
    public void onCalculateSuccess(float total) {
        Log.d(TAG,"onCalculateSuccess");
        getProgressDialog().dismiss();
        final String tt = String.format("%.02f", total);
        totalText.setText(String.format(getResources().getString(R.string.total),tt));

    }

    @Override
    public void onCalculateFailure() {
        getProgressDialog().dismiss();
    }

    public void getAllIntents() {
        allTransactions = (getIntent().getExtras().containsKey(Preferences.SELECTED_PRODUCT)) ? (ArrayList)getIntent().getExtras().getSerializable(Preferences.SELECTED_PRODUCT) : new ArrayList<>();
        selectedProductName = (getIntent().getExtras().containsKey(Preferences.SELECTED_PRODUCT_NAME)) ? getIntent().getExtras().getString(Preferences.SELECTED_PRODUCT_NAME) : "";
    }

    @Override
    public void onAdapterItemSelected(int selectedPosition) {
        //TODO don't do anything
    }
}
