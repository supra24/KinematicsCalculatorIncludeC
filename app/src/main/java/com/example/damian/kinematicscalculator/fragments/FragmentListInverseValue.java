package com.example.damian.kinematicscalculator.fragments;

import android.view.View;
import android.widget.ListView;

import com.example.damian.kinematicscalculator.R;
import com.example.damian.kinematicscalculator.adapters.AdapterInverseValue;
import com.example.damian.kinematicscalculator.models.ModelKinematicsInverseValueParent;
import com.example.damian.kinematicscalculator.staticVolumes.StaticVolumesKinematicsInverseValue;

import java.util.ArrayList;

/**
 * Created by Damian on 2016-11-11.
 */

public class FragmentListInverseValue extends FragmentParent {

    private AdapterInverseValue adapterForwardValue;
    private ArrayList<ModelKinematicsInverseValueParent> kinematicsInverseValueParents = new ArrayList<>();

    public FragmentListInverseValue(){
        layoutid = R.layout.fragment_join_list_view;
    }

    @Override
    public void init(View view) {

        kinematicsInverseValueParents = StaticVolumesKinematicsInverseValue.getModels();

        adapterForwardValue = new AdapterInverseValue(getContext(), kinematicsInverseValueParents);
        ListView listView = (ListView) view.findViewById(R.id.list_view_join);
        listView.setAdapter(adapterForwardValue);

    }
}
