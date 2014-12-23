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

import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;
import ch.unige.ng.tools.rng.JavaUtilRandom;

/**
 * This class contains some data:
 * <ul>
 * <li> The random number genrator</li>
 * <li> The state of the algorithm</li>
 * <li> It will contain the Multi-threading informations.</li>
 * </ul>
 */
public class Context {

    /**
     * Is the algorithm still working
     */
    private boolean evolving;

    /**
     * The random number generator
     */
    private RNG rng;


    /**
     *
     * @param rng the random number generator
     */
    public Context(RNG rng){
        this();
        setRNG(rng);
    }

    /**
     * This method creates a new context
     */
    public Context() {
        evolving = true;
        rng = new JavaUtilRandom();
    }

    /**
     * Sets a new Random Generator
     * @param rng
     */
    @Forced public void setRNG(RNG rng){
        this.rng = rng;
    }

    /**
     * @return The random generator
     */
    public RNG getRng() {
        return rng;
    }


    /**
     * @return if the population is still evolving: If the algorithm is terminated or not.
     */
    public boolean isEvolving() {
        return evolving;
    }

    /**
     * sets the evolving parameter
     *
     * @param _evolving
     */
    public void setEvolving(boolean _evolving) {
        evolving = _evolving;
    }

}
