    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            String sql = "insert into task(area_id,conid,cusname,linkman,phone,addr,status,begindate," + "enddate,engineerid,matterinfo,servicelog,diffmod,taskaddress," + "triptype,normaltime,worktime,tasktime,tripfee,spartfee,outefee,tasklile)" + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            conn.prepare(sql);
            conn.setString(1, getArea_id());
            conn.setInt(2, getConid());
            conn.setString(3, getCusname());
            conn.setString(4, getLinkman());
            conn.setString(5, getPhone());
            conn.setString(6, getAddr());
            conn.setInt(7, getStatus());
            conn.setDate(8, getBegindate());
            conn.setDate(9, getEnddate());
            conn.setString(10, getEngineerid());
            conn.setString(11, getMatterinfo());
            conn.setString(12, getServicelog());
            conn.setInt(13, getDiffmod());
            conn.setString(14, getTaskaddress());
            conn.setInt(15, getTriptype());
            conn.setInt(16, getNormaltime());
            conn.setInt(17, getWorktime());
            conn.setInt(18, getTasktime());
            conn.setDouble(19, getTripfee());
            conn.setDouble(20, getSpartfee());
            conn.setDouble(21, getOutefee());
            conn.setString(22, getTasklile());
            conn.executeUpdate();
            sql = "update techconeng  set  taskid=lastval() where conid=? and engid=? and taskid=?";
            conn.prepare(sql);
            conn.setInt(1, getConid());
            conn.setString(2, getEngineerid());
            conn.setInt(3, 0);
            conn.executeUpdate();
            sql = "update techcontract  set  linkman=?,phone=? where conid=? ";
            conn.prepare(sql);
            conn.setString(1, getLinkman());
            conn.setString(2, getPhone());
            conn.setInt(3, getConid());
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
