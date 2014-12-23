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


package ch.unige.ng.ops.crossover;

import ch.unige.ng.species.Individual;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.gen.Context;

/**
 * This crossover calculates the mean individual of two mates. It only works with:
 * <ul>
 * <li>double</li>
 * <li>float </li>
 * <li>long </li>
 * <li>integer</li>
 * <li>byte</li>
 * <li>short</li>
 * </ul>
 *
 * @author Jacques Fontignie
 * @version 21 avr. 2005
 */
public class MeanCrossover <S extends Number, T extends Individual<S>> extends SingleOffspringCrossover<T> {
    private Mean mean;

    public void crossover(T mate1, T mate2, IGroup<T> population, Context context) {
        if (mate1.size() != mate2.size())
            throw new IllegalStateException("The size of the individuals is not equal");
        //Impossible to avoid easily this warning
        T offspring = (T) mate1.copy();
        if (mean == null) {
            Class c = mate1.get(0).getClass();
            if (c == Double.class)
                mean = new DoubleMean();
            else if (c == Integer.class)
                mean = new IntegerMean();
            else if (c == Float.class)
                mean = new FloatMean();
            else if (c == Long.class)
                mean = new LongMean();
            else if (c == Byte.class)
                mean = new ByteMean();
            else if (c == Short.class)
                mean = new ShortMean();
        }
        //Impossible to avoid easily this warning
        for (int i = 0, length = mate1.size(); i < length; i++)
            offspring.set((S) mean.mean(mate1.get(i), mate2.get(i)), i);
        population.add(offspring);

    }

    private static interface Mean <U extends Number> {
        public U mean(U val1, U val2);
    }


    private class DoubleMean implements Mean<Double> {
        public Double mean(Double val1, Double val2) {
            return (val1 + val2) / 2.0;
        }
    }

    private class IntegerMean implements Mean<Integer> {
        public Integer mean(Integer val1, Integer val2) {
            return (val1 + val2) / 2;
        }
    }

    private class FloatMean implements Mean<Float>{
        public Float mean(Float val1, Float val2) {
            return (val1+val2)/2.f;
        }
    }

    private class LongMean implements Mean<Long>{
        public Long mean(Long val1, Long val2) {
            return (long) ((val1+val2)/2);
        }
    }

    private class ByteMean implements Mean<Byte>{
        public Byte mean(Byte val1, Byte val2) {
            return (byte) ((val1 + val2) /2);
        }
    }

    private class ShortMean implements Mean<Short>{
        public Short mean(Short val1, Short val2) {
            return (short) ((val1+val2)/2);
        }
    }



}
