    public void insert() {
        clearErr();
        DbConn conn = new DbConn();
        try {
            conn.setAutoCommit(false);
            if (getId().trim().equals("")) setId(KeyGen.nextID(""));
            if (getMsgType() == 0) setReplyId(getId());
            String sql = "insert into t_message_board (" + " list_id,msg_title,msg_info,author_id,author_name," + " msg_type,reply_id,reply_num,issue_time" + " ) values (?,?,?,?,?,?,?,?,?)";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.setString(2, getMsgTitle());
            conn.setString(3, getMsgInfo());
            conn.setString(4, getAuthorId());
            conn.setString(5, getAuthorName());
            conn.setInt(6, getMsgType());
            conn.setString(7, getReplyId());
            conn.setInt(8, getReplyNum());
            conn.setString(9, getIssueTime());
            conn.executeUpdate();
            if (getMsgType() == 1) {
                sql = "update t_message_board set reply_num = reply_num + 1 where list_id = ?";
                conn.prepare(sql);
                conn.setString(1, getReplyId());
                conn.executeUpdate();
            }
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
