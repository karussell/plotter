/*
 * XYInterface.java
 *
 * Created on 30. April 2006, 13:17
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

package de.genvlin.core.data;

/**
 *
 * @author Peter Karich
 */
public interface XYInterface extends CollectionInterface{    
    /** This method returns the Number at specified index from the x-vector.
     */
    public Number getX(int index);
    
    /** This method returns the Number at specified index from the y-vector.
     */
    public Number getY(int index);

    /** This method returns both Numbers at specified index from the <tt>XYVector</tt>
     * as <tt>Point2D.Double</tt>.
     *
    public Point2D.Double get(int i);
    */
    
    public double getXDouble(int index);
    
    public double getYDouble(int index);    
    
    public double getMaxX();
    public double getMinX();
    public double getMaxY();
    public double getMinY();            
}
