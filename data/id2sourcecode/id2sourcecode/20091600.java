    public boolean isMoreChannel(DocumentPublish doc) throws Exception {
        boolean bReturn = false;
        DBOperation dbo = null;
        ResultSet resultSet = null;
        String sql = "select count(doc_id) from t_ip_browse where doc_id=" + doc.getId() + " and channel_path like '" + doc.getChannelPath().substring(0, 10) + "%'";
        try {
            dbo = createDBOperation();
            resultSet = dbo.select(sql);
            if (resultSet.next() && resultSet.getInt(1) > 1) {
                bReturn = true;
            }
        } catch (Exception e) {
        } finally {
            close(resultSet, null, null, null, dbo);
        }
        return bReturn;
    }
