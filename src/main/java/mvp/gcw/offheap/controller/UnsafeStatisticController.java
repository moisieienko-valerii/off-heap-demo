package mvp.gcw.offheap.controller;

import mvp.gcw.offheap.model.DailyMetricResult;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class UnsafeStatisticController implements IStatisticController {
    private static final Unsafe unsafe;
    private final long address;
    private final UnsafeWeatherEvent window = new UnsafeWeatherEvent();
    private int recordsCount = 0;

    static {
        unsafe = getUnsafe();
    }

    public UnsafeStatisticController() {
        try {
            this.address = unsafe.allocateMemory(EVENTS * window.getObjectSize());
        } catch (Throwable t) {
            throw t;
        }
    }

    @SuppressWarnings("restriction")
    private static Unsafe getUnsafe() {
        try {
            Field singleoneInstanceField = Unsafe.class.getDeclaredField("theUnsafe");
            singleoneInstanceField.setAccessible(true);
            return (Unsafe) singleoneInstanceField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void trackSensorData(int sensorId, double pressure, double temperature, boolean ok) {
        UnsafeWeatherEvent event = get(recordsCount++);
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
        UnsafeWeatherEvent event;
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
        unsafe.freeMemory(address);
    }

    private UnsafeWeatherEvent get(int index) {
        long offset = address + (index * window.getObjectSize());
        window.setObjectOffset(offset);
        return window;
    }

    private static class UnsafeWeatherEvent {
        private static long offSet = 0;
        private static final long sensorIdOffSet = offSet += 0;
        private static final long pressureOffSet = offSet += 4;
        private static final long tempOffSet = offSet += 8;
        private static final long okOffSet = offSet += 8;

        private static final long objectSize = offSet += 1;

        private long objectOffset = 0;

        private static long getObjectSize() {
            return objectSize;
        }

        public void setObjectOffset(long objectOffset) {
            this.objectOffset = objectOffset;
        }

        public int getSensorId() {
            return unsafe.getInt(objectOffset + sensorIdOffSet);
        }

        public void setSensorId(int sensorId) {
            unsafe.putInt(objectOffset + sensorIdOffSet, sensorId);
        }

        public double getPressure() {
            return unsafe.getDouble(objectOffset + pressureOffSet);
        }

        public void setPressure(double pressure) {
            unsafe.putDouble(objectOffset + pressureOffSet, pressure);
        }

        public double getTemperature() {
            return unsafe.getDouble(objectOffset + tempOffSet);
        }

        public void setTemperature(double temp) {
            unsafe.putDouble(objectOffset + tempOffSet, temp);
        }

        public boolean isOk() {
            byte value = unsafe.getByte(objectOffset + okOffSet);
            return value == 1 ? true : false;
        }

        public void setOk(boolean ok) {
            unsafe.putByte(objectOffset + okOffSet, (byte) (ok ? 1 : 0));
        }
    }
}
