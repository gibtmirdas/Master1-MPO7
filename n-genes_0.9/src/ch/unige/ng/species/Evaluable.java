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
 * @author Jacques Fontignie
 * @version 21 mars 2005
 */
public interface Evaluable <T> extends Iterable<T>  {


    /**
     * Return a reference of the Gene at the <i>i</i> position. You DO NOT HAVE to change the value of the gene.
     *
     * @param i
     * @return the reference of the gene at the <i> i </i> position
     */
    T get(int i);

    /**
     * Give the number of Gene in this Individual
     *
     * @return the size of this Individual.
     */
    int size();

    /**
     *
     * @return the HashCode of the genome
     */
    @Override int hashCode();

}
