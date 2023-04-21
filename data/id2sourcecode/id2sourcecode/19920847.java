    public Channel getChannel(int chanid) throws SQLException {
        PreparedStatement pstmt = dbConnection.prepareStatement("SELECT channum, sourceid, " + "CONVERT(CONVERT(callsign USING binary) USING UTF8) AS callsign, " + "CONVERT(CONVERT(name USING binary) USING UTF8) AS name, " + "visible FROM channel WHERE chanid = ? LIMIT 1");
        pstmt.setInt(1, chanid);
        ResultSet srs = pstmt.executeQuery();
        srs.first();
        return new Channel(chanid, srs.getInt("channum"), getSourceByID(srs.getInt("sourceid")), srs.getString("callsign"), srs.getString("name"), srs.getInt("visible") == 1);
    }
