    public void update() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            String sql = "";
            sql = "update companyinfo set comname =? ,comman=?,comaddress=?, comtel=? ,commantel=?,commanmail=?,comremark=?  where comid = ?";
            conn.prepare(sql);
            conn.setString(1, getComnam());
            conn.setString(2, getComman());
            conn.setString(3, getComaddress());
            conn.setString(4, getComtel());
            conn.setString(5, getCommantel());
            conn.setString(6, getCommanmail());
            conn.setString(7, getComremark());
            conn.setInt(8, getComid());
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
