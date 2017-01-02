package cn.zhihu.daily.zhihu_daily.util;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import cn.zhihu.daily.zhihu_daily.Interface.SensorCallback;

/**
 * Created by morc on 17-1-2.
 */

public class SensorUtil {
    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private int count = 0;
    private SensorCallback sensorCallback;

    public SensorUtil(SensorManager sensorManager, SensorCallback sensorCallback) {
        this.sensorManager = sensorManager;
        accelerometerSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.sensorCallback = sensorCallback;
        count = 0;
        //setListener();
    }

    public void setListener() {
        count = 0;
        sensorManager.registerListener(sensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    public void rmListener() {
        sensorManager.unregisterListener(sensorEventListener);
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            final int maxValue = 19;
            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    if (Math.abs(event.values[0]) > maxValue || Math.abs(event.values[1]) > maxValue
                            || Math.abs(event.values[2]) > maxValue) {
                        count++;
                        if (count > 5) {
                            sensorCallback.onShake();
                            count = 0;
                        }
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //do nothing;
        }
    };
}
