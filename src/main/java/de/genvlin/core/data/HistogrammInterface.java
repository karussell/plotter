/*
 * HistogrammInterface.java
 *
 * Created on 21. Mai 2007, 14:17
 * This stands under Public domain
 */

package de.genvlin.core.data;

/**
 *
 * @author Peter Karich
 */
public interface HistogrammInterface extends VectorInterface {
    
    /**
     * @return sum of all entries
     */
    double getSum();
    
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
