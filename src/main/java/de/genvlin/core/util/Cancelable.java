/*
 * Cancelable.java
 *
 * Created on 1. April 2006, 12:23
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

package de.genvlin.core.util;

/** This interface defines only two methods: cancel() and isCanceled().
 *
 * @author Peter Karich
 */
public interface Cancelable {
    
    /** This method cancel the current task and returns true if 
     * canceling has success. 
     */
    public boolean cancel();
    
    /** This method returns true if canceling had success. */
    public boolean isCanceled();    
}