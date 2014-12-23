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


package ch.unige.ng.fitness;

import ch.unige.ng.species.Evaluable;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.utils.GrayCode;

import java.util.Iterator;

/**
 * This class is used to calculate a fitness. You have to give a fitness to this class and it will call the fitness with a
 * created individual identical to the current but into double using the grayCode .
 *
 * @author Fontignie Jacques
 * @version 2 mai 2005
 */
public class GrayCodeFitness implements Fitness<Boolean>{

    private Fitness<Double> fitness;
    private int numBits;
    private int maxValue;
    private double min;
    private double max;
    private PseudoEvaluable pseudoEvaluable;

    public GrayCodeFitness() {
        pseudoEvaluable = new PseudoEvaluable();
    }

    @Forced public void setFitness(Fitness<Double> fitness){
        this.fitness = fitness;
    }


    @Forced public void setNumBits(int numBits) {
        this.numBits = numBits;
        maxValue = 1<<numBits;
    }

    @Forced public void setMin(double min) {
        this.min = min;
    }

    @Forced public void setMax(double max) {
        this.max = max;
    }


    public double compute(Evaluable<Boolean> evaluable) {
        pseudoEvaluable.setEvaluable(evaluable);
        return fitness.compute(pseudoEvaluable);
    }

    private class PseudoEvaluable implements Evaluable<Double>{
        private Evaluable<Boolean> evaluable;
        private int size;

        public void setEvaluable(Evaluable<Boolean> evaluable){
            this.evaluable = evaluable;
            size = evaluable.size()/numBits;
        }

        public Double get(int i) {
            int value = GrayCode.grayToInt(evaluable,i*numBits,numBits);
            return (maxValue-value)*(max-min)/(maxValue) + min;
        }

        public int size() {
            return size;
        }

        public int hashCode() {
            return evaluable.hashCode();
        }

        public Iterator<Double> iterator() {
            //TODO IMPLEMENT
            throw new RuntimeException("Not implemented yet");
        }
    }
}
