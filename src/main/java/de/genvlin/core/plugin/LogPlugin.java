/*
 * LogPlugin.java
 *
 * Created on 2. Mai 2006, 22:38
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

/** This Service Provider Interface defines the contract which all loggers
 * has to implement if they want to "be" used from <tt>Log</tt>.
 *
 * @author Peter Karich
 */
public interface LogPlugin extends PluginSPI {
    /** This method writes the whole exception to an error-log file.
     * @param notify if true, the user gets a message.
     */
    public void err(Throwable t, boolean notify);
    
    /** This method writes the specified message to a log file.
     * @param notify if true, the user gets the message.
     */
    public void err(String message, boolean notify);
    
    /** This method writes the message to a log file to a log file.
     * @param notify if true, the user gets a message.
     */
    public void log(String message, boolean notify);
    
    /**
     * This method returns the name of the LogPlugin implementation.
     */
    public String toString();
}