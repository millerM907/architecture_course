package org.millerM907.hw01_tdd_methodology.quadratic_solver;

public class QuadraticEquationSolver {

    private static final double EPSILON = 1e-12;

    public static double[] solve(double a, double b, double c) {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isNaN(c)) {
            throw new IllegalArgumentException("Коэффициенты не могут быть NaN");
        }
        if (Double.isInfinite(a) || Double.isInfinite(b) || Double.isInfinite(c)) {
            throw new IllegalArgumentException("Коэффициенты не могут быть бесконечными");
        }

        if (Math.abs(a) < EPSILON) {
            throw new IllegalArgumentException("Коэффициент a не может быть равен 0");
        }

        double discriminant = b * b - 4 * a * c;

        if (discriminant < 0) {
            return new double[]{};
        } else if (Math.abs(discriminant) < EPSILON) {
            double root = -b / (2 * a);
            return new double[]{root};
        } else {
            double sqrtD = Math.sqrt(discriminant);
            double root1 = (-b + sqrtD) / (2 * a);
            double root2 = (-b - sqrtD) / (2 * a);
            return new double[]{root1, root2};
        }
    }
}
