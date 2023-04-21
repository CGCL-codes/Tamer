    public void delete(File file) throws XAException {
        if (lockedXid != null && lockedXid != xidThreadLocal.get()) {
            throw new XAException("This directory [" + directory + "] has been locked by anthoer thread, you can't write it");
        }
        lockedXid = xidThreadLocal.get();
        modifiedDataMapping.get(xidThreadLocal.get()).put(file, null);
    }
