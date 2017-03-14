package com.archide.hsb.view.fragments;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by Nithish on 06-02-2016.
 */
public class FragmentsUtil {

    public static void replaceFragment(AppCompatActivity activity, Fragment fragment, int containerId){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();

          fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
    }


    public static void replaceFragmentNoStack(AppCompatActivity activity, Fragment fragment, int containerId){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void addFragment(AppCompatActivity activity, Fragment fragment, int containerId){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(containerId, fragment)
                .commit();
    }

    public static void addRemoveFragment(AppCompatActivity activity, Fragment fragment, int containerId){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment =  fragmentManager.findFragmentById(containerId);
        if(currentFragment != null){
            fragmentManager.beginTransaction()
                    .replace(containerId, fragment)
                    .addToBackStack(null)
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .add(containerId, fragment)
                    .commit();
        }

    }

    public static Fragment getCurrentFragment(AppCompatActivity activity, int containerId){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment currentFragment =  fragmentManager.findFragmentById(containerId);
        return currentFragment;
    }

    public static void removeFragment(AppCompatActivity activity, int containerId){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment =  fragmentManager.findFragmentById(containerId);
        if(fragment != null){
            fragmentManager.beginTransaction().remove(fragment).commit();
        }
    }


    public static void backPressed(AppCompatActivity activity){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        if(fragmentManager.getBackStackEntryCount() > 0)
            fragmentManager.popBackStack();
    }
}
