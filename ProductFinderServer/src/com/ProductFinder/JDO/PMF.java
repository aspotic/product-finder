package com.ProductFinder.JDO;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

/**
 * Creates a persistent data storage manager for use in the application
 * @author adam knox
 *
 */
public final class PMF {
    private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

    private PMF() {}
    
    public static PersistenceManagerFactory get() {
        return pmfInstance;
    }
}
