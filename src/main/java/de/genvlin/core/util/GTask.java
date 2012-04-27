/*
 * GTask.java
 *
 * Created on 31. März 2006, 14:00
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;

/** This class provides an easy to use <tt>Runnable</tt>.<br>
 * You should use this class to call long-running exercises from EventQueue.
 * This will avoid blocking of userinterface.
 *
 * TODO: test!
 *
 * @author Peter Karich
 */
public abstract class GTask extends Thread {
    
    private boolean finished = false;
    private boolean started = false;
    private final Set listeners = new HashSet();
    final public static String FINISHED = "Finished";
    final public static String STARTED = "Started";
    //final public static String RESULT = "Result Changed";
    final public static String STATUS = "Status Changed";
    private PropertyChangeListener pcl;
    
    /** Constructs a new task. You have to add Listeners and than call start()
     */
    public GTask() {
    }
    
    /** This method returns true, if the task is valid and only if it has finished!
     * @see #isFinished()
     */
    public boolean isValid() {
        return isFinished();
    }
    
    /** @Override  */
    abstract public void runTask();
    
    abstract public Object getResult();
    
    public void run() {
        started = true;
        fireChangeEvent(STARTED, null);
        runTask();
        finished = true;
        fireChangeEvent(FINISHED, null);
    }
    
    /** Returns true task is already finished. */
    public boolean isFinished() {
        return finished;
    }
    
    /** Returns true task is already finished. */
    public boolean isStarted() {
        return started;
    }
    
    /** This method adds an listener to this task. Listener's will
     * recieve events from EventQueue if task will finish and if status changes.
     * We don't need more than one listener, don't we??? So only one listener
     * could added!
     */
    public synchronized void addChangeListener(PropertyChangeListener cl) {
        if(cl != null) pcl = cl;
    }
    
    /** This method removes a listener from the list.
     * @see #addChangeListener
     */
    public synchronized void removeChangeListener(PropertyChangeListener cl) {
        if(cl == pcl) pcl = null;
    }
    
    protected synchronized void fireChangeEvent(String prop, Object newVal) {
        final PropertyChangeEvent ec = new PropertyChangeEvent(
                this, prop, null, newVal);
        
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                pcl.propertyChange(ec);
            }
        });
    }
}
