    public LockToken lock(com.bradmcevoy.http.LockInfo lock, User user, String uniqueId) {
        LockToken lt = new LockToken();
        String lockTokenStr = lock.owner + "-" + uniqueId;
        String lockToken = md5Encoder.encode(md5Helper.digest(lockTokenStr.getBytes()));
        resourceLocks.put(lockToken, lock);
        lt.tokenId = lockToken;
        lt.info = lock;
        lt.timeout = new LockTimeout(new Long(45));
        return lt;
    }
