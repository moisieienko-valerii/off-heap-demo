package mvp.gcw.offheap;

import mvp.gcw.offheap.controller.*;
import mvp.gcw.offheap.model.WeatherMetricEvent;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class Benchmarks {
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void linkedList() {
        ListStatisticController controller = new ListStatisticController(new LinkedList<WeatherMetricEvent>());
        doWork(controller);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void arrayList() {
        ListStatisticController controller = new ListStatisticController(new ArrayList<WeatherMetricEvent>(IStatisticController.EVENTS));
        doWork(controller);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void byteBuffer() {
        ByteBufferStatisticController controller = new ByteBufferStatisticController(false);
        doWork(controller);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void directByteBuffer() {
        ByteBufferStatisticController controller = new ByteBufferStatisticController(true);
        doWork(controller);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void unsafe() {
        UnsafeStatisticController controller = new UnsafeStatisticController();
        doWork(controller);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void mapdb() {
        MapDbStatisticController controller = new MapDbStatisticController();
        doWork(controller);
    }

    private void doWork(IStatisticController controller) {
        Random r = new Random();
        for (int i = 0; i < IStatisticController.EVENTS; i++) {
            controller.trackSensorData(r.nextInt(20), r.nextDouble() * 1000, r.nextDouble() * 10, i % 10 == 0);
        }
        controller.calculate();
        controller.clear();
    }
}
