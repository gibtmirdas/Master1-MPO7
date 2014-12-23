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


package ch.unige.ng.problems.santafe.context;

import ch.unige.ng.tools.Forced;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * @author Jacques Fontignie
 * @version 7 mars 2005
 */
public class FileBoard extends Board {

    private String filename;

    public FileBoard(String filename) {
        this();
        setFilename(filename);
    }

    public FileBoard() {

    }

    @Forced public void setFilename(String filename) {
        this.filename = filename;
        //Create the array
        createArray();

    }


    protected void createArray() {
        numFoods = 0;
        BufferedReader bReader = null;
        FileReader fReader = null;
        try {
            fReader = new FileReader(filename);

            bReader = new BufferedReader(fReader);
            StringTokenizer stk = new StringTokenizer(bReader.readLine());
            setWidth(Integer.parseInt(stk.nextToken()));
            setHeight(Integer.parseInt(stk.nextToken()));
            array = new int[width * height];
            setBeginX(0);
            setBeginY(0);
            setBeginDirection(Direction.EAST);
            //We know create the array
            int y = 0;
            do {
                String line = bReader.readLine();
                if (line == null || y == height) break;
                //We are looking for the food:
                int index = 0;
                while ((index = line.indexOf("#", index)) > 0) {
                    addFood(index++, y);
                    numFoods++;
                }
                index = 0;
                y++;
            } while (true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
