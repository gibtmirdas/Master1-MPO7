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


package ch.unige.ng.tools.rng;

import ch.unige.ng.tools.rng.mt.MersenneTwisterFast;

/**
 * Adapter for Sean Lucas fast Mersenne-Twister implementation.
 *
 * @author Jean-Luc Falcone
 * @version Feb 22, 2005 - 1:38:00 PM
 */
public final class MersenneTwister implements RNG {

    private MersenneTwisterFast mtRand;
    private long seed;

    public MersenneTwister() {
        mtRand = new MersenneTwisterFast();
        seed = System.currentTimeMillis();
        mtRand.setSeed(seed);
    }

    public void setSeed(long seed) {
        this.seed = seed;
        mtRand.setSeed(seed);
    }

    public long getSeed() {
        return seed;
    }

    public boolean nextBoolean() {
        return mtRand.nextBoolean();
    }

    public boolean nextBoolean(double trueProbability) {
        return mtRand.nextBoolean(trueProbability);
    }

    public int nextInt(int upperBound) {
        return mtRand.nextInt(upperBound);
    }

    public int nextInt(int lowerBound, int upperBound) {
        return lowerBound + mtRand.nextInt(upperBound - lowerBound);
    }

    public double nextDouble() {
        return mtRand.nextDouble();
    }

    public double nextGaussian() {
        return mtRand.nextGaussian();
    }

    public byte[] nextBytes(int length) {
        byte[] randBytes = new byte[length];
        for (int i = (length - 1); i >= 0; i--) {
            randBytes[i] = mtRand.nextByte();
        }
        return randBytes;
    }

    public long nextLong() {
        return mtRand.nextLong();
    }
}
