package org.millerM907;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QuadraticEquationSolverTest {

    @Test
    @DisplayName("Уравнение x² + 1 = 0 не имеет вещественных корней → возвращается пустой массив")
    void givenQuadraticEquationWithNoRealRoots_whenSolve_thenReturnsEmptyArray() {
        double a = 1;
        double b = 0;
        double c = 1;


        double[] result = QuadraticEquationSolver.solve(a, b, c);


        assertNotNull(result, "Результат не должен быть null");
        assertEquals(0, result.length, "Ожидается, что корней нет, массив должен быть пустой");
    }

    @Test
    @DisplayName("Уравнение x² - 1 = 0 имеет два корня: 1 и -1")
    void givenQuadraticEquationWithTwoDistinctRoots_whenSolve_thenReturnsTwoRoots() {
        double a = 1;
        double b = 0;
        double c = -1;


        double[] roots = QuadraticEquationSolver.solve(a, b, c);


        assertNotNull(roots, "Результат не должен быть null");
        assertEquals(2, roots.length, "Ожидается два корня");
        assertTrue(
                (almostEqual(roots[0], 1.0) && almostEqual(roots[1], -1.0)) ||
                        (almostEqual(roots[0], -1.0) && almostEqual(roots[1], 1.0)),
                "Корни должны быть 1 и -1 в любом порядке"
        );
    }

    @Test
    @DisplayName("Уравнение x² + 2x + 1 = 0 имеет один корень кратности 2 равный -1")
    void givenQuadraticEquationWithOneDoubleRoot_whenSolve_thenReturnsOneRoot() {
        double a = 1;
        double b = 2;
        double c = 1;


        double[] roots = QuadraticEquationSolver.solve(a, b, c);


        assertNotNull(roots, "Результат не должен быть null");
        assertEquals(1, roots.length, "Ожидается один корень");
        assertTrue(almostEqual(roots[0], -1.0), "Корень должен быть равен -1");
    }

    @Test
    @DisplayName("При коэффициенте a, близком к 0, метод solve выбрасывает IllegalArgumentException")
    void givenCoefficientANearZero_whenSolve_thenThrowsException() {
        double a = 1e-15;
        double b = 1;
        double c = 1;


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> QuadraticEquationSolver.solve(a, b, c));


        assertEquals("Коэффициент a не может быть равен 0", exception.getMessage());
    }

    @Test
    @DisplayName("Дискриминант близок к нулю — уравнение имеет один корень кратности 2")
    void givenQuadraticEquationWithDiscriminantCloseToZero_whenSolve_thenReturnsOneDoubleRoot() {
        double a = 1;
        double b = 2 + 1e-13;
        double c = 1;


        double[] roots = QuadraticEquationSolver.solve(a, b, c);


        assertNotNull(roots, "Результат не должен быть null");
        assertEquals(1, roots.length, "Ожидается один корень");
        assertTrue(almostEqual(roots[0], -1.0), "Корень должен быть равен -1");
    }

    private boolean almostEqual(double a, double b) {
        final double EPSILON = 1e-9;
        return Math.abs(a - b) < EPSILON;
    }

    @Test
    @DisplayName("Проверка, что коэффициенты не могут быть NaN или бесконечными — выбрасывается исключение")
    void givenSpecialDoubleValues_whenSolve_thenThrowsOrHandles() {
        double[] specialValues = {
                Double.NaN,
                Double.POSITIVE_INFINITY,
                Double.NEGATIVE_INFINITY
        };


        for (double a : specialValues) {
            double b = 1;
            double c = 1;
            assertThrows(IllegalArgumentException.class,
                    () -> QuadraticEquationSolver.solve(a, b, c),
                    "Должно выбрасывать исключение при a = " + a);
        }


        for (double b : specialValues) {
            double a = 1;
            double c = 1;
            assertThrows(IllegalArgumentException.class,
                    () -> QuadraticEquationSolver.solve(a, b, c),
                    "Должно выбрасывать исключение при b = " + b);
        }


        for (double c : specialValues) {
            double a = 1;
            double b = 1;
            assertThrows(IllegalArgumentException.class,
                    () -> QuadraticEquationSolver.solve(a, b, c),
                    "Должно выбрасывать исключение при c = " + c);
        }
    }
}
