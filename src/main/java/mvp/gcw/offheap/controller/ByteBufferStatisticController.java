package mvp.gcw.offheap.controller;

import mvp.gcw.offheap.model.DailyMetricResult;

import java.nio.ByteBuffer;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class ByteBufferStatisticController implements IStatisticController {
    private final ByteBuffer byteBuffer;
    private final ByteBufferWeatherEvent window = new ByteBufferWeatherEvent();
    private int recordsCount = 0;

    public ByteBufferStatisticController(boolean direct) {
        int capacity = IStatisticController.EVENTS * window.getObjectSize();
        this.byteBuffer = direct ? ByteBuffer.allocateDirect(capacity) :
                ByteBuffer.allocate(capacity);
    }

    @Override
    public void trackSensorData(int sensorId, double pressure, double temperature, boolean ok) {
        ByteBufferWeatherEvent event = get(recordsCount++);
        event.setSensorId(sensorId);
        event.setPressure(pressure);
        event.setTemperature(temperature);
        event.setOk(ok);
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
        ByteBufferWeatherEvent event;
        for (int i = 0; i < recordsCount; i++) {
            event = get(i);
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
        byteBuffer.clear();
    }

    private ByteBufferWeatherEvent get(int index) {
        int offset = index * window.getObjectSize();
        window.setObjectOffset(offset);
        return window;
    }

    class ByteBufferWeatherEvent {
        private int offSet = 0;
        private final int sensorIdOffSet = offSet += 0;
        private final int pressureOffSet = offSet += 4;
        private final int tempOffSet = offSet += 8;
        private final int okOffSet = offSet += 8;

        private final int objectSize = offSet += 1;

        private int objectOffset = 0;

        public int getObjectSize() {
            return objectSize;
        }

        public int getObjectOffset() {
            return objectOffset;
        }

        public void setObjectOffset(int objectOffset) {
            this.objectOffset = objectOffset;
        }

        public int getSensorId() {
            return byteBuffer.getInt(objectOffset + sensorIdOffSet);
        }

        public void setSensorId(int sensorId) {
            byteBuffer.putInt(objectOffset + sensorIdOffSet, sensorId);
        }

        public double getPressure() {
            return byteBuffer.getDouble(objectOffset + pressureOffSet);
        }

        public void setPressure(double pressure) {
            byteBuffer.putDouble(objectOffset + pressureOffSet, pressure);
        }

        public double getTemperature() {
            return byteBuffer.getDouble(objectOffset + tempOffSet);
        }

        public void setTemperature(double temp) {
            byteBuffer.putDouble(objectOffset + tempOffSet, temp);
        }

        public boolean isOk() {
            byte value = byteBuffer.get(objectOffset + okOffSet);
            return value == 1 ? true : false;
        }

        public void setOk(boolean ok) {
            byteBuffer.put(objectOffset + okOffSet, (byte) (ok ? 1 : 0));
        }
    }
}
