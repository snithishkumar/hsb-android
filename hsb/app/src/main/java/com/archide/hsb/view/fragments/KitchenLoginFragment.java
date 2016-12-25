package com.archide.hsb.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.MainActivity;

import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;


/**
 *
 */
public class KitchenLoginFragment extends Fragment implements View.OnClickListener{

    List<String> selectedPin = new ArrayList<>(6);
    Button firstPin = null;
    Button secondPin = null;
    Button thirdPin = null;
    Button fourthPin = null;
    Button fifthPin = null;
    Button sixthPin = null;

    private MainActivity mainActivity;
    private MainActivityCallback mainActivityCallback;
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
        init(loginView);

        return loginView;
    }


    private void init( View view){
       /* try{
            InstanceID instanceID = InstanceID.getInstance(mainActivity);
            token = instanceID.getToken(getString(R.string.google_cloud_id),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
        }catch (Exception e){
            Log.e("Error","Error  in GoogleCloudMessaging",e);
        }*/
        Button zero = (Button)   view.findViewById(R.id.kitchen_log_zero);
        Button one = (Button)  view.findViewById(R.id.kitchen_log_one);
        Button two = (Button)  view.findViewById(R.id.kitchen_log_two);
        Button three = (Button)  view.findViewById(R.id.kitchen_log_three);
        Button four = (Button) view.findViewById(R.id.kitchen_log_four);
        Button five = (Button) view.findViewById(R.id.kitchen_log_five);
        Button six = (Button) view.findViewById(R.id.kitchen_log_six);
        Button seven = (Button)  view.findViewById(R.id.kitchen_log_seven);
        Button eight = (Button) view.findViewById(R.id.kitchen_log_eight);
        Button nine = (Button) view.findViewById(R.id.kitchen_log_nine);
        Button delButton = (Button) view.findViewById(R.id.kitchen_log_del);
        Button forgetButton = (Button) view.findViewById(R.id.kitchen_log_forget_pin);
        setListener(zero);
        setListener(one);
        setListener(two);
        setListener(three);
        setListener(four);
        setListener(five);
        setListener(six);
        setListener(seven);
        setListener(eight);
        setListener(nine);
        setListener(delButton);
        setListener(forgetButton);


        firstPin = (Button)   view.findViewById(R.id.kitchen_log_first_value);
        secondPin = (Button)   view.findViewById(R.id.kitchen_log_second_value);
        thirdPin = (Button)   view.findViewById(R.id.kitchen_log_third_value);
        fourthPin = (Button)   view.findViewById(R.id.kitchen_log_fourth_value);
        fifthPin = (Button)   view.findViewById(R.id.kitchen_log_fifth_value);
        sixthPin = (Button)   view.findViewById(R.id.kitchen_log_sixth_value);

    }


    private void setListener(Button button){
        button.setOnClickListener(this);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity)context;
        mainActivityCallback = (MainActivityCallback)context;

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
            String data = TextUtils.join("",selectedPin);
            int result =  mainActivity.getTableListService().verifyLogin(data);
            if(result == 2){
                mainActivityCallback.success(5000,null);
                //login success
            }else if(result == 3){
                ActivityUtil.showDialog(mainActivity,"Error","InValid Password");
                reSetPin();
                // Invalid Password
            }else{
                ActivityUtil.showDialog(mainActivity,"Error","OOPS! Something went wrong. Please contact System Admin");
                reSetPin();
                // Contact System Admin
            }


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
            case R.id.kitchen_log_zero:
                addValue("0");
                break;
            case R.id.kitchen_log_one:
                addValue("1");
                break;
            case R.id.kitchen_log_two:
                addValue("2");
                break;
            case R.id.kitchen_log_three:
                addValue("3");
                break;
            case R.id.kitchen_log_four:
                addValue("4");
                break;
            case R.id.kitchen_log_five:
                addValue("5");
                break;
            case R.id.kitchen_log_six:
                addValue("6");
                break;
            case R.id.kitchen_log_seven:
                addValue("7");
                break;
            case R.id.kitchen_log_eight:
                addValue("8");
                break;
            case R.id.kitchen_log_nine:
                addValue("9");
                break;
            case R.id.kitchen_log_del:
                if(count > 0){
                    selectedPin.remove(count - 1);
                    resetColor(count, R.drawable.small_round_background);
                }
                break;
            case R.id.kitchen_log_forget_pin:
                ActivityUtil.showDialog(mainActivity,"Error","Contact System Admin");
                break;


        }
        verifyLogin();
    }

    private void reSetPin(){
        int totalCount = selectedPin.size();
        for(int i = totalCount; i > 0; i--){
            selectedPin.remove(i - 1);
            resetColor(i, R.drawable.small_round_background);
        }
    }

    public interface MainActivityCallback {
        void success(int code, Object data);
    }



}
