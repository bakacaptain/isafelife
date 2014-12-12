package sandwitch.isafelife.models;

/**
 * Created by Sam on 10/12/2014.
 */
public class Weather {
    private double temperature;
    private double windspeed;
    private double rain;

    public Weather(double temperature, double windspeed, double rain) {
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

    @Override
    public String toString(){
        return "Temp:{"+temperature+"}, Wind:{"+windspeed+"}, Rain:{"+rain+"}";
    }
}
