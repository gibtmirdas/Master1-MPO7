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


package ch.unige.ng.tools.io.writer;

import ch.unige.ng.species.Individual;
import ch.unige.ng.statistics.Statistic;

import java.util.List;


/**
 * n-genes
 *
 * @author Jacques Fontignie
 * @version 16 dec. 2004
 */
public interface LogWriter<T,S extends Individual> {

    /**
     * Informs the class that it is the first calculate
     * <p/>
     * SubClasses can for example create the file, run a graphical interface...
     * @param statistics
     */
    void init(List<Statistic<T,S>> statistics);

    /**
     * Starts a new population
     * @param step : the calculate th generation
     */
    void start(int step);

    /**
     * Sets the value of the argument, it is the current method
     * @param value
     */
   void setValue(T value);

    /**
     * This method is called to the writer to inform it that the log has been done for the current edition
     */
    void stop();

    /**
     * This method is called to inform a writer that the program is terminated
     */
    void close();
}
