    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            String sql = "insert into companysales(salename,comid,saletel,salemail," + "remark) values(?,?,?,?,?)";
            conn.prepare(sql);
            conn.setString(1, getSalename());
            conn.setInt(2, getComid());
            conn.setString(3, getSaletel());
            conn.setString(4, getSalemail());
            conn.setString(5, getRemark());
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
