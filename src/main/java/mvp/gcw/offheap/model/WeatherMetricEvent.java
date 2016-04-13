package mvp.gcw.offheap.model;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class WeatherMetricEvent implements IWeatherMetricEvent {
    private int sensorId;
    private double pressure;
    private double temperature;
    private boolean ok;

    public WeatherMetricEvent(int sensorId, double pressure, double temperature, boolean ok) {
        this.sensorId = sensorId;
        this.pressure = pressure;
        this.temperature = temperature;
        this.ok = ok;
    }

    public int getSensorId() {
        return sensorId;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public boolean isOk() {
        return ok;
    }
}
