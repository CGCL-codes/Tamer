    public void restoreProfile() throws InvalidStateException, ManagementException {
        if (logger.isDebugEnabled()) {
            logger.debug("restoreProfile() on: " + profileName + ", from table:" + profileTable.getProfileTableName());
        }
        if (!isProfileWriteable()) {
            throw new InvalidStateException("The restoreProfile method must throw a javax.slee.InvalidStateException if the Profile MBean object is not in the read-write state.");
        }
        final SleeTransactionManager txManager = sleeContainer.getTransactionManager();
        try {
            txManager.resume(transaction);
            txManager.rollback();
            transaction = null;
            readMode();
        } catch (Throwable e) {
            throw new ManagementException(e.getMessage(), e);
        }
    }
