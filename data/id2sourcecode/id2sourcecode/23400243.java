    public int getChannelMaxLevel(String path) throws Exception {
        DBOperation dbo = null;
        ResultSet resultSet = null;
        int maxLevel = -1;
        try {
            dbo = createDBOperation();
            String sql = "select max(channel_path) from t_ip_channel where channel_path like '" + path + "%'";
            resultSet = dbo.select(sql);
            if (resultSet.next()) {
                String mPath = resultSet.getString(1);
                maxLevel = mPath.length() / 5 - 1;
            }
        } catch (Exception e) {
            log.error("�õ�ĳƵ��Path�¿�������ʱ����", e);
            throw e;
        } finally {
            close(resultSet, null, null, null, dbo);
        }
        return maxLevel;
    }
