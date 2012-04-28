/*
 * PlotDecorator.java
 *
 * Created on 18. Juli 2007, 17:11
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
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * General Public for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this project; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * or look at http://www.gnu.org
 */

package de.genvlin.gui.plot;

import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
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
