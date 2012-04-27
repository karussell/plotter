/*
 * Function.java
 *
 * Created on 27. April 2006, 20:01
 *
 * genvlin project.
 * Copyright (C) 2005, 2006 Peter Karich.
 *
 * This project is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this project; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * or look at http://www.gnu.org
 */

package de.genvlin.core.math;

import de.genvlin.core.data.XYInterface;

//package numbercruncher.mathutils;

/**
 * The base class for functions that can have derivatives.
 */
public interface Function extends XYInterface
{
    /**
     * Return the value of the function at x.
     *
     * @param x the value of x
     * @return the function value
     */
    public double at(double x);

    /**
     * Return the value of the function's derivative at x.
     * @param x the value of x
     * @return the derivative value
     */
    public double derivativeAt(double x);
    
    /**
     * This method sets the range for the function. We need this method
     * to autoscale an "infinity vector". But keep in mind that
     * implementation are not forced to implement this method.
     *
     */
    public void setRange(double minX, double minY, double maxX, double maxY);
    
    /**
     * This makes no sense on functions??
     */
    public void clear();
    
    /**
     * This makes no sense on functions??
     */
     public boolean remove(int index);
}