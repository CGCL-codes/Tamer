    public void update(String[] power) {
        clearErr();
        DbConn conn = new DbConn();
        try {
            String sql = "";
            conn.setAutoCommit(false);
            sql = "update roleinfo set role_name = ?,show_order=?,role_desc=? where role_id = ?";
            conn.prepare(sql);
            conn.setString(1, getRoleName());
            conn.setInt(2, getShowOrder());
            conn.setString(3, getRoleDesc());
            conn.setString(4, getId());
            conn.executeUpdate();
            sql = "delete from rolepower where role_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.executeUpdate();
            if (power != null && power.length > 0) {
                for (int i = 0; i < power.length; i++) {
                    sql = "insert into rolepower (role_id,power_id) values (" + "?,?)";
                    conn.prepare(sql);
                    conn.setString(1, getId());
                    conn.setString(2, power[i]);
                    conn.executeUpdate();
                }
            }
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            setErr(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception subEx) {
                subEx.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }
