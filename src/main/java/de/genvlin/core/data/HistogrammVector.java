/*
 * HistogrammVector.java
 *
 * Created on 21. Mai 2007, 14:13
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
 * This class adds some statistical methods to DataVector.
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
class HistogrammVector extends DataVector implements HistogrammInterface {
    
    /**
     * Creates a new instance of HistogrammVector
     */
    HistogrammVector(ID id) {
        super(id, null);
    }
        
    public double getRMSError() {
        int size = size();
        if(size == 0) {
            return 0;
        }
        double mean = getMean();
        
        double result = 0;
        double tmp;
        Number n;
        int noOfNonNullValues = size;
        
        for(int i = 0; i < size; i++) {
            n = get(i);
            if(n != null) {
                tmp = n.doubleValue() - mean;
                result += tmp * tmp;
            } else {
                noOfNonNullValues--;
            }
        }
        if(noOfNonNullValues > 0) {
            return Math.sqrt(result/noOfNonNullValues);
        } else {
            return 0;
        }
    }
        
    public double getRMS() {
        int size = size();
        if(size == 0) {
            return 0;
        }
        
        double result = 0;
        double tmp;
        int noOfNonNullValues = size;
        Number n;
        
        for(int i = 0; i < size; i++) {
            n = get(i);
            if(n != null) {
                tmp = n.doubleValue();
                result += tmp * tmp;
            } else {
                noOfNonNullValues--;
            }
        }
        
        if(noOfNonNullValues > 0) {
            return Math.sqrt(result/size);
        } else {
            return 0;
        }
    }
        
    public double getMean() {
        //TODO PERFORMANCE: we can sum + substract on every add/remove call
        int size = size();
        if(size == 0) {
            return 0;
        }
        
        double result = 0;
        Number n;
        int noOfNonNullValues = size;
        
        for(int i = 0; i < size; i++) {
            n = get(i);
            if(n != null) {
                result += n.doubleValue();
            } else {
                noOfNonNullValues --;
            }
        }
        
        if(noOfNonNullValues > 0) {
            return result / noOfNonNullValues;
        } else {
            return 0;
        }
    }
}