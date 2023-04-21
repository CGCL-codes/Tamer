    public boolean dbExists(Connection dbConnection) throws SQLException {
        String query = " select channel_name, post_date, user_id from " + TABLE_NAME + " where channel_name = ? AND post_date = ? AND user_id = ? ";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setString(1, getChannelName());
        stmt.setTimestamp(2, getPostDate());
        stmt.setString(3, getUserId());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(this.getClass().getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: " + query);
        }
        ResultSet rs = stmt.executeQuery();
        int num = 0;
        while (rs.next()) {
            num++;
        }
        stmt.close();
        return num != 0;
    }
