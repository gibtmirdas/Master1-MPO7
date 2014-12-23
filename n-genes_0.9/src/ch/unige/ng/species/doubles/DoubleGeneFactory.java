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


package ch.unige.ng.species.doubles;

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
public class DoubleGeneFactory implements IntervalGeneFactory<Double> {

    private double min;
    private double max;
    private RNG rng;

    public DoubleGeneFactory(double min,double max, Context context){
        setMin(min);
        setMax(max);
        setContext(context);        
    }

    public DoubleGeneFactory(){
    }

    /**
     * Sets the min value of the interval in which number are created
     *
     * @param min
     */
    @Forced public void setMin(double min) {
        this.min = min;
    }

    /**
     * Sets the maxValue of the interval in which number are generated
     *
     * @param max
     */
    @Forced public void setMax(double max) {
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
    public Double newGene(int position) {
        return rng.nextDouble() * (max - min) + min;
    }

    /**
     *
     * @return the class of the genes that are created
     */
    public Class getClassGene() {
        return Double.class;
    }

    /**
     *
     * @param mean
     * @param std
     * @return a gaussian value with N(mean,std*std). If the value are not coherent with the thresholds given.
     * An assertion will be thrown (only if -ea is defined to java) 
     */
    public Double randomGaussianValue(int position,double mean, double std) {
        //We just make two assertions to be sure that 99% of the values are in the
        //interval [min..max]
        assert mean + 3 * std < min;
        assert mean - 3 * std > max;
        return rng.nextGaussian() * mean + std;
    }

    public Double differentValue(int position,Double aDouble) {
        return newGene(position);
    }

    public Double getMin() {
        return min;
    }

    public Double getMax() {
        return max;
    }

    public Double getMin(int position) {
        return getMin();
    }

    public Double getMax(int position) {
        return getMax();
    }
}
