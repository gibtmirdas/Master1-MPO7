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

package ch.unige.ng.fitness.cache;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.species.Evaluable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.CachingObject;

import java.util.WeakHashMap;

/**
 * A cache that contains the the fitness of the objects allready evaluated.
 *
 * @author Fontignie Jacques
 * @version 28 juil. 2005
 */
public class CachedFitness <T> implements Fitness<T>, CachingObject {

    private Fitness<T> fitness;
    private WeakHashMap<TemporaryEvaluable<T>, Double> map;
    private int cacheHit;

    public CachedFitness(Fitness<T> fitness) {
        this();
        setFitness(fitness);
    }

    public CachedFitness() {
        cacheHit = 0;
        map = new WeakHashMap<TemporaryEvaluable<T>, Double>();
    }

    @Forced public void setFitness(Fitness<T> fitness) {
        this.fitness = fitness;
    }

    public double compute(Evaluable<T> evaluable) {
        Double result = map.get(evaluable);
        if (result != null) {
            cacheHit++;
            return map.get(evaluable);
        }
        double fitnessValue = fitness.compute(evaluable);
        map.put(new TemporaryEvaluable<T>(evaluable), fitnessValue);
        return fitnessValue;
    }

    public int getCacheHit() {
        return cacheHit;
    }

    public int getCacheSize() {
        return map.size();
    }

}
