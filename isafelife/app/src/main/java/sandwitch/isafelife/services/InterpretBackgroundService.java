package sandwitch.isafelife.services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;

import sandwitch.isafelife.models.Weather;
import sandwitch.isafelife.utils.JSONParser;

/**
 * Intents are sent to the service periodically to infer
 * if there is any bystanders around
 * Created by Baka on 11-12-2014.
 */
public class InterpretBackgroundService extends IntentService {

    private LocationManager locationManager;

    public InterpretBackgroundService(){

        super("InterpretBackgroundService");
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public InterpretBackgroundService(String name) {
        super(name);

        // get the location manager service
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

    }

    /**
     * Handler for when you send an intent to this service
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // do work based on the contents of the intent

        Log.i("service","interpret location");

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
    }

    private Weather getWeather(double lat, double lon) {
        // http://api.openweathermap.org/data/2.5/weather?lat=56.162939&lon=10.203921&units=metric

        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric";

        HttpGet request = new HttpGet(url);
        JSONArray result = JSONParser.getJSONFromUrl(request);

        // do magic stuff with JSONArray

        return new Weather(0, 0, 0);
    }

    // TODO: Christian, insæt det du har lavt i denne funktion
    private int getPlacesNear(double lat, double lon){
        return -1;
    }
}
