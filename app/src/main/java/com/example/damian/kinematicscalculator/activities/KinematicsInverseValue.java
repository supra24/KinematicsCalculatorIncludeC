package com.example.damian.kinematicscalculator.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.damian.kinematicscalculator.R;
import com.example.damian.kinematicscalculator.calculations.CalculationKinematicsInverse;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Damian on 2016-10-21.
 */

public class KinematicsInverseValue extends AppCompatActivity {

    private static final int RETURN_BACK_STACK = 0;
    private boolean doubleBackToExitPressedOnce = false;
    private static int CLOSE_APP_ON_BACK = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_value_kinematics_inverse);
        ButterKnife.bind(this);
    }

    @Override
    public void onBackPressed() {

        if (getSupportFragmentManager().getBackStackEntryCount() > RETURN_BACK_STACK) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, CLOSE_APP_ON_BACK);
        } else {
            super.onBackPressed();
        }
    }

    @OnClick(R.id.floating_action_button_inverse_play_value)
    public void OnClickFloatingActionButtonPlayInverse() {

        String[][] tableParameter = {
                {"0", "10", "a", "10"},
                {"0", "10", "b", "10"},
                {"0", "10", "c", "10"}
        };

        CalculationKinematicsInverse calculationKinematicsInverse = new CalculationKinematicsInverse(tableParameter);

    }
}
