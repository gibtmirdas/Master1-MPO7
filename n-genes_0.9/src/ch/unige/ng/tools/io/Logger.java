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


package ch.unige.ng.tools.io;

import ch.unige.ng.gen.Context;
import ch.unige.ng.ops.select.Selector;
import ch.unige.ng.populations.IGroup;
import ch.unige.ng.populations.Population;
import ch.unige.ng.species.Individual;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.io.writer.LogWriter;
import ch.unige.ng.statistics.Statistic;
import ch.unige.ng.timer.GeneticLogger;

import java.util.LinkedList;

/**
 * n-genes
 *
 * @author Jacques Fontignie
 * @version 15 dec. 2004
 */
public class Logger <T extends Individual> implements GeneticLogger<T> {


    private boolean first;
    private LinkedList<Statistic<Object,T>> statistics;
    private LinkedList<LogWriter<Object,T>> writers;

    private IGroup population;

    public Logger(LogWriter<Object,T> writers [], Statistic<Object,T>[] statistics) {
        this();
        for (LogWriter<Object,T> writer : writers)
            addWriter(writer);
        for (Statistic<Object,T> statistic : statistics)
            addStatistic(statistic);

    }

    public Logger() {
        first = true;
        statistics = new LinkedList<Statistic<Object,T>>();
        writers = new LinkedList<LogWriter<Object,T>>();
    }
    
    @Forced
            public void addStatistic(Statistic<Object,T> statistic) {
        statistics.add(statistic);
    }

    @Forced
            public void addWriter(LogWriter<Object,T> writer) {
        writers.add(writer);
    }

    /**
     * This method is called by a timer
     *
     * @param population
     */
    public void firePrintEvent(Population<T> population, Selector<T> selector, Context context) {
        if (this.population == null || population != this.population) {
            this.population = population;
            for (Statistic<Object,T> stat : statistics)
                population.getStatisticCache().addStatistic(stat);
        }
        if (first) {
            for (LogWriter<Object,T> writer : writers)
                writer.init(statistics);
            first = false;
        }
        for (LogWriter writer : writers)
            writer.start(population.getStep());
        for (Statistic<Object,T> statistic : statistics) {
            Object value = population.getStatisticCache().getStatistic(statistic.getName());
            for (LogWriter<Object,T> writer : writers)
                writer.setValue(value);
        }
        for (LogWriter writer : writers)
            writer.stop();
    }

    public void fireWorkEvent(IGroup<T> population, Selector<T> selector, Context context) {
    }

    public void finish() {
        for (LogWriter writer : writers)
            writer.close();
    }
}
