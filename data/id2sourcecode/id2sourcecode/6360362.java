    public static ItemIF getItem(long id) throws Exception {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ITEM_ID, CHANNEL_ID, TITLE, DESCRIPTION, UNREAD, LINK, CREATOR, SUBJECT, DATE, FOUND, GUID, COMMENTS, SOURCE, ENCLOSURE ");
        sql.append("FROM ITEMS WHERE ITEM_ID = ?");
        Connection con = Database.getInstance().getConnection();
        PreparedStatement stmt = con.prepareStatement(sql.toString());
        stmt.setLong(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            ItemIF item = new Item();
            item.setId(rs.getLong("ITEM_ID"));
            item.setChannel(DAOChannel.getChannel(rs.getLong("CHANNEL_ID")));
            item.setTitle(rs.getString("TITLE"));
            item.setDescription(rs.getString("DESCRIPTION"));
            item.setUnRead(rs.getInt("UNREAD") != 0);
            item.setLink(rs.getObject("LINK") == null ? null : (URL) Utils.deserialize(rs.getBytes("LINK")));
            item.setCreator(rs.getString("CREATOR"));
            item.setSubject(rs.getString("SUBJECT"));
            item.setDate(rs.getDate("DATE"));
            item.setFound(rs.getDate("FOUND"));
            item.setGuid(rs.getObject("GUID") == null ? null : DAOItemGuid.getItemGuid(rs.getInt("GUID"), item));
            item.setComments(rs.getObject("COMMENTS") == null ? null : (URL) Utils.deserialize(rs.getBytes("COMMENTS")));
            item.setSource(rs.getObject("SOURCE") == null ? null : DAOItemSource.getItemSource(rs.getInt("SOURCE"), item));
            item.setEnclosure(rs.getObject("ENCLOSURE") == null ? null : DAOItemEnclosure.getItemEnclosure(rs.getInt("SOURCE"), item));
            rs.close();
            stmt.close();
            return item;
        } else return null;
    }
