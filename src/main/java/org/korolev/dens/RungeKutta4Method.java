package org.korolev.dens;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class RungeKutta4Method extends ModifiedEulerMethod {

    private double k1;
    private double k2;
    private double k3;
    private double k4;
    private final int methodOrder = 4;

    public RungeKutta4Method(String differentialEquation, double Y0, double X0, double XN, double stepH, double accuracy) {
        super(differentialEquation, Y0, X0, XN, stepH, accuracy);
    }

    public void decideWithFixedStep() {
        calcXi(this.stepH);
        calcYi();
    }

    protected void calcYi() {
        yi = new ArrayList<>();
        yi.add(Y0);

        for (int i = 0; i < xi.size() - 1; i++) {
            calcCoefficients(xi.get(i), yi.get(i));
            double newY = yi.get(i) + (1d/6d)*(k1 + 2*k2 + 2*k3 + k4);
            yi.add(i + 1, newY);
        }
    }

    private void calcCoefficients(double xi, double yi){
        k1 = stepH * calcXiYi(xi, yi);
        k2 = stepH * calcXiYi(xi + stepH/2, yi + k1/2);
        k3 = stepH * calcXiYi(xi + stepH/2, yi + k2/2);
        k4 = stepH * calcXiYi(xi + stepH, yi + k3);
    }

}
