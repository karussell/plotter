/*
 * Platform.java
 *
 * Created on 7. April 2006, 15:27
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

package de.genvlin.core.plugin;

import java.awt.Component;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;

/**
 *
 * @author Peter Karich
 */
public class Platform {    
    
    public interface SPI extends PluginSPI {
        
        /** Returns a human-readable identification of this platform.
         */
        //public String getName();
        
        
        /** This method sends a panel - which should be shown on specified position
         * - to the default platform.
         * @param position could be: "south","north","east","west","center". It could
         * be that a platform do not support some of these. So position is only
         * a suggestion!
         */
        public boolean showPanel(Component panel, String position);
        
        //TODO showDialog, showPropertyPanel ...
        
        /** This method register the platform (gui) specific logger.
         */
        //public void registerLog();
        
        public void showPopup(PopupSupport s);
        
        /** This method returns the platform specific mouse listener.
         * To let the platform broadcast mouseClicks and popupTriggers.
         */
        //public MouseListener getMouseListener();
        
        /** This method returns an internationalised message of specified
         * message key.
         */
        //public String getMessage(String className, String messageKey);
        //This won't work e.g. NB can't get bundles from JFreechart or genvlinCore etc
        //miniplatform should use private ResourceBundle bundle = java.util.ResourceBundle.getBundle("de/genvlin/gui/util/Bundle");
        //one bundle and handle it as singleton!
    }
    
    public interface PopupSupport {
       /** @return true, if source want to display a popup<br>
        *   and false if Platform should suppress popup creation.
        */
        public boolean isPopupAllowed();
        
        /** This method returns an offset, e.g. for JScrollPanes or embedded
         * Components
         */
        //public Point getViewPosition();
        
        /** This method creates a contextdepended popupmenu created from the source component.
         * Returns null if nothing todo!
         */
        public JPopupMenu createPopup();
        
        /** This method returns the MouseEvent useful for retrieving
         * point and component of the mouseclick.
         */
        public MouseEvent getMouseEvent();
    }          
    
    //static private SPI spi;
    
    /** This method sets the platform of genvlin-core.
     * Netbeans is the only one, which is currently available.<br>
     * In the future there should be a platform implementation for<br>
     * eclipse<br>
     * (jbuilder)<br>
     * and the "standalone" miniplatform, which should be a good
     * choice on small or old devices with small ram and discmemory.
     *
    static public void set(Platform.SPI spiImpl) {
        spi = spiImpl;
        //spi.registerLog();
    }*/
    
    /** This method returns THE platform which was registered before.
     *
    static synchronized public Platform.SPI getDefault() {
        return spi;
    }*/
}
