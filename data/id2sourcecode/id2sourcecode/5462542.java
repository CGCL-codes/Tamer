    private static List getChannelList(Channel chan, String orgChanPath) throws Exception {
        List list = new ArrayList();
        DBOperation dbo = createDBOperation();
        Connection con = dbo.getConnection();
        String sql = "select * from T_IP_CHANNEL t where t.CHANNEL_PATH like '" + orgChanPath + "%'";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        while (rs.next()) {
            String strArr[] = new String[2];
            strArr[0] = rs.getString("channel_path");
            list.add(strArr);
        }
        stm.close();
        con.close();
        dbo.close();
        return list;
    }
