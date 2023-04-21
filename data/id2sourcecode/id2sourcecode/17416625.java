    public void inputPower(String[] sqlInfo) {
        if (sqlInfo == null || sqlInfo.length == 0) return;
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            String sql = "delete from t_power_info";
            conn.prepare(sql);
            conn.executeUpdate();
            for (int i = 0; i < sqlInfo.length; i++) {
                conn.prepare(sqlInfo[i]);
                conn.executeUpdate();
            }
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (Exception sex) {
                sex.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }
