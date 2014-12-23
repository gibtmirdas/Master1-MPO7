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

package ch.unige.ng.tools.log;

import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

//TODO Javadoc !!!

/**
 * @author Jacques Fontignie
 * @version 25 juil. 2005
 */
public class HtmlFormatter extends Formatter {
    private static final String FINEST = "000000";
    private static final String FINER = "000000";
    private static final String FINE = "000000";
    private static final String CONFIG = "000000";
    private static final String INFO = "00FF00";
    private static final String WARNING = "FFFF00";
    private static final String SEVERE = "FF0000";

    private static final String STYLE = "<style TYPE=\"text/css\">" +
            "a:link { \n" +
            "\tfont-weight: bold; \n" +
            "\ttext-decoration: none; \n" +
      //      "\tcolor: #3366CC;\n" +
            "}\n" +
            "a:visited { \n" +
            "\tfont-weight: bold; \n" +
            "\ttext-decoration: none; \n" +
            //"\tcolor: #3366CC;\n" +
            "\t}\n" +
            "a:hover, a:active { \n" +
            "\ttext-decoration: underline; \n" +
          //  "\tcolor: #D4CDDC;\n" +
            "}" +
            "h1 { \n" +
            "\tfont-size: 22px;\n" +
            "\tmargin: 0px 0px 10px 0px;\n" +
            "\tline-height: 30px;\n" +
            "\tletter-spacing: 1px; \n" +
            "\tfont-weight: 900;\n" +
            "\tcolor: #000A95;\n" +
            "}" +
            "th {\n" +
            "\ttext-align: center;\n" +
            "\tpadding: 2px;\n" +
            "\tbackground-color: #000A95;\n" +
            "\tcolor: #ddd;\n" +
            "\tfont-family: verdana, arial, helvetica, sans-serif;\n" +
            "\tfont-weight:bold;\n" +
            "}\n" +
            "</style>";


    private static final Date dat = new Date();
    private final static String format = "{0,date} {0,time}";
    private static final MessageFormat formatter = new MessageFormat(format);
    private static final Object[] args = new Object[1];

    private HashMap<Level, Integer> list;

    public HtmlFormatter() {
        list = new HashMap<Level, Integer>();
    }

    private String getColor(int level) {
        if (level <= Level.FINEST.intValue())
            return FINEST;
        if (level <= Level.FINER.intValue())
            return FINER;
        if (level <= Level.FINE.intValue())
            return FINE;
        if (level <= Level.CONFIG.intValue())
            return CONFIG;
        if (level <= Level.INFO.intValue())
            return INFO;
        if (level <= Level.WARNING.intValue())
            return WARNING;
        else
            return SEVERE;
    }

    private String printThrowable(Throwable t) {
        String s = t.getClass().getName();
        String message = t.getLocalizedMessage();
        StackTraceElement elements [] = t.getStackTrace();
        StringBuffer trace = new StringBuffer((message != null) ? (s + ": " + message) : s);
        trace.append("\n");
        for (int i = 0; i < elements.length; i++) {
            trace.append(elements[i]);
            trace.append("\n");
        }
        return trace.toString();
    }

//    private String header(String name) {
//        StringBuffer buffer = new StringBuffer("<th>");
//        buffer.append(name);
//        buffer.append("</th>\n");
//        return buffer.toString();
//    }

    private String cell(String content) {
        StringBuffer buffer = new StringBuffer("<td>");
        buffer.append(content);
        buffer.append("</td>\n");
        return buffer.toString();
    }


    public String format(LogRecord record) {
        StringBuffer buffer = new StringBuffer();
        //Creating the line
        buffer.append("<tr VALIGN=TOP>\n");

        logLevel(buffer, record);
        dat.setTime(record.getMillis());
        args[0] = dat;
        StringBuffer text = new StringBuffer();
        formatter.format(args, text, null);
        buffer.append(cell(text.toString()));

        StringBuffer newCell = new StringBuffer("<table><tr><td>");
        newCell.append("<pre>" + record.getMessage() + "</pre>" );
        if (record.getThrown() != null) {
            newCell.append("<br/></td>\n<tr><td><font size=1><pre style=\"color:#FF0000\">");
            Throwable t = record.getThrown();
            newCell.append(printThrowable(t));
            newCell.append("</pre></font>");
        }
        newCell.append("</td></tr></table>");

        buffer.append(cell(newCell.toString()));
        //Finishing the line
        buffer.append("</tr>\n");
        return buffer.toString();
    }

    private void logLevel(StringBuffer buffer, LogRecord record) {

        Level level = record.getLevel();
        int id = 0;
        if (!list.containsKey(level)) {
            list.put(level, 0);
            id = 0;
        } else {
            id = list.get(level) + 1;
            list.put(level, id);
        }
        buffer.append("<td>");
        String sLevel = getColor(level.intValue());
        buffer.append("<a name=\"");
        buffer.append(level + "" + id);
        buffer.append("\" title=\"Go to next message with the same type\" href=\"#");
        buffer.append(level+"" + (id+1));
        buffer.append("\" style=\"color:#");
        buffer.append(sLevel);
        buffer.append("\">");
        buffer.append(record.getLevel());
        buffer.append("</font></a></td>\n");
    }

    public String getHead(Handler h) {
        return "<html><head>" +
                STYLE +
                "<title>Log from.ng./title>" +
                "</head>\n<body>\n" +
                "<div STYLE=\"margin:20px 0px 10px 0px;\n" +
                "\tpadding:20px 0px 0px 20px;\n" +
                "\tborder-style:solid;\n" +
                "\tborder-color:black;\n" +
                "\tborder-width:1px 0px;\n" +
                "\tbackground-color:#eee;\n" +
                "\theight:80px;\">" +
                "<h1> Evolutionnary Programming Kit .ng. Log</h1> " +
                "<p STYLE=\"font-size: 15px;\n" +
                "\tfont-weight: bold;\n" +
                "\tmargin: 0px 0px 0px 0px;\n" +
                "\tpadding: 0px;\"> Level=" + h.getLevel() + "</p></div>" +
                "<div STYLE=\"\tmargin-top: 20px;\n" +
                "   \tmargin-left: 160px;\n" +
                "   \tmargin-right:10px;\n" +
                //"   \tmax-width: 400px;
                "\">" +
                "<table WIDTH=\"50%\" STYLE=\"" +
                "\tborder-style:solid;\n" +
                "\tborder-color:black;\n" +
                "\tborder-width:1px 0px;\n" +
                "\tbackground-color:#eee;\n" +                
                "\">" +
                "<tr><th>LEVEL</th><th>DATE</th><th>MESSAGE</th></tr>";
    }

    public String getTail(Handler h) {
        return "</table></div>\n<div STYLE=\"position: absolute;\n" +
                "\t\tleft:0px;\n" +
                "\t\ttop:140px;\n" +
                "\t\twidth:120px;\n" +
                "\t\tpadding: 10px;\n" +
                "\t\tbackground:#eee;\n" +
                "\t\tborder-width:1px 1px 1px 0px;\n" +
                "\t\tborder-style: solid;\n" +
                "\t\tborder-color: #000;\n" +
                "\t\tline-height: 25px; \">" + printStatistics() + "</div>\n</body>\n</html>";
    }

    private String printStatistics() {
        StringBuffer buffer = new StringBuffer("<table>");
        Set<Level> levels = list.keySet();
        Level array [] = levels.toArray(new Level[levels.size()]);
        Arrays.sort(array, new Comparator<Level>() {
            public int compare(Level level1, Level level2) {
                return level2.intValue() - level1.intValue();
            }
        });
        for (Level level : array) {
            String sLevel = getColor(level.intValue());
            buffer.append("<tr><td><a href=\"#");
            buffer.append(level + "0");
            buffer.append("\" style=\"color:#");
            buffer.append(sLevel);
            buffer.append("\" name=\"");
            buffer.append(level + "" + (list.get(level)+1));
            buffer.append("\">");
            buffer.append(level);
            buffer.append("</a></td><td align=right>");
            buffer.append(list.get(level) + 1);
            buffer.append("</td></tr>");
        }
        buffer.append("</table>");
        return buffer.toString();
    }

}
