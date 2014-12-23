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

import ch.unige.ng.tools.parser.objects.IParserObject;
import ch.unige.ng.tools.parser.objects.ObjectCreator;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Stack;
import java.util.logging.Logger;
import java.util.logging.Level;


/**
 * This class is used by the XMLParser. This function creates the nodes and use the reflexion
 * to define the new code
 */
public class ConfigHandler extends DefaultHandler {


    /**
     * The method that muste be called
     */
    private String entryPoint;

    /**
     * The object on which the method has to be executed
     */
    private IParserObject object;

    /**
     * Is it the first node?
     */
    private boolean first;

    /**
     * The stack containing objects
     */
    private Stack<IParserObject> stack;

    private Stack<DTDContext.Type> stackType;

    /**
     * Is the current object a class?
     */
    private DTDContext.Type type;

    /**
     * The string to define the method to objectCreated
     */
    private String flushMethod;

    /**
     * The context of the DTD
     */
    private DTDContext contextDTD;

    /**
     * This hashmap contains the ids of the classes that have been declared for now
     */
    private HashMap<String, IParserObject> ids;

    private ConstantHandler constantHandler;

    /**
     * Are we reading some temporary objects.
     */
    private boolean temporaryMode;

    private ObjectCreator creator;

    private boolean fatalError;

    private Logger logger;

    /**
     * The location used to know the position of the error
     */
    private Locator locator;

    private String location() {
        return "(" + locator.getLineNumber() + ";" + locator.getColumnNumber() + "): ";
    }

    /**
     * logs the message and enter in the rescue mode: there was an error and then the program qill not be started, but
     * the config file will be read to know if there are other errors
     *
     * @param message
     */
    public void severe(String message) {
        logger.severe(location() + message);
        fatalError = true;
    }


    public void warn(String s) {
        logger.warning(location() + s);
    }

    public void fatal(String s, Throwable e) {
        logger.log(Level.SEVERE, location() + s, e);
        fatalError = true;
    }


    /**
     * The constructor
     *
     * @param constantsArray Is an array containing the constants name=value. If a constant is declared in the config file, thes values
     *                       will NOT be overriden.
     */
    public ConfigHandler(Logger logger, String... constantsArray) {
        this.logger = logger;
        stack = new Stack<IParserObject>();
        stackType = new Stack<DTDContext.Type>();
        first = true;
        ids = new HashMap<String, IParserObject>();
        constantHandler = new ConstantHandler(this, constantsArray);
        temporaryMode = false;
        creator = new ObjectCreator(this);
        fatalError = false;

    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //If we are in the first, we just have to read the entryPoint
        if (first) {
            //We read the different parameters
            entryPoint = attributes.getValue("main").trim();
            contextDTD = new DTDContext();
            flushMethod = attributes.getValue("flush").trim();
            first = false;
            return;
        }

        attributes = new AttributesDecorator(attributes, constantHandler);
        type = contextDTD.getType(localName);
        stackType.push(type);

        try {
            switch (contextDTD.getType(localName)) {
                case CONSTANT:
                    createConstant(attributes);
                    break;
                case PARAMETER:
                    setAttribute(object, attributes);
                    break;
                case TEMPORARY:
                    temporaryMode = true;
                    //Nothing to do...
                    break;
                case CLASS:
                    createClass(attributes);
                    break;
            }
        } catch (InvocationTargetException e) {
            fatal("Error during the creation of an object", e.getCause());
        } catch (ParserException e) {
            severe(e.getMessage());
        } catch (Exception e) {
            fatal("Error during the creation of an object", e);
        }

    }


    /**
     * This method creates a new constant
     *
     * @param attributes
     */
    private void createConstant(Attributes attributes) {
        String name = attributes.getValue(contextDTD.getAttributeName());
        String value = attributes.getValue(contextDTD.getAttributeValue());
        constantHandler.add(name, value);
    }

    /**
     * This method looks into the table if there is allready a reference
     *
     * @param value
     */
    private IParserObject createReference(String value) {
        IParserObject o = null;
        if (ids.containsKey(value))
            o = ids.get(value);
        else {
            severe("The identifier --" + value + "-- does not exist or you wanted to use forward reference, but this" +
                    " is not implemented yet, AND IT WILL NOT BE IMPLEMENTED");
            return creator.createRescueObject(value);
        }

        //we push it in the stack
        return o;
    }

    /**
     * Sets the locator to know the position of the error
     *
     * @param locator
     */
    public void setDocumentLocator(Locator locator) {
        this.locator = locator;
    }

    /**
     * @return the locator to know the position of an error if it occurs
     */
    public Locator getLocator() {
        return locator;
    }

    /**
     * This method sets or add the attributes to the object
     *
     * @param object
     * @param attributes
     */
    private void setAttribute(IParserObject object, Attributes attributes) throws Exception, IllegalAccessException, InvocationTargetException {
        //TODO gerer les multi-attributs
        object.setAttribute(attributes.getValue(contextDTD.getAttributeName()).trim(),
                attributes.getValue(contextDTD.getAttributeValue()).trim());
    }

    public Logger getLogger() {
        return logger;
    }


    /**
     * This method creates a new class and add it to the object.
     *
     * @param attributes
     * @throws Exception
     */
    private void createClass(Attributes attributes) throws Exception {
        IParserObject o;
        boolean isReference = attributes.getValue(contextDTD.getReferenceValue()) != null;
        if (isReference) {
            String reference = attributes.getValue(contextDTD.getReferenceValue());
            logger.config("Reading the reference: " + reference);
            o = createReference(reference);

        } else {
            String className = null;
            String value = attributes.getValue(contextDTD.getClassName());
            if (value == null) {
                severe("The " + contextDTD.getClassName() +
                        " attribute is not defined, it is impossible to create a class");
                o = creator.createRescueObject("rescue");
            } else {
                className = value.trim();
                logger.config("Creating " + className);
                //Creating the object of the good class
                o = creator.createObject(className);
            }
        }

        //we push it in the stack
        stack.push(o);

        createMonoAttribute(attributes, o);

        //If the object contains an id, the object is put into the hashmap for future use.
        if (attributes.getValue(contextDTD.getID()) != null) {
            ids.put(attributes.getValue(contextDTD.getID()), o);
        }
        object = o;
    }

    /**
     * This function is called where a new object has been created. The metod will create
     * the different attributes, and if there is a parent, will be setted by the parent
     *
     * @param attributes
     * @param o
     * @throws IntrospectionException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private void createMonoAttribute(Attributes attributes, IParserObject o) throws Exception, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        //If we already have an object, we have to push the new one in it
        if (object != null) {
            //We getCopy the name
            String attributeName = attributes.getValue(contextDTD.getAttributeName());
            //Setting the current object to the class
            if (attributeName != null) {
                //We have an attribute that knows the attribute to set or add
                object.setAttribute(attributeName.trim(), o);
                //set(object, attributeName.trim(), o);
            } else if (!object.set(o)) {
                fatalError = true;
            }
        }
        setAttributes(o, attributes);
    }


    private void setAttributes(IParserObject object, Attributes attributes) throws Exception, IllegalAccessException, InvocationTargetException {
        //We loop on each attribute
        for (int i = 0; i < attributes.getLength(); i++) {
            String name = attributes.getQName(i).trim();
            //if it is neither the className nor the attributeName, we set the new value
            if (!contextDTD.isClassName(name) &&
                    !contextDTD.isAttributeName(name) &&
                    !contextDTD.isID(name) &&
                    !contextDTD.isReferenceValue(name)) {
                object.setAttribute(name, attributes.getValue(i).trim());
            }
        }
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        //We define the context if it has to be done
        if (!stackType.isEmpty()) {
            if (stackType.peek() == DTDContext.Type.CLASS) {
                //We try to call method objectCreated, if it does not exist, there is no matter
                //We only flush it if it is not in the temporary mode
                object.flush(flushMethod);

                //We delete the object only if there we are in temporaryMode, if this is not
                //done the access point will be deleted
                if (temporaryMode) object = null;
                if (stack.size() != 0) {
                    stack.pop();
                    if (stack.size() != 0) {
                        IParserObject o = stack.peek();
                        object = o;
                    }
                }
            }

            type = stackType.pop();
        }

        if (contextDTD.isTemporaryElement(localName)) {
            temporaryMode = false;
            //There is no more class
        }

    }

    /**
     * This function is used to run the main program
     *
     * @throws Exception
     */
    public void start() throws Exception {

        if (!fatalError) {
            //We clean the stack
            stack.clear();

            //Garbage collection
            System.gc();
            System.gc();
            logger.entering(object.getClass().toString(), entryPoint);
            //Invokation
            object.call(entryPoint);
            logger.exiting(object.getClass().toString(), entryPoint);
        } else
            logger.severe("Due to error, the program can not be started");

    }

    public IParserObject get(String value) {
        return ids.get(value);
    }

    public boolean containsID(String value) {
        return ids.containsKey(value);
    }
   
}
