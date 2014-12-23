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


package ch.unige.ng.gen;

import ch.unige.ng.halt.HaltFunction;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;

import java.util.List;

/**
 * GeneticAlgorithm is the generation handler. This class decides what will do what.
 */
public class GeneticAlgorithm <T extends GlobalIndividual> {

    /**
     * The population
     */
    private Population<T> pop;

    /**
     * The context
     */
    private Context ctx;

    /**
     * The functions that are tested at the end
     */
    private HaltFunction[] functions;

    /**
     * @param population The population on which to work
     * @param _ctx             the context of the population
     * @param functionList     the list of haltFunctions
     */
    public GeneticAlgorithm(Population<T> population, Context _ctx,HaltFunction... functionList) {
        ctx = _ctx;
        functions = new HaltFunction[functionList.length];
        int i = 0;
        for (HaltFunction function : functionList)
            functions[i++] = function;
        pop = population;
    }

    /**
     * @param population The population on which to work
     * @param _ctx             the context of the population
     * @param functionList     the list of haltFunctions
     */
    public GeneticAlgorithm(Population<T> population, Context _ctx, List<HaltFunction> functionList) {
        ctx = _ctx;
        functions = new HaltFunction[functionList.size()];
        int i = 0;
        for (HaltFunction function : functionList)
            functions[i++] = function;
        pop = population;
    }

    /**
     * Sets the population that will be used
     * @param _pop
     */
    public void setPopulation(Population<T> _pop) {
        pop = _pop;
    }

    /**
     * Initializes the population with the prototype
     * @param size
     */
    public void initializePopulation(int size, T prototype) {
        pop.initialize(size,prototype);
    }

    /**
     *
     * @return the population used
     */
    public Population<T> getPopulation() {
        return pop;
    }

    /**
     * @param population
     * @param ctx
     * @return if the algorithm is terminated, it will just check the different haltFunction
     * @see HaltFunction
     */
    private boolean isTerminated(Population<T> population, Context ctx) {
        for (HaltFunction function : functions)
            if (function.isTerminated(population, ctx)) return true;
        return false;
    }

    /**
     * This function iterates while no haltFunction is not fired
     *
     * @param gen
     * @return the number of calculate that where generated
     */
    public int iterate(Generation<T> gen) {
        if (isTerminated(pop, ctx)) {
            return 0;
        }
        do {
            gen.calculate(pop, ctx);
            pop.nextStep();
            gen.refresh(pop,ctx);
        } while (!isTerminated(pop, ctx));
        return pop.getStep();
    }
}
