    public List getChannelDocuments(String chanelId) throws Exception {
        List documents = new ArrayList();
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            dbo = createDBOperation();
            StringBuffer sql = new StringBuffer(" select browse.* ");
            sql.append(" from t_ip_channel channel,t_ip_browse browse ");
            sql.append(" where channel.id = ");
            sql.append(chanelId);
            sql.append(" and channel.channel_path=browse.channel_path ");
            ResultSet rs = dbo.select(sql.toString());
            while (rs.next()) {
                DocumentPublish doc = new DocumentPublish();
                doc.setAbstractWords(rs.getString("doc_abstract"));
                doc.setAttachStatus(rs.getString("attach_state"));
                doc.setAuthor(rs.getString("author"));
                doc.setChannelPath(rs.getString("channel_path"));
                doc.setContentFile(rs.getString("content_file"));
                doc.setCreateDate(rs.getTimestamp("create_date"));
                doc.setCreater(rs.getInt("creater"));
                doc.setDoctypePath(rs.getString("doctype_path"));
            }
        } catch (Exception e) {
            log.error("�õ�Ƶ���б�ʱ����", e);
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
        return documents;
    }
