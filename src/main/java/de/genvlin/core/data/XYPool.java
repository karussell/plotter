/*
 * XYPool.java
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
 *
 */

package de.genvlin.core.data;

/**
 * This class provide a <tt>Pool</tt> of <tt>XYInterface</tt>'s.
 *
 * @author Peter Karich
 */
class XYPool extends Pool<XYVectorInterface> 
        implements PoolInterface<XYVectorInterface> {
    
    XYPool(ID id) {
        super(id);
    }
    
    /**
     * This method will create a new <tt>XYInterface</tt>(created by MainPool)
     * and sets specified arguments as x and y respectivly.
     */
    public XYVectorInterface add(VectorInterface x, VectorInterface y) {
        XYVectorInterface xy = MainPool.getDefault().createXYVector(x,y);
        
        return super.add(xy);
    }
    
    /**
     * A "foreign" <tt>XYVectorInterface</tt> could be added to this pool. Foreign means:
     * This pool doesn't contain this <tt>XYInterface</tt> with its ID!
     * But you cannot import <tt>XYInterface</tt>'s which aren't created by
     * {@link MainPool}!!<br>
     * Use <pre> MainPool.import(MainPool pool) </pre> instead.
     */
    public <S extends XYVectorInterface> S add(S data){
        return super.add(data);
    }    
    
    /**
     * This method create's a new XYVectorInterface specified via the specified
     * classes and adds it if possible.
     *
     * @return the added and created <tt>XYInterface</tt>
     */
    public XYVectorInterface create(Class<VectorInterface> clazzX,
            Class<VectorInterface> classY) {
        return add(MainPool.getDefault().createXYVector(clazzX, classY));
    }
}