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


package ch.unige.ng.tools.parser.objects;

import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.formulas.FormulaParser;
import ch.unige.ng.tools.parser.ConfigHandler;

import java.beans.MethodDescriptor;
import java.beans.IntrospectionException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

import static java.beans.Introspector.getBeanInfo;

/**
 * This class defines an object that is created by the parser and that is used to simplify the use of objects
 * <p/>
 * n-genes
 *
 * @author Jacques Fontignie
 * @version 2 febr. 2005
 */
public class ParserObject implements IParserObject {

    private Object object;
    private MethodDescriptor descriptors [];
    private LinkedList<Method> forcedMethod;
    private ConfigHandler handler;
    private boolean fatal;

    /**
     * Create an object with the className, if there was an error it throws the error
     *
     * @param className
     * @param _handler
     */
    public ParserObject(String className, ConfigHandler _handler) {
        handler = _handler;
        try {
            object = createObject(className);
            if (object == null) {
                fatal = true;
                return;
            }
        } catch (Exception e) {
            handler.fatal("Impossible to create class: " + className, e);
            fatal = true;
            return;
        }
        try {
            descriptors = getBeanInfo(object.getClass()).getMethodDescriptors();
        } catch (IntrospectionException e) {
            handler.fatal("Impossible to get the methods available", e);
            fatal = true;
            return;
        }
        forcedMethod = new LinkedList<Method>();
        findAnnotated();
        checkApi();
    }

    /**
     * @return if the constructor made an error that could be considered as fatal
     */
    protected boolean isFatal() {
        return fatal;
    }

    private void checkApi() {
        if (forcedMethod.size() != 0) {
            Constructor[] list = object.getClass().getConstructors();
            boolean found = false;
            for (Constructor c : list) {
                if (c.getParameterTypes().length >= forcedMethod.size()) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                handler.warn("The class " + object.getClass() + " should have a constructor that possess at least" +
                        " the \"Forced\" attributes to facilitate the utilisation with an api");
            }

        }
    }

    private boolean isAnnotated(Method method, Class currentClass) {
        try {
            Method currentMethod = currentClass.getMethod(method.getName(), method.getParameterTypes());
            if (currentMethod.isAnnotationPresent(Forced.class))
                return true;
        } catch (NoSuchMethodException e) {
            //We know that the method exists
        }
        //We go through the interfaces
        for (Class currentInterface : currentClass.getInterfaces())
            if (isAnnotated(method, currentInterface)) return true;
        Class superClass = currentClass.getSuperclass();
        if (superClass == null)
            return false;
        else
            return isAnnotated(method, superClass);

    }

    /**
     * Find the annotated methods in the object
     */
    private void findAnnotated() {
        for (MethodDescriptor descriptor : descriptors) {
            Method method = descriptor.getMethod();
            if (method.isAnnotationPresent(Forced.class))
                forcedMethod.add(method);
            else {
                if (isAnnotated(method, object.getClass()))
                    forcedMethod.add(method);
            }
        }
    }

    public MethodDescriptor[] getDescriptors() {
        return descriptors;
    }

    public boolean flush(String name) {
        if (!forcedMethod.isEmpty()) {
            StringBuffer text = new StringBuffer("Some methods have not been called in " +
                    object.getClass() + ":\n");
            for (Method method : forcedMethod) {
                text.append("\t" + method.getName() + "(" + printParameters(method.getParameterTypes()) + ")");
            }
            handler.severe(text.toString());
            return false;
        }

        Method method = null;
        try {
            method = object.getClass().getMethod(name);
        } catch (NoSuchMethodException e) {
        }
        if (method != null) {
            try {
                method.invoke(object);
            } catch (InvocationTargetException e) {
                handler.fatal("Flushing aborted", e.getCause());
                return false;
            } catch (Exception e) {
                handler.fatal("Flushing aborted", e);
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings({"finally"})
    public boolean call(String name) {
        Method method = null;
        for (MethodDescriptor descriptor : descriptors) {
            if (descriptor.getName().equals(name) &&
                    descriptor.getMethod().getParameterTypes().length == 0) {
                method = descriptor.getMethod();
                break;
            }
        }
        if (method == null) {
            errorNoSuchRunMethod(name);
            return false;
        }
        try {
            method.invoke(object);
            return true;
        } catch (IllegalAccessException e) {
            handler.fatal("impossible to invoke the method: " + name, e);
        } catch (InvocationTargetException e) {
            handler.fatal("Impossible to invoke the method: " + name, e.getCause());
        }
        return false;
    }

    private void call(Method method, Object... attributes) {
        if (forcedMethod.contains(method)) {
            boolean removed = forcedMethod.remove(method);
            if (!removed && method.getName().startsWith("set"))
                handler.severe("The method " + method + " has been called two times, maybe you wanted to use an adder or " +
                        "there is an error in the config file.");
        }

        try {
            method.invoke(object, attributes);
        } catch (IllegalAccessException e) {
            handler.fatal("Impossible to access the method", e);
        } catch (InvocationTargetException e) {
            handler.fatal("Error during the call to: " + method, e.getCause());
        }

    }

    public Object getObject() {
        return object;
    }

    /**
     * Creates the object with the class className
     *
     * @param className
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws InstantiationException
     */
    private Object createObject(String className) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class currentClass = Class.forName(className);
        if (currentClass == null) {
            handler.severe("The class " + className + " does not exist");
            return null;
        }
        Constructor constructors [] = currentClass.getConstructors();
        for (Constructor c : constructors)
            if (c.getParameterTypes().length == 0)
                return c.newInstance();
        handler.severe("No empty constructor have been found in class: " + currentClass);
        return null;
    }


    public boolean setAttribute(String name, String value) throws Exception, IllegalAccessException, InvocationTargetException {
        String attributeName = name;
        String attributeValue = value;
        Method method = findMethod(attributeName);
        if (method == null) {
            errorNoSuchMethod(attributeName);
            return false;
        }
        //If the handler contains an object with that name
        if (handler.containsID(attributeValue)) {
            IParserObject object = handler.get(value);
            if (object instanceof RescueObject) {
                Object o = ((ParserObject) object).getObject();
                call(method, o);
                return true;
            }
        }
        //Call the method with the object transformed
        Object object = transformValue(attributeValue, method.getParameterTypes()[0]);
        if (object != null)
            call(method, object);
        return true;
    }

    public boolean setAttribute(String name, IParserObject o) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Method method = findMethod(name);
        if (method == null) {
            errorNoSuchMethod(name);
            return false;
        }
        if (o instanceof ParserObject)
            call(method, ((ParserObject) o).getObject());
        return true;
    }


    public boolean set(IParserObject parserObject) throws IllegalAccessException, InvocationTargetException {
        if (parserObject instanceof RescueObject) return true;
        ParserObject o = (ParserObject) parserObject;
        Method method = findMethod(o.getObject());

        //If the method does not exist, we return false
        if (method == null) {
            return false;
        }

        call(method, o.getObject());
        return true;
    }

    /**
     * This method transforms the string value into type. Not every simple types are implemented yet
     *
     * @param value the value to transform
     * @param type  the new type
     * @return the object transformed
     */
    private Object transformValue(String value, Class type) throws Exception {
        String typeName = type.getSimpleName();
        if (typeName.equals("String"))
            return value;
        if (typeName.equals("int")) {
            return FormulaParser.parseInt(value);
        }
        if (typeName.equals("boolean")) {
            return FormulaParser.parseBoolean(value);
        }
        if (typeName.equals("double")) {
            return FormulaParser.parseDouble(value);
        }
        if (typeName.equals("long"))
            return FormulaParser.parseLong(value);
        if (typeName.equals("float"))
            return FormulaParser.parseFloat(value);
        if (typeName.equals("short"))
            return FormulaParser.parseShort(value);
        if (typeName.equals("byte"))
            return FormulaParser.parseByte(value);

        //Reading the enumerations
        if (type.isEnum()) {
            for (Object o : type.getEnumConstants()) {
                if (o.toString().equals(value)) {
                    return o;
                }
            }
            handler.severe("There is no enumeration that is declared as " + value + " in " + typeName +
                    ".Available are: \n" + printEnum(type));
            return null;
        }
        handler.severe("the type " + type + " is not implemented yet");
        return null;
    }

    /**
     * @param type
     * @return all the enumarations available in type
     */
    private String printEnum(Class type) {
        StringBuffer buffer = new StringBuffer();
        for (Object o : type.getEnumConstants()) {
            buffer.append("\t" + o + "\n");
        }
        return buffer.toString();
    }


    /**
     * This method looks for a method that sets or adds the attribute attributeName
     *
     * @return the method or null, if none is found
     */
    private Method findMethod(String attributeName) {
        String addName = "add" + capitalize(attributeName);
        String setName = "set" + capitalize(attributeName);
        for (MethodDescriptor descriptor : descriptors) {
            //If names are OK
            if (descriptor.getName().equals(addName) || descriptor.getName().equals(setName)) {
                return descriptor.getMethod();
            }
        }
        return null;
    }

    /**
     * This method will search in the descriptors if there exists a setter or an adder that
     * can add an object of type parameter
     *
     * @param parameter
     * @return the method found or null if there are none
     */
    private Method findMethod(Object parameter) {
        LinkedList<Method> list = new LinkedList<Method>();
        for (MethodDescriptor descriptor : descriptors) {
            String functionName = descriptor.getName();
            if (functionName.startsWith("add") || functionName.startsWith("set")) {
                //If we have the same root.
                Method method = descriptor.getMethod();
                Class parameters [] = method.getParameterTypes();
                if (parameters.length == 1 && parameters[0].isInstance(parameter)) {
                    //If there is exactly one argument and this
                    // one corresponds with the class we have found the good class
                    //return method;
                    list.add(method);
                }
            }
        }
        if (list.size() == 1)
            return list.remove();
        if (list.size() > 1) {
            String text = "There are " + list.size() + " methods that can set or add the attribute in class" +
                    object.getClass() + ".\n" + "These methods are: \n";
            for (Method m : list)
                text += "\t" + m.getName() + "(" + printParameters(m.getParameterTypes()) + ")\n";
            text += "You have to define the function name.";
            handler.severe(text);
        } else {
            errorNoSuchMethod(parameter.getClass().toString());
        }
        return null;
    }

    private String capitalize(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    private void errorNoSuchRunMethod(String attributeName) {
        StringBuffer buffer = new StringBuffer("No such method: " + attributeName + " in " + object.getClass().toString());
        buffer.append("\nAvailable methods are:\n" + printAllExistant());
        handler.severe(buffer.toString());
    }

    private void errorNoSuchMethod(String attributeName) {
        StringBuffer buffer = new StringBuffer("No such method: that sets or adds " + attributeName + " in " + object.getClass().toString() + "\n");
        buffer.append("Available methods are:\n" + printExistant());
        handler.severe(buffer.toString());
    }

    private String printExistant() {
        String text = "";
        for (MethodDescriptor descriptor : descriptors) {
            String name = descriptor.getMethod().getName();
            if (name.startsWith("add") || name.startsWith("set")) {
                text += "\t" + name + "( " + printParameters(descriptor.getMethod().getParameterTypes()) + " )";
                text += "\n";
            }
        }
        return text;
    }

    private String printAllExistant() {
        String text = "";
        for (MethodDescriptor descriptor : descriptors) {
            String name = descriptor.getMethod().getName();
            text += "\t" + name + "( " + printParameters(descriptor.getMethod().getParameterTypes()) + " )";
            text += "\n";
        }
        return text;
    }

    private String printParameters(Class[] parameterTypes) {
        if (parameterTypes.length == 0)
            return " ";
        String text = parameterTypes[0].getName();
        for (int i = 1; i < parameterTypes.length; i++) {
            text += " , " + parameterTypes[i].getName();
        }
        return text;
    }


    public String toString() {
        return object.toString();
    }


}
