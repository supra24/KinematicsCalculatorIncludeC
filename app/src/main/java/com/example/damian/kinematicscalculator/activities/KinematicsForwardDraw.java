package com.example.damian.kinematicscalculator.activities;

import android.icu.math.BigDecimal;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.TextView;

import com.example.damian.kinematicscalculator.R;
import com.example.damian.kinematicscalculator.calculations.CalculationKinematicsForward;
import com.example.damian.kinematicscalculator.models.ModelKinematicsForwardValueEffector;
import com.example.damian.kinematicscalculator.models.ModelKinematicsForwardValueJoin;
import com.example.damian.kinematicscalculator.models.ModelKinematicsForwardValueParent;
import com.example.damian.kinematicscalculator.openGL.AbstractRenderer;
import com.example.damian.kinematicscalculator.openGL.RenderManipulator;
import com.example.damian.kinematicscalculator.staticVolumes.StaticVolumesKinematicsForwardValue;

import java.util.ArrayList;

/**
 * Created by Damian on 2016-10-15.
 */


public class KinematicsForwardDraw extends AppCompatActivity {

    private static final int AMOUNT_VARIABLES = 4;
    private static final int SCALE_TEXT = 5;
    private static final int ONE_TOUCH_POINTER = 1;
    private static final int TWO_TOUCH_POINTER = 2;
    private static final int AMOUNT_COORDINATES = 4;


    private GLSurfaceView mTestHarness;
    private VelocityTracker vTracker = null;  // VelocityTracer określa zachowanie sekwencji dotyku
    private float startingDistance;
    private ArrayList<ModelKinematicsForwardValueParent> kinematicsForwardValueParents;

    private DrawerLayout drawer;

    /**
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_kinematics_forward);

        kinematicsForwardValueParents = StaticVolumesKinematicsForwardValue.getModels();

        float[][] tableParameters = new float[kinematicsForwardValueParents.size()][AMOUNT_VARIABLES];

        // dodanie do tablcy wartosci z wybranych czlonow
        for (int i = 0; i < tableParameters.length - 1; i++) {

            ModelKinematicsForwardValueJoin modelKinematicsForwardValueJoin = (ModelKinematicsForwardValueJoin) kinematicsForwardValueParents.get(i);

            tableParameters[i][0] = modelKinematicsForwardValueJoin.getEt_alpha();
            tableParameters[i][1] = modelKinematicsForwardValueJoin.getEt_a();
            tableParameters[i][2] = modelKinematicsForwardValueJoin.getEt_theta();
            tableParameters[i][3] = modelKinematicsForwardValueJoin.getEt_d();
        }

        // stworzenei nowej tablicy do wartosci effectora
        float[] tableEffector = new float[AMOUNT_COORDINATES];
        ModelKinematicsForwardValueEffector modelKinematicsForwardValueEffector = (ModelKinematicsForwardValueEffector) kinematicsForwardValueParents.get(kinematicsForwardValueParents.size() - 1);
        tableEffector[0] = modelKinematicsForwardValueEffector.getEt_x();
        tableEffector[1] = modelKinematicsForwardValueEffector.getEt_y();
        tableEffector[2] = modelKinematicsForwardValueEffector.getEt_z();
        tableEffector[3] = 0;


        CalculationKinematicsForward calculationKinematicsForward = new CalculationKinematicsForward(tableParameters, tableEffector);
        ArrayList<float[][]> coordinates = calculationKinematicsForward.getCoordinatesEndEffector();

        TextView textX = (TextView) findViewById(R.id.textX);
        TextView textY = (TextView) findViewById(R.id.textY);
        TextView textZ = (TextView) findViewById(R.id.textZ);

        textX.setText(String.valueOf(new BigDecimal(coordinates.get(coordinates.size() - 1)[0][3]).setScale(SCALE_TEXT, BigDecimal.ROUND_HALF_EVEN).doubleValue()));
        textY.setText(String.valueOf(new BigDecimal(coordinates.get(coordinates.size() - 1)[1][3]).setScale(SCALE_TEXT, BigDecimal.ROUND_HALF_EVEN).doubleValue()));
        textZ.setText(String.valueOf(new BigDecimal(coordinates.get(coordinates.size() - 1)[2][3]).setScale(SCALE_TEXT, BigDecimal.ROUND_HALF_EVEN).doubleValue()));

        // openGlES
        mTestHarness = (GLSurfaceView) findViewById(R.id.GLView);
        mTestHarness.setEGLConfigChooser(false); // nie wymagamy wyboru specjalnej konfiguracji EDL i wystarczają domyśle ustawienia
        mTestHarness.setRenderer(new RenderManipulator(this, tableParameters, coordinates));
        mTestHarness.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        mTestHarness.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                onTouchEventGLSurfaceView(motionEvent);
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        mTestHarness.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mTestHarness.onPause();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean onTouchEventGLSurfaceView(MotionEvent event) {

//        float x = event.getX();
//        float y = event.getY();

        int action = event.getAction() & MotionEvent.ACTION_MASK;

        if (event.getPointerCount() == ONE_TOUCH_POINTER) {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    if (vTracker == null) {
                        vTracker = VelocityTracker.obtain();
                    } else {
                        vTracker.clear();
                    }
                    vTracker.addMovement(event);
                    break;
                case MotionEvent.ACTION_MOVE:
                    vTracker.addMovement(event);
                    vTracker.computeCurrentVelocity(1000);
                    AbstractRenderer.setRotate(vTracker.getXVelocity(), vTracker.getYVelocity());
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
            }
        } else if (event.getPointerCount() == TWO_TOUCH_POINTER) {

//            switch (action) {
//                case MotionEvent.ACTION_POINTER_DOWN: // przygotowanie do gestu ściskania/rozciągania
//                    startingDistance = distanceBetweenTwoFingers(event); // zapamiętania początkowej odległości mięszy palcami
//                    break;
//                case MotionEvent.ACTION_POINTER_UP:
//                case MotionEvent.ACTION_MOVE:
//                    float newDistance = distanceBetweenTwoFingers(event);
//                    if (newDistance != startingDistance) { // palce się oddalają
//
//                        AbstractRenderer.setRadiusDistance(newDistance - startingDistance);
//                    }
//                    break;
//            }


            switch (action) {
                case MotionEvent.ACTION_POINTER_DOWN: // przygotowanie do gestu ściskania/rozciągania
                    startingDistance = distanceBetweenTwoFingers(event); // zapamiętania początkowej odległości mięszy palcami

                    if (vTracker == null) {
                        vTracker = VelocityTracker.obtain();
                    } else {
                        vTracker.clear();
                    }
                    vTracker.addMovement(event);

                    break;
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_MOVE:

                    vTracker.addMovement(event);
                    vTracker.computeCurrentVelocity(1000);

//                    if ((vTracker.getXVelocity(0) < 5 && vTracker.getYVelocity(0) < 5) || (vTracker.getXVelocity(1) < 5 && vTracker.getYVelocity(1) < 5)) {
//
//                        if (vTracker.getXVelocity(0) < 5 && vTracker.getYVelocity(0) < 5) {
//                            AbstractRenderer.setShiftOneSide(vTracker.getXVelocity(1), vTracker.getYVelocity(1));
//                        } else if (vTracker.getXVelocity(1) < 5 && vTracker.getYVelocity(1) < 5) {
//                            AbstractRenderer.setShiftOneSide(vTracker.getXVelocity(0), vTracker.getYVelocity(0));
//                        }
//
//                    } else {

                    float newDistance = distanceBetweenTwoFingers(event);
                    if (newDistance != startingDistance) { // palce się oddalają

                        AbstractRenderer.setRadiusDistance(newDistance - startingDistance);
                    }
//                    }
                    break;
            }
        }
        return true;
    }

    /**
     * @param e
     * @return
     */
    private float distanceBetweenTwoFingers(MotionEvent e) {

        float x = e.getX(0) - e.getX(1);
        float y = e.getY(0) - e.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }

}