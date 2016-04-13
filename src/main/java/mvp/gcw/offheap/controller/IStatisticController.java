package mvp.gcw.offheap.controller;

import mvp.gcw.offheap.model.DailyMetricResult;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public interface IStatisticController {
    int EVENTS = 20000000;

    void trackSensorData(int sensorId, double pressure, double temperature, boolean ok);

    DailyMetricResult calculate();

    void clear();
}
