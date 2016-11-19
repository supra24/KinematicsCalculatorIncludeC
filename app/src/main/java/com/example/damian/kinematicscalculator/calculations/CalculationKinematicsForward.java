package com.example.damian.kinematicscalculator.calculations;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Damian on 2016-09-13.
 */
public class CalculationKinematicsForward {

    private float[][] tableParameters;
    private float [] tableEffector;

    //    private float[] coordinatesEndEffector;
    private ArrayList<float[][]> Aa = new ArrayList<>();

    public CalculationKinematicsForward(float[][] tableParameters, float [] tableEffector) {
        this.tableParameters = tableParameters;
        this.tableEffector = tableEffector;
        Calculation();
    }

    public void Calculation() {

        float[][] A = {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };

        float[][] B;

        Aa.add(A);

        for (int i = 0; i < tableParameters.length; i++) {

            float[][] RotZ = SingeltonMatrixKinematicsForward.DHRotZ(tableParameters[i][2]);
            float[][] TransZ = SingeltonMatrixKinematicsForward.DHTransZ(tableParameters[i][3]);
            float[][] TransX = SingeltonMatrixKinematicsForward.DHTransX(0);
            float[][] RotX = SingeltonMatrixKinematicsForward.DHRotX(tableParameters[i][0]);

            float[][] RotZxTransZ = SingeltonMatrixKinematicsForward.Multiplication(RotZ, TransZ);
            float[][] xTransX = SingeltonMatrixKinematicsForward.Multiplication(RotZxTransZ, TransX);
            float[][] xRotX = SingeltonMatrixKinematicsForward.Multiplication(xTransX, RotX);

            B = SingeltonMatrixKinematicsForward.Multiplication(A, xRotX);
            Aa.add(B);

            Log.v("coo X= ", String.valueOf(Aa.get(Aa.size() - 1)[0][3]));
            Log.v("coo Y= ", String.valueOf(Aa.get(Aa.size() - 1)[1][3]));
            Log.v("coo Z= ", String.valueOf(Aa.get(Aa.size() - 1)[2][3]));
//---------------------------------------------------------------------------------------

            RotZ = SingeltonMatrixKinematicsForward.DHRotZ(tableParameters[i][2]);
            TransZ = SingeltonMatrixKinematicsForward.DHTransZ(tableParameters[i][3]);
            TransX = SingeltonMatrixKinematicsForward.DHTransX(tableParameters[i][1]);
            RotX = SingeltonMatrixKinematicsForward.DHRotX(tableParameters[i][0]);

            RotZxTransZ = SingeltonMatrixKinematicsForward.Multiplication(RotZ, TransZ);
            xTransX = SingeltonMatrixKinematicsForward.Multiplication(RotZxTransZ, TransX);
            xRotX = SingeltonMatrixKinematicsForward.Multiplication(xTransX, RotX);

            A = SingeltonMatrixKinematicsForward.Multiplication(A, xRotX);
            Aa.add(A);
            Log.v("coo X= ", String.valueOf(Aa.get(Aa.size() - 1)[0][3]));
            Log.v("coo Y= ", String.valueOf(Aa.get(Aa.size() - 1)[1][3]));
            Log.v("coo Z= ", String.valueOf(Aa.get(Aa.size() - 1)[2][3]));
        }

        float [][] effecotr = {
                {1,0,0,tableEffector[0]},
                {0,1,0,tableEffector[1]},
                {0,0,1,tableEffector[2]},
                {0,0,0,1}
        };

        Aa.add(SingeltonMatrixKinematicsForward.Multiplication(Aa.get(Aa.size()-1), effecotr));
    }

    public ArrayList<float[][]> getCoordinatesEndEffector() {
        return Aa;
    }
}
