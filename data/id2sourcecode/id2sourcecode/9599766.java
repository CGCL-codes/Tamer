    public void dbInsert(Connection dbConnection) throws SQLException {
        String query = "insert into " + TABLE_NAME + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(this.getClass().getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: " + query);
        }
        stmt.setString(1, getUserId());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [1]=" + getUserId());
        }
        stmt.setString(2, getChannelName());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [2]=" + getChannelName());
        }
        stmt.setString(3, getChannelDisplayName());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [3]=" + getChannelDisplayName());
        }
        stmt.setString(4, getChannelDateFormat());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [4]=" + getChannelDateFormat());
        }
        stmt.setString(5, getLocLang());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [5]=" + getLocLang());
        }
        stmt.setString(6, getLocCountry());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [6]=" + getLocCountry());
        }
        stmt.setString(7, getFtpHost());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [7]=" + getFtpHost());
        }
        stmt.setString(8, getFtpUser());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [8]=" + getFtpUser());
        }
        stmt.setString(9, getFtpPasswd());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [9]=" + getFtpPasswd());
        }
        stmt.setString(10, getFtpPath());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [10]=" + getFtpPath());
        }
        stmt.executeUpdate();
        stmt.close();
    }
