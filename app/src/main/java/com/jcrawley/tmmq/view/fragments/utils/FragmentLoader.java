package com.jcrawley.tmmq.view.fragments.utils;

import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.loadFragment;
import static com.jcrawley.tmmq.view.fragments.utils.FragmentUtils.onBackButtonPressed;

import androidx.fragment.app.Fragment;

import com.jcrawley.tmmq.view.fragments.AboutFragment;
import com.jcrawley.tmmq.view.fragments.GameOverFragment;
import com.jcrawley.tmmq.view.fragments.MainMenuFragment;
import com.jcrawley.tmmq.view.fragments.OptionsFragment;
import com.jcrawley.tmmq.view.fragments.game.GameFragment;

public class FragmentLoader {


    public static void loadGame(Fragment parentFragment){
        loadFragment(parentFragment, new GameFragment(), "gameScreenFragment");
    }


    public static void loadOptionsFragment(Fragment parentFragment){
        loadFragment(parentFragment, new OptionsFragment(), "Options_fragment");
    }


    public static void loadAboutFragment(Fragment parentFragment){
        loadFragment(parentFragment, new AboutFragment(), "about_fragment");
    }


    public static void loadGameOverFragment(Fragment parentFragment){
        loadFragment(parentFragment, new GameOverFragment(), "game_over_fragment");
    }


    public static void loadMainMenuFragment(Fragment parentFragment){
        loadFragment(parentFragment, new MainMenuFragment(), "main_menu_fragment");
    }


    public static void loadOptionsFragmentOnBackButtonPressed(Fragment parentFragment){
        onBackButtonPressed(parentFragment, () -> loadOptionsFragment(parentFragment));
    }


    public static void loadMainMenuFragmentOnBackButtonPressed(Fragment parentFragment){
        onBackButtonPressed(parentFragment, () -> loadMainMenuFragment(parentFragment));
    }

}
