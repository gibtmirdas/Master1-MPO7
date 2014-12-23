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


package ch.unige.ng.bin;

import ch.unige.ng.halt.HaltFunction;
import ch.unige.ng.populations.Population;
import ch.unige.ng.gen.Context;

import java.util.concurrent.Semaphore;
import java.io.IOException;

/**
 * This haltfunction informs if the current generation is terminated, this will be the case if
 * a shutdown hook has been called. The goal is to avoid losing all data, with that class, we can wait
 * for the generation to terminate.
 * <p/>
 * This class should not be used directly as a Haltfunction, because it is totally connected with the algorithm
 * It is why it is a protected class. If you want to use it, you should set the flag safeShutting in Genetic
 *
 * @author Jacques Fontignie
 * @version 3 juin 2005
 * @see Genetic
 */
class ShuttingFunction implements HaltFunction {
    /**
     * Boolean informing if the evolution is terminated
     */
    private boolean terminated;
    private boolean shuttingDown;
    private Thread thread;

    /**
     * A semaphore used to detect that a shutdown is caught and that the algorithm has to close
     */
    private Semaphore semaphore;

    protected ShuttingFunction() {
        terminated = false;
        semaphore = new Semaphore(0);
        Runnable runnable = new Runnable() {
            public void run() {
                shuttingDown = true;
                if (!terminated)
                    System.out.println("\n\n---\nTermination caught... Current Generation terminating, " +
                            "press enter if you do not want to go to the end.\n---\n");
                Thread keyboard = new Thread(new KeyBoardReader());
                keyboard.start();
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread = new Thread(runnable);
        Runtime.getRuntime().addShutdownHook(thread);
        shuttingDown = false;
    }

    /**
     * Terminate the function: The shutdown hook is released.
     */
    void terminate() {
        if (!shuttingDown)
            Runtime.getRuntime().removeShutdownHook(thread);
    }

    public boolean isTerminated(Population pop, Context context) {
        return shuttingDown;
    }

    /**
     * Inform that the genetic algorithm is terminated.
     */
    void informTerminated() {
        terminated = true;
        semaphore.release();
    }

    /**
     * This class will wait until the <enter> key is pressed.
     */
    private class KeyBoardReader implements Runnable {

        public void run() {
            while (true) {
                try {
                    //Read the code
                    int code = System.in.read();
                    //If the enter code has been pressed then we wilol just break
                    if (code == '\n')
                        break;
                } catch (IOException e) {
                    //We do not matter if there is an exception
                }
            }
            //free the semaphore, then the programm can quit.
            semaphore.release();
        }
    }
}