package com.example.ld_street_lights_maintenance.act;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ld_street_lights_maintenance.R;
import com.example.ld_street_lights_maintenance.base.BaseFragment;

public class BuleFragment extends BaseFragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.layout_blue,
                container, false);

        return rootView;
    }

}