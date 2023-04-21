    public void delete(String name, int row) throws FidoDatabaseException {
        try {
            Connection conn = null;
            Statement stmt = null;
            try {
                String sql = "delete from ProperNouns where Noun = '" + name + "' and SenseNumber = " + row;
                conn = fido.util.FidoDataSource.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.createStatement();
                int max = findMaxRank(stmt, name);
                stmt.executeUpdate(sql);
                for (int i = row; i < max; ++i) {
                    stmt.executeUpdate("update ProperNouns set SenseNumber = " + i + " where SenseNumber = " + (i + 1) + " and Noun = '" + name + "'");
                }
                conn.commit();
            } catch (SQLException e) {
                if (conn != null) conn.rollback();
                throw e;
            } finally {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            }
        } catch (SQLException e) {
            throw new FidoDatabaseException(e);
        }
    }
