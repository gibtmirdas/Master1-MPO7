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

import ch.unige.ng.tools.Flushable;
import ch.unige.ng.tools.Forced;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;


/**
 * n-genes
 * <p/>
 * This class defines a file printer. It is a class that writes in a class, it can check if the object allready exists,
 * it can overwrite or not the file. It is possible to compress the created files.
 *
 * @author Jacques Fontignie
 * @version 16 febr. 2005
 */
public class FilePrinter extends OutputStream implements Flushable {

    private boolean compressed;
    private String filename;
    private boolean clobber;
    private OutputStream stream;

    /**
     *
     * @param directory The directory in which write the file
     * @param filename The filename
     * @param clobber Do we have to clobber the file
     * @param compressed Do we have to compress the file
     * @throws IOException
     */
    public FilePrinter(String directory, String filename, boolean clobber, boolean compressed) throws IOException {
        this(directory+"/" + filename,clobber,compressed);
    }

    /**
     *
     * @param filename The filename
     * @param clobber Does the file have to be deleted
     * @param compressed do we have to compress the file with gzip
     * @throws IOException
     */
    public FilePrinter(String filename, boolean clobber, boolean compressed) throws IOException {
        this();
        this.filename = filename;
        this.clobber = clobber;
        this.compressed = compressed;
        objectCreated();
    }

    public FilePrinter() {
        super();
        clobber = false;
        compressed = false;
    }

    /**
     * Sets the filename in which write the file
     * @param filename
     */
    @Forced public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * If this is set to TRUE, the file will be compressed with gzip and the filename will be appended
     * with the .gz extension
     * @param compressed
     */
    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    /**
     * If this is set to True the file if it exists will be deleted else an extension will be thrown
     * @param clobber
     */
    public void setClobber(boolean clobber) {
        this.clobber = clobber;
    }

    public void objectCreated() throws IOException {
        //If it is compressed append the gzip extension
        if (compressed && !filename.endsWith(".gz")){
            filename = filename + ".gz";
        }
        File file = new File(filename);
        if (!clobber && file.exists())
            throw new IllegalStateException("The file: " + file.getName() + " allready exists and the clobber variable is defined" +
                    " to false");

        //We have to create the path if it does not exist
        if (compressed) {
            //Create an outputSteam using a gzip algorithm
            stream = new GZIPOutputStream(new FileOutputStream(file));
        } else
            stream = new FileOutputStream(file);
    }

    public void write(int b) throws IOException {
        stream.write(b);
    }

    public void close() throws IOException {
        super.close();
        stream.close();
    }
}
