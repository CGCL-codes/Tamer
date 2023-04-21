    public void delete() {
        clearErr();
        DbConn conn = new DbConn();
        DbRs rs = null;
        try {
            String sql = "";
            if (getId().trim().equals("")) {
                setErr("û��Ҫɾ��ļ�¼!");
                return;
            }
            conn.setAutoCommit(false);
            sql = "delete from t_article_info where article_id = ?";
            conn.prepare(sql);
            conn.setString(1, getId());
            conn.executeUpdate();
            sql = "update userinfo set article_num = article_num - 1 where user_id = ?";
            conn.prepare(sql);
            conn.setString(1, getAuthorId());
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
