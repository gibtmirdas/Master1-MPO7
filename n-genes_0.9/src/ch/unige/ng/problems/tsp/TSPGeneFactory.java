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

package ch.unige.ng.problems.tsp;

import ch.unige.ng.species.AbstractPermutableGeneFactory;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.gen.Context;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;

/**
 * @author Fontignie Jacques
 * @version 6 aug. 2005
 */
public class TSPGeneFactory extends AbstractPermutableGeneFactory<Town> {

    private final Comparator<Town> comparator = new Comparator<Town>(){
        public int compare(Town o1, Town o2) {
            int diff = o1.hashCode()-o2.hashCode();
            if (diff != 0 || o1.equals(o2)) return diff;
            double distance = (o1.getX()-o2.getX());
            //Different values for the return to be sure that wehave an unique order
            if (distance > 0) return 1; else if (distance < 0) return -1;
            distance = o1.getY()-o2.getY();
            if (distance > 0) return 2; else if (distance < 0) return -2;
            throw new IllegalStateException("Two cities have the same position");
        }
    };

    public TSPGeneFactory(){}

    public TSPGeneFactory(String fileName, Context context) throws IOException {
        super(context);
        setFileName(fileName);
    }

    public Class getClassGene() {
        return Town.class;
    }

    @Forced
    public void setFileName(String name) throws IOException {
        ArrayList<Town>towns = new ArrayList<Town>();
        BufferedReader reader = new BufferedReader(new FileReader(name));
        do {
            String line = reader.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.startsWith("#")) continue;
            StringTokenizer tokenizer = new StringTokenizer(line);
            if (tokenizer.countTokens() == 0) continue;
            if (tokenizer.countTokens() != 3) {
                throw new IllegalStateException("The towns have to be declared with: <name> <x> <y>");
            }
            String townsName = tokenizer.nextToken();
            String townX = tokenizer.nextToken();
            String townY = tokenizer.nextToken();
            towns.add(new Town(
                    townsName,
                    Double.valueOf(townX),
                    Double.valueOf(townY)
            ));

        } while (true);
        setGenes(towns);
    }

    protected void sort(Town[] temporary) {
        Arrays.sort(temporary,comparator);
    }
}
