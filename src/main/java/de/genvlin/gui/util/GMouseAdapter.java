/*
 * GMouseAdapter.java
 *
 * Created on 9. April 2006, 23:31
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

package de.genvlin.gui.util;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/** Use the gMouse* method instead of mouse*! To provide popup
 * abilities override showPopup.
 * @see #showPopup
 *
 * @author Peter Karich
 */
public class GMouseAdapter implements MouseListener {
    
    public GMouseAdapter() {
    }

    /** This method will be called after a popup action detection.
     */
    public void showPopup(MouseEvent me){}

    
    final public void mousePressed(MouseEvent e) {
        if(e.isPopupTrigger()) showPopup(e);
        gMousePressed(e);
    }

    final public void mouseReleased(MouseEvent e) {
        if(e.isPopupTrigger()) showPopup(e);
        gMouseReleased(e);
    }

    final public void mouseEntered(MouseEvent e) {
        gMouseEntered(e);
    }

    final public void mouseExited(MouseEvent e) {
        gMouseExited(e);
    }
    
    final public void mouseClicked(MouseEvent e) {
        gMouseClicked(e);
    }
    
    public void gMousePressed(MouseEvent e) {
    }

    public void gMouseClicked(MouseEvent e) {
    }

    public void gMouseReleased(MouseEvent e) {
    }

    public void gMouseEntered(MouseEvent e) {
    }

    public void gMouseExited(MouseEvent e) {
    }
}
