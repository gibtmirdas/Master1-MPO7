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


package ch.unige.ng.species.bytes;

import ch.unige.ng.gen.Context;
import ch.unige.ng.species.IntervalGeneFactory;
import ch.unige.ng.tools.rng.RNG;

/**
 * @author Jacques Fontignie
 * @version 15 mars 2005
 */
public class ByteGeneFactory implements IntervalGeneFactory<Byte> {
    private static final int LENGTH = 10;
    private RNG rng;
    private int index;
    private byte array [];

    public ByteGeneFactory() {
        array = new byte[LENGTH];
        index = -1;
    }

    public void setContext(Context _ctx) {
        rng = _ctx.getRng();
    }

    public Byte newGene(int position) {
        if (index < 0) {
            index = LENGTH - 1;
            array = rng.nextBytes(LENGTH);
        }
        return array[index--];
    }

    public Class getClassGene() {
        return Byte.class;
    }

    public Byte randomGaussianValue(int position,double mean, double std) {
        throw new RuntimeException("Cet appel de methode n'a pas vraiment de sens...");
    }

    public Byte differentValue(int position, Byte aByte) {
        byte value;
        do {
            value = newGene(position);
        } while (value == aByte);
        return value;
    }

    public Byte getMin() {
        return Byte.MIN_VALUE;
    }

    public Byte getMax() {
        return Byte.MAX_VALUE;
    }

    public Byte getMin(int position) {
        return getMin();
    }

    public Byte getMax(int position) {
        return getMax();
    }
}
