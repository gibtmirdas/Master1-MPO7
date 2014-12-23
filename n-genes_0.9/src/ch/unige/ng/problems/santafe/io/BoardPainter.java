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


package ch.unige.ng.problems.santafe.io;

import ch.unige.ng.problems.santafe.context.Board;
import ch.unige.ng.species.Individual;
import ch.unige.ng.species.VariableSize;
import ch.unige.ng.species.stackGP.DataStack;
import ch.unige.ng.species.stackGP.evaluations.IEvaluator;
import ch.unige.ng.species.stackGP.operations.Operation;
import ch.unige.ng.tools.Forced;
import ch.unige.ng.tools.io.writer.LogWriter;
import ch.unige.ng.statistics.Statistic;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.abs;

/**
 * @author Jacques Fontignie
 * @version 10 janv. 2005
 */
public class BoardPainter extends JFrame implements LogWriter<VariableSize<Operation>,Individual> {
    private int array[];
    private DataStack<Integer> stack;
    private Board board;
    private IEvaluator evaluator;

    public BoardPainter(Board board, IEvaluator evaluator) {
        this();
        this.board = board;
        this.evaluator = evaluator;
    }

    public BoardPainter() {
        super("Board");
        stack = new DataStack<Integer>();
        setMinimumSize(new Dimension(500, 500));
        setSize(500, 500);
    }

    @Forced public void setBoard(Board board){
        this.board = board;
    }

    @Forced public void setEvaluator(IEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    /**
     * initializing the class
     *
     * @param statistics
     */
    public void init(List<Statistic<VariableSize<Operation>,Individual>> statistics) {
        array = new int[board.getWidth() * board.getHeight()];
        setVisible(true);
    }

    public void start(int step) {
        setTitle("Board " + step);
        int length = board.getWidth() * board.getHeight();
        for (int i = 0; i < length; i++)
            array[i] = 0;
    }

    public void setValue(VariableSize<Operation> individual) {
        stack.clear();
        stack.push(Board.toInteger(board.getBeginX(),
                board.getBeginY(),
                board.getBeginDirection()));
        evaluator.evaluate(stack,individual);
        repaint();
    }

    private Image offscreenImage;

    //Overide ths method to avoid flickering
    public void update(Graphics g) {
        if (offscreenImage == null ||
                offscreenImage.getWidth(this) != getWidth() ||
                offscreenImage.getHeight(this) != getHeight())
            offscreenImage = createImage(getWidth(), getHeight());
        paint(offscreenImage.getGraphics());
        g.drawImage(offscreenImage, 0, 0, this);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Insets insets = getInsets();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        int widthPlace = (getWidth() - insets.left - insets.right) / board.getWidth();
        int heightPlace = (getHeight() - insets.top - insets.bottom) / board.getHeight();
        int offsetColumn = ((getWidth() - insets.left - insets.right) % board.getWidth()) / 2 + insets.left;
        int offsetLine = ((getHeight() - insets.top - insets.bottom) % board.getHeight()) / 2 + insets.top;
        for (int y = 0; y < board.getHeight(); y++) {
            for (int x = 0; x < board.getWidth(); x++) {
                g.setColor(Color.WHITE);
                g.fillRect(offsetColumn + widthPlace * x,
                        offsetLine + heightPlace * y,
                        widthPlace, heightPlace);
                if (board.containsFood(x, y)) {
                    g.setColor(Color.RED);
                    g.fillOval(offsetColumn + widthPlace * x,
                            offsetLine + heightPlace * y,
                            widthPlace,
                            heightPlace);
                }
            }
        }
        g.setColor(Color.GREEN);
        int lastX = board.getBeginX();
        int lastY = board.getBeginY();

        g.fillOval(offsetColumn + lastX * widthPlace,
                offsetLine + lastY * heightPlace,
                widthPlace, heightPlace);
        int i = stack.size();
        for (Integer position : stack) {
            i--;
            int xSize = (widthPlace * i) / stack.size();
            int ySize = (heightPlace * i) / stack.size();
            int x = Board.getX(position);
            int y = Board.getY(position);
            g.setColor(Color.BLACK);
            g.drawOval(offsetColumn + lastX * widthPlace + widthPlace / 2 - xSize / 2,
                    offsetLine + lastY * heightPlace + heightPlace / 2 - ySize / 2,
                    xSize, ySize);
            g.setColor(Color.GREEN);
            if (abs(lastX - x) > 1) {
                int line = offsetLine + y * heightPlace + heightPlace / 2;
                int minX = min(x, lastX);
                int maxX = max(x, lastX);
                g.drawLine(offsetColumn + minX * widthPlace, line,
                        offsetColumn + minX * widthPlace + widthPlace / 2, line);
                g.drawLine(offsetColumn + maxX * widthPlace + widthPlace / 2, line,
                        offsetColumn + (maxX + 1) * widthPlace, line);
            } else if (abs(lastY - y) > 1) {
                int column = offsetColumn + x * widthPlace + widthPlace / 2;
                int minY = min(y, lastY);
                int maxY = max(y, lastY);
                g.drawLine(column, offsetLine + minY * heightPlace,
                        column, offsetLine + minY * heightPlace + heightPlace / 2);
                g.drawLine(column, offsetLine + maxY * heightPlace + heightPlace / 2,
                        column, offsetLine + (maxY + 1) * heightPlace);
            } else {
                g.drawLine(offsetColumn + lastX * widthPlace + widthPlace / 2,
                        offsetLine + lastY * heightPlace + heightPlace / 2,
                        offsetColumn + x * widthPlace + widthPlace / 2,
                        offsetLine + y * heightPlace + heightPlace / 2);
            }
            lastX = x;
            lastY = y;
        }
    }

    public void stop() {
    }

    public void close() {

    }


}
