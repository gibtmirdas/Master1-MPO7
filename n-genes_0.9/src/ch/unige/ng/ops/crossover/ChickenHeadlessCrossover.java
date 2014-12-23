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
import ch.unige.ng.ops.Operator;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Population;
import ch.unige.ng.populations.Group;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.gen.Context;

/**
 *
 * Makes a chicken headless crossover. It creates a randomly generated individual and an individual in the population.
 * Then a crossover is done between the 2. 
 *
 * @author Jacques Fontignie
 * @version 2 juin 2005
 */
public class ChickenHeadlessCrossover<T extends Individual> implements Operator<T>{

    private Crossover<T> crossover;


    public ChickenHeadlessCrossover(){}

    public ChickenHeadlessCrossover(Crossover<T> crossover){
        setCrossover(crossover);
    }

    public void setCrossover(Crossover<T> crossover) {
        this.crossover = crossover;
    }

    public void operate(Population<T> population, Selector<T> selector, Context context, int number) {
        for (int i = 0; i < number; i++){
            T mate1 = selector.select(population,context);
            T mate2 = (T) mate1.emptyCopy();
            mate2.randomize();
            T newInd = crossover.crossover(mate1,mate2,context);
            population.add(newInd);
            mate2.selfRelease();
        }
    }

    public void operate(Group<T> source, IGroup<T> destination, Selector<T> selector, Context context) {
        for (int i = 0, length = source.size(); i < length; i++){
            T mate1 = source.get(i);
            T mate2 = (T) mate1.emptyCopy();
            mate2.randomize();
            T newInd = crossover.crossover(mate1,mate2,context);
            destination.add(newInd);
            mate2.selfRelease();
        }
    }
}
