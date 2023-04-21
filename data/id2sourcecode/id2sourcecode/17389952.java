    public void reback(String sn) {
        clearErr();
        DbConn conn = new DbConn();
        try {
            if (getId().trim().equals("")) {
                setErr("û��Ҫ���µļ�¼ID��");
                return;
            }
            conn.setAutoCommit(false);
            String sql = "";
            if (sn == null || sn.trim().equals("")) {
                sql = "update t_storage_info set status = ? where list_id = ?";
                conn.prepare(sql);
                conn.setInt(1, 0);
                conn.setString(2, getStorageId());
                conn.executeUpdate();
            } else {
                sql = "update t_storage_info set SN = ?,status = ? where list_id = ?";
                conn.prepare(sql);
                conn.setString(1, sn);
                conn.setInt(2, 0);
                conn.setString(3, getStorageId());
                conn.executeUpdate();
            }
            sql = "update t_storage_apply set" + " return_date = ? ,status = ?" + " where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, TSSDate.shortDate());
            conn.setInt(2, 0);
            conn.setString(3, getId());
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
