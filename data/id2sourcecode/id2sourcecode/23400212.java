    public List getChannelsList(String parentPath) throws Exception {
        List channels = new ArrayList();
        DBOperation dbo = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            dbo = createDBOperation();
            StringBuffer sql = new StringBuffer(" select channel.id ");
            sql.append(" from t_ip_channel channel,t_ip_tree_frame tree ");
            sql.append(" where channel.channel_path = tree.path ");
            sql.append(" and channel.channel_path like '");
            sql.append(parentPath).append("%' ");
            sql.append(" and tree.tree_level = ");
            sql.append(parentPath.length() / 5);
            ResultSet rs1 = dbo.select(sql.toString());
            while (rs1.next()) {
                String sql0 = new StringBuffer().append(" select channel.*").append(",site.ascii_name site_ascii_name").append(" from t_ip_channel channel").append(",t_ip_site site").append(" where channel.id=" + rs1.getInt("id")).append(" and channel.site_id = site.id").toString();
                channels.add(getInstance0(sql0));
            }
        } catch (Exception e) {
            log.error("�õ�Ƶ���б�ʱ����", e);
            throw e;
        } finally {
            close(resultSet, null, preparedStatement, connection, dbo);
        }
        return channels;
    }
