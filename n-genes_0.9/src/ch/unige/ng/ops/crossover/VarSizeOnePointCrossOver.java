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
import ch.unige.ng.species.VariableSize;

/**
 * Created by IntelliJ IDEA.
 * User: falcone
 * Date: Sep 13, 2004
 * Time: 12:02:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class VarSizeOnePointCrossOver <S,T extends VariableSize<S>> extends DoubleOffspringCrossOver<T> {

    public enum Politic {
        RANDOM
    };

    private VarSizeOnePointCrossOver.Politic politic;


    public VarSizeOnePointCrossOver(VarSizeOnePointCrossOver.Politic _politic) {
        politic = _politic;
    }

    /**
     * Empty Constructor for parser compliance
     */
    public VarSizeOnePointCrossOver() {
        politic = Politic.RANDOM;
    }

    /**
     * Sets the politic to be used
     *
     * @param politic
     */
    public void setPolitic(Politic politic) {
        this.politic = politic;
    }

    private int randomCrossPoint(Individual ind, Context context) {
        if (ind.size() < 2) {
            return 0;
        } else {
            return context.getRng().nextInt(ind.size());
        }
    }


    public void twoOffspringCrossover(T mate1, T mate2, IGroup<T> destination, Context context) {
        int crossPoint1;
        int crossPoint2;        
        switch (politic) {
            case RANDOM:
            default:
                crossPoint1 = randomCrossPoint(mate1, context);
                crossPoint2 = randomCrossPoint(mate2, context);
                break;
        }
        T offspring1 = (T) mate1.emptyCopy();
        T offspring2 = (T) mate1.emptyCopy();

        offspring1.copy(0,crossPoint1,mate1,0);
        offspring1.copy(crossPoint2,mate2.size()-crossPoint2,mate2,crossPoint1);        
        offspring2.copy(0,crossPoint2,mate2,0);
        offspring2.copy(crossPoint1,mate1.size()-crossPoint1,mate1,crossPoint2);
        destination.add(offspring1);
        destination.add(offspring2);
    }

    public void oneOffspringCrossover(T mate1, T mate2,  IGroup<T> destination, Context context) {
        int crossPoint1;
        int crossPoint2;
        switch (politic) {
            case RANDOM:
            default:
                crossPoint1 = randomCrossPoint(mate1, context);
                crossPoint2 = randomCrossPoint(mate2, context);
                break;
        }
        T offspring = (T) mate1.emptyCopy();
        offspring.copy(0,crossPoint1,mate1,0);
        offspring.copy(crossPoint2,mate2.size()-crossPoint2,mate2,crossPoint1);
        destination.add(offspring);
    }

}
