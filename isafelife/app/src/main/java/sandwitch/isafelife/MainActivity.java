package sandwitch.isafelife;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import sandwitch.isafelife.services.WeatherTask;


public class MainActivity extends ActionBarActivity implements SensorEventListener { // added fra Rasmus

    private int interpretInterval = 10000; // 10 sec for each poll
    private Handler interpretHandler; // handler for starting background threads

    private LocationManager locationManager;
    private LocationListener locListener;
    private Location currentLocation = null;

    Context context; // Rasmus
    String fileName = "CA" + System.currentTimeMillis();
    File file = new File(context.getExternalFilesDir(null), fileName);
    FileOutputStream outputStream;

    String sbody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locListener = new SuperLocationListener();

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10,locListener);

        interpretHandler = new Handler();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    // ----------------------------------------------------------------------------------
    // Store Accelerometer data to file
    // ----------------------------------------------------------------------------------

    public void generateNote(String sFileName, String sBody){

        try
        {
            File root = new File(Environment.getExternalStorageDirectory(), null);
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, sFileName);
            FileWriter writer = new FileWriter(file);
            writer.append(sBody);
            writer.flush();
            writer.close();
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(IOException e)
        {

        }
    }

    // Handle event called when the toggle button for accelerometer has changed
    public void accelerate(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {

            // Enable sensing
            // added fra Rasmus
            registerAccelerometer();
            Log.i("info","On");
            generateNote(fileName, sbody);
        } else {
            // Disable sensing
            Log.i("info","Off");
        }
    }

    public void registerAccelerometer() {  //added fra Rasmus
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // Success! There's a accelerometer.
        if (accelerometer != null)
            mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        else {
// Failure! No accelerometer.
        }
    }



    long lastUpdate = 0; //added fra Rasmus
    String recording;
    @Override
    public final void onSensorChanged(SensorEvent event) {
        float x= event.values[0];
        float y= event.values[1];
        float z= event.values[2];

        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 100) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;

            recording = curTime + "  " + x + "  " + y + "  " + z + ":::"; // if everything else fails... split on :::
            Log.i("Accelerometer:", recording);
            sbody = sbody + "  " + recording;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // ----------------------------------------------------------------------------------
    // Interpreting surroundings for bystanders
    // ----------------------------------------------------------------------------------


    // Handle event called when the toggle button for interpret has changed
    public void interpret(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            // Enable sensing
            startInterpreting();
        } else {
            // Disable sensing
            stopInterpreting();
        }
    }

    private void createInterpretation(){
        Log.i("interpret","interpret start");

        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnable){
            // will return null if the last position wasn't update by location changed
            Location currentPos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(currentPos!=null){
                double lat = currentPos.getLatitude();
                double lon = currentPos.getLongitude();

                Log.i("position","{"+lat+"} {"+lon+"}");

                try{
                    new WeatherTask().execute(currentPos);
                }catch (Exception e){}
            } else {
                Log.w("position", "position was null");
            }
        } else {
            // GPS not enabled - warn user
            Log.w("position", "GPS off!");
        }
    }

    // A dummy listener needed for setting the last known location
    private class SuperLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            currentLocation = location;
            Log.i("location listener","lat:{"+location.getLatitude()+"}, lon;{"+location.getLongitude()+"}");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onProviderDisabled(String provider) {}

    }

    // created as a task to avoid stopping the UI thread
    Runnable interpretTask = new Runnable() {
        @Override
        public void run() {
            createInterpretation();
            interpretHandler.postDelayed(interpretTask, interpretInterval);
        }
    };

    // start the task to run forever
    private void startInterpreting(){
        interpretTask.run();
    }

    // stops the task
    private void stopInterpreting(){
        interpretHandler.removeCallbacks(interpretTask);
    }
}
