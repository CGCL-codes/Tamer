    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            String sql = "";
            DbRs rs = null;
            sql = "select * from pal where taskid = ?";
            conn.prepare(sql);
            conn.setInt(1, getTaskid());
            rs = conn.executeQuery();
            if (rs != null && rs.size() > 0) {
                setErr("此工单已经做过损益分析");
                return;
            }
            sql = "insert into pal(taskid," + "createman,y,m,monthin) values(?, ?,?,?,?)";
            conn.prepare(sql);
            conn.setInt(1, getTaskid());
            conn.setString(2, getCreateman());
            conn.setInt(3, getY());
            conn.setInt(4, getM());
            conn.setInt(5, getMonthin());
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
