    public static void save(ExperimentResultHasProperty e) throws NoConnectionToDBException, SQLException {
        if (e.isDeleted()) {
            PreparedStatement ps = DatabaseConnector.getInstance().getConn().prepareStatement(deleteValueQuery);
            ps.setInt(1, e.getId());
            ps.executeUpdate();
            ps = DatabaseConnector.getInstance().getConn().prepareStatement(deleteQuery);
            ps.setInt(1, e.getId());
            ps.executeUpdate();
            ps.close();
            cache.remove(e);
        } else if (e.isModified()) {
            boolean autocommit = DatabaseConnector.getInstance().getConn().getAutoCommit();
            try {
                DatabaseConnector.getInstance().getConn().setAutoCommit(false);
                PreparedStatement ps = DatabaseConnector.getInstance().getConn().prepareStatement(updateQuery);
                ps.setInt(1, e.getExpResId());
                ps.setInt(2, e.getPropId());
                ps.setInt(3, e.getId());
                ps.executeUpdate();
                ps.close();
                ps = DatabaseConnector.getInstance().getConn().prepareStatement(deleteValueQuery);
                ps.setInt(1, e.getId());
                ps.executeUpdate();
                ps.close();
                DatabaseConnector.getInstance().getConn().commit();
                for (int i = 0; i < e.getValue().size(); i++) {
                    ps = DatabaseConnector.getInstance().getConn().prepareStatement(insertValueQuery);
                    ps.setInt(1, e.getId());
                    ps.setString(2, e.getValue().get(i));
                    ps.setInt(3, i);
                    ps.execute();
                    ps.close();
                }
                e.setSaved();
            } catch (SQLException ex) {
                DatabaseConnector.getInstance().getConn().rollback();
                throw ex;
            } finally {
                DatabaseConnector.getInstance().getConn().setAutoCommit(autocommit);
            }
        } else if (e.isNew()) {
            PreparedStatement ps = DatabaseConnector.getInstance().getConn().prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, e.getExpResId());
            ps.setInt(2, e.getPropId());
            ps.executeUpdate();
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                e.setId(generatedKeys.getInt(1));
            }
            generatedKeys.close();
            ps.close();
            e.setSaved();
            cache.cache(e);
        }
    }
