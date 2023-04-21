    protected void finalizeUpdate(DBConnection theConnection) throws DBException {
        boolean alreadyInTransaction = !(theConnection.getAutoCommit());
        boolean success = false;
        if (!alreadyInTransaction && theConnection.supportsTransactions()) {
            if (log.isDebugEnabled()) {
                log.debug("Turning off auto-commit");
            }
            theConnection.setAutoCommit(false);
        }
        try {
            theConnection.executeUpdate(null);
            success = true;
        } finally {
            if (success == false) {
                if (!alreadyInTransaction && theConnection.supportsTransactions()) {
                    if (log.isDebugEnabled()) {
                        log.debug("rolling back");
                    }
                    theConnection.rollback();
                }
            }
            if (!alreadyInTransaction && theConnection.supportsTransactions()) {
                if (log.isDebugEnabled()) {
                    log.debug("Finishing commit and turning auto-commit back to true");
                }
                theConnection.commit();
                theConnection.setAutoCommit(true);
            }
        }
    }
