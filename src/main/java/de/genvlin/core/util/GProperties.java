/*
 * GProperties.java
 *
 * Created on 13. März 2006, 16:13
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

import de.genvlin.core.plugin.Log;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;

/** This class holds all necessary information to store main things.
 * Use <pre>GProperties.getDefault()</pre> .
 * TODO: rewrite! + rethink about it!
 *
 * @author Peter Karich
 */
public class GProperties {
    
    private Hashtable data;
    private HashSet listeners;
    
    /** This variable contains all properties usable for genvlin, see GProperties class
     */
    static private GProperties properties;
    
    /** This method returns the singleton instance of <tt>GProperties</tt>.
     */
    static synchronized public GProperties getDefault() {
        if(properties == null)
            properties = new GProperties();
        
        return properties;
    }
    
    private HashSet getListeners() {
        if(listeners == null)
            listeners = new HashSet();
        
        return listeners;
    }
    
    /** This variable are use to know where to save data.<p>
     * In StartupDialog we try to load properties 1. from folder.user<p>
     * 2. if this fails, we create default properties from GProperties.createDefault()
     * method.
     */
    static public String PROJECT = "folder.project",
            USER = "folder.user";
    
    
    /** Creates a new instance of GProperties */
    private GProperties() {
        this(createDefault());
    }
    
    /** Creates a new instance of GProperties with initial rawData*/
    private GProperties(Hashtable rawData) {
        data = new Hashtable(rawData);
    }
    
    /** Creates a new instance of GProperties with initial properties*/
    private GProperties(GProperties properties) {
        this(properties.getHashtable());
    }
    
    /** This method returns the used Hashtable for storing all properties  */
    protected Hashtable getHashtable() {
        return data;
    }
    
    /** This method creates a default Hashtable for usage in GProperties
     * constructor.
     */
    static protected Hashtable createDefault() {
        Hashtable dData = new Hashtable();
        
        dData.put("/", System.getProperty("file.separator"));
        
        dData.put("file.plugins", "plugins.xml");
        
        
        Font font = new Font("Monospaced",Font.PLAIN, 14);
        
        dData.put("font", font);
        dData.put("font.out", font);
        dData.put("font.error", new Font(font.getFamily(), font.getStyle(), font.getSize()+2));
        String home = System.getProperty("user.home");
        String program = System.getProperty("user.dir");
        
        dData.put(PROJECT, new File(program + dData.get("/")+"project"));
        dData.put("folder.import", new File(home));
        
        dData.put("separator.col","\t");
        dData.put("separator.line","\n");
        
        dData.put("table.defaultrows",new Integer(50));
        dData.put("table.defaultcols",new Integer(2));
        
        /** for versioning purposes */
        dData.put("version","0.0.1");
        
        Locale loc = Locale.UK;
        //may be you want: Locale loc = Locale.getDefault();
        dData.put("locale", loc);
        //numberformat will NOT automatically created HERE, so:
        dData.put("numberformat", NumberFormat.getNumberInstance(loc));
        
        return dData;
    }
    
    Font tmpFont;
    
    /** This method writes the current properties to a String. This string could be invoked
     * from beanshell interpreter! the method loadProperties returns a tmpGProperty object.
     */
    public String toJavaSource() {
        throw new UnsupportedOperationException("Not yet implemented!");
        
/*        StringBuffer sb = new StringBuffer("public GProperties loadProperties()\n{ ");
        sb.append("GProperties tmpProperties = new GProperties();\n");
        Iterator iter = data.entrySet().iterator();
        Object obj;
        String key, value;
        Entry entry;
 
        while(iter.hasNext())
        {
            try
            {
                entry = (Entry)iter.next();
                key = (String)entry.getKey();
                obj = entry.getValue();
 
                if(obj instanceof File)
                {
                    value = "new File("+ s(obj.toString()) +")";
                }
                else if(obj instanceof Font)
                {
                    tmpFont = (Font)obj;
                    value = "new Font("+ s(tmpFont.getName())+","
                    + i(tmpFont.getStyle())+ "," + i(tmpFont.getSize())+")";
                }
                else if(obj instanceof Locale)
                {
                    value = "new Locale("+ s(((Locale)obj).getDisplayName()) +")";
                }
                else if(obj instanceof Integer)
                {
                    value = "new Integer("+ i(((Number)obj).intValue()) +")";
                }
                else if(obj instanceof NumberFormat)
                {
                    //Numberformat will added if we use properties.put("locale");
                    continue;
                }
                else
                    value = s((String)obj);
 
            }
            catch(ClassCastException cce)
            {
                System.out.println("Only some few classes are implemented in GProperties.toJavaSource()");
                cce.printStackTrace();
                continue;
            }
 
            sb.append("tmpProperties.put("+s(key)+", "+value+");\n");
        }
 
        sb.append("\nreturn tmpProperties;\n}");
 
        return sb.toString();
 */
    }
    
    /** This method returns a readable string from a real string.
     * E.g. all \? will returned as \\?.
     */
    //TODO unsused!
    static private String s(String s) {
        if(s == null || s.length() == 0) return "\"\"";
        //this wiont work??: s.replaceAll("\\","\\\\");
        char c;
        StringBuffer sb = new StringBuffer();
        for(int i=0; i < s.length(); i++) {
            c = s.charAt(i);
            switch(c) {
                case '\n':
                    sb.append("\\n"); break;
                case '\t':
                    sb.append("\\t"); break;
                case '\b':
                    sb.append("\\b"); break;
                case '\\'://windows files
                    sb.append("\\\\"); break;
                default:
                    sb.append(c);
            }
        }
        
        return "\""+sb.toString()+"\"";
    }
    
    /** Returns an integer as string
     */
    static public String i(int i) {
        return ""+i;
    }
    
    /** This method sets a specific value to key.
     * Available keys: font (Font), font.error (Font), folder.project (File),
     * folder.import (File), separator.col (String),
     * separator.line (String), locale (Locale), numberformat (NumberFormat),
     * filename.properties (String), filename.history (String), filename.datapool (String),
     * / (String) == the systems file separator,
     * table.defaultcols, table.defaultrows<p>
     * But do not SET numberformat because this will be done if you set locale<p>
     * Be sure if you create new key that they wont have capital letters.
     * And make use of dots '.'
     * @return oldValue
     */
    public Object put(String key, Object value) {
        //todo: how to make put() type-secure? works the following?
        //if(data.get(key).getClass().isInstance(value))
        
        //if somebody changes locale this has effects on numberformat
        if(key.equals("locale")) {
            try {
                NumberFormat nf = NumberFormat.getNumberInstance((Locale)value);
                _put("numberformat", nf);
            } catch(ClassCastException exc) {
                Log.log("GProperties.put: Value of locale should be from class Locale!", false);
            }
        }
        //if somebody make changes on font -> font.error.size will changed too!
        else if(key.equals("font")) {
            Font font = (Font)get("font.error");
            if(font != null) {
                _put("font.error",new Font(font.getName(), font.getStyle(), font.getSize()+2));
            }
        }
        return _put(key, value);
    }
    
    private Object _put(String key, Object value) {
        firePropertyChanged(key, get(key), value);
        return data.put(key, value);
    }
    
    /** This method returns a specific value from @param key.
     * Available keys: font, font.error, folder.project,
     * folder.import, separator.col, separator.line, locale, numberformat,
     * filename.properties, filename.history, filename.datapool,
     * table.defaultcols, table.defaultrows<p>
     * @return oldValue
     * @see put for more detail
     */
    public Object get(String key) {
        return data.get(key);
    }
    
    public GProperties load() throws FileNotFoundException, IOException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
    
    public boolean save() throws IOException {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
    
    public File getPropertiesFile() {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
    
    public void addPropertyChangeListener(PropertyChangeListener cl) {
        getListeners().add(cl);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener cl) {
        getListeners().remove(cl);
    }
    
    /** TODO: Performance is bad if many properties will changed.
     * This could be solved via running in a thread and collecting events,
     * before firing??
     */
    private void firePropertyChanged(String propName, Object oldVal,
            Object newVal) {
        Iterator iter = getListeners().iterator();
        PropertyChangeEvent pce =
                new PropertyChangeEvent(this, propName, oldVal, newVal);
        
        while(iter.hasNext()) {
            ((PropertyChangeListener)iter.next()).propertyChange(pce);
        }
    }
}