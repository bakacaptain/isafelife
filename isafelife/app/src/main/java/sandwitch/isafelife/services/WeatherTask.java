package sandwitch.isafelife.services;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sandwitch.isafelife.models.Weather;
import sandwitch.isafelife.utils.JSONParser;
import sandwitch.isafelife.utils.LogWriter;

/**
 * Created by Sam on 12/12/2014.
 */
public class WeatherTask extends AsyncTask<Location,Void,Weather> {

    private LogWriter writer;

    public WeatherTask(LogWriter writer) {
        this.writer = writer;
    }

    @Override
    protected Weather doInBackground(Location... params) {
        // http://api.openweathermap.org/data/2.5/weather?lat=56.162939&lon=10.203921&units=metric

        double lat = params[0].getLatitude();
        double lon = params[0].getLongitude();
        double acc = params[0].getAccuracy();

        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+lon+"&units=metric";

        HttpGet request = new HttpGet(url);
        JSONObject result = JSONParser.getJSONFromUrl(request);

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
            Log.e("JSON parse", e.getMessage(), e);
        }

        Weather weather = new Weather(temp_value, wind_speed, rain_value);
        Log.i("weather",weather.toString());

        // format:
        // time,latitute, longtitude, accuracy, temperature, wind speed, rain
        writer.write(System.currentTimeMillis()+","+lat+","+lon+","+acc+","+temp_value+","+wind_speed+","+rain_value+"\n");

        return weather;
    }
}
