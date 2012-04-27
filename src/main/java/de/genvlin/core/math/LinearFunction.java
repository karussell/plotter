/*
 * LinearFunction.java
 *
 * Created on 28. April 2006, 11:48
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

import de.genvlin.core.data.AbstractCollection;
import de.genvlin.core.data.ID;
import java.util.Iterator;

/**
 * The linear equation is defined as: y = m*x + n.
 *
 * @author Peter Karich
 */
public class LinearFunction extends AbstractCollection
        implements Function {
    
    private double m;
    private double n;
    private int size;
    private double minX;
    private double minY;
    private double maxX;
    private double maxY;
    
    /** Creates a new instance of LinearFunction.
     * The linear equation is defined as: y = m*x + n<br>
     * <b>
     * TODO?<br>
     * Use MainPool.getDefault().create(LinearFunction.class) instead, if you
     * want to add this to an XYPool!
     * </b>
     */
    public LinearFunction(double m, double n, ID id) {
        super(id);
        this.m = m;
        this.n = n;
        setSize(2000);
        setRange(0,0,10,10);
    }
    
    public void setRange(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    
    public double getMaxX() {
        return maxX;
    }
    
    public double getMinX() {
        return minX;
    }
    
    public double getMaxY() {
        return maxY;
    }
    
    public double getMinY() {
        return minY;
    }
    
    /** This method sets the resolution of a function
     */
    public void setSize(int size) {
        if(size<=0)
            return;
        this.size = size;
    }
    
    public double at(double x) {
        return m*x + n;
    }
    
    public double getM() {
        return m;
    }
    
    public double getN() {
        return n;
    }
    
    public void setM(double m) {
        this.m = m;
    }
    
    public void setN(double n) {
        this.n = n;
    }
    
    public double derivativeAt(double x) {
        return m;
    }
    
    public Number getX(int index) {
        return new Double(getXDouble(index));
    }
    
    public Number getY(int index) {
        return new Double(getYDouble(index));
    }
    
    public double getXDouble(int index) {
        return index*(getMaxX()-getMinX())/size()+getMinX();
    }
    
    public double getYDouble(int index) {
        return at(getXDouble(index));
    }
    
    public int size() {
        return size;
    }
    
    public void clear() {
        //m=0; n=0;
    }
    
    /**
     * Todo not implemented!!
     */
    public Iterator iterator() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
    
    public boolean remove(int index) {
        return false;
    }
    
    public String toString() {
        return "Linear Function:("+getID()+") y = " + getM() + "x + (" + getN()+")";
    }
}