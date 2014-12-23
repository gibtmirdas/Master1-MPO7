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



/**
 * This class defines a board where containing the food, and a begin position.
 * The basic problem has size 32x32 with 89 foods on the board. If no args are given, the
 * problem will be like this.
 * <p/>
 * There are some possible choices:
 * <ul>
 * <li> You can define that the board mute: the begin position changes and the position
 * of food too. </li>
 * <li> You can define that the food and position is a constant</li>
 * </ul>
 *
 * @author Jacques Fontignie
 * @version 10 janv. 2005
 */
public abstract class Board {
    protected int width;
    protected int height;
    protected int numFoods;
    protected int beginX;
    protected int beginY;
    protected Direction beginDirection;
    int[] array;

    public Board() {
        numFoods = 89;
        height = 32;
        beginX = 2;
        width = 32;
        beginDirection = Direction.EAST;
        beginY = 2;
    }

    /**
     * @param x
     * @param y
     * @param direction
     * @return the integer value of elements x,y, and beginDirection concatenated
     */
    public static int toInteger(int x, int y, Direction direction) {
        return direction.ordinal() | (x << 2) | (y << 17);
    }

    /**
     * @param position
     * @return the inverse transformation of the number
     */
    public static int getX(int position) {
        return (position >> 2) & 0x7F;
    }

    public static int getY(int position) {
        return (position >> 17) & 0x7F;
    }

    public static Direction getDirection(int position) {
        int val = position & 3;
        for (Direction d : Direction.values())
            if (d.ordinal() == val) return d;
        return null;
    }

    /**
     * @param position
     * @return the position without the beginDirection
     */
    public static int getPosition(int position) {
        return position >> 2;
    }

    /**
     * @param x
     * @param y
     * @return the number of food at position p
     */
    public int getNumFoods(int x, int y) {
        return array[x + width * y];
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getNumFoods() {
        return numFoods;
    }

    public void setNumFoods(int numFoods) {
        this.numFoods = numFoods;
    }

    public int[] getArray() {
        return array;
    }

    public void setArray(int[] array) {
        this.array = array;
    }

    public int getBeginX() {
        return beginX;
    }

    public void setBeginX(int beginX) {
        this.beginX = beginX;
    }

    public int getBeginY() {
        return beginY;
    }

    public void setBeginY(int beginY) {
        this.beginY = beginY;
    }

    public Direction getBeginDirection() {
        return beginDirection;
    }

    public void setBeginDirection(Direction beginDirection) {
        this.beginDirection = beginDirection;
    }

    public int getBeginPosition() {
        return toInteger(beginX, beginY, beginDirection);
    }

    public boolean containsFood(int x, int y) {
        return array[x + y * width] != 0;
    }

    public void addFood(int x, int y) {
        array[x + width * y]++;
    }

    public enum Direction {
        NORTH{
            public Direction left() {
                return WEST;
            }

            public Direction right() {
                return EAST;
            }
        },
        EAST{
            public Direction left() {
                return NORTH;
            }

            public Direction right() {
                return SOUTH;
            }
        },
        SOUTH{
            public Direction left() {
                return EAST;
            }

            public Direction right() {
                return WEST;
            }
        },
        WEST{
            public Direction left() {
                return SOUTH;
            }

            public Direction right() {
                return NORTH;
            }
        };

        /**
         * @return the beginDirection at left
         */
        public abstract Direction left();

        /**
         * @return the beginDirection at rigfht;
         */
        public abstract Direction right();
    }
}
