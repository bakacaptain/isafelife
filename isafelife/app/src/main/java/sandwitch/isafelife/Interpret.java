package sandwitch.isafelife;


import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;

import com.google.android.gms.location.LocationRequest;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;

import sandwitch.isafelife.models.Weather;
import sandwitch.isafelife.utils.JSONParser;

/**
 *  This activity gets the GPS location, weather from Yahoo,
 *  Google places and time. This data is sent to evaluation
 *  to a server.
 */

public class Interpret extends ActionBarActivity {

    private LocationManager locationManager;
    private LocationRequest locationRequest;

    public Interpret() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        locationRequest = LocationRequest.create();

        boolean isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isGPSEnable){
            Location currentPos = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
}
