/*
package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.archide.hsb.service.TableListService;
import com.archide.hsb.service.impl.TableListServiceImpl;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.MainActivity;
import com.satsuware.usefulviews.LabelledSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import hsb.archide.com.hsb.R;

*/
/**
 * Created by Nithish on 16/01/17.
 *//*


public class TableChangeFragment extends Fragment implements View.OnClickListener{

    private LabelledSpinner vTableNumber;
    private ProgressDialog progressDialog = null;
    private MainActivity mainActivity;
    private MainActivityCallback mainActivityCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView =  inflater.inflate(R.layout.fragment_table_change, container, false);
        vTableNumber = (LabelledSpinner) loginView.findViewById(R.id.vTableNumber);
        Button vSubmit = (Button)loginView.findViewById(R.id.vtable_change_submit);
        vSubmit.setOnClickListener(this);
//submit
        getTableList();
        return loginView;
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    */
/**
     * Get Table List from the Server
     *//*

    private void getTableList(){
        boolean isNetWorkConnected =  Utilities.isNetworkConnected(mainActivity);
        if(isNetWorkConnected){
            TableListService tableListService = new TableListServiceImpl(mainActivity);
            progressDialog = ActivityUtil.showProgress(getString(R.string.get_table_list_heading), getString(R.string.get_table_list_message), mainActivity);
            tableListService.getTableList();
        }else{
            ActivityUtil.showDialog(mainActivity, getString(R.string.no_network_heading), getString(R.string.no_network));
        }
    }

    */
/**
     * Process Table list response
     * @param responseData
     *//*

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleServerSyncResponse(ResponseData responseData) {
        intiView(responseData);
        return;
    }

    */
/**
     * Process Table List Data and Show that component
     * @param responseData
     *//*

    private void intiView(ResponseData responseData){
        dismiss();
        if(responseData.getSuccess()){
            List<String> data  =  (List<String>)responseData.getMessage();
            vTableNumber.setItemsArray(data);
        }

    }

    private void dismiss(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onClick(View view) {
        String vTableNumberText = vTableNumber.getSpinner().getSelectedItem().toString();
        mainActivity.getTableListService().changeTableNumber(vTableNumberText);
        mainActivityCallback.success(7000,null);
        return;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
        mainActivityCallback = (MainActivityCallback)context;

    }

    public interface MainActivityCallback {
        void success(int code, Object data);
    }
}
*/
