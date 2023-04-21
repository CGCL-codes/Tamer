    public void dbUpdate(Connection dbConnection) throws SQLException {
        String query = "update " + TABLE_NAME + " set " + "channel_display_name = ?, " + "channel_date_format = ?, " + "loc_lang = ?, " + "loc_country = ?, " + "ftp_host = ?, " + "ftp_user = ?, " + "ftp_passwd = ?, " + "ftp_path = ? " + " where " + " channel_name = ? " + "AND " + " user_id = ? " + "";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(this.getClass().getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: " + query);
        }
        stmt.setString(1, getChannelDisplayName());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [1]=" + getChannelDisplayName());
        }
        stmt.setString(2, getChannelDateFormat());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [2]=" + getChannelDateFormat());
        }
        stmt.setString(3, getLocLang());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [3]=" + getLocLang());
        }
        stmt.setString(4, getLocCountry());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [4]=" + getLocCountry());
        }
        stmt.setString(5, getFtpHost());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [5]=" + getFtpHost());
        }
        stmt.setString(6, getFtpUser());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [6]=" + getFtpUser());
        }
        stmt.setString(7, getFtpPasswd());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [7]=" + getFtpPasswd());
        }
        stmt.setString(8, getFtpPath());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST_CHANNEL.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [8]=" + getFtpPath());
        }
        stmt.setString(9, getChannelName());
        stmt.setString(10, getUserId());
        stmt.executeUpdate();
        stmt.close();
    }
