package com.example.zhoumohan.luckymorning.common.widget.parallaxView;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class ParallaxFragment extends Fragment {

    private List<View> parallaxViews = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        int layoutId = args.getInt("layoutId");
        ParallaxLayoutInflater inflater1 = new ParallaxLayoutInflater(inflater,getActivity(),this);
        return inflater1.inflate(layoutId,null);
    }

    public List<View> getParallaxViews() {
       return parallaxViews;
    }
}
