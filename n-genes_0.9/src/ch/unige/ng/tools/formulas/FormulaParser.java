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

import java.util.*;
import java.lang.*;

/**
 * This class makes a parse of a String and returns a new obejct equation that can be evaluated.
 * <p/>
 * I am quite lazy and this class only creates a file (compiled) within the equation is. If there is a compilation error, the
 * string is considered as malformed
 *
 * @author Jacques Fontignie
 * @version 11 avr. 2005
 */
public class FormulaParser {
    
    private static final String[] KEYWORDS = {
        "sqrt", "abs", "tan", "sin", "cos", "atan", "exp", "atan2", "floor", "round", "trunc", "asin", "acos",
        "log", "log10", "pow", "PI"
    };

    private static final String PATTERN_EQUATION = "import ch.unige.ng.tools.formulas.*;\n" +
            "public class #NAME# implements Equation<#Type#>{\n" +
            "  /**\n" +
            "     *\n" +
            "     * @return the number of inputs\n" +
            "     */\n" +
            "    public int getNumInputs(){return #NUMINPUTS#;}\n" +
            "\n" +
            "    /**\n" +
            "     * evaluates the input and returns the value\n" +
            "     */\n" +
            "    public #Type# evaluate(double ... input){\n" +
            "       if (#NUMINPUTS# != input.length)" +
            "           throw new IllegalStateException(\"The number of inputs is not equals to the number of variables in equation\");" +
            "      return #CODE#;\n" +
            "    }\n" +
            "   \n" +
            "}";


    public synchronized static <T>  Equation<T> parse(String text, Class type) throws Exception {
        final HashMap<String, String> map = new HashMap<String, String>();
        int index = 0;
        if (text.contains("{") || text.contains("}"))
            throw new IllegalArgumentException("You can not use { or } to the parser. " +
                    "It is done to avoid some attacks like: \n\"x;}  \nstatic{Runtime.getRuntime().exec(\"rm *\")}\n\"");
        Scanner scanner = new Scanner(text);
        //Creating the delimiter.
        scanner.useDelimiter("[\\*\\+\\-\\/\\(\\)\\[\\]%!^?:|&<>=]");
        for (; scanner.hasNext();) {
            String name = scanner.next().trim();
            //If we have not a function
            boolean isKeyword = name.equals("");
            for (int i = 0; i < KEYWORDS.length; i++)
                if (name.equals(KEYWORDS[i])) {
                    map.put(KEYWORDS[i], "Math." + KEYWORDS[i]);
                    isKeyword = true;
                    break;
                }

            if (!isKeyword) {
                //if we have a number...
                try {
                    Double.valueOf(name);
                }
                        //YEAH, we have a variable
                catch (NumberFormatException e) {
                    if (index == 0 || !map.containsKey(name)) {
                        map.put(name, "input[" + index++ + "]");
                    }
                }
            }
        }
        List<String> list = new LinkedList<String>();
        for (String key : map.keySet())
            list.add(key);
        //Sort the key in the bigger to the smaller variable.
        //Collections.sort(list, COMPARATOR);
        int beginIndex = 0;
        do {

            int minIndex = -1;
            String currentKey = "";
            for (String key : list) {
                int currentIndex = text.indexOf(key, beginIndex);
                if ((currentIndex < minIndex || minIndex == -1) && currentIndex != -1 ) {
                    minIndex = currentIndex;
                    currentKey = key;
                }
            }
            String replacement = map.get(currentKey);
            if (minIndex == -1) break;
            text = text.substring(0, minIndex) + replacement + text.substring(minIndex + currentKey.length());
            beginIndex = minIndex + replacement.length();
        } while (beginIndex != -1);

        //We replace the different functions.
        String code = PATTERN_EQUATION.replaceAll("#NUMINPUTS#", String.valueOf(index));
        code = code.replaceAll("#Type#", type.getSimpleName());
        code = code.replaceAll("#CODE#", text);

        return Compiler.compile(code);
    }

    /**
     *
     * @param value
     * @return the byte value of the string
     * @throws Exception
     */
    public static Object parseByte(String value) throws Exception {
        try {
            return Byte.parseByte(value);
        } catch (NumberFormatException e) {
            return FormulaParser.parse(value, Byte.class).evaluate();
        }
    }

    /**
     *
     * @param value
     * @return the short value of the string
     * @throws Exception
     */
    public static Object parseShort(String value) throws Exception {
        try {
            return Short.parseShort(value);
        } catch (NumberFormatException e) {
            return FormulaParser.parse(value, Short.class).evaluate();
        }
    }

    /**
     *
     * @param value
     * @return the float value of the string
     * @throws Exception
     */
    public static Object parseFloat(String value) throws Exception {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return FormulaParser.parse(value, Float.class).evaluate();
        }
    }

    /**
     *
     * @param value
     * @return the long value of the string
     * @throws Exception
     */
    public static Object parseLong(String value) throws Exception {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return FormulaParser.parse(value, Long.class).evaluate();
        }
    }

    /**
     *
     * @param value
     * @return the double value of the string
     * @throws Exception
     */
    public static Object parseDouble(String value) throws Exception {
        try {
            if (value.toLowerCase().equals("eps"))
                return Double.MIN_VALUE;
            else if (value.toLowerCase().equals("-eps"))
                return -Double.MIN_VALUE;
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return FormulaParser.parse(value, Double.class).evaluate();
        }
    }

    /**
     *
     * @param value
     * @return the int value of the string
     * @throws Exception
     */
    public static Object parseInt(String value) throws Exception {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            //If there is a number format exception, we will check if it is an expression
            return FormulaParser.parse(value, Integer.class).evaluate();
        }
    }

    /**
     *
     * @param value
     * @return the boolean value of the string
     * @throws Exception
     */
    public static Object parseBoolean(String value) throws Exception {
        try {
            return Boolean.parseBoolean(value);
        } catch (NumberFormatException e) {
            //If there is a number format exception, we will check if it is an expression
            return FormulaParser.parse(value, Boolean.class).evaluate();
        }
    }

    public static void main(String[] args) throws Exception {
        Equation eq = parse("-abs(x*sin(sqrt(abs(x))))-abs(y*sin(20*sqrt(abs(x/y))))", Double.class);
        System.out.println(eq.evaluate(0, 1));
    }
}
