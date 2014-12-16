package sandwitch.isafelife.services;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import sandwitch.isafelife.models.Weather;
import sandwitch.isafelife.utils.JSONParser;
import sandwitch.isafelife.utils.LogWriter;

/**
 * Created by Sam on 12/12/2014.
 */
public class WeatherTask extends AsyncTask<Location,Void,Weather> {

    private File file;

    public WeatherTask(File file) {
        this.file = file;
        Log.i("weather task","file init");
    }

    @Override
    protected Weather doInBackground(Location... params) {

        /// ----------------------------------------------------------------------
        /// Open Weather API
        // http://api.openweathermap.org/data/2.5/weather?lat=56.162939&lon=10.203921&units=metric

        double lat = params[0].getLatitude();
        double lon = params[0].getLongitude();
        double acc = params[0].getAccuracy();

        String weather_url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric";

        HttpGet weather_request = new HttpGet(weather_url);
        JSONObject result = JSONParser.getJSONFromUrl(weather_request);

        double temp_value = 0;
        double wind_speed = 0;
        double rain_value = 0;

        // do magic stuff with JSONArray
        try {
            JSONObject temp = result.getJSONObject("main");
            temp_value = temp.getDouble("temp");

            JSONObject wind = result.getJSONObject("wind");
            wind_speed = wind.getDouble("speed");

            JSONObject rain = result.getJSONObject("rain");
            rain_value = rain.getDouble("3h");
        } catch (JSONException e) {
            Log.e("JSON weather", e.getMessage(), e);
        }

        /// ----------------------------------------------------------------------
        /// Google Places API

        // https://maps.googleapis.com/maps/api/place/search/json?key=AIzaSyDlrPrOOJnen7_dvxNc8gYXDLrkec-UbzI&location=56.2919,10.1613&radius=200
        String api_key = "AIzaSyDlrPrOOJnen7_dvxNc8gYXDLrkec-UbzI";
        int places_radius = 200; // meters in a circular from the position

        String places_url = "https://maps.googleapis.com/maps/api/place/search/json?key="+api_key+"&location="+lat+","+lon+"&radius="+places_radius;

        HttpGet places_request = new HttpGet(places_url);
        JSONObject places = JSONParser.getJSONFromUrl(places_request);

        int no_of_nearby_places = 0; // initially none

        try{
            JSONArray places_array = places.getJSONArray("results");
            no_of_nearby_places = places_array.length();
        }catch(Exception e){
            Log.e("JSON place", e.getMessage(), e);
        }

        /// ----------------------------------------------------------------------
        /// results

        // yes we meant to make a parser to weather-object by this is not a nice code course, so it was dropped
        Weather weather = new Weather(temp_value, wind_speed, rain_value);
        Log.i("weather",weather.toString());

        // format:
        // time,latitute, longtitude, accuracy, temperature, wind speed, rain, no of places
        LogWriter.write(this.file,System.currentTimeMillis()+","+lat+","+lon+","+acc+","+temp_value+","+wind_speed+","+rain_value+","+no_of_nearby_places+"\n");

        return weather;
    }
}
