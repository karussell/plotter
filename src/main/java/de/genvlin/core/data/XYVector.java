/*
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

import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** This class is a wrapper around two Vectors. They can used as x-y-pairs
 * in a plot. Where plot could be a <tt>XYVectorPool</tt>.
 */
class XYVector extends AbstractCollection 
    implements XYVectorInterface {
    private VectorInterface x;
    private VectorInterface y;
    
    XYVector(VectorInterface xVector, VectorInterface yVector, ID id) {
        super(id);
        x = xVector;
        y = yVector;
    }
    
    public void add(Number xNum, Number yNum) {
        x.add(xNum);
        y.add(yNum);
    }
    
    private VectorInterface getX() {
        return x;
    }
    
    private VectorInterface getY() {
        return y;
    }
    
    Number n;
        
    /** This method returns the maximal value in x data array
     */
    public double getMaxX() {
        if(size()== 0)
            return Double.POSITIVE_INFINITY;
        
        //return ((Number)Collections.max(xyVector.getX())).doubleValue();
        double ret = Double.NEGATIVE_INFINITY;
        for(int i=0; i < getX().size(); i++) {
            n = getX(i);
            if(n!=null && ret < n.doubleValue()) 
                ret = n.doubleValue();
        }
        
        return ret;
    }
    
    /** This method returns the minimal value in x data array
     */
    public double getMinX() {
        if(size() == 0) 
            return Double.NEGATIVE_INFINITY;
        
        double ret = Double.POSITIVE_INFINITY;
        for(int i=0; i < getX().size(); i++) {
            n = getX(i);
            if(n!=null && ret > n.doubleValue()) 
                ret = n.doubleValue();
        }
        return ret;
    }
    
    
    /** This method returns the maximal value in y data array
     */
    public double getMaxY() {
        if(size()== 0) 
            return Double.POSITIVE_INFINITY;
        
        double ret = Double.NEGATIVE_INFINITY;
        for(int i=0; i < getY().size(); i++) {
            n = getY(i);
            if(n!=null && ret < n.doubleValue()) 
                ret = n.doubleValue();
        }
        return ret;
    }
    
    /** This method returns the minimal value in y data array
     */
    public double getMinY() {
        if(size() == 0) 
            return Double.NEGATIVE_INFINITY;
        
        double ret = Double.POSITIVE_INFINITY;
        for(int i=0; i < getY().size(); i++) {
            n = getY(i);
            if(n!=null && ret > n.doubleValue()) 
                ret = n.doubleValue();
        }
        return ret;
    }
            
    public Point2D.Double get(int i) {
        return new Point2D.Double(getX(i).doubleValue(),
                getY(i).doubleValue());
    }
    
    public int size() {
        return Math.min(x.size(), y.size());
    }
    
    public void clear(){
        x.clear();
        y.clear();
    }
    
    public boolean remove(int index) {
        if(index < size() && index >= 0) {
            x.remove(index);
            y.remove(index);
            
            return true;
        }
        return false;
    }
    
    public Iterator iterator() {
        return new XYVectorIterator();
    }
    
    public Number getX(int index) {
        return x.get(index);
    }
    
    public Number getY(int index) {
        return y.get(index);
    }    

    public double getXDouble(int index) {
        if(x.get(index) == null) return Double.NaN;
        else return x.get(index).doubleValue();
    }

    public double getYDouble(int index) {
        if(y.get(index) == null) return Double.NaN;
        else return y.get(index).doubleValue();
    }

    public void addVectorListener(CollectionListener vl) {
        x.addVectorListener(vl);
        y.addVectorListener(vl);
    }
    
    public void removeVectorListener(CollectionListener vl) {
        x.removeVectorListener(vl);
        y.removeVectorListener(vl);
    }
    
    /** The Iterator on Point.Double.
     */
    class XYVectorIterator implements Iterator {
        
        /** points to current element (will returned by next()) */
        int cursor = 0;
        
        /** Index of element returned by most recent call to next or
         * previous.  Reset to -1 if this element is deleted by a call
         * to remove.
         */
        int lastRet = -1;
        
        public boolean hasNext() {
            return cursor != size();
        }
        
        public Object next() {
            try {
                Object next = get(cursor);
                lastRet = cursor++;
                return next;
            } catch(IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }
        
        public void remove() {
            if (lastRet == -1)
                throw new IllegalStateException();
            
            XYVector.this.remove(lastRet);
            if (lastRet < cursor)
                cursor--;
            lastRet = -1;
        }
    }
}