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


package ch.unige.ng.tools.formulas;

import java.io.*;
import java.nio.channels.FileLock;
import java.net.URLClassLoader;
import java.net.URL;
import java.lang.*;

/**
 * Compile a class with a given code and return the object created. The object has to be an empty constructor
 *
 * @author Jacques Fontignie
 * @version 6 mai 2005
 */
public class Compiler {

    private static final String TEMP_PATH = System.getProperty("java.io.tmpdir");
    /**
     * The pattern defining the code name
     */
    public static String CODE_NAME = "#NAME#";

    /**
     * The default filename in which the code will be placed
     */
    private static final String FILE_NAME = "Temp";

    private static final String SEPARATOR = System.getProperty("file.separator");

    /**
     * Find a temporary file in which the code can be placed
     *
     * @return
     */
    private static String findTemporaryFile() {
        String className;
        String fileName;
        File file = null;
        while (true) {
            //Randomly choose a file to avoid two file to be created at same time
            className = FILE_NAME + (int) (Math.random() * 1000);
            fileName = TEMP_PATH + SEPARATOR + className + ".java";
            file = new File(fileName);
            //If the file does not exists we create a new file
            if (!file.exists()) break;
        }
        return className;
    }

    /**
     * Compile the code as a string and return the new created object. We do not know in which file
     * the class will be compiled, then you have to define the name with the pattern #NAME#. An example <br/>
     *
     * <pre>
      import //all the stuff
      public class #NAME# {
      public #NAME#() {}
            //Some code
        }
     * </pre>
     *
     *
     * @param code the code like explained
     * @return A new object that is described by the source code
     * @throws Exception if there is a problem writing the code, reading the file, compiling,...
     * @see Compiler#CODE_NAME
     */
    public static <T> T compile(String code) throws Exception {

        //find a temporary file in which we want to put the code
        String className = findTemporaryFile();
        String fileName = TEMP_PATH + SEPARATOR +className + ".java";

        File file = new File(fileName);
        //We inform that we want to delete this file at the end of its operation.
        file.deleteOnExit();
        //Create a fileOutputStream
        FileOutputStream fileStream = new FileOutputStream(file);
        //And lock it
        FileLock lock = fileStream.getChannel().lock();

        code = code.replaceAll(CODE_NAME, className);

        //Create the printWriter to write easily in
        PrintStream writer = new PrintStream(fileStream);
        //Write the code
        writer.print(code);

        compileFile(fileName);

        String path = file.getCanonicalFile().getParent();
        T newObject = (T) createObject(path, className);

        //we can close the writer and release the lock
        lock.release();
        writer.close();

        //Delete the class file, because it is no more used
        new File(TEMP_PATH + SEPARATOR + className + ".class").delete();

        return newObject;
    }

    /**
     * Compile the current fileName
     * @param fileName
     * @throws Exception
     */
    private static void compileFile(String fileName) throws Exception {
        //Getting the same class path to be sure to have the namespace
        String classPath = System.getProperty("java.class.path");
        //Running the command
        String command = "javac -classpath " + classPath + " " + fileName;
        Process process = Runtime.getRuntime().exec(command);
        //Print the stream wait for it to be printed. Be sure that this function does not loop
        //infinitly
        printStream(process.getErrorStream());
        try {
            //Wait for the process to be terminated
            int term = process.waitFor();
            if (term != 0)
                throw new Exception("Error in the code");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the object
     *
     * @param path      the directory in which is the file
     * @param className the class
     * @return the object created
     */
    private static Object createObject(String path, String className) throws Exception {
        //Get the current path
        Object o = URLClassLoader.newInstance(new URL[]{
            new URL("file:////" + path + SEPARATOR)
        }).loadClass(className).newInstance();
        return o;
    }

    /**
     * Prints The stream on the standard error
     *
     * @param stream the stream to read until there is no more in it
     * @throws IOException
     */
    private static void printStream(InputStream stream) throws IOException {
        int x;
        while (((x = stream.read())) != -1) {
            System.err.print((char) x);
        }
    }

    public static void main(String[] args) throws Exception {
        Compiler.compile("public class #NAME# {}");
    }
}

