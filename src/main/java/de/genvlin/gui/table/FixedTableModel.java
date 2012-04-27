/*
 * FixedTableModel.java
 *
 * Created on 25. April 2006, 13:38
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

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

/**
 *
 * @author Peter Karich
 */
public class FixedTableModel extends DefaultTableModel
        implements TableModelListener {
    
    TableModel sourceModel;
    
    /** Creates a new instance of FixedTableModel */
    public FixedTableModel(TableModel link) {
        link.addTableModelListener(this);
        sourceModel = link;
        setRowCount(1);
    }
    
    public void tableChanged(TableModelEvent e) {
        setColumnCount(sourceModel.getColumnCount());
    }
    
    public String getColumnName(int column) {
        return sourceModel.getColumnName(column);
    }
}
