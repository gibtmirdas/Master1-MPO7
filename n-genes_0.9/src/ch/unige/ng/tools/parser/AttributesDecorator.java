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


package ch.unige.ng.tools.parser;

import org.xml.sax.Attributes;

import java.util.StringTokenizer;

/**
 * @author Fontignie Jacques
 * @version 7 mai 2005
 */
public class AttributesDecorator implements Attributes {
    private Attributes attributes;
    private ConstantHandler constants;

    public AttributesDecorator(Attributes attributes, ConstantHandler constants) {
        this.attributes = attributes;
        this.constants = constants;
    }

    public int getLength() {
        return attributes.getLength();
    }

    public String getURI(int index) {
        throw new IllegalStateException("Not implemented yet");
        //return attributes.getURI(index);
    }

    public String getLocalName(int index) {
        throw new IllegalStateException("Not implemented yet");
        //return attributes.getLocalName(index);
    }

    public String getQName(int index) {
        return attributes.getQName(index);
    }

    public String getType(int index) {
        throw new IllegalStateException("Not implemented yet");
        //return attributes.getType(index);
    }

    public String getValue(int index) {
        String value = attributes.getValue(index);
        if (value == null || !value.contains("#")) {
            return value;
        }
        return transform(value);
    }

    public int getIndex(String uri, String localName) {
        throw new IllegalStateException("Not implemented yet");
        //return attributes.getIndex(uri,localName);
    }

    public int getIndex(String qName) {
        throw new IllegalStateException("Not implemented yet");
        //return attributes.getIndex(qName);
    }

    public String getType(String uri, String localName) {
        throw new IllegalStateException("Not implemented yet");
        //return attributes.getType(uri,localName);
    }

    public String getType(String qName) {
        throw new IllegalStateException("Not implemented yet");
        //return attributes.getType(qName);
    }

    public String getValue(String uri, String localName) {
        throw new IllegalStateException("Not implemented yet");
        //return attributes.getValue(uri,localName);
    }

    public String getValue(String qName) {
        String value = attributes.getValue(qName);
        if (value == null || !value.contains("#")) {
            return value;
        }
        return transform(value);
    }

    /**
     *
     * @param value
     * @return parses the String to replace macros.
     */
    private String transform(String value) {
        StringBuffer text = new StringBuffer();
        boolean even = true; //We have seen an even number of elements
        for (StringTokenizer tokenizer = new StringTokenizer(value, "#", true); tokenizer.hasMoreElements();) {
            //Get the token
            String current = tokenizer.nextToken();
            //If the current is equal to a #, we just skip this token
            if (current.equals("#")) {
                even = !even;
                continue;
            }
            //If the number is even, we add the string
            if (even) {
                text.append(current);
            } else {
                //Get the constant value and add it into the string
                String object = constants.get(current);
                if (object == null){                    
                    throw new ParserException("the constant: " + current + " does not exist");
                }
                text.append(object);
            }
        }
        //We are not closing the dies
        if (!even){
            throw new ParserException("There is some DIES that do not close on: " + value);
        }

        return text.toString();
    }


}
