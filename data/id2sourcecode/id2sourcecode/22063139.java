    public boolean dbSelect(Connection dbConnection) throws SQLException {
        String query = "select " + "user_id, " + "channel_name, " + "post_email, " + "post_date, " + "title, " + "description, " + "image_mime_type " + "from " + TABLE_NAME + " " + "where " + " channel_name = ? " + "AND " + " post_date = ? " + "AND " + " user_id = ? " + "";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        stmt.setString(1, getChannelName());
        stmt.setTimestamp(2, getPostDate());
        stmt.setString(3, getUserId());
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
