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


package ch.unige.ng.problems.nklandscape;

import ch.unige.ng.fitness.Fitness;
import ch.unige.ng.species.Evaluable;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static ch.unige.ng.species.booleans.BooleanGeneFactory.FALSE;

/**
 * @author Jacques Fontignie
 * @version 3 mai 2005
 */
public class NkLandscapeReader implements Fitness<Boolean> {


    private int neighbours [];
    private boolean lut [];
    private int k;
    private int indSize;

    public void setFilename(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(new FileInputStream(filename));        
        k = stream.readInt();
        neighbours = (int[]) stream.readObject();
        lut = (boolean[]) stream.readObject();
        stream.close();
    }

    public double compute(Evaluable<Boolean> evaluable) {
        double sum = 0;

        indSize = evaluable.size();
        for (int i = 0; i < indSize; i++){
            Boolean val = estimate(evaluable,i);
            if (val == FALSE) sum++;
        }
        return sum;
    }

    private Boolean estimate(Evaluable<Boolean> evaluable, int current) {
        int index = (evaluable.get(current).booleanValue())? 1: 0;
        for (int i = 0; i < k-1; i++){
            int currentIndex = (neighbours[current*(k-1)+i]+i) % indSize;
            index = index*2 + ((evaluable.get(currentIndex).booleanValue())? 1:0);
        }
        return lut[index];
    }
}
