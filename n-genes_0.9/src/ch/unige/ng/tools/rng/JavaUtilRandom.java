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

/**
 * This class is an adapter for the java.util.Random class.
 * @author Jean-Luc Falcone
 * @version Feb 22, 2005 - 1:22:45 PM
 */

import java.util.Random;

public class JavaUtilRandom implements RNG {

    private Random jRand;
    private long seed;


    public JavaUtilRandom() {
        jRand = new Random();
        seed = System.currentTimeMillis();
        jRand.setSeed(seed);
    }

    public void setSeed(long seed) {
        this.seed = seed;
        jRand.setSeed(seed);
    }

    public long getSeed() {
        return seed;
    }

    public boolean nextBoolean() {
        return jRand.nextBoolean();
    }

    public boolean nextBoolean(double trueProbability) {
        double roll = jRand.nextDouble();
        if (roll <= trueProbability) {
            return true;
        } else {
            return false;
        }
    }

    public int nextInt(int upperBound) {
        return jRand.nextInt(upperBound);
    }

    public int nextInt(int lowerBound, int upperBound) {
        return jRand.nextInt(upperBound-lowerBound) + lowerBound;
    }

    public double nextDouble() {
        return jRand.nextDouble();
    }

    public double nextGaussian() {
        return jRand.nextGaussian();
    }

    public byte[] nextBytes(int length) {
        byte[] randBytes = new byte[length];
        jRand.nextBytes(randBytes);
        return randBytes;
    }

    public long nextLong() {
        return jRand.nextLong();
    }
}
