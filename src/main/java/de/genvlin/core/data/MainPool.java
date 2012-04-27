/*
 * MainPool.java
 *
 * Created on 17. März 2006, 20:42
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
package de.genvlin.core.data;

import de.genvlin.core.math.LinearFunction;

/**
 * This is the factory of pool's, vector's and xyvector's. Or you specific IDData if you want
 * implement. Get the singleton instance via getDefault().
 *
 * @author Peter Karich
 */
public class MainPool extends Pool {

    /**
     * This is the singleton instance of this class!
     */
    static private MainPool single;
    /**
     * to create new id's we need a counter:
     */
    private int counter = 1;

    private MainPool() {
        super(new IntID(0));
    }

    /**
     * This method returns the singleton instance of this factory class.
     */
    static synchronized public MainPool getDefault() {
        if (single == null)
            single = new MainPool();

        return single;
    }

    /**
     * This method adds the specified pool.
     *
     * @return true if successfully imported
     */
    public boolean importPool(MainPool pool) {
        return false;
    }

    /**
     * This method returns a clone of specified object. All Object.clone() implementions should call
     * this factory method!
     */
    public Object clone(Object o) throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Not yet implemented!");
        //return null;
    }

    /**
     * Use {@link create(Class)} instead!
     *
     * public boolean add(Comparable com) { throw new UnsupportedOperationException("Please use
     * create(Class) instead!"); }
     */
    /**
     * This method will create a new entry. Known entries could be VectorPool, XYPool,
     * DoubleVectorInterface and VectorInterface. The specified class should implement IDData.
     *
     * @throws UnsupportedOperationException if class not supported!
     */
    public <T extends IDData> T create(Class<T> clazz) {
        /*
         * TODO try { if(!(Class.forName("de.genvlin.core.data.IDData").
         * isInstance(clazz.newInstance()))) throw new UnsupportedOperationException("The specified
         * class should " + "implement IDData."); } catch(ClassNotFoundException cnfe) {
         * }catch(InstantiationException ie) { }catch(IllegalAccessException iae) { }
         */

        if (clazz.getName().equals("de.genvlin.core.data.XYPool"))
            return (T) new XYPool(new IntID(counter));
        else if (clazz.getName().equals("de.genvlin.core.data.VectorPool"))
            return (T) new VectorPool(new IntID(counter));
        else if (clazz.getName().equals("de.genvlin.core.data.PoolInterface"))
            return (T) new Pool(new IntID(counter));
        else if (clazz.getName().equals("de.genvlin.core.data.VectorInterface"))
            return (T) new DataVector(new IntID(counter), null);
        else if (clazz.getName().equals("de.genvlin.core.data.DoubleVectorInterface"))
            return (T) new DoubleVector(new IntID(counter), null, 50, 100);
        else if (clazz.getName().equals("de.genvlin.core.math.LinearFunction"))
            //TODO?
            return (T) new LinearFunction(0, 0, new IntID(counter));
        else if (clazz.getName().equals("de.genvlin.core.data.XYVectorInterface"))
            throw new UnsupportedOperationException("use 'create(classX, classY)' instead");
        else if (clazz.getName().equals("de.genvlin.core.data.XYInterface"))
            throw new UnsupportedOperationException("use 'create(YourSpecialClass.class)' instead");
        else
            throw new UnsupportedOperationException("See javadoc which classes are available!");
    }

    /**
     * This method will create a new <tt>XYVectorInterface</tt>. Known entries could be
     * DoubleVectorInterface and VectorInterface. The specified classes should implement IDData.
     *
     * @see #create(VectorInterface, VectorInterface)
     * @throws UnsupportedOperationException if class not supported!
     */
    public IDData create(Class x, Class y) {
        return create((VectorInterface) create(x), (VectorInterface) create(y));
    }

    /**
     * This method will create a new <tt>XYVectorInterface</tt>. Known entries could be
     * DoubleVectorInterface and VectorInterface. The specified classes should implement IDData.
     *
     * @see #create(Class,Class)
     * @throws UnsupportedOperationException if class not supported!
     */
    public XYVectorInterface create(VectorInterface x, VectorInterface y) {
        return (XYVectorInterface) add(new XYVector(x, y, new IntID(counter)));
    }

    /**
     * This method should be called from all subclasses to let MainPool fullfill his
     * "factory"-contract.
     */
    final protected IDData add(IDData data) {
        counter++;
        super.add(data);
        return data;
    }

    /**
     * TODO: rethink about this here:<br> I hope you know that other instances may still hold
     * references to the vectors! So garbage collector couldn't remove them! This method will only
     * remove the direct access to them! The ID-Counter will not be reseted, because of possible
     * bugs! Please do only use, if you know what you do!
     */
    public void clear() {
        super.clear();
    }
}