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
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.io.FilePrinter;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.LinkedList;

/**
 * n-genes
 *
 * @author Jacques Fontignie
 * @version 17 dec. 2004
 */
public abstract class AbstractWriter <T,S extends Individual> implements LogWriter<T,S> {

    protected LinkedList<PrintStream> list;
    //private String filename;
    private PrintStream console;

    public AbstractWriter() {
        list = new LinkedList<PrintStream>();
    }

    /**
     * This method is called if this writer had to log on the console
     *
     * @param console
     */
    @Forced
            public void setConsole(boolean console) {
        if (console) {
            list.add(System.out);
            this.console = System.out;
        }
    }

    /**
     * Adds a filepprinter to the writer
     * @param printer
     * @throws FileNotFoundException
     */
    public void addFilePrinter(FilePrinter printer) throws FileNotFoundException {
        list.add(new PrintStream(printer));        
    }

    /**
     * Print a endline caracter
     */
    protected void println() {
        for (PrintStream stream : list)
            stream.println();
    }

    /**
     * Prints the field field
     * @param field
     */
    protected void printField(Object field) {
        for (PrintStream stream : list)
            stream.print(field);
    }

    public void close() {
        for (PrintStream stream : list)
            if (stream != console)
                stream.close();
    }
}
