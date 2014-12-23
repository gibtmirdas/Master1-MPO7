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


package ch.unige.ng.species.booleans;

import ch.unige.ng.gen.Context;
import ch.unige.ng.species.GeneFactory;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;

/**
 * It is useless to transform this class into a poolable because object are too small.
 *
 * @author Jacques Fontignie
 * @version 14 janv. 2005
 */
public class BooleanGeneFactory implements GeneFactory<Boolean> {
    public static final Boolean TRUE = Boolean.TRUE ;
    public static final Boolean FALSE = Boolean.FALSE;

    /**
     * The context of the program
     */
    private RNG rng;

    public BooleanGeneFactory(Context context){
        this();
        setContext(context);
    }

    /**
     * The constructor: it creates a new gene factory
     */
    public BooleanGeneFactory() {
    }

    @Forced public void setContext(Context _ctx) {
        rng = _ctx.getRng();
    }

    public Boolean newGene(int position) {
        //It is just a stupid way to avoid creating new Boolean each time...
        return (rng.nextBoolean()) ? TRUE : FALSE;
    }

    public Class getClassGene() {
        return Boolean.class;
    }

    public Boolean randomGaussianValue(int posiiton,double mean, double std) {
        throw new RuntimeException("Cet appel de methode n'a pas vraiment de sens...");
    }

    public Boolean differentValue(int posiiton,Boolean booleanGene) {
        return (booleanGene == TRUE)? FALSE : TRUE;
    }

}
