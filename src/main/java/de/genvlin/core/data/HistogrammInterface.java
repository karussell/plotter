/*
 * HistogrammInterface.java
 *
 * Created on 21. Mai 2007, 14:17
 *
 * This file is part of the gstpl project.
 * Visit http://gstpl.sourceforge.net/ for more information.
 * Copyright (C) 2005 - 2007 Peter Karich.
 *
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
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public interface HistogrammInterface extends VectorInterface {
    
    /**
     * This method returns the root mean squared error of all added values. <br>
     * The definition is RMSError = sqrt[ sum_i_to_n((x_i - x_mean)^2) / n] <br>
     * Also known as the second central moment or variance.
     */
    double getRMSError();
    
    /**
     * This method returns the root means square of all added values. <br>
     * The definition is rms = sqrt[ sum_i_to_n( (x_i)^2) / n]
     */
    double getRMS();
    
    /**
     * This method returns the mean value for all added values. <br>
     * The definition is mean = sum_i_to_n( x_i ) / n.
     */    
    double getMean();
}
