package org.korolev.dens;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ModifiedEulerMethod extends CauchyProblem {

    protected List<Double> xiDiv2;
    protected  double stepHDiv2;
    private final int methodOrder = 2;

    public ModifiedEulerMethod(String differentialEquation, double Y0, double X0, double XN, double stepH, double accuracy) {
        super(differentialEquation, Y0, X0, XN, stepH, accuracy);
        this.stepHDiv2 = stepH/2;
    }

    @Override
    public void decide() {
        rungeCheck();
        roundResults();
    }

    public void decideWithFixedStep() {
        calcXi(this.stepH);
        calcYi();
    }

    protected void calcYi() {
        yi = new ArrayList<>();
        firstApproximations = new ArrayList<>();
        yi.add(Y0);

        for (int i = 0; i < xi.size() - 1; i++) {
            System.out.println("xi = " + xi.get(i) + "; yi = " +  yi.get(i));
            double firstApproximation = BigDecimal.valueOf(yi.get(i))
                    .add(BigDecimal.valueOf(stepH).multiply(BigDecimal.valueOf(calcXiYi(xi.get(i), yi.get(i)))))
                    .setScale(6, RoundingMode.HALF_UP).doubleValue();

            double newY = yi.get(i) + 0.5 * stepH * (calcXiYi(xi.get(i), yi.get(i))
                            + calcXiYi(xi.get(i + 1), firstApproximation));
            firstApproximations.add(firstApproximation);
            //System.out.println(firstApproximation);
            yi.add(i + 1, newY);
        }
    }

    protected void rungeCheck() {
        calcXi(this.stepH);
        calcYi();

        ArrayList<Double> yh = new ArrayList<>(yi);
        ArrayList<Double> xh = new ArrayList<>(xi);

        this.stepH /= 2d;
        calcXi(this.stepH);
        calcYi();

        ArrayList<Double> yhDiv2 = new ArrayList<>(yi);

        System.out.println("y при h = " + stepH * 2 +  ": " + yh.get(yh.size() - 1) + "; y при h/2 = " + stepH + ": " + yhDiv2.get(yhDiv2.size() - 1));

        boolean R = (Math.abs(yh.get(yh.size() - 1) - yhDiv2.get(yhDiv2.size() - 1))
                / (Math.pow(2d, methodOrder) - 1)) <= accuracy;
        if (!R) rungeCheck();
        else {
            //System.out.println("y при h = " + stepH * 2 +  ": " + yh.get(yh.size() - 1) + "; y при h/2 = " + stepH + ": " + yhDiv2.get(yhDiv2.size() - 1));
            this.yi = yh;
            this.xi = xh;
            this.stepH *= 2;
            System.out.println("Выбранный шаг: " + stepH);
        }

        //System.out.println("y при h: " + yh.get(yh.size() - 1) + "; y при h/2: " + yh.get(yh.size() - 1));
        //System.out.println("Полученный шаг: " + stepH);
    }

}
