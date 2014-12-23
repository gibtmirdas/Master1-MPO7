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
 * This interface represents the basic methods of every Random Number Generator. It mimics the methods
 * of the java.util.Random class.
 * @author Jean-Luc Falcone
 * @version Feb 22, 2005 - 12:56:59 PM
 */
public interface RNG {

    /**
     * Set the seed. If not set, the system ms are used instead.
     * @param seed
     */
    public void setSeed(long seed);

    /**
     * Return the RNG seed.
     * @return the seed
     */
    public long getSeed();

    /**
     * Return a random uniformly distributed boolean
     * @return random boolean
     */
    public boolean nextBoolean();

    /**
     * Return a random boolean, with an arbitrary distribution.
     * For performance reasons this method do not check the consistancy
     * of the provided probability.
     * @param trueProbability The probability of true.
     * @return  random boolean
     */
    public boolean nextBoolean(double trueProbability);


    /**
     * Return an int between 0 and (upperBound-1). This method is equals to
     * nextInt(0, upperBound) (see below).
     * @param upperBound the (excluded) upper bound
     * @return random int
     */
    public int nextInt(int upperBound);

    /**
     * Return an int between lowerbound and (upperBound-1)
     * @param lowerBound the lower bound
     * @param upperBound the (excluded) upper bound
     * @return  random int
     */
    public int nextInt(int lowerBound, int upperBound);

    /**
     * Return a double between 0 and 1.
     * @return a random double
     */
    public double nextDouble();

    /**
     * Return a double based on a gaussian distribution with mean 0 and sd 1.
     * @return a gaussian random double.
     */
    public double nextGaussian();

    /**
     * Return a number of random bytes.
     * @param length  the required number of bytes.
     * @return an array of bytes
     */
    public byte[] nextBytes(int length);

    /**
     * Return a random long.
     * @return a random long
     */
    public long nextLong();

}
