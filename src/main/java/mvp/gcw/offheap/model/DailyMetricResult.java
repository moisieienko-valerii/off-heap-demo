package mvp.gcw.offheap.model;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class DailyMetricResult {
    private double minPressure;
    private double maxPressure;
    private double avgPressure;
    private double minTemp;
    private double maxTemp;
    private double avgTemp;

    public DailyMetricResult(double minPressure, double maxPressure, double avgPressure, double minTemp, double maxTemp, double avgTemp) {
        this.minPressure = minPressure;
        this.maxPressure = maxPressure;
        this.avgPressure = avgPressure;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.avgTemp = avgTemp;
    }

    public double getMinPressure() {
        return minPressure;
    }

    public double getMaxPressure() {
        return maxPressure;
    }

    public double getAvgPressure() {
        return avgPressure;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }

    public double getAvgTemp() {
        return avgTemp;
    }


    @Override
    public String toString() {
        return "DailyMetricResult{" +
                "minPressure=" + minPressure +
                ", maxPressure=" + maxPressure +
                ", avgPressure=" + avgPressure +
                ", minTemp=" + minTemp +
                ", maxTemp=" + maxTemp +
                ", avgTemp=" + avgTemp +
                '}';
    }
}
