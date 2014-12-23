/*************************************************************************
*
* Copyright (C) 2005 University of Geneva, Switzerland.
*
* This file is part of n-genes
*
* n-genes is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
* 
* n-genes is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with n-genes; if not, write to the Free Software
* Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301  USA
*
* http://cui.unige.ch/spc/tools/n-genes
* n-genes@cui.unige.ch
*
******************************************************************************/


package ch.unige.ng.statistics;

import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;
import ch.unige.ng.species.VariableSize;

import static java.lang.Math.sqrt;
import java.util.Arrays;

/**
 * Calculates the standard deviation of each gene on the population and sum it. Like STDStatistic, it is not the real
 * standard deviation.
 *
 * @author Jacques Fontignie
 * @version 3 mai 2005
 * @see STDStatistic
 */
public class GenotypeStdStatistic <T extends Number,S extends GlobalIndividual<T>> implements Statistic<Double, S> {
    private Calculator calculator;
    private double sum [];
    private double sum2[];
    private int indSize;

    public Double evaluate(Population<S> population) {
        if (calculator == null) {
            Class c = population.get(0).getGeneFactory().getClassGene();
            if (c == Double.class)
                calculator = new DoubleCalculator();
            else if (c == Boolean.class)
                calculator = new BooleanCalculator();
            else
                throw new IllegalStateException("Not implemented yet");
            if (population.get(0) instanceof VariableSize) {
                throw new IllegalStateException("It is only possible to calculate the dispersion with fixed length individuals");
            }
            indSize = population.get(0).size();
            sum = new double[indSize];
            sum2 = new double[indSize];
        }
        Arrays.fill(sum, 0);
        Arrays.fill(sum2, 0);
        for (S ind : population) {
            for (int i = 0; i < indSize; i++) {
                double value = calculator.getVal(ind.get(i));
                sum[i] += value;
                sum2[i] += sqr(value);
            }
        }
        double result = 0;
        for (int i = 0; i < indSize; i++){
            double val = sum2[i] / population.size() - sqr(sum[i] / population.size());
            result += (val > 0)? sqrt(val) : 0;
        }
        return result;
    }

    private static final double sqr(double x) {
        return x * x;
    }

    public String getName() {
        return "dispersion";
    }

    private interface Calculator <T> {
        public double getVal(T number);
    }

    private class DoubleCalculator implements Calculator<Double> {

        public double getVal(Double aDouble) {
            return aDouble;
        }
    }

    private class BooleanCalculator implements Calculator<Boolean> {
        public double getVal(Boolean aBoolean) {
            return aBoolean.booleanValue() ? 1 : 0;
        }
    }
}
