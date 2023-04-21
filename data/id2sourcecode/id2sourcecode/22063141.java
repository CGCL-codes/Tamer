    public void dbUpdate(Connection dbConnection) throws SQLException {
        String query = "update " + TABLE_NAME + " set " + "post_email = ?, " + "title = ?, " + "description = ?, " + "image_mime_type = ? " + " where " + " channel_name = ? " + "AND " + " post_date = ? " + "AND " + " user_id = ? " + "";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(this.getClass().getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: " + query);
        }
        stmt.setString(1, getPostEmail());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [1]=" + getPostEmail());
        }
        stmt.setString(2, getTitle());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [2]=" + getTitle());
        }
        stmt.setString(3, getDescription());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [3]=" + getDescription());
        }
        stmt.setString(4, getImageMimeType());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [4]=" + getImageMimeType());
        }
        stmt.setString(5, getChannelName());
        stmt.setTimestamp(6, getPostDate());
        stmt.setString(7, getUserId());
        stmt.executeUpdate();
        stmt.close();
    }
