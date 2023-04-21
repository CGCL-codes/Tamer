    public void insert() {
        DbRs rs = null;
        clearErr();
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            String sql = "select * from sevgrade where sgid = ?";
            conn.prepare(sql);
            conn.setInt(1, getSgid());
            rs = conn.executeQuery();
            if (rs != null && rs.size() > 0) {
                setErr("等级编号已经存在请重新输入");
                return;
            }
            sql = "insert into sevgrade(sgid,sgname)values(?,?)";
            conn.prepare(sql);
            conn.setInt(1, getSgid());
            conn.setString(2, getSgname());
            conn.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            setErr(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }
