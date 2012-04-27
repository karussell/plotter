/*
 * AbstractVector.java
 *
 * Created on 18. März 2006, 15:55
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

/** This interface defines method to access a Vector of Number.
 * Known implementations are {@link DataVector} and extension
 * {@link DoubleVectorInterface}.
 *
 * @author Peter Karich
 */
public interface VectorInterface extends CollectionInterface {    
    
    /** This method adds a Number to the vector */
    public boolean add(Number o);
    
    /** This method adds a double to the vector */
    public boolean addDouble(double d);
    
    /** This method appends a {@link Collection} to this
     * vector.<p>
     * It returns true if successfully added, false otherwise.
     */
    public boolean addAll(java.util.Collection coll);
    
    /** This method returns the i-th element in the vector */
    public Number get(int i);
    
    /** This method returns the i-th element in the vector */    
    public double getDouble(int i);
    
    /** This method sets the i-th element to the specified one.
     * It returns the old element.*/
    public Number set(int i, Number n);
    
    /** TODO This method inserts a number before the i-th element.
     * It returns wether insertation was successful.*/
    //abstract public boolean insert(int i, Number n);
    
    /**
     * This method ensure that the set method will not fire NullPointer
     * Exceptions until the new size.
     * @param size the new size of the VectorInterface's implementation.
     */
    public void trimToSize(int size);
}
