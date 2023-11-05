package com.jcrawley.tmmq.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcrawley.tmmq.R;

import java.util.function.Consumer;

public class FragmentManagerHelper {


    public static void showDialog(Fragment parentFragment, DialogFragment dialogFragment, String tag, Bundle bundle){
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fragmentTransaction, tag);
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag, Bundle bundle){
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag){
        FragmentManager fragmentManager = parentFragment.getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        removePreviousFragmentTransaction(fragmentManager, tag, fragmentTransaction);
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


    private static void removePreviousFragmentTransaction(FragmentManager fragmentManager, String tag, FragmentTransaction fragmentTransaction){
        Fragment prev = fragmentManager.findFragmentByTag(tag);
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
    }


    public static void setListener(Fragment fragment, String key, Consumer<Bundle> consumer){
        fragment.getParentFragmentManager().setFragmentResultListener(key, fragment, (requestKey, bundle) -> consumer.accept(bundle));
    }


    public static void sendMessage(Fragment fragment, String key){
        sendMessage(fragment, key, new Bundle());
    }


    public static void sendMessage(Fragment fragment, String key, Bundle bundle){
        fragment.getParentFragmentManager().setFragmentResult(key, bundle);
    }




}
