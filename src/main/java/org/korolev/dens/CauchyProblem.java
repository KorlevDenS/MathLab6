package org.korolev.dens;

import lombok.Getter;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class CauchyProblem implements Decidable {
    protected final Expression differentialEquation;
    protected double Y0;
    protected double X0;
    protected final double XN;
    protected double stepH;
    protected final double accuracy;
    protected final double firstStepH;

    protected List<Double> xi;
    protected List<Double> xiFinal = new ArrayList<>();
    protected List<Double> yi;
    protected List<Double> firstApproximations;

    public CauchyProblem(String differentialEquation, double Y0, double X0, double XN, double stepH, double accuracy) {
        this.differentialEquation = new ExpressionBuilder(differentialEquation)
                .variables("x", "y").build();
        this.Y0 = Y0;
        this.X0 = X0;
        this.XN = XN;
        this.stepH = stepH;
        this.accuracy = accuracy;
        this.firstStepH = stepH;
    }

    public Expression getDifferentialEquation() {
        return differentialEquation;
    }

    protected double calcXiYi(double xi, double yi) {
        return differentialEquation.setVariable("x", xi).setVariable("y", yi).evaluate();
    }

    protected void calcXi(double h) {
        xi = new ArrayList<>();
        xi.add(X0);
        double x0 = X0;
        while (x0 < XN) {
            xi.add(BigDecimal.valueOf(x0).add(BigDecimal.valueOf(h)).doubleValue());
            x0 = BigDecimal.valueOf(x0).add(BigDecimal.valueOf(h)).doubleValue();
        }
    }

    protected void roundResults() {
        int scale = BigDecimal.valueOf(this.accuracy).scale();
        List<Double> scaledY = yi.stream().map(y -> BigDecimal.valueOf(y)
                .setScale(scale, RoundingMode.HALF_UP).doubleValue()).toList();
        this.yi = new ArrayList<>(scaledY);
    }

    public double getY0() {
        return Y0;
    }

    public double getX0() {
        return X0;
    }

    public double getXN() {
        return XN;
    }

    public double getStepH() {
        return stepH;
    }

    public double getAccuracy() {
        return accuracy;
    }
}
