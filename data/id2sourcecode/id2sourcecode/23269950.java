    public ObjectPool[] selectPool(T connection, V queryObject) {
        beforeSelectPool(connection, queryObject);
        StringBuffer loggerBuffer = null;
        boolean isRead = true;
        if (queryObject instanceof Request) {
            isRead = ((Request) queryObject).isRead();
        }
        if (logger.isDebugEnabled()) {
            loggerBuffer = new StringBuffer("query=");
            loggerBuffer.append(queryObject);
            if (queryObject instanceof Request) {
                loggerBuffer.append(((Request) queryObject).isPrepared() ? ",prepared=true" : "");
            }
        }
        List<String> poolNames = new ArrayList<String>();
        poolNames = evaluate(loggerBuffer, connection, queryObject);
        ObjectPool[] pools = new ObjectPool[poolNames.size()];
        int i = 0;
        for (String name : poolNames) {
            ObjectPool pool = ProxyRuntimeContext.getInstance().getPoolMap().get(name);
            if (pool == null) {
                logger.error("cannot found Pool=" + name + ",sqlObject=" + queryObject);
                throw new RuntimeException("cannot found Pool=" + name + ",sqlObject=" + queryObject);
            }
            pools[i++] = pool;
        }
        if (pools == null || pools.length == 0) {
            pools = (isRead ? this.readPools : this.writePools);
            if (logger.isDebugEnabled() && pools != null && pools.length > 0) {
                if (isRead) {
                    loggerBuffer.append(",  route to queryRouter readPool:" + readPool + "\r\n");
                } else {
                    loggerBuffer.append(",  route to queryRouter writePool:" + writePool + "\r\n");
                }
            }
            if (pools == null || pools.length == 0) {
                pools = this.defaultPools;
                if (logger.isDebugEnabled() && pools != null && pools.length > 0) {
                    loggerBuffer.append(",  route to queryRouter defaultPool:" + defaultPool + "\r\n");
                }
            }
        } else {
            if (logger.isDebugEnabled() && pools != null && pools.length > 0) {
                loggerBuffer.append(",  route to pools:" + poolNames + "\r\n");
            }
        }
        if (logger.isDebugEnabled()) {
            if (loggerBuffer != null) {
                logger.debug(loggerBuffer.toString());
            }
        }
        return pools;
    }
