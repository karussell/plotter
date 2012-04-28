/*
 * VectorPool.java
 *
 * Created on 8. March 2006, 15:15
 *
 * genvlin project.
 * Copyright (C) 2005 - 2007 Peter Karich.
 *
 * The initial version for the genvlin plotter you will find here:
 * http://genvlin.berlios.de/
 * The current release you will find here:
 * http://nlo.wiki.sourceforge.net/
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
 * This class provide a <tt>Pool</tt> of <tt>VectorInterface</tt>'s.
 *
 * @author Peter Karich
 */
public class VectorPool extends Pool<VectorInterface> {
    
    VectorPool(ID id) {
        super(id);
    }
    
    /** A "foreign" vector could be added to this pool. Foreign means:
     * This pool doesn't contain this vector! But it doesn't mean:
     * you can import vectors which aren't created by {@link MainPool}!!<br>
     * Use <pre> MainPool.import(MainPool pool) </pre> instead.
     */
    public <S extends VectorInterface> S add(S av) {
        return super.add(av);
    }
    
    /**
     * Use {@link #create(Class)} or {@link #add(VectorInterface)} instead!
     *
     public boolean add(Comparable com) {
     throw new UnsupportedOperationException("Please use create or add instead!");
     }*/
    
    
    /**
     * This method create's a new vector (<tt>VectorInterface</tt>) specified via
     * by the class argument and adds it - if possible (this should always work) -
     * to the pool.
     *
     * @return the added <tt>VectorInterface</tt>
     */
    public VectorInterface create(Class<VectorInterface> clazz) {
        
        try {
            return add(MainPool.getDefault().createVector(clazz));
        } catch(ClassCastException cce) {
            throw new UnsupportedOperationException("Argument class should be an " +
                    "instance of AbstractVector!");
        }
    }
}
