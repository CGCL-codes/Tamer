    public Recorded[] getRecordingsDescending() throws SQLException {
        Statement stmt = dbConnection.createStatement();
        ResultSet srs = stmt.executeQuery("SELECT chanid, starttime, endtime, " + "CONVERT(CONVERT(title USING binary) USING UTF8) AS title, " + "CONVERT(CONVERT(description USING binary) USING UTF8) AS description, " + "CONVERT(CONVERT(basename USING binary) USING UTF8) AS basename, " + "progstart, progend, storagegroup FROM recorded ORDER BY recorded.starttime DESC");
        Vector<Recorded> v = new Vector<Recorded>();
        while (srs.next()) {
            v.add(new Recorded(getChannel(srs.getInt("chanid")), srs.getDate("starttime"), srs.getDate("endtime"), srs.getString("title"), srs.getString("description"), srs.getString("basename"), srs.getDate("progstart"), srs.getDate("progend"), getStoragegroupByName(srs.getString("storagegroup"))));
        }
        Recorded[] returnValue = new Recorded[v.size()];
        return v.toArray(returnValue);
    }
