    public int getChannelIDBYChannum(int sourceid, int channum) throws SQLException {
        PreparedStatement pstmt = dbConnection.prepareStatement("SELECT chanid FROM channel WHERE channum = ? AND sourceid = ?");
        pstmt.setInt(1, channum);
        pstmt.setInt(2, sourceid);
        ResultSet srs = pstmt.executeQuery();
        srs.first();
        return srs.getInt("chanid");
    }
