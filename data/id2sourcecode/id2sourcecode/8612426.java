    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            String sql = "";
            conn.setAutoCommit(false);
            sql = "insert into techcontract (" + "serviceid,area,customer,linkman,busid,phone,saleid,status," + "begindate,enddate,servicelevel,contractdesc,contractlile, " + "techbegindate,techenddate,sevbegindate,sevenddate,analyreport," + "techsupport,comid,techmanager,engineer,assigntime,contype, " + " fifee,mfee,myear,spartfee,outefee,prjname,createman,signfee) values ( " + " ?,?,?,?,?,?,?,?,?,?," + " ?,?,?,?,?,?,?,?,?,?," + " ?,?,?,?,?,?,?,?,?,?,?,? )";
            conn.prepare(sql);
            conn.setString(1, getServiceid());
            conn.setString(2, getArea());
            conn.setString(3, getCustomer());
            conn.setString(4, getLinkman());
            conn.setInt(5, getBusid());
            conn.setString(6, getPhone());
            conn.setString(7, getSaleid());
            conn.setInt(8, getStatus());
            conn.setDate(9, getBegindate());
            conn.setDate(10, getEnddate());
            conn.setInt(11, getServicelevel());
            conn.setString(12, getContractdesc());
            conn.setString(13, getContractlile());
            conn.setDate(14, getTechbegindate());
            conn.setDate(15, getTechenddate());
            conn.setDate(16, getSevbegindate());
            conn.setDate(17, getSevenddate());
            conn.setString(18, getAnalyreport());
            conn.setString(19, getTechsupport());
            conn.setInt(20, getComid());
            conn.setString(21, getTechmanager());
            conn.setString(22, getEngineer());
            conn.setDate(23, null);
            conn.setInt(24, getContype());
            conn.setDouble(25, getFifee());
            conn.setDouble(26, getMfee());
            conn.setDouble(27, getMyear());
            conn.setDouble(28, getSpartfee());
            conn.setDouble(29, getOutefee());
            conn.setString(30, getPrjname());
            conn.setString(31, getCreateman());
            conn.setDouble(32, getSignfee());
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
