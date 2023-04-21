package org.teaframework.services.cache;

import java.util.Properties;
import org.teaframework.exception.CacheServiceException;

/**
 * <p>
 * JCSCacheService for pluggable cache.
 * </p>
 * 
 * @author <a href="mailto:founder_chen@yahoo.com.cn">Peter Cheng </a>
 * @version $Revision: 1.1 $ $Date: 2005/05/22 06:48:52 $
 * @version Revision: 1.0
 */
public class JCSCacheServiceProvider implements CacheServiceProvider {

    /**
     * @see org.teaframework.services.cache.CacheServiceProvider#buildCache(java.lang.String,
     *      java.util.Properties)
     */
    public CacheService buildCacheService(String regionName, Properties properties) throws CacheServiceException {
        return new JCSCacheServiceImpl(regionName);
    }
}
