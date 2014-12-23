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

/**
 * n-genes
 * <p/>
 * Describes a container.
 *
 * @author Jacques Fontignie
 * @version 15 d?c. 2004
 */
public class DTDContext {

    private static final String ATTRIBUTE_NAME = "NAME";
    private static final String CLASS_NAME = "CLASS";
    private static final String OBJECT_ELEMENT = "object";
    private static final String PARAMETER_ELEMENT = "parameter";
    private static final String ATTRIBUTE_VALUE = "VALUE";
    private static final String ID = "id";
    private static final String REFERENCE_VALUE = "reference";
    private static final String CONSTANT = "constant";
    private static final String TEMPORARY_ELEMENT = "temporary";

    public static enum Type {
        CLASS,
        PARAMETER,
        CONSTANT,
        TEMPORARY
    };


    public DTDContext() {
    }


    public boolean isID(String name) {
        return name.equals(ID);
    }

    public boolean isReferenceValue(String name) {
        return name.equals(REFERENCE_VALUE);
    }

    public boolean isTemporaryElement(String name) {
        return name.equals(TEMPORARY_ELEMENT);
    }

    public boolean isAttributeValue(String name) {
        return name.equals(ATTRIBUTE_VALUE);
    }

    public boolean isParameterElement(String name) {
        return name.equals(PARAMETER_ELEMENT);
    }

    public boolean isObjectElement(String name) {
        return name.equals(OBJECT_ELEMENT);
    }

    public boolean isAtttributeName(String name) {
        return name.equals(ATTRIBUTE_NAME);
    }

    public boolean isClassName(String name) {
        return name.equals(CLASS_NAME);
    }

    public boolean isConstant(String name) {
        return name.equals(CONSTANT);
    }

    public String getAttributeName() {
        return ATTRIBUTE_NAME;
    }

    public String getAttributeValue() {
        return ATTRIBUTE_VALUE;
    }

    public String getReferenceValue() {
        return REFERENCE_VALUE;

    }

    public String getClassName() {
        return CLASS_NAME;
    }


    public String getID() {
        return ID;
    }

    public String getTemporaryElement() {
        return TEMPORARY_ELEMENT;
    }

    public boolean isAttributeName(String name) {
        return name.equals(ATTRIBUTE_NAME);
    }

    public Type getType(String name){
        if (name.equals(PARAMETER_ELEMENT)) return Type.PARAMETER;
        if (name.equals(CONSTANT)) return Type.CONSTANT;
        if (name.equals(TEMPORARY_ELEMENT)) return Type.TEMPORARY;
        return Type.CLASS;
    }
}
