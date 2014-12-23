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

import ch.unige.ng.gen.Context;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.species.Individual;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;

/**
 *  This class recombines two Individual, using a two point cross-over.
 * This operator is useful for Individual where the specific order of
 * genes does not matter, but the epistatic interactions are important.
 * There are two "politics": a totaly random two points crossover and
 * an equilibrated method, which choose the first point at random and the
 * second at <i>(i + N/2)%N</i> genes from the first. Where <i>i</i> is the position
 * of the first crossing point and <i>N</i> the size of the Individual.
 */
public class TwoPointsCrossover <S extends Object,T extends Individual<S>> extends DoubleOffspringCrossOver<T> {

    public enum Politic {
        EQUILIBRATED, RANDOM
    };

    private Politic politic;

    public TwoPointsCrossover(Politic _politic) {
        politic = _politic;
    }

    /**
     * The default constructor to be parser compliant
     */
    public TwoPointsCrossover() {
    }

    /**
     * Defines the new politic that is used for the crossover.
     *
     * @param _politic
     */
    @Forced public void setPolitic(Politic _politic) {
        politic = _politic;
    }

    public void twoOffspringCrossover(T mate1, T mate2, IGroup<T> destination, Context context) {
        RNG rng = context.getRng();
        int indSize = mate1.size();
        if (indSize != mate2.size()) {
            throw new IllegalArgumentException("The two mates have different sizes... " + indSize + " vs. " + mate2.size());
        }
        int[] crossPoints = chooseCrossPoints(rng, indSize);
        T offspring1 = crossMates(mate1, mate2, crossPoints, indSize);
        T offspring2 = crossMates(mate2, mate1, crossPoints, indSize);
        destination.add(offspring1);
        destination.add(offspring2);
    }

    public void oneOffspringCrossover(T mate1, T mate2, IGroup<T> destination, Context context) {
        int indSize = mate1.size();
        if (indSize != mate2.size()) {
            throw new IllegalArgumentException("The two mates have different sizes... " + indSize + " vs. " + mate2.size());
        }
        int[] crossPoints = chooseCrossPoints(context.getRng(), indSize);

        T offspring = crossMates(mate1, mate2, crossPoints, indSize);
        destination.add(offspring);
    }

    /* Choose two crossing points. The one corresponding
    to the first element is the first in the individual.*/
    private int[] chooseCrossPoints(RNG rng, int indSize) {
        int[] crossPoints = new int[] {0,0};
        switch (politic) {
            case RANDOM:
                crossPoints[0] = rng.nextInt(indSize - 1) + 1;
                crossPoints[1] = rng.nextInt(indSize - 1) + 1;
                break;
             case EQUILIBRATED:
                crossPoints[0] = rng.nextInt(indSize - 1) + 1;
                crossPoints[1] = (crossPoints[0] + indSize/2) % indSize;
                break;
        }
        if (crossPoints[0] > crossPoints[1]) {
            int c = crossPoints[0];
            crossPoints[0] = crossPoints[1];
            crossPoints[1] = c;
        }
        return crossPoints;
    }

    /* Create an empty individual and cross the mates */
    private T crossMates(T mateA, T mateB, int[] crossPoints, int indSize) {
        T offspring = (T) mateA.emptyCopy();
        offspring.copy(0,crossPoints[0],mateA,0);
        offspring.copy(crossPoints[0],crossPoints[1]-crossPoints[0],mateB,crossPoints[0]);
        offspring.copy(crossPoints[1],indSize-crossPoints[1],mateA,crossPoints[1]);
        return offspring;
    }


}
