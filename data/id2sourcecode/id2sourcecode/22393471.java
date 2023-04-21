    public static void saveTabs(String[] tabIds, String user) throws Exception {
        if (tabIds == null) return;
        Db db = null;
        String sql = "";
        Connection conn = null;
        try {
            db = new Db();
            conn = db.getConnection();
            conn.setAutoCommit(false);
            int seq = 0;
            for (String tabId : tabIds) {
                seq = seq + 1;
                sql = "update tab_user set sequence = " + seq + " where tab_id = '" + tabId + "' and user_login = '" + user + "'";
                db.getStatement().executeUpdate(sql);
            }
            conn.commit();
        } catch (SQLException sqex) {
            try {
                conn.rollback();
            } catch (SQLException rollex) {
            }
            throw sqex;
        } finally {
            if (db != null) db.close();
        }
    }
