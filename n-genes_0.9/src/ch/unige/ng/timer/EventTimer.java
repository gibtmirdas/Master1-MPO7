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
import java.util.List;

/**
 * @author Jacques Fontignie
 * @version 23 mars 2005
 */
public class EventTimer<T extends GlobalIndividual> {


    private GeneticListener<T> listener;
    private LinkedList<Condition> workConditions;

    /**
     *
     * @param listener The listener that has to be fired
     * @param conditions the conditions that must be respected to fire the event
     */
    public EventTimer(GeneticListener<T> listener,List<Condition> conditions){
        this();
        setListener(listener);
        workConditions.addAll(conditions);
    }

    public EventTimer() {
        workConditions = new LinkedList<Condition>();
    }

    @Forced public void setListener(GeneticListener<T> _listener) {
        this.listener = _listener;
    }


    public void addWorkCondition(Condition condition) {
        workConditions.add(condition);
    }


    public void step(Population<T> pop, Selector<T> selector,Context context) {
        boolean canWork = false;
        for (Condition condition : workConditions)
            if (condition.canFire(pop, context)) {
                canWork = true;
                break;
            }
        if (canWork || workConditions.size() == 0)
            listener.fireEvent(pop,selector,context);
    }

}
