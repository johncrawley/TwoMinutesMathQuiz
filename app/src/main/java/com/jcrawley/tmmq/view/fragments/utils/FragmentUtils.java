package com.jcrawley.tmmq.view.fragments.utils;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.jcrawley.tmmq.R;
import com.jcrawley.tmmq.view.fragments.game.GameFragment;

import java.util.function.Consumer;

public class FragmentUtils {


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
                .setCustomAnimations(R.anim.slide_in, R.anim.slide_out, R.anim.pop_enter, R.anim.pop_exit )
                .replace(R.id.fragment_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }


    public static void onBackButtonPressed(Fragment parentFragment, Runnable action){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                action.run();
            }
        };
        parentFragment.requireActivity().getOnBackPressedDispatcher().addCallback(parentFragment.getViewLifecycleOwner(), callback);
    }


    public static void doNothingWhenBackButtonPressed(Fragment parentFragment){
        onBackButtonPressed(parentFragment, ()->{});
    }


    public static void loadFragment(Fragment parentFragment, Fragment fragment, String tag){
        loadFragment(parentFragment, fragment, tag, new Bundle());
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


    public static int getInt(Bundle bundle, GameFragment.Tag tag){
        return bundle.getInt(tag.toString());
    }


    public static String getStr(Bundle bundle, GameFragment.Tag tag){
        return bundle.getString(tag.toString());
    }


    public static boolean getBoolean(Bundle bundle, GameFragment.Tag tag){
        return  bundle.getBoolean(tag.toString());
    }


}