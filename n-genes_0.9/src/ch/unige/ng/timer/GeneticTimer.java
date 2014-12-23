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


package ch.unige.ng.timer;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.GlobalIndividual;
import ch.unige.ng.timer.conditions.Condition;
import ch.unige.ng.tools.Forced;

import java.util.LinkedList;

/**
 * n-genes
 * <p/>
 * This class is used to be called by the algorithm to make some stuff at different rates
 *
 * @author Jacques Fontignie
 * @version 15 d?c. 2004
 */
public class GeneticTimer <T extends GlobalIndividual> {

    private GeneticLogger<T> logger;
    private LinkedList<Condition> conditions;

    public GeneticTimer(Condition [] printConditions, GeneticLogger<T> [] loggers){
        this();
        for (Condition c : printConditions)
            addCondition(c);
        for (GeneticLogger<T> logger : loggers)
            addListener(logger);        
    }

    public GeneticTimer() {
        conditions = new LinkedList<Condition>();
    }

    @Forced public void addListener(GeneticLogger<T> _logger) {
        this.logger = _logger;
    }

    @Forced public void addCondition(Condition condition) {
        conditions.add(condition);
    }


    public void step(Population<T> pop, Selector<T> selector,Context context) {
       boolean canPrint = false;
        for (Condition condition : conditions) {
            if (condition.canFire(pop, context)) {
                canPrint = true;
                break;
            }
        }
        if (canPrint || conditions.size() == 0)
            logger.firePrintEvent(pop,selector,context);
        if (!context.isEvolving())
            logger.finish();
    }
}
