package com.example.nicole.fitness_predictor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class WeatherPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weatherpicker);

        Button btn1 = (Button)findViewById(R.id.btnGetAvgTemp);
        Button btn2 = (Button)findViewById(R.id.btnGetAvgWindSpeed);

        btn1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View method)
            {

                //Opens avg temp graph activity
                WeatherGrapher.chosenGraphType = WeatherGrapher.graphType.AVG_TEMP;
                Intent intent = new Intent(getApplicationContext(), WeatherGrapher.class);
                startActivity(intent);
                //If it isn't necessary to open the graph in a new activity then you can place the method here
                //getAvgTemperature()
            }
        });

        btn2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View method)
            {

                //Opens avg wind speed graph activity
                WeatherGrapher.chosenGraphType = WeatherGrapher.graphType.WIND_SPEED;
                Intent intent = new Intent(getApplicationContext(), WeatherGrapher.class);
                startActivity(intent);
                //If it isn't necessary to open the graph in a new activity then you can place the method here
                //getAvgWindSpeed();
            }
        });


    }

}
