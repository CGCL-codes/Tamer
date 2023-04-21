    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            String sql = "";
            setId(KeyGen.nextID(""));
            conn.setAutoCommit(false);
            sql = "insert into t_storage_apply (" + " list_id,storage_id,contract_id,task_id,apply_date," + " return_date,customer_name,addr,engineer_id,engineer_name," + " apply_type,fix_date,fixed_date,upkeep,origin," + " status" + " ) values ( " + " ?,?,?,?,?,?,?,?,?,?," + " ?,?,?,?,?,?)";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.setString(2, getStorageId());
            conn.setString(3, getContractId());
            conn.setString(4, getTaskId());
            conn.setString(5, getApplyDate());
            conn.setString(6, getReturnDate());
            conn.setString(7, getCustomerName());
            conn.setString(8, getAddr());
            conn.setString(9, getEngineerId());
            conn.setString(10, getEngineerName());
            conn.setInt(11, getApplyType());
            conn.setString(12, getFixDate());
            conn.setString(13, getFixedDate());
            conn.setLong(14, getUpkeep());
            conn.setString(15, getOrigin());
            conn.setInt(16, getStatus());
            conn.executeUpdate();
            sql = "update t_storage_info set status = ? where list_id = ?";
            conn.prepare(sql);
            conn.setInt(1, getStatus());
            conn.setString(2, getStorageId());
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
