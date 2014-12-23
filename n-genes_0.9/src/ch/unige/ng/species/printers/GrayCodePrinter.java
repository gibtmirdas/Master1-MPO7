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


package ch.unige.ng.species.printers;

import ch.unige.ng.species.Individual;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.utils.GrayCode;

/**
 * Prints the individual coded with boolean into an individual of number
 *
 * @author Jacques Fontignie
 * @version 15 avr. 2005
 */
public class GrayCodePrinter<T extends Individual<Boolean>> implements Printer<T> {

    private int numBits;
    private double min;
    private double max;


    public GrayCodePrinter(){
    }

    @Forced public void setMin(double min) {
        this.min = min;
    }

    @Forced  public void setMax(double max) {
        this.max = max;
    }

    /**
     * Derfines the number of bytes per number
     * @param sizeNumber
     */
    @Forced public void setNumBits(int sizeNumber) {
        this.numBits = sizeNumber;
    }

    public String toString(T ind) {
        //Creating the buffer        
        final int maxValue = 1<<numBits;
        StringBuffer buffer = new StringBuffer("[ ");
        for (int i = 0,length = ind.size(); i< length; i+= numBits){
            int value =   GrayCode.grayToInt(ind,i,numBits);
            double result = (maxValue-value)*(max-min)/maxValue + min;
            buffer.append(result);
            buffer.append(" ");
        }
        buffer.append("]");
        return buffer.toString();
    }
}
