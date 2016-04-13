package mvp.gcw.offheap.controller;

import mvp.gcw.offheap.model.DailyMetricResult;
import mvp.gcw.offheap.model.WeatherMetricEvent;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class MapDbStatisticController implements IStatisticController {
    private DB db;
    private BlockingQueue<WeatherMetricEvent> queue;
    private int records = 0;

    public MapDbStatisticController() {
        db = DBMaker.newMemoryDirectDB().transactionDisable().closeOnJvmShutdown().make();
        queue = db.getQueue("cache");
    }

    @Override
    public void trackSensorData(int sensorId, double pressure, double temperature, boolean ok) {
        WeatherMetricEvent event = new WeatherMetricEvent(sensorId, pressure, temperature, ok);
        queue.add(event);
        records++;
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
        for (int i = 0; i < records; i++) {
            WeatherMetricEvent event = queue.peek();
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
        queue.clear();
        db.close();
    }
}
