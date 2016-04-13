package mvp.gcw.offheap;

import mvp.gcw.offheap.controller.IStatisticController;
import mvp.gcw.offheap.controller.MapDbStatisticController;
import mvp.gcw.offheap.controller.UnsafeStatisticController;
import mvp.gcw.offheap.model.DailyMetricResult;

import java.util.Random;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class TestMain {

    public static void main(String[] args) {
        MapDbStatisticController controller = new MapDbStatisticController();
        Random r = new Random();
        for (int i = 0; i < IStatisticController.EVENTS; i++) {
            controller.trackSensorData(r.nextInt(20), r.nextDouble() * 1000, r.nextDouble() * 10, i % 10 == 0);
            if (i % 1000 == 0)
                System.out.println("Done " + i);
        }
        DailyMetricResult result = controller.calculate();
        System.out.println(result);
        controller.clear();
    }
}
