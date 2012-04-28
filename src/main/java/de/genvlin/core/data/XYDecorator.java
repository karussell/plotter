/*
 * XYDecorator.java
 *
 * Created on 27. Juli 2007, 13:10
 * This stands under Public domain
 */

package de.genvlin.core.data;

import de.genvlin.gui.plot.CoordinateSystem;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Peter Karich
 */
class XYDecorator extends AbstractCollection
        implements XYDataInterface, Serializable {
    
    private boolean hidden;
    private Color color;
    private List<XYDataEntry> decorators;
    private Font font;
    private int startX = 0;
    private int endX = 0;
    private AffineTransform rotTransform;
    private Font rotFont;
    
    XYDecorator(ID id) {
        super(id);
        decorators = new ArrayList<XYDataEntry>();
        rotTransform = AffineTransform.getRotateInstance(-Math.PI / 2.0);
        rotFont = new Font("Helvetica", Font.PLAIN, 17);
        rotFont = rotFont.deriveFont(rotTransform);
    }
    
    public int size() {
        return decorators.size();
    }
    
    public Iterator<XYDataEntry> iterator() {
        return decorators.iterator();
    }
    
    public void clear() {
        decorators.clear();
    }
    
    public boolean remove(int index) {
        decorators.remove(index);
        return true;
    }
    
    public boolean isHidden() {
        return hidden;
    }
    
    public void setHidden(boolean b) {
        hidden = b;
    }
    
    public void draw(Graphics g, CoordinateSystem coordSys) {
        //WARNING: if you change sth. here, change it appropriate there: XYData.draw
        if(isHidden()) {
            return;
        }
        Graphics2D g2 = (Graphics2D)g;
        AffineTransform original = g2.getTransform();
        
        g2.setColor(color);
        
        double tmpx, tmpy;
        int x, y;
        int i = startX;
        int vectorSize = Math.min(decorators.size(), endX);
        
        //set to whole plot range
        if(startX == endX) {
            i = 0;
            vectorSize = decorators.size();
        }
        Font origFont = g2.getFont();
        g2.setFont(rotFont);
        double xOffSet = rotFont.getStringBounds("Hallo", g2.getFontRenderContext()).getHeight() - 2;
        //new GlyphVector().getPixelBounds(g2.getFontRenderContext(), x, y);
        //new TextLayout("test", rotFont, g2.getFontRenderContext()).getPixelBounds(null, x, y);
        
        for(; i < vectorSize ; i++) {
            
            tmpx = decorators.get(i).getX();
            tmpy = decorators.get(i).getY();
            x = coordSys.xToWin(tmpx);
            y = coordSys.yToWin(tmpy);
            
            if(!coordSys.contains(tmpx, tmpy)) {
                continue;
            }
            g2.drawString(decorators.get(i).getObject().toString(), x + (int)xOffSet, y);
        }//for
        g2.setFont(origFont);
    }
    
    public Color getColor() {
        return color;
    }
    
    public void setColor(Color color) {
        this.color = color;
    }
    
    /**
     * Known properties:
     * "text.font"; instanceof Font; default is ?
     */
    public void setProperty(String propertyName, Object propertyValue) {
        if("text.font".equalsIgnoreCase(propertyName)) {
            font = (Font)propertyValue;
        } else {
            throw new IllegalArgumentException("Unsupported property name:" + propertyName);
        }
    }
    
    public void add(double x, double y, String string) throws ParseException {
        decorators.add(new XYDataEntry(x, y, string));
    }
    
    //TODO PERFORMANCE:
    public Number getMaxX() {
        double max = Double.NEGATIVE_INFINITY;
        int size = size();
        
        for(int i = 0; i < size; i++) {
            if(get(i) == null) {
                continue;
            }
            if(get(i).getX() > max) {
                max = get(i).getX();
            }
        }
        
        return max;
    }
    
    public Number getMinX() {
        double min = Double.POSITIVE_INFINITY;
        int size = size();
        
        for(int i = 0; i < size; i++) {
            if(get(i) == null) {
                continue;
            }
            if(get(i).getX() < min) {
                min = get(i).getX();
            }
        }
        
        return min;
    }
    
    public Number getMaxY() {
        double max = Double.NEGATIVE_INFINITY;
        int size = size();
        
        for(int i = 0; i < size; i++) {
            if(get(i) == null) {
                continue;
            }
            if(get(i).getY() > max) {
                max = get(i).getY();
            }
        }
        
        return max;
    }
    
    public Number getMinY() {
        double min = Double.POSITIVE_INFINITY;
        int size = size();
        
        for(int i = 0; i < size; i++) {
            if(get(i) == null) {
                continue;
            }
            if(get(i).getY() < min) {
                min = get(i).getY();
            }
        }
        
        return min;
    }
    
    public XYDataEntry get(int i) {
        return decorators.get(i);
    }
    
    public void setPlotRange(int lowX, int hightX) {
        startX = lowX;
        endX = hightX;
    }
}