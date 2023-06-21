package org.korolev.dens;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class AdamsMethod extends CauchyProblem {

    private double fi;
    private double deltaFi;
    private double delta2Fi;
    private double delta3Fi;
    private final String equation;

    public AdamsMethod(String differentialEquation, double Y0, double X0, double XN, double stepH, double accuracy) {
        super(differentialEquation, Y0, X0, XN, stepH, accuracy);
        this.equation = differentialEquation;
    }

    @Override
    public void decide() {
        useRungeKutta();
        calcXi(this.stepH);
        calcYi();
    }

    protected void calcYi() {
        for (int i = 3; i < xi.size() - 1; i++) {
            calcFiniteDifferences(i);
            double newY = yi.get(i) + stepH * fi + (Math.pow(stepH, 2)/2d) * deltaFi
                    + (Math.pow(5 * stepH, 3)/12d) * delta2Fi + (Math.pow(3 * stepH, 4)/8d) * delta3Fi;
            yi.add(i + 1, newY);
        }
    }

    private void calcFiniteDifferences(int index) {
        fi = calcXiYi(xi.get(index), yi.get(index));
        deltaFi = fi - calcXiYi(xi.get(index - 1), yi.get(index - 1));
        delta2Fi = fi - 2 * calcXiYi(xi.get(index - 1), yi.get(index - 1)) + calcXiYi(xi.get(index - 2), yi.get(index - 2));
        delta3Fi = fi - 3 * calcXiYi(xi.get(index - 1), yi.get(index - 1))
                + 3 * calcXiYi(xi.get(index - 2), yi.get(index - 2)) - calcXiYi(xi.get(index - 3), yi.get(index - 3));
    }

    private void useRungeKutta() {
        RungeKutta4Method rungeMethod = new RungeKutta4Method
                (equation, Y0, X0, X0 + stepH * 3, stepH, accuracy);
        rungeMethod.decideWithFixedStep();
        yi = new ArrayList<>(rungeMethod.getYi());
        //System.out.println(yi);
    }
}
