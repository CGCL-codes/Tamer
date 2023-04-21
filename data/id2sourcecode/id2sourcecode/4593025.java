    void setSetting(String var, String val) {
        boolean autoCommit = true;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            settingsRmQry.setString(1, var);
            settingsRmQry.executeUpdate();
            settingsInsQry.setString(1, var);
            settingsInsQry.setString(2, val);
            settingsInsQry.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error(SQL_ERROR, e1);
            }
            LOG.error(SQL_ERROR, e);
        } finally {
            try {
                conn.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                LOG.error(SQL_ERROR, e);
            }
        }
    }
