    public void delete(DocumentPublish doc) throws Exception {
        DBOperation dbo = null;
        Connection connection = null;
        Statement stmt = null;
        try {
            String sql = "delete from t_ip_browse where doc_id=" + doc.getId() + " and channel_path='" + doc.getChannelPath() + "'";
            dbo = createDBOperation();
            connection = dbo.getConnection();
            stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        } catch (Exception ex) {
            log.error("ɾ���ĵ�ʧ��!", ex);
            throw ex;
        } finally {
            close(null, stmt, null, connection, dbo);
        }
    }
