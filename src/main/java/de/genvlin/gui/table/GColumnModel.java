/*
 * GColumnModel.java
 *
 * Created on 25. April 2006, 22:21
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


import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;

/**
 * This class inherits the default model.
 * Column model makes the columns of table visibles.
 * Set the headerrenderer etc.
 * See GTableModel for the data model of GTable
 * @author Peter Karich
 */
public class GColumnModel extends DefaultTableColumnModel {
    static final long serialVersionUID = 432233245;
    
    public GColumnModel() {
        super();
    }
    
    /** This methods add a column to the fixed table.
     */
    public void addColumn(TableColumn tc) {
        //tc.setHeaderRenderer(null);
        tc.setMinWidth(120);
        super.addColumn(tc);
    }
}
