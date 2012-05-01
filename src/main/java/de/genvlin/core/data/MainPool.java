/*
 * MainPool.java
 *
 * Created on 17. March 2006, 20:42
 * This stands under Public domain
 */

package de.genvlin.core.data;

/** This is the factory of pool's, vector's and xyvector's. Or you
 * specific IDData if you want implement. Get the singleton instance
 * via getDefault().
 * 
 * WARNING: not thread safe!
 *
 * @author Peter Karich
 */
public class MainPool extends Pool<IDData> {
    
    /** This is the singleton instance of this class!*/
    static private MainPool single;
    
    /** to create new id's we need a counter:*/
    protected int counter = 1;
    
    protected MainPool() {
        super(new IntID(0));
    }
    
    /** This method returns the singleton instance of this factory class.*/
    public static synchronized MainPool getDefault() {
        if(single == null) {
            single = new MainPool();
        }
        
        return single;
    }
    
    public static synchronized void setDefault(MainPool mainPool) {
        if(mainPool == null) {
            throw new NullPointerException();
        }
        
        single = mainPool;        
    }
    
    /**
     * This method adds the specified pool.
     * @return true if successfully imported
     */
    public boolean importPool(MainPool pool) {
        return false;
    }
    
    /**
     * This method returns a clone of specified object.
     * All Object.clone() implementions should call this factory method!
     */
    public Object clone(Object o) {
        throw new UnsupportedOperationException("Not yet implemented!");        
    }
    
    /**
     * This method creates a new IDData entry of this.
     *
     * Known entries could be HistogrammInterface, VectorPool, PoolInterface, 
     * XYPoolInterface, DoubleVectorInterface and VectorInterface.
     *
     * @throws UnsupportedOperationException if class unsupported!
     */
    @SuppressWarnings("unchecked")
    public <T extends VectorInterface> T createVector(Class<T> clazz) {
        IDData o;               
        
        if(clazz.equals(HistogrammInterface.class)) {
            o = new HistogrammVector(new IntID(counter));
        } else if(clazz.equals(VectorInterface.class)) {
            o = new DataVector(new IntID(counter), null);
        } else if(clazz.equals(DoubleVectorInterface.class)) {
            o = new DoubleVector(new IntID(counter), null, 50, 100);
        } else {
            throw new UnsupportedOperationException("Couldn't create the vector!");
        }
        
        return (T)addToPool(o);
    }
    
    /**
     * This method creates a new Pool.
     *
     * Known elements of the pool can be XYVectorInterface, VectorInterface and
     * XYDataInterface.
     *
     * @throws UnsupportedOperationException if class unsupported!
     */
    @SuppressWarnings("unchecked")
    public <T extends CollectionInterface> Pool<T> createPool(Class<T> elementsClass) {
        Pool pool;
        
        if(elementsClass.equals(XYVectorInterface.class)) {
            pool = new XYPool(new IntID(counter));
        } else if(elementsClass.equals(VectorInterface.class)) {
            pool = new VectorPool(new IntID(counter));
        } else if(elementsClass.equals(XYDataInterface.class)){
            pool = new Pool<XYDataInterface>(new IntID(counter));
        } else {
            throw new UnsupportedOperationException("Couldn't create the pool!");
        }
         
        return (Pool<T>)addToPool(pool);
    }
    
    /**
     * This method creates an implementation of the XYDataInterface for the
     * specified XYVectorInterface object.
     */
    public XYDataInterface createXYData(XYVectorInterface xyVector) {
        XYData d = new XYData(xyVector, new IntID(counter));
        addToPool(d);
        return d;
    }
    
    /**
     * This method creates a specific implementation of the XYDataInterface.
     * Known types are: "decorator"
     */
    public XYDataInterface createXYData(String type) {
        XYDataInterface d;
        
        if("decorator".equalsIgnoreCase(type)) {
            d = new XYDecorator(new IntID(counter));
        } else {
            throw new UnsupportedOperationException("Couldn't find an implementation of specified type:" + type);
        }
        
        addToPool(d);
        return d;
    }
    
    /**
     * This method creates an implementation of the XYDataInterface for the
     * specified vectors.
     */
    public XYDataInterface createXYData(VectorInterface xVector, VectorInterface yVector) {
        XYData d = new XYData(xVector, yVector, new IntID(counter));
        addToPool(d);
        
        return d;
    }
    
    /**
     * This method creates an implementation of the XYDataInterface for the
     * specified vectors.
     */
    public <S extends VectorInterface, T extends VectorInterface>
            XYDataInterface createXYData(Class<S> x, Class<T> y) {
        XYVectorInterface xyV = createXYVector(x, y);
        return createXYData(xyV);
    }
    
    
    /**
     * This method will create a new <tt>XYVectorInterface</tt>.
     * Known entries could be DoubleVectorInterface and VectorInterface.
     * The specified classes should implement IDData.
     *
     * @see #create(VectorInterface, VectorInterface)
     * @throws UnsupportedOperationException if class not supported!
     */
    public <S extends VectorInterface, T extends VectorInterface>
            XYVectorInterface createXYVector(Class<S> x, Class<T> y) {
        return createXYVector(createVector(x), createVector(y));
    }
    
    /**
     * This method will create a new <tt>XYVectorInterface</tt>.
     * Known entries could be DoubleVectorInterface and VectorInterface.
     * The specified classes should implement IDData.
     *
     * @see #create(Class,Class)
     * @throws UnsupportedOperationException if class not supported!
     */
    public XYVectorInterface createXYVector(VectorInterface x, VectorInterface y){
        return addToPool(new XYVector(x, y, new IntID(counter)));
    }
    
    /**
     * This method should be called from all subclasses to let MainPool
     * fullfill his "factory"-contract.
     */
    final protected <T extends IDData> T addToPool(T data){
        counter++;
        super.add(data);
        return data;
    }
    
    /**
     * Please: only use this method, if you know what you do!
     */
    public void clear() {
        super.clear();
    }     
}