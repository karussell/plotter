/*
 * PluginPool.java
 *
 * Created on 5. April 2006, 23:34
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

import de.genvlin.core.plugin.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** This class supports adding/removing of plot plugins.
 * The plugins will add/remove itself to/from this pool.
 * You could retrieve all, by calling {@link #getAll()}.<br>
 * TODO: we need a more general plugin mechanism for all various kinds
 * of plugins...
 *
 * @author Peter Karich
 */
public class PluginPool {
    
    //--------- Action Context -----------
    final static public String HEADER = "tablepanel/header",
            TABLE = "tablepanel/tablegrid",
            
            /** col no == -1 */
            INVISIBLE_HEADER = "tablepanel/invisibletablegrid",
            FIXED = "tablepanel/fixedgrid",
            INVISIBLE_FIXED = "tablepanel/invisiblefixedgrid",
            /** Fine grained action context, useful for plot plugins*/
            SELECTED_COLS = "tablepanel/selectedCols>1",
            ELSE = "tablepanel/ELSE";
    
    
    /** The regression actioncontext
     */
    final public static String REGRESSION_AC = "regression";
    
    /** The plot actioncontext
     */
    final public static String PLOT_AC = "plot";
    
    /**
     * The singleton instance of this plotpool.
     */
    private static PluginPool instance;
    private PluginPool() {
    }
    
    private HashMap engines = new HashMap();
    
    /** The default plotengine */
    private PlotPlugin plotEngine;
    
    /** The default fitengine */
    private FitPlugin fitEngine;
    
    /** The default fitengine */
    private ScriptPlugin scriptEngine;
    
    /** The default fitengine */
    private LogPlugin logEngine;
    
    private Platform.SPI platform;
    
    /** This method returns the singleton pluginpool.
     */
    static synchronized public PluginPool getDefault() {
        if(instance == null)
            instance = new PluginPool();
        return instance;
    }
    
    public ScriptPlugin getScriptEngine() {
        return scriptEngine;
    }
    
    /** You can use Log.log instead of this here:<br>
     * PluginPool.getDefault().getLogEngine().log
     */
    public LogPlugin getLogEngine() {
        return logEngine;
    }
    
    public Platform.SPI getPlatform() {
        return platform;
    }
    
    /** This method returns a plotengine for the specified string.
     * And <tt>null</tt> if not found.
     */
    public PluginSPI getEngine(String str) {
        //addAll();
        return (PluginSPI)engines.get(str);
    }
    
    /**
     * This method returns the default plotengine and <tt>null</tt>
     * if no plugin with plotting feature was added-
     */
    public PlotPlugin getPlotEngine() {
        return plotEngine;
    }
    
    /**
     * This method returns the default fit engine.
     */
    public FitPlugin getFitEngine() {
        return fitEngine;
    }
    
    /** This method adds the specified plotengine to this plotplugin pool.
     */
    public void add(PluginSPI plugin) {
        
        //if(plugin instanceof PluginSPI)
        engines.put(plugin.getName(), plugin);
        
        if(plugin instanceof PlotPlugin) {
            PlotPlugin tmp = (PlotPlugin)plugin;
            if(plotEngine == null)
                plotEngine = tmp;
            else if(acceptAsPlotEngine(tmp))
                plotEngine = tmp;
        }
        
        if(plugin instanceof FitPlugin) {
            FitPlugin tmp = (FitPlugin)plugin;
            if(fitEngine == null)
                fitEngine = tmp;
            else if(acceptAsFitEngine(tmp))
                fitEngine = tmp;
        }
        
        if(plugin instanceof LogPlugin) {
            LogPlugin tmp = (LogPlugin)plugin;
            if(logEngine == null)
                logEngine = tmp;
            else if(acceptAsLogEngine(tmp))
                logEngine = tmp;
        }
        
        if(plugin instanceof ScriptPlugin) {
            ScriptPlugin tmp = (ScriptPlugin)plugin;
            if(scriptEngine == null)
                scriptEngine = tmp;
            else if(acceptAsScriptEngine(tmp))
                scriptEngine = tmp;
        }
        
        if(plugin instanceof Platform.SPI) {
            Platform.SPI tmp = (Platform.SPI)plugin;
            if(platform == null)
                platform = tmp;
            else if(acceptAsPlatform(tmp))
                platform = tmp;
        }
        
        //TODO I18N
        Log.log("Successfully plugged in: "+ plugin.getName()+".", false);
    }
    
    public boolean acceptAsFitEngine(FitPlugin plugin) {
        return plugin.getName().compareTo(fitEngine.getName()) < 0;
    }
    
    public boolean acceptAsPlatform(Platform.SPI plugin) {
        return plugin.getName().compareTo(platform.getName()) < 0;
    }
    
    public boolean acceptAsPlotEngine(PlotPlugin plugin) {
        return plugin.getName().compareTo(plotEngine.getName()) < 0;
    }
    
    public boolean acceptAsScriptEngine(ScriptPlugin plugin) {
        return plugin.getName().compareTo(scriptEngine.getName()) < 0;
    }
    
    public boolean acceptAsLogEngine(LogPlugin plugin) {
        return plugin.getName().compareTo(logEngine.getName()) > 0;
    }
    
    /** This method removes the specified plotengine from this plotplugin pool.
     */
    public void remove(PluginSPI plugin) {
        engines.remove(plugin);
        Log.log("Successfully removed plugin: "+ plugin.getName()+".", true);
    }
    
    /**
     * This method returns all plugins as ArrayList<PluginSPI>, which
     * are registered to specified class.<br>
     * Pass <tt>null</tt> if you want all.
     */
    /*public ArrayList getAll(Class clazz) {
        //addAll();
        ArrayList engineList = new ArrayList();
     
        Iterator iter = engines.entrySet().iterator();
        while(iter.hasNext()) {
            PluginSPI entry = (PluginSPI)((Map.Entry)iter.next()).getValue();
            engineList.add(entry);
        }
        return engineList;
    }*/
    
    /** This method broadcasts the RequestEvent only to those plugins, which
     * are registered on "RequestEvent".getActionContextReason().
     */
    public void sendRequest(RequestEvent re) {
        Iterator iter = engines.entrySet().iterator();
        String s[];
        String reason = re.getActionContextReason();
        
        while(iter.hasNext()) {
            PluginSPI entry = (PluginSPI)((Map.Entry)iter.next()).getValue();
            s = entry.getActionContextReasons();
            for(int i=0; i < s.length; i++) {
                if(s[i].equals(reason)) {
                    entry.sendRequest(re);
                    break;
                }
            }
        }
    }
}
