package sandwitch.isafelife.utils;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Sam on 10/12/2014.
 */
public class JSONParser {

    public JSONParser() {
    }

    /**
     * Give it a HttpUriRequest - well you should use a HttpGet
     * @param request
     * @return
     */
    public static JSONObject getJSONFromUrl(HttpUriRequest request){
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();

        try{
            HttpResponse response = client.execute(request);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if(statusCode==200){
                // everything OK
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));

                // read lines in json file and add them to the builder
                String line;
                while((line = reader.readLine()) != null){
                    builder.append(line);
                }
            }else{
                // http request failed
            }
        } catch (Exception e) {
            Log.e("http","cannot get",e);
        }

        JSONObject result = null;

        try {
            Log.i("JSON",builder.toString());

            result = new JSONObject(builder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
