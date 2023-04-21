    public static void addItem(ItemIF item) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO ITEMS ");
        sql.append("(ITEM_ID, CHANNEL_ID, TITLE, DESCRIPTION, UNREAD, LINK, CREATOR, SUBJECT, DATE, FOUND, GUID, COMMENTS, SOURCE, ENCLOSURE) ");
        sql.append("VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        Connection con = Database.getInstance().getConnection();
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, item.getId());
        stmt.setLong(2, item.getChannel().getId());
        stmt.setString(3, Utils.stripToSafeDatabaseString(item.getTitle()));
        stmt.setString(4, Utils.stripToSafeDatabaseString(item.getDescription()));
        stmt.setInt(5, item.getUnRead() ? 1 : 0);
        stmt.setBytes(6, item.getLink() == null ? null : Utils.serialize(item.getLink()));
        stmt.setString(7, Utils.stripToSafeDatabaseString(item.getCreator()));
        stmt.setString(8, Utils.stripToSafeDatabaseString(item.getSubject()));
        stmt.setDate(9, item.getDate() == null ? null : new Date(item.getDate().getTime()));
        stmt.setDate(10, item.getFound() == null ? null : new Date(item.getFound().getTime()));
        stmt.setObject(11, null);
        stmt.setBytes(12, item.getComments() == null ? null : Utils.serialize(item.getComments()));
        stmt.setObject(13, null);
        stmt.setObject(14, null);
        stmt.executeUpdate();
        stmt.close();
    }
