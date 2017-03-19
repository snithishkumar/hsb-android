package com.archide.hsb.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.archide.hsb.entity.ConfigurationEntity;
import com.archide.hsb.entity.UsersEntity;
import com.archide.hsb.enumeration.AppType;
import com.archide.hsb.enumeration.OrderType;
import com.archide.hsb.service.OrderService;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.view.activities.ActivityUtil;
import com.archide.hsb.view.activities.HomeActivity;
import com.archide.hsb.view.adapters.NavigationDrawerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hsb.archide.com.hsb.R;


public class FragmentDrawer extends Fragment {
 
    private static String TAG = FragmentDrawer.class.getSimpleName();

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private HomeActivity homeActivity;

    private TextView vUserName;
    private TextView vMobileNumber;
 
    public FragmentDrawer() {
 
    }
 
    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }
 
    public static List<String> getData() {
        List<String> data = new ArrayList<>();
 
 
        // preparing navigation drawer items
        Collections.addAll(data, titles);
        return data;
    }
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        // drawer labels
        titles = homeActivity.getResources().getStringArray(R.array.nav_menu);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.homeActivity = (HomeActivity)context;
    }

    private void  loadData(){
        OrderService orderService = new OrderServiceImpl(homeActivity);
        ConfigurationEntity configurationEntity =  orderService.getAppType();
        UsersEntity  usersEntity =  orderService.getUsersEntity();
        if(usersEntity.getOrderType().toString().equals(OrderType.TakeAway.toString())){
            vUserName.setText("Table No: T");
        }else{
            vUserName.setText("Table No: "+configurationEntity.getTableNumber());
        }

        vMobileNumber.setText(usersEntity.getUserMobileNumber());

        //  vUserName.setText("Table No: "+ ActivityUtil.TABLE_NUMBER);
        // vMobileNumber.setText(ActivityUtil.USER_MOBILE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        vUserName =  (TextView)layout.findViewById(R.id.navigation_drawer_name);
        vMobileNumber = (TextView)layout.findViewById(R.id.navigation_drawer_mobile_no);

        NavigationDrawerAdapter adapter = new NavigationDrawerAdapter(homeActivity, getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(homeActivity));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(homeActivity, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }
 
            @Override
            public void onLongClick(View view, int position) {
 
            }
        }));
        loadData();
      //  vUserName.setText("Table No: "+ ActivityUtil.TABLE_NUMBER);
       // vMobileNumber.setText(ActivityUtil.USER_MOBILE);
        return layout;
    }
 
 
    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = homeActivity.findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(homeActivity, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //homeActivity.invalidateOptionsMenu();
            }
 
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
               // homeActivity.invalidateOptionsMenu();
            }
 
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };
 
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
 
    }
 
    public interface ClickListener {
        void onClick(View view, int position);
 
        void onLongClick(View view, int position);
    }
 
    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
 
        private GestureDetector gestureDetector;
        private ClickListener clickListener;
 
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
 
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }
 
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
 
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }
 
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }
 
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
 
        }
 
 
    }


    @Override
    public void onResume(){
        super.onResume();
    }


    public interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }
}