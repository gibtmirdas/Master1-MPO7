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


package ch.unige.ng.species;

/**
 *
 * An intervalGenefactory can return the min and the max values in which genes can be taken
 * @author Jacques Fontignie
 * @version 13 avr. 2005
 * @version Can now give the minimal value at a given position. Some genFactories will just return getMin() or getMax()
 */
public interface IntervalGeneFactory<T> extends GeneFactory<T>{


    /**
     *
     * @return the minimal possible value
     */
    public T getMin();

    /**
     *
     * @return the maximal possible value
     */
    public T getMax();

    /**
     *
     * @param position
     * @return the minimum gene value at posiiton positon
     */
    public T getMin(int position);

    /**
     * This method can return the min value that a gene can have at the givent position. Some functions will just return
     * getMax
     *
     * @param position
     * @return the maximum gene value at position position
     */
    public T getMax(int position);
}
