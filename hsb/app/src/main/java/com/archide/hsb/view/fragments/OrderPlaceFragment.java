package com.archide.hsb.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.archide.hsb.view.activities.OrderActivity;
import com.archide.hsb.view.adapters.OrderedMenuItemsAdapter;
import com.archide.hsb.view.model.MenuItemsViewModel;
import com.archide.hsb.view.model.PlaceAnOrderViewModel;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 19/11/16.
 */

public class OrderPlaceFragment extends Fragment {

    LinearLayoutManager linearLayoutManager = null;
    List<MenuItemsViewModel> menuItemsViewModels = new ArrayList<>();

    EditText cookingComments ;
    TextView subTotalBeforeDiscount ;
    TextView discount ;
    TextView subTotal ;
    TextView serviceTax ;
    TextView serviceVat ;
    TextView totalAmount ;
    Button placeAnOrder ;


    private OrderActivity orderActivity;

    private PlaceAnOrderViewModel placeAnOrderViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void init(View view){
         cookingComments =  (EditText)view.findViewById(R.id.cooking_comments);
         subTotalBeforeDiscount =  (TextView)view.findViewById(R.id.subtotal_before_discount);
         discount =  (TextView) view.findViewById(R.id.discount);
         subTotal =  (TextView) view.findViewById(R.id.subtotal);
         serviceTax =  (TextView) view.findViewById(R.id.service_tax);
         serviceVat =  (TextView)view.findViewById(R.id.service_vat);
         totalAmount =  (TextView) view.findViewById(R.id.total_amount);
         placeAnOrder =  (Button) view.findViewById(R.id.place_an_order);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_place_order, container, false);

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
        placeAnOrderViewModel =  orderActivity.getOrderService().getCurrentOrderDetails();
        setAdapters(recyclerView);

        init(view);
        populateAmountDetails();
        return view;
    }

    private void setAdapters(RecyclerView recyclerView){

        OrderedMenuItemsAdapter orderedMenuItemsAdapter = new OrderedMenuItemsAdapter(placeAnOrderViewModel.getMenuItemsViewModels(),orderActivity,OrderPlaceFragment.this);

        recyclerView.setAdapter(orderedMenuItemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(orderActivity,linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
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

    public void calcAmountDetails(){
        orderActivity.getOrderService().calcAmount(placeAnOrderViewModel);
        populateAmountDetails();
    }

    public void populateAmountDetails(){
        cookingComments.setText(placeAnOrderViewModel.getCookingComments());
        subTotalBeforeDiscount.setText(String.valueOf(placeAnOrderViewModel.getSubTotalBeforeDiscount()));
        discount.setText(String.valueOf(placeAnOrderViewModel.getDiscount()));
        subTotal.setText(String.valueOf(placeAnOrderViewModel.getSubTotal()));
        serviceTax.setText(String.valueOf(placeAnOrderViewModel.getServiceTax()));
        serviceVat.setText(String.valueOf(placeAnOrderViewModel.getServiceVat()));
        totalAmount.setText(String.valueOf(placeAnOrderViewModel.getTotalAmount()));

    }
}
