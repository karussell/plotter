/*
 * GThreadHelper.java
 *
 * Created on 28. März 2006, 23:23
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

import javax.swing.SwingUtilities;

/**
 *
 * @author Peter Karich
 */
public abstract class GThreadHelper {
    
    Thread t;
    Object created;
    
    /** Creates a new instance of GThreadHelper */
    public GThreadHelper() {
        Runnable longCreation = new Runnable() {
            public void run() {
                created = longCreation();
                
                SwingUtilities.invokeLater(
                        new Runnable() {
                    public void run() { afterCreation(); }
                });
            }
        };
        
        t = new Thread(longCreation);
    }
    
    public abstract Object longCreation();
    
    public void afterCreation(){};
    
    public void interrupt() {
        t.interrupt();
    }
    
    public Object getCreated() {
        return created;
    }
    
    public void start() {
        t.start();
    }
}
