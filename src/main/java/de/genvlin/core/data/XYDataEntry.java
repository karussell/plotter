/*
 * XYDataEntry.java
 *
 * Created on 27. Juli 2007, 13:18
 * This stands under Public domain
 */

package de.genvlin.core.data;

/**
 *
 * @author Peter Karich
 */
public class XYDataEntry {
    
    private double x;
    private double y;
    private Object o;
    
    public XYDataEntry(double x, double y, Object o) {
        this.x = x;
        this.y = y;
        this.o = o;
    }
    
    public Object getObject() {
        return o;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public void setObject(Object o) {
        this.o = o;
    }
    
    public void setX(double x) {
        this.x = x;
    }
    
    public void setY(double y) {
        this.y = y;
    }
    
    public String toString() {
        return "x:"+x+", y:"+y + ((o == null) ? "" : ", obj:"+o.toString());
    }

    public String toLineString() {
        return x + "\t" + y + ((o == null) ? "" : "\t"+o.toString());
    }
}
