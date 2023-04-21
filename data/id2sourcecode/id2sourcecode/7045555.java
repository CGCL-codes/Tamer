    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            String sql = "";
            DbRs rs = null;
            sql = "select * from companyinfo where comname = ?";
            conn.prepare(sql);
            conn.setString(1, getComnam());
            rs = conn.executeQuery();
            if (rs != null && rs.size() > 0) {
                setErr("此厂商名称已经存在");
                return;
            }
            sql = "insert into companyinfo(comname,comtel," + "comaddress,comremark) values(?,?,?,?)";
            conn.prepare(sql);
            conn.setString(1, getComnam());
            conn.setString(2, getComtel());
            conn.setString(3, getComaddress());
            conn.setString(4, getComremark());
            conn.executeUpdate();
            sql = "select  lastval() ";
            conn.prepare(sql);
            rs = conn.executeQuery();
            int lastid = rs.getInt(0, 0);
            Companysale comsale = getComsale();
            if (!comsale.getSalename().trim().equals("")) {
                sql = "insert into companysales(salename,comid,saletel,salemail," + "remark) values(?,?,?,?,?)";
                conn.prepare(sql);
                conn.setString(1, comsale.getSalename());
                conn.setInt(2, lastid);
                conn.setString(3, comsale.getSaletel());
                conn.setString(4, comsale.getSalemail());
                conn.setString(5, comsale.getRemark());
                conn.executeUpdate();
            }
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
