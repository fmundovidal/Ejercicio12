package com.example.a5alumno.ejercicio12;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private long lastUpdate;
    private RelativeLayout mMainLayout;
    private long eventTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mSensorManager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> myDeviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder mSensorString = new StringBuilder("");

        for(Sensor aSensor:myDeviceSensors)
            mSensorString.append(aSensor.getName());

        Toast.makeText(this, mSensorString, Toast.LENGTH_LONG).show();


        eventTime = System.currentTimeMillis();
        lastUpdate = System.currentTimeMillis();

        while((eventTime-lastUpdate)<3000)
            eventTime=System.currentTimeMillis();

        eventTime = System.currentTimeMillis();
        lastUpdate=System.currentTimeMillis();
        this.mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);


        mMainLayout = (RelativeLayout)this.findViewById(R.id.relLayoutMain);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mSensorManager.registerListener(this,this.mAccelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.mSensorManager.unregisterListener(this,this.mAccelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        eventTime = System.currentTimeMillis();

        float[] sensorValues = sensorEvent.values;
        float accX = sensorValues[0];
        float accY = sensorValues[1];
        float accZ = sensorValues[2];

        float accValuesG = (accX*accX+accY*accY+accZ*accZ)/(SensorManager.GRAVITY_EARTH*SensorManager.GRAVITY_EARTH);

        if(sensorEvent.sensor.getType()==Sensor.TYPE_ACCELEROMETER && (eventTime - lastUpdate)>500 && accValuesG > 4){
            Toast.makeText(this, "Device was shuffled", Toast.LENGTH_SHORT).show();
            if(((ColorDrawable)mMainLayout.getBackground()).getColor() == ContextCompat.getColor(this,R.color.lightBlue)) {
                this.mMainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightRed));
            }
            else
                this.mMainLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBlue));
            lastUpdate=eventTime;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
