    public void dbInsert(Connection dbConnection) throws SQLException {
        String query = "insert into " + TABLE_NAME + " values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = dbConnection.prepareStatement(query);
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(this.getClass().getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: " + query);
        }
        stmt.setString(1, getUserId());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [1]=" + getUserId());
        }
        stmt.setString(2, getChannelName());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [2]=" + getChannelName());
        }
        stmt.setString(3, getPostEmail());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [3]=" + getPostEmail());
        }
        stmt.setTimestamp(4, getPostDate());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [4]=" + getPostDate());
        }
        stmt.setString(5, getTitle());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [5]=" + getTitle());
        }
        stmt.setString(6, getDescription());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [6]=" + getDescription());
        }
        stmt.setString(7, getImageMimeType());
        if (com.pehrs.mailpost.wmlblog.sql.Debug.isOn(MP_POST.class.getName())) {
            com.pehrs.mailpost.wmlblog.sql.Debug.log("SQL: value [7]=" + getImageMimeType());
        }
        stmt.executeUpdate();
        stmt.close();
    }
