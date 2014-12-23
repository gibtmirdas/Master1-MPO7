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
import ch.unige.ng.populations.StatisticCache;
import ch.unige.ng.species.GlobalIndividual;

import static java.lang.Math.sqrt;

/**
 * n-genes
 * <p/>
 * This functions calculates the standard deviation of a population. It is not the real standard deviation
 * because the normalization is wrong. We divide by N not by (N-1). The goal is to avoid to have number too big. 
 *
 * @author Jacques Fontignie
 * @version 3 janv. 2005
 */
public class STDStatistic<T extends GlobalIndividual> implements Statistic<Double,T> {

    public Double evaluate(Population<T> population) {
        double nums = population.size();
        StatisticCache sCache = population.getStatisticCache();
        double variance = sCache.getSum2()/nums - sqr(sCache.getSum()/nums);
        return variance > 0? sqrt(variance) : 0;
    }

    private double sqr(double v) {
        return v * v;
    }

    public String getName() {
        return "std";
    }
}
