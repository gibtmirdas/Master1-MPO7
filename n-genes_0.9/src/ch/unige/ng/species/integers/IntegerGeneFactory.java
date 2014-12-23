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


package ch.unige.ng.species.integers;

import ch.unige.ng.gen.Context;
import ch.unige.ng.species.IntervalGeneFactory;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.rng.RNG;

/**
 * This GeneFactory is used for doubles. It possess two thresholds.
 *
 * @author Jacques Fontignie
 * @version 10 mars 2005
 */
public class IntegerGeneFactory implements IntervalGeneFactory<Integer> {

    private int min;
    private int max;
    private RNG rng;

    public IntegerGeneFactory(){
    }

    /**
     *
     * @param min the min value of a created gene
     * @param max the maxvalue of a created gene
     * @param context the context of the program
     */
    public IntegerGeneFactory(int min,int max,Context context){
        setMin(min);
        setMax(max);
        setContext(context);        
    }

    /**
     * Sets the min value of the interval in which number are created
     *
     * @param min
     */
    @Forced public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the maxValue of the interval in which number are generated
     *
     * @param max
     */
    @Forced public void setMax(int max) {
        this.max = max;
    }

    /**
     * Sets the context
     * @param _ctx
     */
    public void setContext(Context _ctx) {
        this.rng = _ctx.getRng();
    }

    /**
     * @return a newGene
     */
    public Integer newGene(int position) {
        return rng.nextInt(min,max);
    }

    /**
     *
     * @return the class of the genes that are created
     */
    public Class getClassGene() {
        return Integer.class;
    }

    /**
     *
     * @param mean
     * @param std
     * @return a gaussian value with N(mean,std*std). If the value are not coherent with the thresholds given.
     * An assertion will be thrown (only if -ea is defined to java) 
     */
    public Integer randomGaussianValue(int position,double mean, double std) {
        //We just make two assertions to be sure that 99% of the values are in the
        //interval [min..max]
        assert mean + 3 * std < min;
        assert mean - 3 * std > max;
        return (int) (rng.nextGaussian() * mean + std);
    }

    public Integer differentValue(int position,Integer anInt) {
        return newGene(position);
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getMin(int position) {
        //Maybe it is faster to write directly return min; but we can be sure this method will be inlined
        return getMin();
    }

    public Integer getMax(int position) {
        return getMax();
    }

}
