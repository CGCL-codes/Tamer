    public void rollback(Xid xid) throws XAException {
        if (logger.logDebug()) debug("rolling back xid = " + xid);
        try {
            if (currentXid != null && xid.equals(currentXid)) {
                state = STATE_IDLE;
                currentXid = null;
                conn.rollback();
                conn.setAutoCommit(true);
            } else {
                String s = RecoveredXid.xidToString(xid);
                conn.setAutoCommit(true);
                Statement stmt = conn.createStatement();
                try {
                    stmt.executeUpdate("ROLLBACK PREPARED '" + s + "'");
                } finally {
                    stmt.close();
                }
            }
        } catch (SQLException ex) {
            throw new PGXAException(GT.tr("Error rolling back prepared transaction"), ex, XAException.XAER_RMERR);
        }
    }
