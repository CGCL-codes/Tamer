    public boolean dbSelect(Connection dbConnection) throws SQLException {
        String query = "select " + "user_id, " + "channel_name, " + "channel_display_name, " + "channel_date_format, " + "loc_lang, " + "loc_country, " + "ftp_host, " + "ftp_user, " + "ftp_passwd, " + "ftp_path " + "from " + TABLE_NAME + " " + "where " + " channel_name = ? " + "AND " + " user_id = ? " + "";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setString(1, getChannelName());
        stmt.setString(2, getUserId());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(this.getClass().getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: " + query);
        }
        ResultSet rs = stmt.executeQuery();
        boolean hasNext = rs.next();
        if (!hasNext) {
            stmt.close();
            return false;
        }
        setValues(rs);
        stmt.close();
        return true;
    }
