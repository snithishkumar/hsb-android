package com.archide.hsb.view.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.archide.hsb.entity.MenuCourseEntity;
import com.archide.hsb.service.MenuItemService;
import com.archide.hsb.service.OrderService;
import com.archide.hsb.service.impl.MenuItemServiceImpl;
import com.archide.hsb.service.impl.OrderServiceImpl;
import com.archide.hsb.view.fragments.FragmentDrawer;
import com.archide.hsb.view.fragments.MenuItemsFragment;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import hsb.archide.com.hsb.R;

public class HomeActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener{

    private MenuItemService menuItemService;
    private OrderService orderService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        setupViewPager(viewPager,tabLayout);

        tabLayout.setupWithViewPager(viewPager);

        viewPager.setCurrentItem(0);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.root_layout),mToolbar);
        drawerFragment.setDrawerListener(this);

    }

    private void init(){
        menuItemService = new MenuItemServiceImpl(this);
        orderService = new OrderServiceImpl(this);
    }

    private void setupViewPager(ViewPager viewPager,TabLayout tabLayout) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        List<MenuCourseEntity> menuCourseEntityList = menuItemService.getMenuCourseEntity();
        int count = 0;
        for(MenuCourseEntity menuCourseEntity : menuCourseEntityList){
            MenuItemsFragment menuItemsFragment = new MenuItemsFragment();
            Bundle purchaseIdArgs = new Bundle();
            purchaseIdArgs.putString("menuCourseUuid",menuCourseEntity.getMenuCourseUUID());
            purchaseIdArgs.putInt("tabPosition",count);
            menuItemsFragment.setArguments(purchaseIdArgs);

            adapter.addFragment(menuItemsFragment, menuCourseEntity.getCategoryName());
            count += 1;
        }
        if(count < 4){
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        }

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public MenuItemService getMenuItemService() {
        return menuItemService;
    }

    public OrderService getOrderService() {
        return orderService;
    }




    public static String getPath(Context context, String folderName) {
        String appRootFolder = DiskCacheUtils.getAppRootPath(context);
        File file = new File(appRootFolder + File.separator + "hsb" + File.separator + folderName);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }


    public void viewCurrentOrder(View view) {
        Intent intent = new Intent(this, OrderActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
       // finish();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }
    private void displayView(int position) {
        switch (position) {
            case 0:
                Intent intent = new Intent(this, NaviDrawerActivity.class);
                intent.putExtra("options",1);
                startActivity(intent);
                //finish();
                break;
            case 1:
                intent = new Intent(this, NaviDrawerActivity.class);
                intent.putExtra("options",2);
                startActivity(intent);
                break;
            case 2:
                intent = new Intent(this, NaviDrawerActivity.class);
                intent.putExtra("options",3);
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(this, NaviDrawerActivity.class);
                intent.putExtra("options",4);
                startActivity(intent);
                break;
            case 4:
                intent = new Intent(this, NaviDrawerActivity.class);
                intent.putExtra("options",5);
                startActivity(intent);
                break;
            default:
                break;
        }


    }



}
