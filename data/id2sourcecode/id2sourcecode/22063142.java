    public void dbDelete(Connection dbConnection) throws SQLException {
        String query = "delete from " + TABLE_NAME + " where " + " channel_name = ? " + "AND " + " post_date = ? " + "AND " + " user_id = ? " + "";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(this.getClass().getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: " + query);
        }
        stmt.setString(1, getChannelName());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [1]=" + getChannelName());
        }
        stmt.setTimestamp(2, getPostDate());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [2]=" + getPostDate());
        }
        stmt.setString(3, getUserId());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [3]=" + getUserId());
        }
        stmt.executeUpdate();
        stmt.close();
    }
