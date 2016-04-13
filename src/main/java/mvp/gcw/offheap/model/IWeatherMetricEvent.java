package mvp.gcw.offheap.model;

import java.io.Serializable;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public interface IWeatherMetricEvent extends Serializable {
    int getSensorId();

    double getPressure();

    double getTemperature();

    boolean isOk();
}
