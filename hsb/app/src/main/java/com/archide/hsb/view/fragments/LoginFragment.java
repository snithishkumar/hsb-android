package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.archide.hsb.view.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;


/**
 *
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    List<String> selectedPin = new ArrayList<>(6);
    Button firstPin = null;
    Button secondPin = null;
    Button thirdPin = null;
    Button fourthPin = null;
    Button fifthPin = null;
    Button sixthPin = null;

    private MainActivity mainActivity;
    private TextView userMobile;
    private ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View loginView =  inflater.inflate(R.layout.fragment_login, container, false);
        userMobile =  (TextView)loginView.findViewById(R.id.vUserMobileNumber);
        FloatingActionButton button =  (FloatingActionButton)loginView.findViewById(R.id.submit);
        button.setOnClickListener(this);

        return loginView;
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


    private void addValue(String value){
        int count = selectedPin.size();
        if(count < 6){
            selectedPin.add(value);
            resetColor(count + 1, R.drawable.small_round_red);
        }
    }

    private void resetColor(int pos,int id){
        switch (pos){
            case 1:
                firstPin.setBackground(ContextCompat.getDrawable(mainActivity,id));
                break;
            case 2:
                secondPin.setBackground(ContextCompat.getDrawable(mainActivity,id));
                break;
            case 3:
                thirdPin.setBackground(ContextCompat.getDrawable(mainActivity,id));
                break;
            case 4:
                fourthPin.setBackground(ContextCompat.getDrawable(mainActivity,id));
                break;
            case 5:
                fifthPin.setBackground(ContextCompat.getDrawable(mainActivity,id));
                break;
            case 6:
                sixthPin.setBackground(ContextCompat.getDrawable(mainActivity,id));
                break;
        }
    }

    private void verifyLogin(){
        int count = selectedPin.size();
        if(count == 6){
           /* boolean isNet = ServiceUtil.isNetworkConnected(mainActivity);
            if(isNet){
                progressDialog = ActivityUtil.showProgress(getString(R.string.login_submit_heading), getString(R.string.login_submit_message), mainActivity);
                String data = TextUtils.join("",selectedPin);
                mainActivity.getAccountService().login(data);
            }else{
                ActivityUtil.showDialog(mainActivity,getString(R.string.no_network_heading), getString(R.string.no_network));
            }
*/

        }
    }


    @Override
    public void onClick(View v) {
        int count = selectedPin.size();
        if(count == 6){
            verifyLogin();
        }
        switch (v.getId()){
            case R.id.log_zero:
                addValue("0");
                break;
            case R.id.log_one:
                addValue("1");
                break;
            case R.id.log_two:
                addValue("2");
                break;
            case R.id.log_three:
                addValue("3");
                break;
            case R.id.log_four:
                addValue("4");
                break;
            case R.id.log_five:
                addValue("5");
                break;
            case R.id.log_six:
                addValue("6");
                break;
            case R.id.log_seven:
                addValue("7");
                break;
            case R.id.log_eight:
                addValue("8");
                break;
            case R.id.log_nine:
                addValue("9");
                break;
            case R.id.log_del:
                if(count > 0){
                    selectedPin.remove(count - 1);
                    resetColor(count, R.drawable.small_round_background);
                }
                break;



        }
        verifyLogin();
    }




}
