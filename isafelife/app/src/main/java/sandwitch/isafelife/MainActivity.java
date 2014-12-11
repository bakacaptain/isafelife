package sandwitch.isafelife;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ToggleButton;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sandwitch.isafelife.models.Weather;
import sandwitch.isafelife.services.InterpretBackgroundService;
import sandwitch.isafelife.utils.JSONParser;


public class MainActivity extends ActionBarActivity {

    private int interpretInterval = 10000; // 10 sec for each poll
    private Handler interpretHandler; // handler for starting background threads
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);


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

    // TODO: RASMUS - gør som jeg har gjort under med en handler, runnable og service
    // Handle event called when the toggle button for accelerometer has changed
    public void accelerate(View view){
        // Is the toggle on?
        boolean on = ((ToggleButton) view).isChecked();

        if (on) {
            // Enable sensing
            Log.i("info","On");
        } else {
            // Disable sensing
            Log.i("info","Off");
        }
    }


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
        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(isGPSEnable){
            Location currentPos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(currentPos!=null){
                double lat = currentPos.getLatitude();
                double lon = currentPos.getLongitude();

                Log.i("position",String.format("{0} {1}",lat,lon));

                // Weather current_weather = getWeather(lat,lon);
            } else {
                Log.w("position", "position was null");
            }
        } else {
            // GPS not enabled - warn user
            Log.w("position", "GPS off!");
        }

        Weather weather = getWeather(56.1929,10.16);

    }

    private Weather getWeather(double lat, double lon) {
        // http://api.openweathermap.org/data/2.5/weather?lat=56.162939&lon=10.203921&units=metric

        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric";

        HttpGet request = new HttpGet(url);
        JSONArray result = JSONParser.getJSONFromUrl(request);

        // do magic stuff with JSONArray
        try {
            JSONObject root = result.getJSONObject(0);
            JSONObject temp = root.getJSONObject("main");
            JSONObject wind = root.getJSONObject("wind");
            JSONObject rain = root.getJSONObject("rain");

        } catch (JSONException e) {
            Log.e("JSON parse",e.getMessage(),e);
        }


        return new Weather(0, 0, 0);
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
