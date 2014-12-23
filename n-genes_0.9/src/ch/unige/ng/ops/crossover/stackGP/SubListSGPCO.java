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


package ch.unige.ng.ops.crossover.stackGP;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.mutation.stackGP.BackwardCleaner;
import ch.unige.ng.ops.crossover.SingleOffspringCrossover;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.species.stackGP.operations.Operation;

/**
 * Created by IntelliJ IDEA.
 * User: falcone
 * Date: Sep 17, 2004
 * Time: 2:54:14 PM
 * To change this template use File | Settings | File Templates.
 *
 * TODO BUGGEE si les deux individus sont trop grands
 */
public class SubListSGPCO extends SingleOffspringCrossover<VariableSize<Operation>> {

    private BackwardCleaner cleaner;
    private boolean cleaning;

    public SubListSGPCO(boolean cleaning){
        this.cleaning = cleaning;
    }

    /**
     * Do we have to clean the genome before crossover.
     * @param cleaning
     */
    public void setCleaning(boolean cleaning){
        this.cleaning = cleaning;
    }

    /**
     * Empty constructor for parser compliance
     */
    public SubListSGPCO() {
        cleaner = new BackwardCleaner();
        cleaning = true;
    }

    public void crossover(VariableSize<Operation> mate1,
                          VariableSize<Operation> mate2,
                          IGroup<VariableSize<Operation>> population, Context context) {
        VariableSize<Operation> m = (VariableSize<Operation>) mate1.copy();
        VariableSize<Operation> child = (VariableSize<Operation>) mate2.copy();

        if (cleaning) {
            cleaner.operate(m, context);
            cleaner.operate(child, context);
        }
        /* Tester s'ils sont ridicules */
        if (child.size() < 2 || m.size() < 2) {
            population.add(child);
            return;
        }
        /* Tirer au sort un reference de Depart de m */
        int end = context.getRng().nextInt(m.size() - 1);
        /* remonter a la source */
        int backSteps = (m.get(end)).minStackSize();
        int start = end;
        while (backSteps > 0 && start > 0) {
            start--;
            backSteps -= (m.get(start)).modStackSize();
        }
        /* Extraire la sous liste et l'enlever du m */
        VariableSize<Operation> s = m.getSubIndividual(start, end + 1);
        /* Tirer un reference d'insertion chez f et inserer */
        int insPoint = context.getRng().nextInt(child.size() - 1);
        child.insert(insPoint, s);
        s.selfRelease();
        m.selfRelease();
        /* On retourne f */
        population.add(child);
    }
}
