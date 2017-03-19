package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.sync.json.ResponseData;
import com.archide.hsb.util.Utilities;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.MainActivity;
import com.satsuware.usefulviews.LabelledSpinner;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

/**
 * Created by Nithish on 20/11/16.
 */

public class ConfigurationFragment extends Fragment implements View.OnClickListener{

    private MainActivity mainActivity;

    private EditText vPasswordText;
    private EditText vReTypePasswordText;
    private LabelledSpinner vType;
    private AppType selectedAppType = AppType.Kitchen;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View registrationView =  inflater.inflate(R.layout.fragment_configuration, container, false);

        vPasswordText = (EditText) registrationView.findViewById(R.id.vPassword);
        vReTypePasswordText = (EditText) registrationView.findViewById(R.id.vRetypePassword);

        Button button =  (Button)registrationView.findViewById(R.id.submit);
        button.setOnClickListener(this);

        initType(registrationView);

        return registrationView;
    }


    /**
     * Initialize type of User
     * @param registrationView
     */
    private void initType(View registrationView){
        vType = (LabelledSpinner) registrationView.findViewById(R.id.vType);
        final List<String> data  = new ArrayList<>();
        data.add(AppType.Kitchen.toString());
        data.add(AppType.Captain.toString());
        data.add(AppType.User.toString());
        vType.setItemsArray(data);

        vType.setOnItemChosenListener(new LabelledSpinner.OnItemChosenListener() {
            @Override
            public void onItemChosen(View labelledSpinner, AdapterView<?> adapterView, View itemView, int position, long id) {
                switch (position){
                    case 0:
                       selectedAppType = AppType.Kitchen;
                        break;
                    case 1:
                        selectedAppType = AppType.Captain;
                        break;
                    case 2:
                        selectedAppType = AppType.User;
                        break;
                }

            }

            @Override
            public void onNothingChosen(View labelledSpinner, AdapterView<?> adapterView) {

            }
        });

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;

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
    }


    @Override
    public void onClick(View view) {
       String mPin = vPasswordText.getText().toString().trim();
        if(mPin.length() < 6){
            vPasswordText.setError(getString(R.string.mpin_hint));
            return;
        }
        String reTypeMPin = vReTypePasswordText.getText().toString().trim();
        if(reTypeMPin.length() < 6){
            vReTypePasswordText.setError(getString(R.string.mpin_hint_retype));
            return;
        }
        if(!mPin.equals(reTypeMPin)){
            vReTypePasswordText.setError(getString(R.string.mpin_val_equal));
            return;
        }

        mainActivity.getTableListService().createAdmin(mPin,selectedAppType);
        if(selectedAppType.toString().equals(AppType.Captain.toString())){
            mainActivity.success(MainActivity.CONF_SUCCESS_CAPTAIN,null);
        }else if(selectedAppType.toString().equals(AppType.User.toString())){
            mainActivity.success(MainActivity.CONF_SUCCESS_USER,null);
        }else{
            mainActivity.success(MainActivity.CONF_SUCCESS_KITCHEN,null);
        }

        return;
    }

    public interface MainActivityCallback{

        void success(int code,Object data);
    }
}
