/*
 * CollectionIDEvent.java
 *
 * Created on 1. Mai 2006, 11:28
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

package de.genvlin.core.data;

/**
 *
 * @author Peter Karich
 */
class CollectionIDEvent extends CollectionEvent {
    
    //private ID id;
    
    /** Constructs a new CollectionIDEvent.
     *
     * @param idDataSource the source of this event.
     * @param propName the property which changed.
     * @param id the id of the changed element in idDataSource.
     */
    public CollectionIDEvent(IDData idDataSource, String propName, ID id) {
        super(idDataSource, propName, id);
        //this.id = id;
    }
    
    /**
     * This method returns the id of the changed element in IDData (=source).
     * To get the id of IDData itself use:
     * <pre>((IDData)event.getSource()).getID()</pre>
     *
    public ID getID() {
        return id;
    }*/
}
