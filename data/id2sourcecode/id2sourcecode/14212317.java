    @Override
    public void insertItems(List<ImageItem> toInsert) throws DatabaseException {
        if (toInsert == null) throw new NullPointerException("toInsert");
        try {
            getConnection().setAutoCommit(false);
        } catch (SQLException e) {
            LOGGER.warn("Unable to set autocommit off", e);
        }
        try {
            int updated = 0;
            for (ImageItem item : toInsert) {
                getItemDeleteStatement(item.getIdentifier()).executeUpdate();
                updated += getItemInsertStatement(item).executeUpdate();
            }
            if (updated == toInsert.size()) {
                getConnection().commit();
                LOGGER.debug("DB has been updated.");
            } else {
                getConnection().rollback();
                LOGGER.error("DB has not been updated -> rollback!");
            }
        } catch (SQLException e) {
            LOGGER.error(e);
        } finally {
            closeConnection();
        }
    }
