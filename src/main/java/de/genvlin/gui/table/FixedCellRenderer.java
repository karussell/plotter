/*
 * FixedCellRenderer.java
 *
 * Created on 18. November 2005, 16:51
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

package de.genvlin.gui.table;

import java.awt.Color;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Peter Karich
 */
public class FixedCellRenderer extends DefaultTableCellRenderer {
    
    private Color normColor;//later initiation
    private Color warnColor = Color.RED;
    
    /**
     * Creates a new instance of FixedCellRenderer
     */
    public FixedCellRenderer() {
        normColor = getBackground();
        setIgnoreRepaint(false);
    }
    
    protected void setValue(Object value) {
        //Log.log(""+value+(value instanceof Number)+(value instanceof String));
        //if(! (value instanceof Number)) {
        //    setForeground(warnColor);
        //} else setForeground(normColor);
        setForeground(warnColor);
        setBackground(Color.LIGHT_GRAY);
        
        super.setValue(value);
    }
    
}
