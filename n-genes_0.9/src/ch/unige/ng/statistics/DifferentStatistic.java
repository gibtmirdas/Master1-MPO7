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

/**
 * Shows the percentage of different individual in the population
 * <p/>
 * If we have
 * 1 1 1 3 3 2: the result will be 0.5
 *
 * @author Jacques Fontignie
 * @version 17 mars 2005
 */
public class DifferentStatistic<T extends GlobalIndividual> implements Statistic<Double,T> {
    private boolean unique [];

    public Double evaluate(Population<T> population) {
        if (unique == null || unique.length < population.size()) {
            unique = new boolean[population.size()];
        }
        int sum = 0;
        for (int i = 0; i < unique.length; i++)
            unique[i] = true;
        for (int i = 0; i < population.size(); i++) {
            if (unique[i]) {
                sum++;
                for (int j = i + 1; j < population.size(); j++) {
                    if (population.get(i).equals(population.get(j)))
                        unique[j] = false;
                }
            }
        }
        return sum * 100 / (double) population.size();
    }

    public String getName() {
        return "% different";
    }
}
