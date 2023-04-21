    public static void releaseThreadConnection() {
        if (getInstance().isServerSide()) {
            Object con = null;
            con = ThreadLocalConnectionManager.getInstance().get();
            if (con != null) {
                ThreadLocalConnectionManager.getInstance().set(null);
                try {
                    if (JrConnector.class.isAssignableFrom(con.getClass())) {
                        ((JrConnection) con).releaseExplicite();
                    } else {
                        ((Connection) con).close();
                    }
                } catch (SQLException e) {
                    sLogger.log(Level.WARNING, e.getMessage(), e);
                }
                sLogger.fine("Released connection in thread:" + Thread.currentThread().getName());
            } else {
                sLogger.fine("Warn: Tx-Connection to close is already NULL!!! " + "Reasons: Tx was not used by any read or write access or you called release twice! " + "Thread:" + Thread.currentThread().getName());
            }
        } else {
            sLogger.fine("ReleaseThreadConnection ignored, because having singleton JDBC-Connection.");
        }
    }
