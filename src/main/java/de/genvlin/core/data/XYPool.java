/*
 * XYPool.java
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
 *
 */

package de.genvlin.core.data;

/**
 * This class provide a <tt>Pool</tt> of <tt>XYInterface</tt>'s.
 *
 * @author Peter Karich
 */
public class XYPool extends Pool {
    
    XYPool(ID id) {
        super(id);
    }
    
    /**
     * This method will create a new <tt>XYInterface</tt>(created by MainPool)
     * and sets specified arguments as x respectivly y. After <tt>XYInterface</tt>
     * will be added to this pool.
     */
    public XYVectorInterface add(VectorInterface x, VectorInterface y) {
        XYVectorInterface xy = (XYVectorInterface)MainPool.getDefault().create(x,y);
        
        if(super.add(xy) == null) {            
            return xy;
        } else return null;
    }
    
    /**
     * A "foreign" <tt>XYInterface</tt> could be added to this pool. Foreign means:
     * This pool doesn't contain this <tt>XYInterface</tt> with its ID!
     * But it doesn't mean: you can import <tt>XYInterface</tt>'s which aren't
     * created by {@link MainPool}!!<br>
     * Use <pre> MainPool.import(MainPool pool) </pre> instead.
     */
    public boolean add(XYInterface xy) {
        if(super.add(xy) == null) {            
            return true;
        } else return false;        
    }
    
    /**
     * Use {@link #create(Class)} or {@link #add(XYInterface)} instead!
     *
     public boolean add(Comparable com) {
     throw new UnsupportedOperationException("Please use create or add instead!");
     }*/
    
    
    /**
     * This method create's a new XYInterface specified via the class arguments
     * and adds it if possible (this should always work).
     *
     * @return the added and created <tt>XYInterface</tt>
     */
    public XYVectorInterface create(Class clazzX, Class classY) {
        
        try {
            XYVectorInterface ac = (XYVectorInterface)MainPool.
                    getDefault().create(clazzX, classY);
            if(add(ac)) return ac;
            else return null;
            
        } catch(ClassCastException cce) {
            throw new UnsupportedOperationException("Argument class should be an " +
                    "instance of XYVectorInterface!");
        }
    }
}