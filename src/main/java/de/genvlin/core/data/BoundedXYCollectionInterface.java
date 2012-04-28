/*
 * BoundedXYCollectionInterface.java
 *
 * Created on 27. Juli 2007, 10:17
 *
 * This file is part of the nlo project.
 * Visit http://nlo.sourceforge.net/ for more information.
 * Copyright (C) 2007 Peter Karich.
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

package de.genvlin.core.data;

/**
 *
 * @author Peter Karich, peat_hal 'at' users 'dot' sourceforge 'dot' net
 */
public interface BoundedXYCollectionInterface extends CollectionInterface {
    
    Number getMaxX();
    Number getMinX();
    Number getMaxY();
    Number getMinY();
}
