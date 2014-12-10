package sandwitch.isafelife.models;

/**
 * Created by Sam on 10/12/2014.
 */
public class Weather {
    private double temperature;
    private double windspeed;
    private double rain;

    public Weather(double rain, double windspeed, double temperature) {
        this.rain = rain;
        this.windspeed = windspeed;
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public double getRain() {
        return rain;
    }
}
