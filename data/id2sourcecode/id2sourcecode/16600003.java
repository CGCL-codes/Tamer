    public void delete() {
        DbConn conn = new DbConn();
        try {
            String sql = "select * from userinfo where role_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            DbRs rs = conn.executeQuery();
            if (rs != null && rs.size() > 0) {
                setErr("此角色已经创建了用户不能删除！");
                return;
            }
            conn.setAutoCommit(false);
            sql = "delete from roleinfo where role_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.executeUpdate();
            sql = "delete from rolepower where role_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            setErr(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception sex) {
                sex.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }
