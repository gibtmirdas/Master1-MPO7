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


package ch.unige.ng.timer.conditions;

import ch.unige.ng.gen.Context;
import ch.unige.ng.populations.Population;
import ch.unige.ng.tools.Forced;

import java.util.LinkedList;
import java.util.List;

/**
 * n-genes
 *
 * This class is used to make a logical and between two conditions
 *
 * @author Jacques Fontignie
 * @version 2 mars 2005
 */
public class AndCondition implements Condition {

    private List<Condition> conditions;

    public AndCondition() {
        conditions = new LinkedList<Condition>();
    }

    @Forced public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public boolean canFire(Population population, Context context) {        
        for (Condition condition : conditions)
            if (!condition.canFire(population, context))
                return false;
        return true;
    }
}
