package org.xito.boot;

import java.net.URL;

/**
 * CacheResource contains a URL to a resource and a cache policy for how how the resource
 * should be cached
 *
 * @author Deane Richan
 */
public class CacheResource {

    protected URL url;

    protected CachePolicy cachePolicy;

    /**
    * CacheResource for URL. Policy will be CachePolicy.ALWAYS
    * @param url
    */
    public CacheResource(URL url) {
        this(url, null);
    }

    /**
    * CacheResource for URL and policy
    * @param url or resource
    * @param policy to use for caching, if null uses CachePolicy.ALWAYS
    */
    public CacheResource(URL url, CachePolicy policy) {
        this.url = url;
        if (policy == null) policy = CachePolicy.ALWAYS;
        this.cachePolicy = policy;
    }

    /**
    * Get the URL
    * @return url
    */
    public URL getUrl() {
        return url;
    }

    /**
    * Get the Cache Policy
    * @return policy
    */
    public CachePolicy getCachePolicy() {
        return cachePolicy;
    }
}
