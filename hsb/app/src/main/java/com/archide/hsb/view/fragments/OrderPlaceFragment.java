package com.archide.hsb.view.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.activities.MainActivity;
import com.archide.hsb.view.activities.OrderActivity;
import com.archide.hsb.view.adapters.OrderedMenuItemsAdapter;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Locale;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 19/11/16.
 */

public class OrderPlaceFragment extends Fragment implements View.OnClickListener,TextToSpeech.OnInitListener {

    LinearLayoutManager linearLayoutManager = null;

    EditText cookingComments ;
    TextView subTotalBeforeDiscount ;
    TextView discount ;
    TextView subTotal ;
    TextView serviceTax ;
    TextView serviceVat ;
    TextView totalAmount ;
    TextView addMoreItems ;

    RelativeLayout relativeLayout;

    private LayoutInflater mInflater;
    private ViewGroup mContainer;


    private OrderActivity orderActivity;
    private PlaceAnOrderViewModel placeAnOrderViewModel;
    OrderedMenuItemsAdapter orderedMenuItemsAdapter;

    SharedPreferences sharedpreferences;

    private TextToSpeech engine;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        engine = new TextToSpeech(orderActivity, this);
    }

    private void init(View view){
         cookingComments =  (EditText)view.findViewById(R.id.cooking_comments);
         subTotalBeforeDiscount =  (TextView)view.findViewById(R.id.subtotal_before_discount);
         discount =  (TextView) view.findViewById(R.id.discount);
         subTotal =  (TextView) view.findViewById(R.id.subtotal);
         serviceTax =  (TextView) view.findViewById(R.id.service_tax);
          serviceVat =  (TextView) view.findViewById(R.id.service_vat);
          addMoreItems =  (TextView)view.findViewById(R.id.edit_order);
          totalAmount =  (TextView) view.findViewById(R.id.total_amount);
           getCookingComments();
    }


    private void getCookingComments(){
        String key =  "cookingComments";
        String previousComment = sharedpreferences.getString(key,"");
        if(previousComment != null && !previousComment.isEmpty()){
            cookingComments.setText(previousComment.trim());
        }
    }

    public void removeCookingComments(){
        String key =  "cookingComments";
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.remove(key);
        editor.commit();

    }

    public void setCookingComments(){
        String cookingCmt = cookingComments.getText().toString();
        if(cookingCmt != null && !cookingCmt.isEmpty()){
            String key =  "cookingComments";
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(key,cookingCmt);
            editor.commit();
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        mContainer = container;

        sharedpreferences = orderActivity.getSharedPreferences("mobilepayhsb",
                Context.MODE_PRIVATE);


        orderActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        orderActivity.getSupportActionBar().setHomeButtonEnabled(true);

        View view =  inflater.inflate(R.layout.fragment_place_order, container, false);

         placeAnOrderViewModel = orderActivity.getOrderService().getCurrentOrderDetails();

        //order_unavailable_layout
        relativeLayout =  (RelativeLayout)view.findViewById(R.id.order_unavailable_layout);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ordered_data);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager =  new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        setAdapters(recyclerView);


        init(view);
        loadData();
        return view;


    }

    private void setAdapters(RecyclerView recyclerView){
        orderedMenuItemsAdapter = new OrderedMenuItemsAdapter(placeAnOrderViewModel.getMenuItemsViewModels(),orderActivity,OrderPlaceFragment.this);

        recyclerView.setAdapter(orderedMenuItemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void loadData(){
        if(placeAnOrderViewModel.isUnAvailable()){
            relativeLayout.setVisibility(View.VISIBLE);
        }
        populateAmountDetails();
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        orderActivity = (OrderActivity)context;

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        engine.shutdown();
    }


    public void calcAmountDetails(){
        orderActivity.getOrderService().calcAmount(placeAnOrderViewModel);
        populateAmountDetails();
    }

    public void populateAmountDetails(){
        String text = cookingComments.getText().toString();
        if(text != null && text.trim().length() > 0){
            cookingComments.setText(text);
            cookingComments.setFocusable(false);
            cookingComments.setFocusableInTouchMode(true);
        }else{
            cookingComments.setText(placeAnOrderViewModel.getCookingComments());
            cookingComments.setFocusable(false);
            cookingComments.setFocusableInTouchMode(true);
        }

        subTotalBeforeDiscount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotalBeforeDiscount()));
        discount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getDiscount()));
        subTotal.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getSubTotal()));
        serviceTax.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getServiceTax()));
        serviceVat.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getServiceVat()));
        totalAmount.setText(getString(R.string.pound)+" "+String.valueOf(placeAnOrderViewModel.getTotalAmount()));

    }





    @Override
    public void onClick(View view) {
        orderActivity.onBackPressed();
    }

    public void showNoData(){
        View newView = mInflater.inflate(R.layout.fragment_place_order_empty, mContainer, false);
        mContainer.removeAllViews();
        mContainer.addView(newView);
        removeCookingComments();
        ImageView imageView =  (ImageView)newView.findViewById(R.id.place_an_order);
        imageView.setOnClickListener(this);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
            engine.setLanguage(Locale.UK);

        }

    }

    private void speech(String textToSpeech) {
        engine.speak(textToSpeech, TextToSpeech.QUEUE_FLUSH, null, null);
    }


    public String getCookingCommentsValue(){
        removeCookingComments();
       return cookingComments.getText().toString();
    }
}
