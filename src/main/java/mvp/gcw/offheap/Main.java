package mvp.gcw.offheap;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * Created by Moisieienko Valerii on 20.07.2015.
 */
public class Main {
    public static void main(String[] args) throws RunnerException {
        String methodName = args[0];
        Options opt = new OptionsBuilder()
                .include(".*" + Benchmarks.class.getSimpleName() + "." + methodName)
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
