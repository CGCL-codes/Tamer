    public void insert(String[] power) {
        clearErr();
        DbConn conn = new DbConn();
        try {
            String sql = "";
            sql = "select * from userinfo where role_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            DbRs rs = conn.executeQuery();
            if (rs != null && rs.size() > 0) {
                setErr("此角色已经存在！");
                return;
            }
            conn.setAutoCommit(false);
            sql = "insert into roleinfo (role_id,role_name,show_order,role_desc)" + " values (?,?,?,?)";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.setString(2, getRoleName());
            conn.setInt(3, getShowOrder());
            conn.setString(4, getRoleDesc());
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
