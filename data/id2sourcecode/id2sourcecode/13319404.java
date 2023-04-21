    public void delete() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            String sql = "";
            conn.setAutoCommit(false);
            if (getMsgType() == 0) {
                sql = "delete from t_message_board where reply_id = ?";
                conn.prepare(sql);
                conn.setString(1, getId());
                conn.executeUpdate();
                conn.commit();
                return;
            }
            sql = "delete from t_message_board where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.executeUpdate();
            sql = "update t_message_board set reply_num = reply_num - 1 where list_id = ?";
            conn.prepare(sql);
            conn.setString(1, getReplyId());
            conn.executeUpdate();
            conn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            setErr(ex.getMessage());
            try {
                conn.rollback();
            } catch (Exception e) {
                ex.printStackTrace();
            }
        } finally {
            conn.close();
        }
    }
