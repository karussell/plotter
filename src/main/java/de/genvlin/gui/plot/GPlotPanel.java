/*
 * GPlotPanel.java
 *
 * Created on 27. Juni 2004, 00:12
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
package de.genvlin.gui.plot;

import de.genvlin.core.data.AbstractCollection;
import de.genvlin.core.data.CollectionEvent;
import de.genvlin.core.data.CollectionListener;
import de.genvlin.core.data.ID;
import de.genvlin.core.data.MainPool;
import de.genvlin.core.data.VectorInterface;
import de.genvlin.core.data.XYInterface;
import de.genvlin.core.data.XYPool;
import de.genvlin.core.util.GProperties;
import de.genvlin.core.plugin.Log;
import de.genvlin.core.plugin.Platform;
import de.genvlin.core.plugin.PluginPool;
import de.genvlin.gui.util.GMouseAdapter;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Locale;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * The main class for plotting the data in an extra panel. Its a very simple implemention of a plot
 * class!!
 *
 * @author Peter Karich
 */
public class GPlotPanel extends JPanel
        implements ActionListener, CollectionListener {

    static final long serialVersionUID = 432233244;
    public static int DISTANCE = 0, ZOOM = 1, NORMAL = 2;
    private Color axesColor = Color.black, background = Color.white;
    private Color dataColor[] = {new Color(0.0f, 0.0f, 0.5f),
        new Color(0.0f, 0.5f, 0.0f),
        new Color(0.5f, 0.0f, 0.0f),
        new Color(0.5f, 0.5f, 0.0f),
        new Color(0.0f, 0.5f, 0.5f),
        new Color(0.5f, 0.0f, 0.5f),
        new Color(0.5f, 0.5f, 0.5f)
    };
    //private ArrayList data = new ArrayList(10);//ArrayList<XYData>
    //private boolean drawDistance;
    private CoordinateSystem defaultCS;
    private CoordinateSystem cSys;
    private PlotML plotML;
    private int automaticScaleDataNo = -1;
    private PopupSupportImpl popupSupport;
    private XYPool pool;

    public GPlotPanel(XYPool pool) {
        if (pool == null) {
            throw new UnsupportedOperationException("param pool shouldn't be null!");
        }

        this.pool = pool;
        reloadPureXYInterfaceToXYData(0, pool.size());
        pool.addVectorListener(this);
        setName("plot:" + pool.getID());
        setToolTipText("<html>Measure distance per Mouse dragging.<br>"
                + "Zoom with SHIFT + drag.<br>Translate with CTRL + drag.</html>");
        //setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        cSys = getDefaultCoordinateSystem();
        cSys.setColor(axesColor);
        cSys.setBackground(background);

        setBackground(background);

        plotML = new PlotML();
        addMouseListener(plotML);
        addMouseMotionListener(plotML);
        cSys.setWinBounds(getBounds());//this avoids null pointer exceptions
    }

    public GPlotPanel() {
        this((XYPool) MainPool.getDefault().create(XYPool.class));
    }

    /**
     * @return the standard coordinate bounds of the real coordinate and set the locale to default
     * locale
     */
    public CoordinateSystem getDefaultCoordinateSystem() {
        defaultCS = new CoordinateSystem(+10, +30, 0.5, 0.5);
        defaultCS.setLocale((Locale) GProperties.getDefault().get("locale"));
        return defaultCS;
    }

    /**
     * This method sets the locale (=> and the numberformat).
     */
    public void setLocale(Locale locale) {
        cSys.setLocale(locale);
    }
    int tmp, x1, x2, y1, y2;

    /**
     * Overload JPanel.paint
     */
    public void paint(Graphics g) {
        super.paint(g);
        cSys.setWinBounds(getBounds());

        //now automatic scaling for only one time!!
        automaticScale(automaticScaleDataNo);
        automaticScaleDataNo = -1;

        cSys.drawAxes(g);
        //plotFunction(g);
        plotData(g);

        cSys.drawMousePosition(g, plotML.currPoint);
        if (plotML.isDistancing())
            cSys.drawMouseDistance(g, plotML.distPoint, plotML.currPoint);
        if (plotML.isZooming()) {
            x1 = plotML.zoomPoint.x;
            y1 = plotML.zoomPoint.y;
            x2 = plotML.currPoint.x;
            y2 = plotML.currPoint.y;
            if (x2 < x1) {
                tmp = x1;
                x1 = x2;
                x2 = tmp;
            }
            if (y2 < y1) {
                tmp = y1;
                y1 = y2;
                y2 = tmp;
            }

            g.drawRect(x1, y1, x2 - x1, y2 - y1);
        }
    }
    /*
     * public void addFunction() { }
     *
     * public void plotFunction(Graphics g) { int a;
     *
     * g.setColor(functionColor); for(int i=0; i<cB.dX(); i++) { a=yToWin(fkt(xToCoord(i)));
     * //Log.log("a "+a+"; fkt(xToCoord(i)) "+fkt(xToCoord(i)) +"; xToCoord(i) "+xToCoord(i)+"; i
     * "+i); xyPoint.draw(g, i, a); } }
     */
    int colorDataIndex = 0;

    /**
     * which dataset has which color??
     */
    public Color getColor(int index) {
        return ((XYData) pool.get(index)).getColor();
    }

    /**
     * This method adds a plot(with x AND y values) to the window
     *
     * public void addData(double x[], double y[]) { int color = colorDataIndex; color %=
     * dataColor.length; data.add(new XYData(x, y, XYData.CROSS, dataColor[color], data.size()+""));
     *
     * colorDataIndex++; }
     */
    /**
     * This method adds specified data to panel
     *
     * public void addData(XYData xyData) { int color = colorDataIndex;
     * automaticOneScale(colorDataIndex); color %= dataColor.length;
     * xyData.setColor(dataColor[color]); xyData.setDotType(XYData.CROSS);
     * xyData.setName(data.size()+""); data.add(xyData); colorDataIndex++; }
     */
    /**
     * This method adds specified data to panel
     */
    public void addData(VectorInterface xVector, VectorInterface yVector) {
        addData(new XYData(xVector, yVector));
    }

    /**
     * This method adds specified data to panel
     */
    public void addData(XYInterface xyVector) {
        if (xyVector instanceof XYData)
            addData((XYData) xyVector);
        else
            addData(new XYData(xyVector));
    }

    public void addData(XYData xyData) {
        int color = colorDataIndex;
        automaticOneScale(colorDataIndex);
        color %= dataColor.length;
        xyData.setColor(dataColor[color]);
        xyData.setDotType(XYData.CROSS);
        pool.add(xyData);
        colorDataIndex++;
    }

    /**
     * This method removes a plot from the plotwindow
     */
    public void removeData(int i) {
        pool.remove(i);
    }
    int xOff, yOff;

    /**
     * This method plots all datatables to the window. todo: performance!!!
     */
    private void plotData(Graphics g) {
        xOff = cSys.getYAxis().getXOffset();
        yOff = cSys.getXAxis().getYOffset();
        g.setClip(xOff, yOff, cSys.winWidth() - 2 * xOff, cSys.winHeight() - 2 * yOff);

        for (int c = 0; c < pool.size(); c++) {
            ((XYData) pool.get(c)).draw(g, cSys);
        }
        //reset clipping area:
        g.setClip(0, 0, cSys.winWidth(), cSys.winHeight());
    }
    double xTranslate = 1, yTranslate = 1;

    /**
     * This method sets the mouse translate factor
     */
    public void setTranslateFactor(double xFactor, double yFactor) {
        xTranslate = yFactor;
        yTranslate = yFactor;
    }

    /**
     * This method scales the window so that we can see the specified data * (in the most cases)
     * very good.
     */
    protected void automaticScale(int index) {
        if (pool.size() == 0 || index >= pool.size() || index < 0)
            return;

        XYData xyData = (XYData) pool.get(index);

        cSys.automaticScale(xyData.getMinX(), xyData.getMaxX(),
                xyData.getMinY(), xyData.getMaxY());
        repaint();
    }

    /**
     * This method scales the window so that we can see the last added data * (in the most cases)
     * very good.
     */
    protected void automaticScale() {
        automaticScale(pool.size() - 1);
    }

    /**
     * This method sets the data in this plotpanel which should scaled automatically for one time.
     * -1 indicates that no scaling will happen.
     */
    public void automaticOneScale(int dataNo) {
        automaticScaleDataNo = dataNo;
    }

    /**
     * The MouseListener provides distance measurement via dragging, zooming via drag+shift and
     * popupmenu displaying.
     */
    private class PlotML extends GMouseAdapter implements MouseMotionListener, Serializable {

        int clickButton = -1;
        public Point changeVector = new Point();
        public Point clickPoint = new Point();
        public Point currPoint = new Point();
        public Point zoomPoint = new Point();
        public Point distPoint = new Point();
        int state = NORMAL;
        double tmpX, tmpY, xP, yP;

        public boolean isZooming() {
            return state == ZOOM;
        }

        public boolean isDistancing() {
            return state == DISTANCE;
        }

        public boolean isNormal() {
            return state == NORMAL;
        }

        public void gMousePressed(MouseEvent me) {
            clickPoint.x = me.getPoint().x;
            clickPoint.y = me.getPoint().y;

            if (me.isShiftDown() && clickButton == me.BUTTON1) {
                zoomPoint.x = me.getPoint().x;
                zoomPoint.y = me.getPoint().y;

                state = ZOOM;
                cSys.setZoomStart(zoomPoint);
            } else {
                state = DISTANCE;

                distPoint.x = me.getPoint().x;
                distPoint.y = me.getPoint().y;
            }
            clickButton = me.getButton();
        }

        public void gMouseReleased(MouseEvent me) {
            if (isZooming()) {
                cSys.zoom(currPoint.x, currPoint.y);
                repaint();
            }
            state = NORMAL;
        }

        public void mouseMoved(MouseEvent me) {
            currPoint.x = me.getPoint().x;
            currPoint.y = me.getPoint().y;
            repaint();
        }

        public void mouseDragged(MouseEvent me) {
            currPoint.x = me.getPoint().x;
            currPoint.y = me.getPoint().y;
            changeVector.x = currPoint.x - clickPoint.x;
            changeVector.y = currPoint.y - clickPoint.y;

            //for the next call we need for zoom/translate
            clickPoint.x = currPoint.x;
            clickPoint.y = currPoint.y;

            if (me.isControlDown() && clickButton == MouseEvent.BUTTON1) {
                tmpX = -changeVector.x * xTranslate;
                tmpY = changeVector.y * yTranslate;

                cSys.translate(Math.round(tmpX) * 2, Math.round(tmpY) * 2);
                repaint();
            } else if (!isNormal()) {
                repaint();
            }
        }

        public void showPopup(MouseEvent me) {
            if (popupSupport == null)
                popupSupport = new PopupSupportImpl(me);
            else
                popupSupport.setMouseEvent(me);

            PluginPool.getDefault().getPlatform().showPopup(popupSupport);
        }
    }//PlotML

    protected class PopupSupportImpl implements Platform.PopupSupport {

        public PopupSupportImpl(MouseEvent me) {
            setMouseEvent(me);
        }

        public JPopupMenu createPopup() {
            return GPlotPanel.this.createPopup(me);
        }

        private void setMouseEvent(MouseEvent me) {
            this.me = me;
        }
        MouseEvent me;

        public MouseEvent getMouseEvent() {
            return me;
        }

        public boolean isPopupAllowed() {
            return true;
        }
    }

    /**
     * This will create the context sensitive popup menu on this plot panel.
     */
    public JPopupMenu createPopup(MouseEvent me) {
        JPopupMenu popMenu = new JPopupMenu();
        JMenuItem mi;

        for (int i = 0; i < pool.size(); i++) {
            JMenu menu = new JMenu(i + " " + pool.get(i));
            menu.setForeground(getColor(i));

            mi = new JMenuItem("Automatic scaling");
            mi.addActionListener(this);
            menu.add(mi);

            mi = new JMenuItem("Remove");
            mi.addActionListener(this);
            menu.add(mi);
            popMenu.add(menu);
        }
        return popMenu;
    }

    /**
     * This method will be called after popupMenu selection
     */
    public void actionPerformed(ActionEvent ae) {
        String str = ae.getActionCommand();

        int i = -1;
        try {
            i = Integer.parseInt(str.substring(str.indexOf(':') + 1));
        } catch (NumberFormatException nfe) {
            return;
        }

        if (str.startsWith("Automatic scaling:")) {
            automaticScale(i);
        } else
            removeData(i);

        repaint();
    }

    private boolean reloadPureXYInterfaceToXYData(ID id) {
        XYInterface data;
        try {
            data = (XYInterface) pool.get(id);
        } catch (ClassCastException cce) {
            Log.err("Cant'add data with id " + id.toString() + " to pool.", false);
            return false;
        }
        pool.removeVectorListener(this);
        pool.remove(id);
        addData(data);
        pool.addVectorListener(this);
        return true;
    }

    private boolean reloadPureXYInterfaceToXYData(int from, int to) {
        XYInterface data;
        for (int index = from; index < to; index++) {
            try {
                data = (XYInterface) pool.get(index);
            } catch (ClassCastException cce) {
                Log.err("Cant'add data with index " + index + " to pool.", false);
                return false;
            }
            pool.remove(index);
            addData(data);
        }
        return true;
    }

    public void vectorChanged(CollectionEvent ve) {
        if (ve.getProperty() == AbstractCollection.ADD_DATA
                || ve.getProperty() == AbstractCollection.ADD_SOME) {
            if (ve.getID() != null)
                reloadPureXYInterfaceToXYData(ve.getID());
            else
                reloadPureXYInterfaceToXYData(ve.getFrom(), ve.getTo());
        }
    }
    /*
     * //incompatible return types "void JComponent.remove(int)" public boolean remove(Comparable
     * id) { return pool.remove(id); }
     *
     * public ID getID() { return pool.getID(); }
     *
     *
     * public IDData get(Comparable id) { return pool.get(id); }
     *
     * public IDData get(int index) { return pool.get(index); }
     *
     * public int indexOf(Comparable id) { return pool.indexOf(id); }
     *
     * public Iterator iterator() { return pool.iterator(); }
     *
     * public String getInfo() { return pool.getInfo(); }
     *
     * public String getTitle() { return pool.getTitle(); }
     *
     * public void setInfo(String info) { pool.setInfo(info); }
     *
     * public void setTitle(String title) { pool.setTitle(title); }
     *
     * public void addVectorListener(CollectionListener vl) { pool.addVectorListener(vl); }
     *
     * public void removeVectorListener(CollectionListener cl) { pool.removeVectorListener(cl); }
     *
     * public void clear() { pool.clear(); }
     *
     * public int compareTo(Object o) { return pool.compareTo(o); }
     */
}