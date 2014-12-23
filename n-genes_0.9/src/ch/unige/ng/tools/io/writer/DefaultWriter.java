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
 * By default This class uses a tab as a separator and it writes a header: The header is defined as the
 * names of the statisctics that are used.
 *
 * @author Jacques Fontignie
 * @version 12 janv. 2005
 * @see Statistic
 */
public class DefaultWriter <T,S extends Individual> extends AbstractWriter<T, S> {
    private String separator = "\t";
    private boolean header;

    public DefaultWriter(boolean console){
        this();
        setConsole(console);
    }

    public DefaultWriter() {
        header = true;
    }

    /**
     * Sets which string separate each statistic
     *
     * @param separator
     */
    public void setSeparator(String separator) {
        //We exchange the caracter backslash with the escape caracter
        this.separator = separator.replace("\\t", "\t").replace("\\n", "\n");
    }

    /**
     * Do we have to show the header?
     *
     * @param header
     */
    public void setHeader(boolean header) {
        this.header = header;
    }

    public void init(List<Statistic<T, S>> statistics) {
        if (header) {
            printField("generation");

            for (Statistic statistic : statistics)
                printField(statistic.getName());
            println();
        }
    }

    public void start(int step) {
        printField(step);
    }

    public void setValue(T value) {
        printField(value);
    }

    public void stop() {
        println();
    }

    protected void printField(Object field) {
        super.printField(field + separator);
    }
}
