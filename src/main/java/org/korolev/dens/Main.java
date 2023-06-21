package org.korolev.dens;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        double accuracy = 0.001;
        String function = "y + (1 + x) * y^2";
        double y0 = -1;
        double x0 = 1;
        double xn = 1.5;
        double step = 0.1;

        ModifiedEulerMethod eulerMethod = new ModifiedEulerMethod
                (function, y0, x0, xn, step, accuracy);
        eulerMethod.decide();
        System.out.println("Модифицированный метод Эйлера:");
        printResults(eulerMethod.getYi(), eulerMethod.getStepH(), 1);
        //System.out.println(eulerMethod.getStepH());


        RungeKutta4Method rungeMethod = new RungeKutta4Method
                (function, y0, x0, xn, step, accuracy);
        rungeMethod.decide();
        System.out.println("Метод Рунге-Крута:");
        printResults(rungeMethod.getYi(), rungeMethod.getStepH(), 1);

        AdamsMethod adamsMethod = new AdamsMethod
                (function, y0, x0, xn, step, accuracy);
        adamsMethod.decide();
        System.out.println("Метод Адамса:");
        printResults(adamsMethod.getYi(), adamsMethod.getStepH(), 1);

        List<Double> yHonest = new ArrayList<>(Arrays.asList(-1d, -0.909091, -0.833333, -0.769231, -0.714286, -0.666667
        ));
    }

    private static void printResults(List<Double> ys, double step, double x0) {
        for (Double y : ys) {
            System.out.print("(x = " + x0 + "; y = " + y + ") ");
            x0 = BigDecimal.valueOf(x0).add(BigDecimal.valueOf(step)).doubleValue();
        }
        System.out.println();
        System.out.println();
    }
}