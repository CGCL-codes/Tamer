    public void delete() {
        clearErr();
        DbConn conn = new DbConn();
        DbRs rs = null;
        try {
            String sql = "";
            sql = "select * from t_storage_apply where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            rs = conn.executeQuery();
            if (rs == null || rs.size() == 0) {
                setErr("û��Ҫɾ��ļ�¼!");
                return;
            }
            String updateContractId = get(rs, 0, "contract_id");
            long updateUpkeep = getLong(rs, 0, "upkeep");
            conn.setAutoCommit(false);
            sql = "delete from t_storage_apply where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.executeUpdate();
            sql = "update t_contract_info set fact_cost_money = fact_cost_money - " + updateUpkeep + " where contract_id = ?";
            conn.prepare(sql);
            conn.setString(1, updateContractId);
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
