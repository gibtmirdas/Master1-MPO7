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

package ch.unige.ng.species.initializers;

import ch.unige.ng.species.Initializer;
import ch.unige.ng.species.Individual;
import ch.unige.ng.species.GeneFactory;
import ch.unige.ng.tools.Forced;

/**
 * An initializer that initializes the size first genes in the genome.
 *
 * @author Fontignie Jacques
 * @version 26 juin 2005
 */
public class BasicInitializer <T> implements Initializer<Individual<T>> {

    private int size;

    public BasicInitializer(int defaultSize) {
        this();
        setSize(defaultSize);
    }

    public BasicInitializer() {
    }

    @Forced public void setSize(int size) {
        this.size = size;
    }

    public void initialize(Individual<T> individual) {
        final GeneFactory<T> geneFac = individual.getGeneFactory();
        for (int i = 0; i < size; i++)
            individual.set(geneFac.newGene(i), i);
    }
}
