package mvp.gcw.offheap.controller;

import mvp.gcw.offheap.model.DailyMetricResult;
import mvp.gcw.offheap.model.WeatherMetricEvent;

import java.util.*;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class ListStatisticController implements IStatisticController {
    private List<WeatherMetricEvent> events;

    public ListStatisticController(List<WeatherMetricEvent> events) {
        this.events = events;
    }

    @Override
    public void trackSensorData(int sensorId, double pressure, double temperature, boolean ok) {
        events.add(new WeatherMetricEvent(sensorId, pressure, temperature, ok));
    }

    @Override
    public DailyMetricResult calculate() {
        double minPressure = Double.MAX_VALUE;
        double maxPressure = Double.MIN_VALUE;
        double sumPressure = 0;
        double minTemp = Double.MAX_VALUE;
        double maxTemp = Double.MIN_VALUE;
        double sumTemp = 0;
        int eventCount = 0;
        for (WeatherMetricEvent event : events) {
            if (event.isOk()) {
                if (event.getPressure() < minPressure)
                    minPressure = event.getPressure();
                if (event.getPressure() > maxPressure) {
                    maxPressure = event.getPressure();
                }
                sumPressure = sumPressure + event.getPressure();
                if (event.getTemperature() < minTemp)
                    minTemp = event.getTemperature();
                if (event.getTemperature() > maxTemp) {
                    maxTemp = event.getTemperature();
                }
                sumTemp = sumTemp + event.getTemperature();
                eventCount++;
            }
        }
        return new DailyMetricResult(minPressure, maxPressure, sumPressure / eventCount, minTemp, maxTemp, sumTemp / eventCount);
    }

    @Override
    public void clear() {
        this.events.clear();
    }
}
