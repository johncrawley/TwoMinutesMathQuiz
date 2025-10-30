package com.jcrawley.tmmq.view.fragments.utils;

import androidx.fragment.app.Fragment;

import com.jcrawley.tmmq.view.fragments.game.GameFragment;

public class FragmentLoader {


    public static void loadGame(Fragment parentFragment){
        FragmentUtils.loadFragment(parentFragment, new GameFragment(), "gameScreenFragment");
    }
}
