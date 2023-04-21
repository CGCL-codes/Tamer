    public void update() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            if (getId().trim().equals("")) {
                setErr("û��Ҫ���µļ�¼ID��");
                return;
            }
            conn.setAutoCommit(false);
            String sql = "";
            String updateContractId = "";
            String updateStorageId = "";
            long updateUpkeep = 0;
            sql = "select * from t_storage_apply where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            DbRs rs = conn.executeQuery();
            if (rs == null || rs.size() == 0) {
                setErr("û��Ҫ���µļ�¼!");
                return;
            }
            updateContractId = get(rs, 0, "contract_id");
            updateStorageId = get(rs, 0, "storage_id");
            updateUpkeep = getLong(rs, 0, "upkeep");
            sql = "update t_storage_apply set" + " storage_id = ? , contract_id = ? , task_id = ? , apply_date = ? , return_date = ? ," + " customer_name = ? , addr = ? , engineer_id = ? , engineer_name = ? , apply_type = ? ," + " fix_date = ? , fixed_date = ? , upkeep = ? , origin = ? , status = ?" + " where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, getStorageId());
            conn.setString(2, getContractId());
            conn.setString(3, getTaskId());
            conn.setString(4, getApplyDate());
            conn.setString(5, getReturnDate());
            conn.setString(6, getCustomerName());
            conn.setString(7, getAddr());
            conn.setString(8, getEngineerId());
            conn.setString(9, getEngineerName());
            conn.setInt(10, getApplyType());
            conn.setString(11, getFixDate());
            conn.setString(12, getFixedDate());
            conn.setLong(13, getUpkeep());
            conn.setString(14, getOrigin());
            conn.setInt(15, getStatus());
            conn.setString(16, getId());
            conn.executeUpdate();
            if (getApplyType() > 0) {
                sql = "update t_contract_info set fact_cost_money = fact_cost_money + " + getUpkeep() + " where contract_id = ?";
                conn.prepare(sql);
                conn.setString(1, getContractId());
                conn.executeUpdate();
                sql = "update t_contract_info set fact_cost_money = fact_cost_money - " + updateUpkeep + " where contract_id = ?";
                conn.prepare(sql);
                conn.setString(1, updateContractId);
                conn.executeUpdate();
            }
            sql = "update t_storage_info set status = ? where list_id = ?";
            conn.prepare(sql);
            conn.setInt(1, getStatus());
            conn.setString(2, updateStorageId);
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
