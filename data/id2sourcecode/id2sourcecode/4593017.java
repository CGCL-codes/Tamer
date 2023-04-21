    boolean addOverride(int airingId, String title, String subtitle, boolean active) {
        boolean autoCommit = false;
        try {
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            overrideRmQry.setInt(1, airingId);
            overrideRmQry.executeUpdate();
            overrideInsQry.setInt(1, airingId);
            overrideInsQry.setString(2, title);
            overrideInsQry.setString(3, subtitle);
            overrideInsQry.setBoolean(4, active);
            overrideInsQry.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                LOG.error(SQL_ERROR, e1);
            }
            LOG.error(SQL_ERROR, e);
            return false;
        } finally {
            try {
                conn.setAutoCommit(autoCommit);
            } catch (SQLException e) {
                LOG.error(SQL_ERROR, e);
            }
        }
        return true;
    }
