package com.example.damian.kinematicscalculator.fragments;

import android.view.View;
import android.widget.ListView;

import com.example.damian.kinematicscalculator.R;
import com.example.damian.kinematicscalculator.adapters.AdapterInverseVariables;
import com.example.damian.kinematicscalculator.models.ModelKinematicsInverseVariablwsParent;
import com.example.damian.kinematicscalculator.staticVolumes.StaticVolumesInverseVariables;
import com.example.damian.kinematicscalculator.staticVolumes.StaticVolumesKinematicsInverseValue;

import java.util.ArrayList;

/**
 * Created by Damian on 2016-11-11.
 */

public class FragmentListInverseVariables extends FragmentParent {

    private AdapterInverseVariables adapterInverseVariables;
    private ArrayList<ModelKinematicsInverseVariablwsParent> modelKinematicsInverseVariablwsParents = new ArrayList<>();

    public FragmentListInverseVariables(){
        layoutid = R.layout.fragment_join_list_view;
    }

    @Override
    public void init(View view) {

        modelKinematicsInverseVariablwsParents = StaticVolumesInverseVariables.getModels();

        adapterInverseVariables = new AdapterInverseVariables(getContext(), modelKinematicsInverseVariablwsParents);
        ListView listView = (ListView) view.findViewById(R.id.list_view_join);
        listView.setAdapter(adapterInverseVariables);

    }

    public void addObjectJoin(int typeObject) {

        StaticVolumesInverseVariables.addObjects(typeObject);
        StaticVolumesKinematicsInverseValue.addObjects(typeObject);

        adapterInverseVariables.notifyDataSetInvalidated();
    }

    public boolean undoObject() {

        if (modelKinematicsInverseVariablwsParents.isEmpty()) {
            return false;
        } else {
            modelKinematicsInverseVariablwsParents.remove(modelKinematicsInverseVariablwsParents.size() - 1);
            StaticVolumesKinematicsInverseValue.removeEnd();
            adapterInverseVariables.notifyDataSetInvalidated();
            return true;
        }
    }
}
