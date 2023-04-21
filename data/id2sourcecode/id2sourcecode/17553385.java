    @Override
    public boolean put(RecentlyModifiedItem toPut, String openID) throws DatabaseException {
        if (toPut == null) throw new NullPointerException("toPut");
        if (toPut.getUuid() == null || "".equals(toPut.getUuid())) throw new NullPointerException("toPut.getUuid()");
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.warn("Unable to set autocommit off", e);
        }
        boolean found = true;
        try {
            PreparedStatement findSt = getConnection().prepareStatement(FIND_ITEM_STATEMENT);
            PreparedStatement statement = null;
            findSt.setString(1, toPut.getUuid());
            findSt.setString(2, openID);
            ResultSet rs = findSt.executeQuery();
            found = rs.next();
            int modified = 0;
            if (found) {
                int id = rs.getInt(1);
                statement = getConnection().prepareStatement(UPDATE_ITEM_STATEMENT);
                statement.setInt(1, id);
                modified = statement.executeUpdate();
            } else {
                statement = getConnection().prepareStatement(INSERT_ITEM_STATEMENT);
                statement.setString(1, toPut.getUuid());
                statement.setString(2, toPut.getName() == null ? "" : ClientUtils.trimLabel(toPut.getName(), Constants.MAX_LABEL_LENGTH));
                statement.setString(3, toPut.getDescription() == null ? "" : toPut.getDescription());
                statement.setInt(4, toPut.getModel().ordinal());
                statement.setString(5, openID);
                modified = statement.executeUpdate();
            }
            if (modified == 1) {
                getConnection().commit();
                LOGGER.debug("DB has been updated. Queries: \"" + findSt + "\" and \"" + statement + "\"");
            } else {
                getConnection().rollback();
                LOGGER.error("DB has not been updated -> rollback! Queries: \"" + findSt + "\" and \"" + statement + "\"");
                found = false;
            }
        } catch (SQLException e) {
            LOGGER.error(e);
            found = false;
        } finally {
            closeConnection();
        }
        return found;
    }
