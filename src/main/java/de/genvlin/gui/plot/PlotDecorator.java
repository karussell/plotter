/*
 * PlotDecorator.java
 *
 * Created on 18. Juli 2007, 17:11
 * This stands under Public domain
 */

package de.genvlin.gui.plot;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Peter Karich
 */
public interface PlotDecorator {        
    /**
     * This method allows extern programs to directly draw some additional
     * gimmicks into the plot panel. They could add e.g. text.
     * @param g the graphics from the plotpanel
     * @param windowBounds the actual size and position of the associated plot panel.     
     * @param cSys the coordinate system of the underlying mathmetical view to 
     * the associated plot panel.
     */
    void decorate(Graphics g, CoordinateSystem cSys, Rectangle windowBounds);
}
