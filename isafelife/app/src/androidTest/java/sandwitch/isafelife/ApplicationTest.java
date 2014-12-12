package sandwitch.isafelife;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    String json = "{\"coord\":{\"lon\":10.21,\"lat\":56.19},\"sys\":{\"type\":1,\"id\":5241,\"message\":0.1899,\"country\":\"DK\",\"sunrise\":1418370178,\"sunset\":1418395388},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"base\":\"cmc stations\",\"main\":{\"temp\":2.75,\"pressure\":984,\"humidity\":86,\"temp_min\":2.5,\"temp_max\":3},\"wind\":{\"speed\":4.6,\"deg\":140,\"var_beg\":100,\"var_end\":190},\"clouds\":{\"all\":92},\"dt\":1418388894,\"id\":2624652,\"name\":\"Arhus\",\"cod\":200}";

}

