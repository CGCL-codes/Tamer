    public void updateMap(Map<String, String> mappa) throws DBAccessException {
        Connection userConnection = null;
        PreparedStatement userPs = null;
        try {
            userConnection = userDataSource.getRoutedConnection(username);
            userConnection.setAutoCommit(false);
            userPs = userConnection.prepareStatement(Query.DELETE_ITEMS);
            userPs.setLong(1, getPrincipal());
            userPs.setString(2, getSourceUri());
            userPs.executeUpdate();
            String GUID = null;
            userPs = userConnection.prepareStatement(Query.INSERT_ITEMS);
            Iterator<String> itemKeys = mappa.keySet().iterator();
            while (itemKeys.hasNext()) {
                GUID = (String) itemKeys.next();
                if (GUID != null) {
                    userPs.setLong(1, getPrincipal());
                    userPs.setString(2, getSourceUri());
                    userPs.setString(3, "N");
                    userPs.setString(4, GUID);
                    userPs.setString(5, mappa.get(GUID));
                    userPs.executeUpdate();
                }
            }
            userConnection.commit();
        } catch (SQLException sqle) {
            try {
                if (userConnection != null) {
                    userConnection.rollback();
                }
            } catch (SQLException sqlee) {
                throw new DBAccessException("Error rollbacking", sqlee);
            }
            throw new DBAccessException("Error refreshing Local Items", sqle);
        } finally {
            try {
                DBTools.close(userConnection, userPs, null);
            } catch (Exception e) {
                throw new DBAccessException("Error setting ServerItems into DataBase (" + e.getMessage() + ")", e);
            }
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Refresh Cached N-U-D Items");
        }
    }
