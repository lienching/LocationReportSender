package coding.lien.charles.locationreportsender.util;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Author: lienching
 * Description: This class will handle compass information
 */
public class CompassManager implements SensorEventListener {

    private final String TAG = "CompassManager";

    private static CompassManager instance;
    private final Activity myActivity;
    private double currentdegree = 0.0;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;


    private CompassManager(Activity activity) {
        this.myActivity = activity;
        this.sensorManager = (SensorManager) myActivity.getSystemService(myActivity.SENSOR_SERVICE);
        this.accelerometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.magnetometer = this.sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public static void initManager(Activity activity) {
        instance = new CompassManager(activity);
    }


    public static CompassManager getInstance() {
        if ( instance != null )
            return instance;
        return null;
    }

    public void SensorPause() {
        this.sensorManager.unregisterListener(this);
    }

    public void SensorResume() {
        this.sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_UI);
        this.sensorManager.registerListener(this, magnetometer,
                SensorManager.SENSOR_DELAY_UI);
    }



    @Override
    public void onSensorChanged(SensorEvent event) {


        if ( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER )
            mGravity = event.values;
        if ( event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD )
            mGeomagnetic = event.values;

        if ( mGravity != null && mGeomagnetic != null ) {
            float[] R = new float[9];
            float[] I = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if ( success ) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                currentdegree = orientation[0]; // orientation contains: azimut, pitch and roll
            }
        }

    }

    public double getCurrentdegree() {
        return Math.toDegrees(this.currentdegree);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // not going to use...
    }
}
