    public static void resetTransmisions() throws Exception {
        DBConnectionManager dbm = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtResetChunks = null;
        String SQL = "UPDATE EDONKEYFILE SET STATE=? WHERE STATE<>?";
        String SQL2 = "UPDATE CHUNK SET SENT=NULL WHERE SENT IS NOT NULL";
        try {
            dbm = DBConnectionManager.getInstance();
            conn = dbm.getConnection("satmule");
            stmt = conn.prepareStatement(SQL);
            stmt.setInt(1, Efile.STATE_UNKNOWN);
            stmt.setInt(2, Efile.STATE_UNKNOWN);
            stmtResetChunks = conn.prepareStatement(SQL2);
            stmt.executeUpdate();
            stmtResetChunks.executeUpdate();
            conn.commit();
            stmt.close();
            stmtResetChunks.close();
            dbm.freeConnection("satmule", conn);
        } catch (Exception e) {
            log.error("SQL error: " + SQL, e);
            Exception excep;
            if (dbm == null) excep = new Exception("Could not obtain pool object DbConnectionManager"); else if (conn == null) excep = new Exception("The Db connection pool could not obtain a database connection"); else {
                conn.rollback();
                excep = new Exception("SQL Error : " + SQL + " error: " + e);
                dbm.freeConnection("satmule", conn);
            }
            throw excep;
        }
    }
