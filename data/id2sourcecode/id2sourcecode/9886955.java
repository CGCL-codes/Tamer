    private boolean deregisterSubscription(String convID) throws SQLException {
        Connection conn = getConnectionWrapper().getConnection();
        try {
            PreparedStatements pss = getPreparedStatements();
            pss.stm_delSubscription.setString(1, convID);
            int rowCount = pss.stm_delSubscription.executeUpdate();
            conn.commit();
            return (rowCount != 0);
        } catch (SQLException sqle) {
            try {
                conn.rollback();
            } catch (SQLException se) {
                logger.log(Logger.SEVERE, "Rollback for incomplete un-subscription failed.", se);
            }
            throw sqle;
        }
    }
